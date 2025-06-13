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
import { DomSanitizer } from '@angular/platform-browser';
declare var bootstrap: any;
 

@Component({
  selector: 'app-gestion-caisse',
  standalone: true,
  imports: [FilterPipe,CommonModule,FormsModule,ReactiveFormsModule,NgxPaginationModule],
  templateUrl: './gestion-caisse.component.html',
  styleUrl: './gestion-caisse.component.scss'
})
export class GestionCaisseComponent {

  fermetureForm!: FormGroup;
  caisseEnCours: any;
  caisses: any[] = [];
  aujourdHui: Date = new Date();
  @Input() caisseId!: number;
 
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
   private modalService: NgbModal,
    private sanitizer: DomSanitizer
  ) { 

    this.fermetureForm = this.fb.group({
      soldeFinal: [null, Validators.required],
     caisseId: [null, Validators.required]
    });

    }


   ngOnInit(): void {
    this.getListCaisseDuJour();
     this.fermetureForm.patchValue({ caisseId: this.caisseId });
  }

 ouvrirModalFermeture(caisse: any) {
  this.caisseEnCours = caisse;

  // Met à jour le formulaire avec l'ID de la caisse sélectionnée
  this.fermetureForm.patchValue({
    caisseId: caisse.caisseId
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

 
   private getListCaisseDuJour(){
    this.mouvementCaisseService.getListCaisseDuJour().subscribe({
      next: (response: any) => {
        console.log('Données reçues :', response); // 🔍 Vérifier la structure dans la console
        if (response) {
          this.caisses = response.caisses; // ✅ Extraire `data`
        }
      },
      error: (err) => {
        console.error('Erreur lors du chargement des caisse', err);
      }
    });

  }

  ouvrirCaisse(){ this.router.navigate(['/dashboard/caisses/ouverture']);}

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
    this.getListCaisseDuJour()
  }

  onTableSizeChange(event: any){
    this.tableSize = event.target.value;
    this.page = 1;
    this.getListCaisseDuJour()
  }
}
