import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-ajout-paiement-modal',
  standalone: true,
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './ajout-paiement-modal.component.html',
  styleUrl: './ajout-paiement-modal.component.scss'
})
export class AjoutPaiementModalComponent {

 
  @Input() factureId!: number;
  @Output() paiementAjoute = new EventEmitter<any>();

  paiementForm: FormGroup;
  isVisible = false;

  constructor(private fb: FormBuilder) {
    this.paiementForm = this.fb.group({
      montant: [null, [Validators.required, Validators.min(1)]],
      commentaire: ['N/A'],
      modePaiement: ['', Validators.required],
      datePaiement: ['', Validators.required]
    });
  }

  openModal() {
    this.isVisible = true;
  }

  closeModal() {
    this.isVisible = false;
    this.paiementForm.reset();
  }

  soumettre() {
    if (this.paiementForm.valid) {
      const paiementData = this.paiementForm.value;
      this.paiementAjoute.emit({ factureId: this.factureId, ...paiementData });
      this.closeModal();
    }
  }
}
