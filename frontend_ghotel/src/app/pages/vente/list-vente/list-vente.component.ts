import { Component, OnInit } from '@angular/core';
import { Vente } from '../../../components/models/vente'; 
import { VenteService } from '../../../components/services/vente/vente.service'; 
import { ActivatedRoute, Router } from '@angular/router';
import { NgxPaginationModule } from 'ngx-pagination';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FilterPipe } from '../../../pipes/filter.pipe';
import { AlertService } from '../../../components/services/alert/alert.service';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { LigneVente } from '../../../components/models/ligne-vente';
import { Article } from '../../../components/models/article';
import { ArticleService } from '../../../components/services/article/article.service';
import { AuthService } from '../../../components/services/auth/auth.service';
import jsPDF from 'jspdf';
import { PaiementService } from '../../../components/services/paiement/paiement.service';

@Component({
  selector: 'app-list-vente',
  standalone: true,
  imports: [NgxPaginationModule, FormsModule, CommonModule, FilterPipe],
  templateUrl: './list-vente.component.html',
  styleUrl: './list-vente.component.scss'
})
export class ListVenteComponent implements OnInit {

  id!: number;
  myFilterText!: any;
  title = 'Pagination';
  page: number = 1;
  count: number = 0;
  tableSize: number = 5;
  tableSizes: any = [5, 10, 15, 20];
  role!: string
  ventes: Vente[] = [];
  isLoading = true;
  expandedId: number | null = null;
  displayedColumns: string[] = ['id', 'code', 'dateVente', 'montantTotal'];
  articles: Article[] = [];
  ligneVente: LigneVente[] = [];
  selectedLignes: { [venteId: number]: LigneVente[] } = {};
  errorMsgFromBackend: string | null = null;
  utilisateur!: string
  
  constructor(
     private articleService: ArticleService,
    private venteService: VenteService,
    private alertService: AlertService,
    private route: ActivatedRoute,
    private router: Router,
    private sanitizer: DomSanitizer,
    private authService: AuthService,
    private paiementService : PaiementService
  ) {}

  ngOnInit(): void {
    this.getVenteList();
    this.role = this.authService.getUserRole();
     this.utilisateur = this.authService.getUserName();
  }

  private getVenteList() {
    this.venteService.getVenteList().subscribe({
      next: (response: any) => {
        console.log('Données reçues :', response);
        if (response) {
          // Trier les ventes par dateVente décroissante
          this.ventes = response.sort((a: any, b: any) => {
            return new Date(b.dateVente).getTime() - new Date(a.dateVente).getTime();
          });
        }
      },
      error: (err) => {
        console.error('Erreur lors du chargement des ventes', err);
      }
    });
  }
  

  deleteVente(id: number) {
    this.alertService.confirmDelete(() => {
      this.venteService.deleteVente(id).subscribe(() => {
        this.alertService.success("Vente supprimée avec succès !");
        this.getVenteList();
      }, error => {
        this.alertService.error("La suppression a échoué.");
      });
    });
  }

  onTableDataChange(event: any) {
    this.page = event;
    this.getVenteList();
  }

  onTableSizeChange(event: any) {
    this.tableSize = event.target.value;
    this.page = 1;
    this.getVenteList();
  }

  updateVente(id: number) {
     this.router.navigate(['/dashboard/ventes/update', id], {
     queryParams: { from: 'liste' }
    });

  }
  

  reloadPage() {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigate(['./'], {
      relativeTo: this.route
    });
  }

  goToPos() {
    this.router.navigate(['/dashboard/ventes/pos']);
  }

  
  // Méthode pour basculer l'affichage des détails
toggleDetails(vente: Vente): void {
  vente.showDetails = !vente.showDetails;
  
 //  Optionnel: Fermer les autres détails ouverts
     this.ventes.forEach(v => {
        if (v !== vente) v.showDetails = false;
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
