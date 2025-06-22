import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxPaginationModule } from 'ngx-pagination';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FilterPipe } from '../../../pipes/filter.pipe';
import { MouvementCaisse, RecapJournalierDto } from '../../../components/models/mouvementCaisse';
import { MouvementCaisseService } from '../../../components/services/mouvementCaisse/mouvement-caisse.service';
import { AlertService } from '../../../components/services/alert/alert.service';
import autoTable from 'jspdf-autotable';
import jsPDF from 'jspdf';

@Component({
  selector: 'app-mouvement-caisse',
  standalone: true,
  imports: [NgxPaginationModule, FormsModule, CommonModule, FilterPipe],
  templateUrl: './mouvement-caisse.component.html',
  styleUrl: './mouvement-caisse.component.scss'
})
export class MouvementCaisseComponent {
listeRecaps: any[] = [];
   recap!: RecapJournalierDto;
   recaps!: RecapJournalierDto;
   dateChoisie: string = new Date().toISOString().split('T')[0];

   mouvements: MouvementCaisse[] = [];

   id!: number;
   myFilterText!: any;

  title = 'Pagination';
  page: number = 1;
  count: number = 0;
  tableSize: number = 5;
  tableSizes: any = [5, 10, 15, 20];

  recapList: RecapJournalierDto[] = [];
  dateDebut:string = new Date().toISOString().split('T')[0];
  dateFin: string = new Date().toISOString().split('T')[0];

   constructor(
      private mouvementCaisseService: MouvementCaisseService,
      private alerteService: AlertService,
      private route: ActivatedRoute,
      private router: Router
    ) {}

    ngOnInit(): void {

       const today = new Date();
  
       this.dateDebut = new Date(today.setHours(0, 0, 0, 0)).toISOString(); // début de journée
       this.dateFin = new Date().toISOString(); // maintenant

     this.chargerRecap()
    
  }

   
  chargerRecap(){

      this.alerteService.showLoading('Chargement des données en cours...', 4000);
          setTimeout(() => {
            this.alerteService.closeLoading();
             this.chargerRecapInterval()
            this.alerteService.confirmSuccess('Chargement effectué', 1000);
            
          }, 2000);

  }

  

    chargerRecapInterval() {

    if (this.dateDebut && this.dateFin) {

    const startRaw = this.dateDebut;
    const endRaw = this.dateFin;

    const startDate = new Date(startRaw);
    const endDate = new Date(endRaw);

    const startDateTime = startDate.toISOString();
    const endDateTime = endDate.toISOString();

     if (startDateTime === endDateTime) {
      console.warn('Les deux dates sont identiques. Recherche annulée.');
      this.alerteService.error("La date de début et de fin ne doivent pas être identiques.");
     // alert('La date de début et de fin ne doivent pas être identiques.');
      return; // On sort sans appeler le backend
     }

      this.mouvementCaisseService.getRecapitulatifEntreDates({startDateTime,endDateTime}).subscribe({
        
        next: data =>  console.log( this.recapList = data),
        error: () =>  this.alerteService.error('Aucune Donée pour les dates selectionnées!'),
      });
    }   else {
     this.alerteService.error("Veuillez sélectionner une date de début et une date de fin.");
    }
  }

  



  
 
  printMultiRecap(recapList: any[]): void {

   
  const doc = new jsPDF();

  // console.log('RECAP JOURNALIER', recapList);

  recapList.forEach((recap, index) => {
    if (index !== 0) {
      doc.addPage();
    }

    // En-tête
    doc.setTextColor(33, 150, 243); // Bleu
    doc.setFontSize(18);
    doc.text(`GURUTUMU PALACE`, 14, 15); // Titre de l'entité

    doc.setTextColor(0, 0, 0); // Noir
    doc.setFontSize(14);
    doc.text(`Récapitulatif du ${new Date(recap.dateOuverture).toLocaleDateString()}`, 14, 25);

    // Informations financières
    const infos = [
      `Solde de départ : ${recap.soldeDepart.toFixed(2)} FCFA`,
      `Total entrées : ${recap.totalEntree.toFixed(2)} FCFA`,
      `Total sorties : ${recap.totalSortie.toFixed(2)} FCFA`,
      `Solde final théorique : ${recap.soldeFinalTheorique.toFixed(2)} FCFA`,
      `Solde final réel : ${recap.soldeFinal.toFixed(2)} FCFA`,
    ];

    doc.setFontSize(12);
    let y = 35;
    infos.forEach(info => {
      doc.text(info, 14, y);
      y += 7;
    });

    // Ligne de séparation
    doc.setDrawColor(200);
    doc.line(14, y, 195, y);
    y += 5;

    // Tableau des mouvements
    autoTable(doc, {
      startY: y,
      head: [[
        { content: 'Description', styles: { halign: 'left' } },
        { content: 'Type', styles: { halign: 'center' } },
        { content: 'Montant', styles: { halign: 'right' } },
        { content: 'Date', styles: { halign: 'center' } },
      ]],
      body: recap.mouvements?.map((m: any) => [
        m.description,
        m.type,
        `${m.montant.toFixed(2)} FCFA`,
        new Date(m.date).toLocaleDateString()
      ]),
      styles: {
        fontSize: 10,
        cellPadding: 3,
      },
      headStyles: {
        fillColor: [33, 150, 243],
        textColor: 255,
        fontStyle: 'bold',
      },
      bodyStyles: {
        textColor: 50,
      },
      theme: 'grid'
    });
  });

  // Aperçu PDF
  window.open(doc.output('bloburl'), '_blank')?.print();
}


  
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

   onTableDataChange(event: any) {
    this.page = event;
     
  }

  onTableSizeChange(event: any) {
    this.tableSize = event.target.value;
    this.page = 1;
    
  }

    reloadPage() {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigate(['./'], {
      relativeTo: this.route
    });
  }

}
