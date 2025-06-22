import { Component, Input } from '@angular/core';
import { FilterPipe } from '../../../pipes/filter.pipe';
import { Caisse } from '../../../components/models/caisse';
import { MouvementCaisseService } from '../../../components/services/mouvementCaisse/mouvement-caisse.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AlertService } from '../../../components/services/alert/alert.service';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NgxPaginationModule } from 'ngx-pagination';
import { MouvementCaisse } from '../../../components/models/mouvementCaisse';
declare var bootstrap: any;
@Component({
  selector: 'app-historique-caisse',
  standalone: true,
  imports: [FilterPipe,CommonModule,FormsModule,ReactiveFormsModule,NgxPaginationModule],
  templateUrl: './historique-caisse.component.html',
  styleUrl: './historique-caisse.component.scss'
})
export class HistoriqueCaisseComponent {

  fermetureForm!: FormGroup;
  caisseEnCours: any;
  caisses: any[] = [];
  aujourdHui: Date = new Date();
  @Input() id!: number;
  caisseId!: number;

  myFilterText!:any
  
  title = 'Pagination'
  page: number = 1
  count: number = 0;
  tableSize: number = 5;
  tableSizes: any = [5, 10, 15, 20]

  
    displayedColumns: string[] = ['id', 'description', 'type', 'montant', 'date'];
    mouvement: MouvementCaisse[] = [];

  constructor(private alertService: AlertService,
  private route: ActivatedRoute, private router: Router,
  private mouvementCaisseService: MouvementCaisseService,
   private fb: FormBuilder,
   private modalService: NgbModal
  ) { this.fermetureForm = this.fb.group({
      soldeFinal: [null, Validators.required],
     caisseId: [null, Validators.required]
    });
 }

   ngOnInit(): void {
this.getListCaisse();
 this.fermetureForm.patchValue({ caisseId: this.caisseId });
  } 
  
   private getListCaisse(){
    this.mouvementCaisseService.getListCaisse().subscribe({
      next: (response: any) => {
        console.log('Données reçues :', response); // 🔍 Vérifier la structure dans la console
        if (response) {
          this.caisses = response; // ✅ Extraire `data`
        }
      },
      error: (err) => {
        console.error('Erreur lors du chargement des caisse', err);
      }
    });

  }

  ouvrirModalFermeture(caisse: any) {
  this.caisseEnCours = caisse;

  // Met à jour le formulaire avec l'ID de la caisse sélectionnée
  this.fermetureForm.patchValue({
    caisseId: caisse.id
  });

  // Ouvre le modal Bootstrap via son ID
  const modalElement = document.getElementById('fermetureModal');
  if (modalElement) {
    const modal = new bootstrap.Modal(modalElement);
    modal.show();
  }
}

fermerCaisse() {
  this.alertService.confirmFermetureCaisse('Confirmation du montant',this.fermetureForm.value.soldeFinal,() => { 
  if (this.fermetureForm.invalid) return;

  const data = this.fermetureForm.value;
  console.log("Données de fermeture :", data);

  // Appel au backend
  this.mouvementCaisseService.fermerCaisse(data).subscribe({

    next: (res) => {
      
      console.log('Caisse fermée avec succès', res);
       this.alertService.confirmSuccess('Caisse fermée avec succès', 3000)
      // Ferme le modal après succès
      const modalElement = document.getElementById('fermetureModal');
      if (modalElement) bootstrap.Modal.getInstance(modalElement)?.hide();

      this.reloadPage(); // Recharge la liste
    },
    error: (err) => {
      console.error('Erreur lors de la fermeture :', err);
    }
  });
})
}
  // Méthode pour basculer l'affichage des détails
  toggleDetails(caisse: Caisse): void {
    caisse.showDetails = !caisse.showDetails;
    
   //  Optionnel: Fermer les autres détails ouverts
       this.caisses.forEach(v => {
          if (v !== caisse) v.showDetails = false;
        });
  }

  
  reloadPage(){ window.location.reload();}
  
  onTableDataChange(event: any){
    this.page = event;
    this.getListCaisse()
  }

  onTableSizeChange(event: any){
    this.tableSize = event.target.value;
    this.page = 1;
    this.getListCaisse()
  }

}
