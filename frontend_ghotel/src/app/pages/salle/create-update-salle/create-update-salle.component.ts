import { Component, OnInit } from '@angular/core';
import { SalleService } from '../../../components/services/salle/salle.service';
import { Router, ActivatedRoute } from '@angular/router';
import { NgToastService } from 'ng-angular-popup';
import { FormsModule } from '@angular/forms';
import { AlertService } from '../../../components/services/alert/alert.service';
import { Salle } from '../../../components/models/salle';

@Component({
  selector: 'app-create-update-salle',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './create-update-salle.component.html',
  styleUrl: './create-update-salle.component.scss'
})
export class CreateUpdateSalleComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private toast: NgToastService,
    private salleService: SalleService,
    private alertService: AlertService
  ) {}

  salle: Salle = new Salle();
  isEditMode: boolean = false;
  selectedSalle: any = null;
  errorMsgFromBackend: string | null = null;

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.isEditMode = true;
      this.salleService.getSalleById(id).subscribe(data => {
        this.salle = data;
      });
    }
  }

  onSubmit() {
    if (this.isEditMode) {
      this.salleService.updateSalle(this.salle.id, this.salle).subscribe(() => {
        this.alertService.success('Salle modifiée avec succès !');
        this.resetForm();
      });
    } else {
      this.salleService.createSalle(this.salle).subscribe({
    next: () => {
        this.errorMsgFromBackend = null;
        this.alertService.success('Salle enregistrée avec succès !');
        this.resetForm();
    },
     error: err => {
        this.alertService.error(this.errorMsgFromBackend = err?.error?.message || 'Erreur inconnue.')
           
       }
      });
    }
  }


 

  resetForm() {
    this.isEditMode = false;
    this.selectedSalle = null;
    this.goToSalleList();
  }

  goToSalleList() {
    this.router.navigate(['/dashboard/salles/liste']);
  }
}
