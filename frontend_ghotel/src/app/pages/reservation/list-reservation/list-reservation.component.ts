import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReservationService } from '../../../components/services/reservation/reservation.service';
import { PrintReservationService } from '../../../components/services/reservation/printReservation.service';
import { Reservation } from '../../../components/models/reservation';
import { Router } from '@angular/router';
import { NgxPaginationModule } from 'ngx-pagination';
import { FormsModule } from '@angular/forms';
import { FilterPipe } from '../../../pipes/filter.pipe';
import { AuthService } from '../../../components/services/auth/auth.service';
import { AjoutPaiementModalComponent } from '../../shared/ajout-paiement-modal/ajout-paiement-modal.component';
import { PaiementService } from '../../../components/services/paiement/paiement.service';
import { AlertService } from '../../../components/services/alert/alert.service';
declare var bootstrap: any;
@Component({
  selector: 'app-list-reservation',
  standalone: true,
  imports: [NgxPaginationModule, FormsModule, CommonModule, FilterPipe,AjoutPaiementModalComponent],
  templateUrl: './list-reservation.component.html',
  styleUrl: './list-reservation.component.scss'
})
export class ListReservationComponent implements OnInit {
  reservations: Reservation[] = [];
  myFilterText = '';
  tableSize = 5;
  tableSizes = [5, 10, 15, 20];
  page = 1;
  count = 0;
  role!: string
  errorMsgFromBackend: string | null = null;

  constructor(private reservationService: ReservationService,
     private router: Router, private authService: AuthService, 
     private printReservationService: PrintReservationService,
     private paiementService : PaiementService,
     private alerteService: AlertService) {}

  ngOnInit(): void {
    this.getReservations();
    this.role = this.authService.getUserRole();
  }

  
  imprimerTicket(reservation: Reservation): void {
    this.printReservationService.generateTicket(reservation);
  }

   imprimerFacture(reservation: Reservation): void {
    this.printReservationService.generateFacture(reservation);
  }

  getReservations(): void {
    this.reservationService.getAll().subscribe({
      next: data => {
        this.reservations = data.map(res => ({ ...res, showDetails: false }));
        this.count = this.reservations.length;
        console.log(this.reservations)
      },
      error: err => console.error('Erreur lors du chargement des réservations', err)
    });
  }

  reloadPage(): void {
    this.getReservations();
  }

  toggleDetails(reservation: Reservation): void {
    reservation.showDetails = !reservation.showDetails;

    //  Optionnel: Fermer les autres détails ouverts
     this.reservations.forEach(v => {
        if (v !== reservation) v.showDetails = false;
      });
      
  }

  

  updateReservation(id: number): void {
    this.router.navigate(['/dashboard/reservations/update', id]);
  }

  deleteReservation(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cette réservation ?')) {
      this.reservationService.delete(id).subscribe(() => {
         // Ajoute un délai avant de recharger la page (par exemple 3 secondes)
        setTimeout(() => {
          window.location.reload();
        }, 1000);  // 3000ms = 3 secondes
        this.getReservations();
      });
    }
  }

  onTableDataChange(event: number): void {
    this.page = event;
  }

  onTableSizeChange(event: any): void {
    this.tableSize = +event.target.value;
    this.page = 1;
  }

  goToNewReservation(): void {
    this.router.navigate(['/dashboard/reservations/add']);
  }

 
goToAddClientFomRes() {
     this.router.navigate(['/dashboard/clients/add'], {
     queryParams: { from: 'reservation' }
    });

  }


  @ViewChild('paiementModal') paiementModal!: AjoutPaiementModalComponent;

factureIdCourante!: number;

ouvrirPaiementModal(factureId: number) {
  this.factureIdCourante = factureId;
  this.paiementModal.openModal();
}

onPaiementAjoute(event: any) {
  const paiementDto = {
    montant: event.montant,
    commentaire: event.commentaire,
    modePaiement: event.modePaiement,
    datePaiement: event.datePaiement // ou ne pas inclure s’il est auto-généré côté backend
  };

  this.paiementService.ajouterPaiement(event.factureId, paiementDto)
    .subscribe({
      next: () => { 
       this.errorMsgFromBackend = null;
      this.alerteService.confirmSuccess('Paiement effectué avec succès !', 3000)
      this.getReservations(); // Recharger la liste
      },
      error: err => {
      this.alerteService.error(this.errorMsgFromBackend = err?.error?.message || 'Erreur inconnue.')
      
    }


    });
}



paiementsActuels: any[] = [];

ouvrirListPaiementModal(factureId: number): void {
  const reservationTrouvee = this.reservations.find(res => res.facture?.id === factureId);
  if (reservationTrouvee && reservationTrouvee.facture?.paiements) {
    this.paiementsActuels = reservationTrouvee.facture.paiements;
  } else {
    this.paiementsActuels = [];
  }

  const modalElement = document.getElementById('paiementModal');
  if (modalElement) {
    const modal = new bootstrap.Modal(modalElement);
    modal.show();
  }
}

}

