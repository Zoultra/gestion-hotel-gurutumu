 import { Component, OnInit } from '@angular/core';
import { ReservationService } from '../../../components/services/reservation/reservation.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Reservation } from '../../../components/models/reservation';
import { FormBuilder, FormGroup, FormArray, ReactiveFormsModule, AbstractControl, Validators, FormControl } from '@angular/forms';
import { ChambreService } from '../../../components/services/chambre/chambre.service';
import { SalleService } from '../../../components/services/salle/salle.service';
import { CommonModule } from '@angular/common';
import { ClientService } from '../../../components/services/client/client.service';
import { AlertService } from '../../../components/services/alert/alert.service';

@Component({
  selector: 'app-update-reservation',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './update-reservation.component.html',
  styleUrl: './update-reservation.component.scss'
})
export class UpdateReservationComponent implements OnInit {
  reservationForm!: FormGroup;
  reservationId!: number;
  chambres: any[] = [];
  salles: any[] = [];
  clients: any[] = [];
  
  isLoading = true;
  errorMsgFromBackend: string | null = null;
  errorMessage: string | null = null;
  reservation: Reservation = new Reservation
  constructor(
    private fb: FormBuilder,
    private reservationService: ReservationService,
    private router: Router,
    private route: ActivatedRoute,
    private chambreService: ChambreService,
    private salleService: SalleService,
    private clientService: ClientService,
     private alertService: AlertService,
  ) {}

  ngOnInit(): void {
    this.initializeForm();
    this.loadData();
    
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.reservationId = +id;
      this.loadReservation(this.reservationId);
    } else {
      this.errorMessage = 'ID de réservation non fourni';
      this.isLoading = false;
    }
  }

 

  initializeForm(): void {
  this.reservationForm = this.fb.group({
    code: ['', Validators.required],
    dateDebut: ['', Validators.required],
    dateFin: ['', Validators.required],
    dateReservation: ['', Validators.required],
    statut: ['', Validators.required],
    client_id: [null, Validators.required], // Initialisé à null
    lignes: this.fb.array([]),
    montantNet: [{value: 0, disabled: true}],
    
    montantTotal: [{value: 0, disabled: true}],
     montantRemise: [{value: 0, disabled: false}],
  });

   this.reservationForm.valueChanges.subscribe(() => {
      this.getMontantTotal()
    });
}

  loadData(): void {
    this.chambreService.getChambreList().subscribe({
      next: (chambres) => this.chambres = chambres,
      error: (err) => console.error('Erreur chargement chambres', err)
    });

    this.salleService.getSalleList().subscribe({
      next: (salles) => this.salles = salles,
      error: (err) => console.error('Erreur chargement salles', err)
    });

    
  }

  // Modifiez loadClients pour accepter un callback
loadClients(callback?: () => void): void {
  this.clientService.getClientList().subscribe({
    next: (clients) => {
      this.clients = clients;
      // Sélectionne automatiquement le client de la réservation
      if (this.reservation?.client_id) {
        this.reservationForm.get('client_id')?.setValue(this.reservation.client_id);
      }
      if (callback) callback();
    },
    error: (err) => {
      console.error('Erreur chargement clients', err);
      if (callback) callback();
    }
  });
}

 // Modifiez la méthode loadReservation
loadReservation(id: number): void {
  this.reservationService.getReservationById(id).subscribe({
    next: (response) => {
      if (response) {
        this.reservation = response;
        
        // D'abord charger les clients
        this.loadClients(() => {
          // Ensuite initialiser le formulaire
          this.initForm(this.reservation);
          this.isLoading = false;
        });
      } else {
        this.errorMessage = 'Erreur lors du chargement de la réservation';
        this.isLoading = false;
      }
    },
    error: (err) => {
      this.errorMessage = 'Erreur lors du chargement de la réservation';
      this.isLoading = false;
      console.error('Erreur', err);
    }
  });
}

    

  initForm(reservation: Reservation): void {

     this.reservationForm.patchValue({
      code: reservation.code,
      dateDebut: reservation.dateDebut,
      dateFin: reservation.dateFin,
      client_id: reservation.client.id,
      dateReservation: reservation.dateReservation,
      statut: reservation.statut,
      montantRemise: reservation.facture?.montantRemise,
   //   montantTotal: reservation.montantTotal,
      montantNet: reservation.facture?.montantNet,
    });
    
     this.reservationForm.valueChanges.subscribe(() => {
      this.getMontantTotal()
    });

    // Création des lignes de réservation
    const lignesFormArray = this.fb.array(
      reservation.lignes.map(ligne => 
        this.createLigneFormGroup(ligne.type, ligne.idObjet, ligne.prixUnitaire, ligne.nombreJours)
      )
    );
    this.reservationForm.setControl('lignes', lignesFormArray);
     this.reservationForm.valueChanges.subscribe(() => {
     
    });

    
  }

  createLigneFormGroup(type?: string, idObjet?: number, prixUnitaire?: number, nombreJours?: number): FormGroup {
    const ligne = this.fb.group({
      type: [type || '', Validators.required],
      idObjet: [idObjet || null, Validators.required],
      prixUnitaire: [prixUnitaire || 0],
      nombreJours: [nombreJours || 1, [Validators.required, Validators.min(1)]]
    });

    this.initLigneListeners(ligne);
    
    return ligne;
  }

  submitModification(): void {
    if (this.reservationForm.invalid) {
     
      this.errorMessage = 'Veuillez remplir tous les champs obligatoires';
      return;
    }

    const updatedReservation = this.reservationForm.value;
    console.log(' Updated Reservation : ',updatedReservation)
    this.reservationService.update(this.reservationId, updatedReservation).subscribe({
     
      next: () => { 
         this.alertService.confirmSuccess("Réservation modifiée avec succès", 2000);
     
         setTimeout(() => {
           this.router.navigate(['/dashboard/reservations/liste']);
        }, 2000);  // 3000ms = 3 secondes
      },
      error: (err) => {
       
        this.alertService.error(this.errorMsgFromBackend = err?.error?.message || 'Erreur inconnue.')
        console.error('Erreur', err);
        console.log(updatedReservation)
      }
    });
  }

  // Gestion des lignes
  get lignes(): FormArray {
    return this.reservationForm.get('lignes') as FormArray;
  }

  addLigne(): void {
    this.lignes.push(this.createLigneFormGroup());
      this.mettreAJourMontants();
  }

  removeLigne(index: number): void {
    this.lignes.removeAt(index);
      this.mettreAJourMontants();
  }

  initLigneListeners(ligne: FormGroup): void {
    ligne.get('type')?.valueChanges.subscribe(type => {
      ligne.get('idObjet')?.reset();
      ligne.get('prixUnitaire')?.setValue(0);
    });

    ligne.get('idObjet')?.valueChanges.subscribe(idObjet => {
      const type = ligne.get('type')?.value;
      this.getPrixUnitaire(ligne, type, idObjet);
    });
  }

  getPrixUnitaire(ligne: FormGroup, type: string, idObjet: number): void {
    if (!type || !idObjet) return;

    if (type === 'CHAMBRE') {
      const chambre = this.chambres.find(c => c.id === +idObjet);
      ligne.get('prixUnitaire')?.setValue(chambre?.tarifParNuit || 0);
    } else if (type === 'SALLE') {
      const salle = this.salles.find(s => s.id === +idObjet);
      ligne.get('prixUnitaire')?.setValue(salle?.tarif || 0);
    }
  }

  castAsFormGroup(control: AbstractControl): FormGroup {
    return control as FormGroup;
  }

  onTypeChange(ligne: AbstractControl, index: number): void {
  ligne.get('idObjet')?.reset();
  ligne.get('prixUnitaire')?.setValue(0);
}



getMontantTotal(): number {
  return this.lignes.controls.reduce((total, ligne) => {
    const prix = ligne.get('prixUnitaire')?.value || 0;
    const jours = ligne.get('nombreJours')?.value || 0;
    return total + (prix * jours);
  }, 0);
}


mettreAJourMontants(): number  {
  const total = this.getMontantTotal();
  this.reservationForm.get('montantTotal')?.setValue(total, { emitEvent: false });
  const remise = this.reservationForm.get('montantRemise')?.value || 0;
  const remiseValide = remise > total ? total : remise;
  const net = total - remiseValide;
  this.reservationForm.get('montantNet')?.setValue(net, { emitEvent: false });
  return net;
}

 


calculateLineTotal(ligne: AbstractControl): number {
  const prix = ligne.get('prixUnitaire')?.value || 0;
  const jours = ligne.get('nombreJours')?.value || 0;
  return prix * jours;
}

  goToReservationList(): void {
    this.router.navigate(['/dashboard/reservations/liste']);
  }
}