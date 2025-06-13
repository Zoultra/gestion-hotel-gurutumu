
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, FormsModule, Validators } from '@angular/forms';
import { ReservationService } from '../../../components/services/reservation/reservation.service';
import { ClientService } from '../../../components/services/client/client.service';
import { ChambreService } from '../../../components/services/chambre/chambre.service';
import { SalleService } from '../../../components/services/salle/salle.service';
import { CommonModule } from '@angular/common';
import { FilterPipe } from '../../../pipes/filter.pipe';
import { ReactiveFormsModule } from '@angular/forms';
import { AlertService } from '../../../components/services/alert/alert.service';
import { Router } from '@angular/router';
import { Reservation } from '../../../components/models/reservation';

@Component({
  selector: 'app-reservation-form',
  standalone: true,
   imports: [ReactiveFormsModule, FormsModule, CommonModule, FilterPipe],
  templateUrl: './reservation-form.component.html',
  styleUrl: './reservation-form.component.scss'
})
export class ReservationFormComponent implements OnInit {
  reservationForm!: FormGroup;
  clients: any[] = [];
  chambres: any[] = [];
  salles: any[] = [];
  errorMsgFromBackend: string | null = null;
  montantTotal: number = 0;
  constructor(
    private fb: FormBuilder,
    private reservationService: ReservationService,
    private clientService: ClientService,
    private chambreService: ChambreService,
    private salleService: SalleService,
     private alertService: AlertService,
     private router: Router,
  ) {}

  ngOnInit(): void {
  

    this.initForm();
    this.loadData();
    
  }

  initForm() {
    this.reservationForm = this.fb.group({
      dateDebut: ['', Validators.required],
      dateFin: ['', Validators.required],
      code: this.genererCodeReservation(),
      statut: ['CONFIRMEE', Validators.required],
      dateReservation: [new Date().toISOString().substring(0, 10), Validators.required],
      client_id: [null, Validators.required],
      montantNet: [{value: 0, disabled: true}],
      montantRemise: [0, [Validators.min(0)]],
      montantTotal: [{value: 0, disabled: true}],
      lignes: this.fb.array([]),

      
    });

    this.reservationForm.valueChanges.subscribe(() => {
      this.getMontantTotal()
    });
  }

  get lignes(): FormArray {
    return this.reservationForm.get('lignes') as FormArray;
  }

  addLigne(): void {
  const ligne = this.fb.group({
    type: ['', Validators.required],
    idObjet: ['', Validators.required],
    nombreJours: [1, Validators.required],
    prixUnitaire: [0]
  });

  this.lignes.push(ligne);
  this.initLigneListeners(ligne); // <== ici

  
   this.mettreAJourMontants();

    ligne.get('type')?.valueChanges.subscribe(() => this.getPrixUnitaire(ligne));
  ligne.get('idObjet')?.valueChanges.subscribe(() => this.getPrixUnitaire(ligne));

}

  removeLigne(index: number) {
    this.lignes.removeAt(index);
   
     this.mettreAJourMontants();
  }

  submitReservation() {
  if (this.reservationForm.valid) {
    this.reservationService.create(this.reservationForm.value).subscribe({
      next: () => {
        // Affiche le message de succès
        this.errorMsgFromBackend = null;
        this.alertService.confirmSuccess("Réservation enregistrée avec succès", 5000);

        // Ajoute un délai avant de recharger la page (par exemple 3 secondes)
        setTimeout(() => {
          window.location.reload();
        }, 3000);  // 3000ms = 3 secondes
      },
      error: err => {
      this.alertService.error(this.errorMsgFromBackend = err?.error?.message || 'Erreur inconnue.')
      
    }
        });
  }
}



  loadData() {
    this.clientService.getClientList().subscribe(data => this.clients = data);
    this.chambreService.getChambreList().subscribe(data => this.chambres = data);
    this.salleService.getSalleList().subscribe(data => this.salles = data);
  }

   private genererCodeReservation(): string {
    const now = new Date();
    return 'RES-MLI-' + now.getTime(); // Exemple simple : RES-1714412345678
  }

  getPrixUnitaire(ligne: FormGroup) {
  const type = ligne.get('type')?.value;
  const idObjet = ligne.get('idObjet')?.value;

  if (type && idObjet) {
    if (type === 'CHAMBRE') {
      const chambre = this.chambres.find(c => c.id === +idObjet);
      ligne.get('prixUnitaire')?.setValue(chambre?.tarifParNuit || 0);
    } else if (type === 'SALLE') {
      const salle = this.salles.find(s => s.id === +idObjet);
      ligne.get('prixUnitaire')?.setValue(salle?.tarif || 0);
    }
  }
}


castAsFormGroup(control: AbstractControl): FormGroup {
  return control as FormGroup;
}

initLigneListeners(ligne: FormGroup) {
  ligne.get('idObjet')?.valueChanges.subscribe(idObjet => {
    const type = ligne.get('type')?.value;

    if (type === 'CHAMBRE') {
      const chambre = this.chambres.find(c => c.id === idObjet);
      if (chambre) {
        ligne.get('prixUnitaire')?.setValue(chambre.tarifParNuit || 0);
      }
    } else if (type === 'SALLE') {
      const salle = this.salles.find(s => s.id === idObjet);
      if (salle) {
        ligne.get('prixUnitaire')?.setValue(salle.tarif || 0);
      }
    }
  });
}

isFormValid(): boolean {
    return this.lignes.length > 0; // Le bouton est activé seulement si des lignes ont été ajoutées
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

calculerDifferenceEnJours(dateDebut: string, dateFin: string): number {
  const start = new Date(dateDebut);
  const end = new Date(dateFin);
  const diff = end.getTime() - start.getTime();
  return Math.ceil(diff / (1000 * 3600 * 24)) || 1; // Au moins 1 jour
}

  

   goToReservationList() {
    this.router.navigate(['/dashboard/reservations/liste']);
  }


getNombreJours(): number {
  const dateDebut = this.reservationForm.get('dateDebut')?.value;
  const dateFin = this.reservationForm.get('dateFin')?.value;
  return dateDebut && dateFin ? this.calculerDifferenceEnJours(dateDebut, dateFin) : 1;
}




   
}

