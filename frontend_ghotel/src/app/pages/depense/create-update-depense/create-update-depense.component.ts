import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { NgToastService } from 'ng-angular-popup';
import { FormsModule } from '@angular/forms';
import { AlertService } from '../../../components/services/alert/alert.service';
import { DepenseService } from '../../../components/services/depense/depense.service';
import { Depense } from '../../../components/models/depense';

@Component({
  selector: 'app-create-update-depense',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './create-update-depense.component.html',
  styleUrl: './create-update-depense.component.scss'
})
export class CreateUpdateDepenseComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private toast: NgToastService,
    private depenseService: DepenseService,
    private alertService: AlertService
  ) {}

  depense: Depense = new Depense();

  isEditMode: boolean = false;
  selectedDepense: any = null;
  errorMsgFromBackend: string | null = null;


  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.isEditMode = true;
      this.depenseService.getDepenseById(id).subscribe(data => {
        this.depense = data;
      });
    }
  }

  onSubmit() {
    if (this.isEditMode) {
      this.depenseService.updateDepense(this.depense.id, this.depense).subscribe({
    next: () => {
        this.alertService.success('Dépense modifiée avec succès !');
        this.resetForm();
        },

     error: err => {
        this.alertService.error(this.errorMsgFromBackend = err?.error?.message || 'Erreur inconnue.')
           
       }
       
      });
    } else {
      this.depenseService.createDepense(this.depense).subscribe({
    next: () => {
       this.errorMsgFromBackend = null;
        this.alertService.success('Dépense enregistrée avec succès !');
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
    this.selectedDepense = null;
    this.goToDepenseList();
  }

  goToDepenseList() {
    this.router.navigate(['/dashboard/depenses/liste']);
  }
}
