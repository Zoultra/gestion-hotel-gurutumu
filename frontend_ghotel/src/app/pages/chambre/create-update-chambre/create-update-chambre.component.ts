
import { Component, OnInit } from '@angular/core';
import { ChambreService } from '../../../components/services/chambre/chambre.service';
import { Router, ActivatedRoute } from '@angular/router';
import { NgToastService } from 'ng-angular-popup';
import { FormsModule } from '@angular/forms';
import { AlertService } from '../../../components/services/alert/alert.service';
import { Chambre } from '../../../components/models/chambre';
@Component({
  selector: 'app-create-update-chambre',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './create-update-chambre.component.html',
  styleUrl: './create-update-chambre.component.scss'
})
export class CreateUpdateChambreComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private toast: NgToastService,
    private chambreService: ChambreService,
    private alertService: AlertService
  ) {}

  chambre: Chambre = new Chambre();
  isEditMode: boolean = false;
  selectedChambre: any = null;
  errorMsgFromBackend: string | null = null;

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.isEditMode = true;
      this.chambreService.getChambreById(id).subscribe(data => {
        this.chambre = data;
      });
    }
  }

  onSubmit() {
    if (this.isEditMode) {
      this.chambreService.updateChambre(this.chambre.id, this.chambre).subscribe(() => {
        this.alertService.success('Chambre modifiée avec succès !');
        this.resetForm();
      });
    } else {
      this.chambreService.createChambre(this.chambre).subscribe({
    next: () => {
        this.errorMsgFromBackend = null;
        this.alertService.success('Chambre enregistrée avec succès !');
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
    this.selectedChambre = null;
    this.goToChambreList();
  }

  goToChambreList() {
    this.router.navigate(['/dashboard/chambres/liste']);
  }
}

