import { Component, OnInit } from '@angular/core';
import { AlertService } from '../../../components/services/alert/alert.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FilterPipe } from '../../../pipes/filter.pipe';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MouvementCaisseService } from '../../../components/services/mouvementCaisse/mouvement-caisse.service';
 

@Component({
  selector: 'app-ouverture-caisse',
  standalone: true,
  imports: [FilterPipe,CommonModule,FormsModule,ReactiveFormsModule],
  templateUrl: './ouverture-caisse.component.html',
  styleUrl: './ouverture-caisse.component.scss'
})
export class OuvertureCaisseComponent implements OnInit{

  ouvertureForm!: FormGroup;

  constructor(private alertService: AlertService,
    private route: ActivatedRoute, private router: Router,
     private fb: FormBuilder,
      private mouvementCaisseService: MouvementCaisseService,
      
    ) { }
    
    


    submitOuverture(): void {
        this.alertService.confirmFermetureCaisse('Confirmation du montant',this.ouvertureForm.value.soldeInitial,() => { 
    if (this.ouvertureForm.invalid) return;
    const data = {
      soldeInitial: this.ouvertureForm.value.soldeInitial,
      
    };

    this.mouvementCaisseService.ouvrirCaisse(data).subscribe({
      next: () => {
        this.alertService.confirmSuccess('Caisse ouverte avec succès.', 3000);
        
        this.router.navigate(['/dashboard/caisses/gestion']);
      },
      error: (err) => {
       
         this.alertService.confirmError('Erreur : ' + err.error.message, 3000);
      }
    });
  })
  }

   ngOnInit(): void {
    this.ouvertureForm = this.fb.group({
      soldeInitial: [null, [Validators.required, Validators.min(0)]]
    });
  }

   goToGestion(){ this.router.navigate(['/dashboard/caisses/gestion']);}
}
