import { Component, OnInit , CUSTOM_ELEMENTS_SCHEMA, Input } from '@angular/core';
import { Article } from '../../../components/models/article';
import { LigneVente } from '../../../components/models/ligne-vente';
import { Vente } from '../../../components/models/vente';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { VenteService } from '../../../components/services/vente/vente.service'; 
import { ArticleService } from '../../../components/services/article/article.service';
import { AlertService } from '../../../components/services/alert/alert.service';
import { NgxPaginationModule } from 'ngx-pagination';
import { FilterPipe } from '../../../pipes/filter.pipe';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { AuthService } from '../../../components/services/auth/auth.service';

import { environment } from '../../../environments/environment';
import { Utilisateur } from '../../../components/models/user';
import { PaiementService } from '../../../components/services/paiement/paiement.service';

@Component({
  selector: 'app-vente-form',
  standalone: true,
   schemas: [CUSTOM_ELEMENTS_SCHEMA],
  imports: [FormsModule,ReactiveFormsModule,CommonModule,NgxPaginationModule,FilterPipe],
  templateUrl: './vente-form.component.html',
  styleUrl: './vente-form.component.scss'
})
export class VenteFormComponent implements OnInit{

 last50Ventes: Vente[] = [];
  myFilterText!:any

  title = 'Pagination'
  page: number = 1
  count: number = 0;
  tableSize: number = 5;
  tableSizes: any = [5, 10, 15, 20]

  articles: Article[] = [];
  factureVente: Vente[] = [];

  errorMsgFromBackend: string | null = null;

  

  role!: string
  utilisateur!: string

  public apiURL = `${environment.apiUrl}/articles/images/`;
  
  constructor(
    private articleService: ArticleService, 
    private venteService: VenteService, 
     private alertService: AlertService,
      private router: Router,
      private modalService: NgbModal,
       private authService: AuthService,
       private paiementService : PaiementService
  ) {
       
  }
    
   
 
  
  ngOnInit(): void { 
        this.role = this.authService.getUserRole();
        this.utilisateur = this.authService.getUserName();
        this.getArticleList();  // Modifié pour getArticleList()
        this.loadLastFiveVentes();
  }

  loadLastFiveVentes(): void {
    this.venteService.getLast50VenteList().subscribe({
      next: (ventes) => {
        console.log('Données reçues du service 5 last ventes:', ventes); // Vérification cruciale
        this.last50Ventes = ventes;
      },
      error: (err) => {
        console.error('Erreur:', err);
        this.last50Ventes = []; // Tableau vide en cas d'erreur
      }
    });
  }

   deleteVente(id: number) {
    this.alertService.confirmDelete(() => {
      this.venteService.deleteVente(id).subscribe(() => {
        this.alertService.success("Vente supprimée avec succès !");
        this.loadLastFiveVentes();
      }, error => {
        this.alertService.error("La suppression a échoué.");
      });
    });
  }

   
  updateVente(id: number) {
     this.router.navigate(['/dashboard/ventes/update', id], {
     queryParams: { from: 'form' }
    });
  }

  toggleDetails(vente: Vente): void {
  vente.showDetails = !vente.showDetails;
  
 //  Optionnel: Fermer les autres détails ouverts
     this.last50Ventes.forEach(v => {
        if (v !== vente) v.showDetails = false;
      });
}
   
  vente: Vente = {
    code: this.genererCodeVente(), // ✅ Ajout du champ `code`
    dateVente: new Date(),
    lignes: [],
    montantTotal: 0,
    montantPaye: 0,
    monnaie: 0,
    etatVente: '',
    tableResto: ''
  };


  private getArticleList() {
    this.articleService.getArticleList().subscribe({
      next: (response: any) => {
        console.log('Données reçues :', response);
        if (response) {
          this.articles = response;  // Modifié pour articles
        }
      },
      error: (err) => {
        console.error('Erreur lors du chargement des articles', err);
      }
    });
  }

  ajouterArticle(article: Article) {
    const ligneExistante = this.vente.lignes.find(l => l.article.id === article.id);
    if (ligneExistante) {
      ligneExistante.quantite += 1;
    } else {
      this.vente.lignes.push({
        article,
        quantite: 1,
        prixUnitaire: article.prixUnitaire
      });
    }
    console.log(this.vente)
    this.calculerMontantTotal();
     this.calculerMonnaie()
  }

  retirerArticle(ligne: LigneVente) {
    this.vente.lignes = this.vente.lignes.filter(l => l !== ligne);
    this.calculerMontantTotal();
    this.calculerMonnaie()
  }

   

  calculerMontantTotal() {
    this.vente.montantTotal = this.vente.lignes.reduce((total, ligne) => {
      return  total + (ligne.quantite * ligne.prixUnitaire);
    }, 0);
  }

   

  calculerMonnaie() {
  const total = this.vente.montantTotal
  this.vente.monnaie= this.vente.montantPaye - total;
}

 // validerVente() {
 //   console.log('Vente enregistrée :', this.vente);
    // TODO : Appeler un service HTTP pour sauvegarder la vente
 // }


 validerVente() {

  if (this.vente.lignes.length === 0) {
    this.alertService.error('Veuillez ajouter au moins un article à la vente. !');
    return; // stoppe la méthode
  }

  if (this.vente.tableResto === '') {
    this.alertService.error('Veuillez choisir au moins une table. !');
    return; // stoppe la méthode
  }

  if(this.vente.montantPaye!=0){
        this.vente.etatVente = 'PAYEE'
      }else{
        this.vente.etatVente = 'NON_PAYEE'
      }

  this.venteService.createVente(this.vente).subscribe({
    next: () => {
      this.alertService.confirmSuccess('Vente enregistrée avec succès !', 3000);
       this.errorMsgFromBackend = null;
      console.log('Vente enregistrée avec succès !');
       setTimeout(() => {
          window.location.reload();
        }, 1000);  // 1000ms = 1 secondes
      this.vente = {
        code: this.genererCodeVente(),
        dateVente: new Date(),
        lignes: [],
        montantTotal: 0,
        montantPaye: 0,
        monnaie: 0,
        etatVente: '',
        tableResto: ''
      };
    },
    error: err => {
       console.log('Vente JSON:', this.vente);
      this.alertService.error(this.errorMsgFromBackend = err?.error?.message || 'Erreur inconnue.')
      console.error('Erreur lors de l’enregistrement de la vente', err);
    }
  });
}

confirmerPaiement(vente: Vente): void {

   this.alertService.confirmPayementVente(() => { 
      
     this.paiementService.payerVente(vente.id!).subscribe({
      next: (updatedVente) => {
         this.errorMsgFromBackend = null;
        vente.etatVente = updatedVente.etatVente; // Mise à jour locale
         this.alertService.confirmSuccess("Vente payée avec succès.", 2000);
      }, 
      error: err => {
      this.alertService.error(this.errorMsgFromBackend = err?.error?.message || 'Erreur lors du paiement ')
      
    }
    });

       });
   
}




  private genererCodeVente(): string {
    const now = new Date();
    return 'VNT-RESTO-MLI-' + now.getTime(); // Exemple simple : VNT-1714412345678
  }

  onTableDataChange(event: any){
    this.page = event;
    this.getArticleList()
  }

  onTableSizeChange(event: any){
    this.tableSize = event.target.value;
    this.page = 1;
    this.getArticleList()
  }

 // ouvrirModalOuverture() {
//  const modalRef = this.modalService.open(OuvertureCaisseComponent, { size: 'lg' });
//}

   async generateTicket(factureVente: Vente): Promise<void> {
  try {
    console.log('TICKET DE VENTE ', factureVente);

    const logoUrl = 'assets/logo-ticket-gurutmu.png';
    const logoData = await this.getImageData(logoUrl);

    const doc = new jsPDF({
      orientation: 'portrait',
      unit: 'mm',
      format: [58, 220],
    });

    let yPosition = 5;

    // === LOGO ===
    if (logoData) {
      const logoWidth = 38;
      const logoHeight = 15;
      const logoX = (58 - logoWidth) / 2;
      doc.addImage(logoData, 'PNG', logoX, yPosition, logoWidth, logoHeight, undefined, 'FAST');
      yPosition += logoHeight + 4;
    }

    // === ENTÊTE ===
    doc.setFont('helvetica', 'normal');
    doc.setFontSize(7);
    const headerLines = [
      'Gurutumu Palace, Les saveurs du Wassulu.',
      'Immeuble Gurutumu, Diombougou Yanfolila, Mali.',
      'Tel: 78 19 19 58 / 68 19 19 58',
      'Email: gurutumu80@gmail.com'
    ];

    headerLines.forEach(line => {
      doc.text(line, 29, yPosition, { align: 'center' });
      yPosition += 3;
    });

    // === INFOS FACTURE ===
    yPosition += 2;
    doc.setFontSize(8);
    doc.setFont('bold');
    doc.text(`Gérant : ${this.utilisateur}`, 5, yPosition);
    yPosition += 4;
    doc.text(`N° Ticket : ${factureVente.id}`, 5, yPosition);
    yPosition += 4;
    doc.text(`N° Table : ${factureVente.tableResto}`, 5, yPosition);
    yPosition += 4;
    doc.text(`Date : ${new Date(factureVente.dateVente).toLocaleDateString()}`, 5, yPosition);
    yPosition += 4;

    doc.line(5, yPosition, 53, yPosition);
    yPosition += 4;

    // === TABLEAU DES ARTICLES ===
    doc.setFontSize(8);
    doc.setFont('helvetica', 'normal');
    doc.text('Article', 5, yPosition);
    doc.text('Qte', 30, yPosition);
    doc.text('P.U', 40, yPosition, { align: 'right' });
    doc.text('Total', 53, yPosition, { align: 'right' });
    yPosition += 3;
    doc.line(5, yPosition, 53, yPosition);
    yPosition += 3;

    factureVente.ligneVentes?.forEach(ligne => {
      const designation = ligne.article.designation.length > 15
        ? ligne.article.designation.slice(0, 12) + '...'
        : ligne.article.designation;

      const prixU = ligne.prixUnitaire.toFixed(0);
      const total = (ligne.quantite * ligne.prixUnitaire).toFixed(0);

      doc.text(designation, 5, yPosition);
      doc.text(ligne.quantite.toString(), 30, yPosition);
      doc.text(prixU, 41, yPosition, { align: 'right' });
      doc.text(total, 53, yPosition, { align: 'right' });

      yPosition += 5;
    });

    // === TOTAL GENERAL ===
    yPosition += 2;
    doc.line(5, yPosition, 53, yPosition);
    yPosition += 4;

    doc.setFontSize(10);
    doc.setFont('helvetica', 'bold');
    doc.text(`Montant Total : ${factureVente.montantTotal?.toFixed(0)} FCFA`, 53, yPosition, { align: 'right' });
        doc.setFontSize(10);
    doc.text(`Montant Payée  : ${factureVente.montantPaye?.toFixed(0)} FCFA`, 53, yPosition+4, { align: 'right' });
        doc.setFontSize(10);
    doc.text(`Monnaie       : ${factureVente.monnaie?.toFixed(0)} FCFA`, 53, yPosition+8, { align: 'right' });
    
    yPosition += 15; // Espace avant le message final
    doc.setFontSize(8);
    doc.setFont('helvetica', 'normal');
    doc.text('Merci pour votre achat !', 29, yPosition, { align: 'center' });

    // === GÉNÉRER & IMPRIMER ===
    const pdfUrl = doc.output('bloburl');
    const newTab = window.open(pdfUrl, '_blank');
    newTab?.focus();
    newTab?.print();
  } catch (error) {
    console.error('Erreur lors de la génération du ticket :', error);
  }
}


// Méthode pour charger l'image en base64
private async getImageData(url: string): Promise<string | null> {
  try {
    const response = await fetch(url);
    const blob = await response.blob();
    return new Promise((resolve) => {
      const reader = new FileReader();
      reader.onloadend = () => resolve(reader.result as string);
      reader.readAsDataURL(blob);
    });
  } catch (error) {
    console.error('Erreur de chargement du logo', error);
    return null;
  }
}

}
