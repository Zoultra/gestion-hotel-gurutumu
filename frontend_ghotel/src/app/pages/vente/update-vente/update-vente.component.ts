 import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  FormArray,
  ReactiveFormsModule,
  AbstractControl,
  Validators
} from '@angular/forms';
import { Vente } from '../../../components/models/vente';
import { ActivatedRoute, Router } from '@angular/router';
import { AlertService } from '../../../components/services/alert/alert.service';
import { ArticleService } from '../../../components/services/article/article.service';
import { VenteService } from '../../../components/services/vente/vente.service';
import { LigneVente } from '../../../components/models/ligne-vente';
import { Article } from '../../../components/models/article';

@Component({
  selector: 'app-update-vente',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './update-vente.component.html',
  styleUrls: ['./update-vente.component.scss'] // corrigé ici
})
export class UpdateVenteComponent implements OnInit {
  venteForm!: FormGroup;
  venteId!: number;
  articles: Article[] = [];
  isLoading = true;
  errorMsgFromBackend: string | null = null;
  errorMessage: string | null = null;
  vente: Vente = new Vente();
  fromPage: string = 'liste'; // valeur par défaut
  constructor(
    private fb: FormBuilder,
    private venteService: VenteService,
    private router: Router,
    private route: ActivatedRoute,
    private articleService: ArticleService,
    private alertService: AlertService
  ) {}

  ngOnInit(): void {
    this.initializeForm();
    this.loadData();
    this.calculerMontantTotal();
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.venteId = +id;
      this.loadVente(this.venteId);
    } else {
      this.errorMessage = 'ID de vente non fourni';
      this.isLoading = false;
    }

     this.route.queryParams.subscribe(params => {
     this.fromPage = params['from'] || 'liste'; // 'form' ou 'liste'
     
  });
  }

  
  

  initializeForm(): void {
    this.venteForm = this.fb.group({
      dateVente: ['', Validators.required],
      tableResto: ['', Validators.required],
      etatVente: ['', Validators.required],
      montantTotal: [0],
      lignesVentes: this.fb.array([])
    });
  }

  loadData(): void {
    this.articleService.getArticleList().subscribe({
      next: (articles) => (this.articles = articles),
      error: (err) => console.error('Erreur chargement articles', err)
    });
  }

  loadVente(id: number): void {
    this.venteService.getVenteById(id).subscribe({
      next: (response) => {
        if (response) {
          this.vente = response;
          this.initForm(this.vente);
        } else {
          this.errorMessage = 'Erreur lors du chargement de la vente';
        }
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Erreur lors du chargement de la vente';
        this.isLoading = false;
        console.error('Erreur', err);
      }
    });
  }

  initForm(vente: Vente): void {
    this.venteForm.patchValue({
      tableResto: vente.tableResto,
      dateVente: vente.dateVente,
      etatVente: vente.etatVente,
      montantTotal: vente.montantTotal
    });

    const lignesArray = this.fb.array(
      (vente.ligneVentes || []).map((ligne) => this.createLigneFormGroup(ligne))
    );

    this.venteForm.setControl('lignesVentes', lignesArray);
  }

  createLigneFormGroup(ligne?: LigneVente): FormGroup {
    return this.fb.group({
      article_id: [ligne?.article?.id || '', Validators.required],
      quantite: [ligne?.quantite || 1, Validators.required],
      prixUnitaire: [ligne?.prixUnitaire || 0, Validators.required]

    });
 
  }

  submitModification(): void {
  if (this.venteForm.invalid) {
    this.errorMessage = 'Veuillez remplir tous les champs obligatoires';
    return;
  }

   // Calcul du montant total
  this.calculerMontantTotal();

  const formValue = this.venteForm.value;
  if (!formValue.lignesVentes) {
    formValue.lignesVentes = []; // Initialize empty array if null
  }

  this.venteService.updateVente(this.venteId, formValue).subscribe({
   
    next: () => { console.log(formValue)
      this.alertService.confirmSuccess('Vente modifiée avec succès !', 2000)
     
      if (this.fromPage === 'form') {
          this.router.navigate(['/dashboard/ventes/pos']);
           } else {
           this.router.navigate(['/dashboard/ventes/liste']);
           }
    },
    error: (err) => {
      this.alertService.error(err?.error?.message || 'Erreur inconnue.');
      console.error('Erreur', err);
    }
  });
}


 onArticleChange(index: number): void {
  try {
    if (!this.articles?.length) {
      throw new Error('Liste des articles non chargée');
    }

    const ligne = this.lignesVentes.at(index) as FormGroup;
    const article_id = ligne.get('article_id')?.value;
    
    if (!article_id) return;

    // Conversion robuste des IDs
    const id = +article_id; // Equivalent à Number(articleId)
    if (isNaN(id)) throw new Error(`ID invalide: ${article_id}`);

    const selectedArticle = this.articles.find(a => +a.id === id);
    
    if (!selectedArticle) {
      throw new Error(`Article ${id} non trouvé. Disponibles: ${
        this.articles.map(a => a.id).join(', ')
      }`);
    }

    ligne.patchValue({
      prixUnitaire: selectedArticle.prixUnitaire,
      quantite: 1
    });

    this.updateLineTotal(index);
  } catch (error) {
    console.error('Erreur onArticleChange:', error);
    // Affichez un message à l'utilisateur si nécessaire
  }
}

updateLineTotal(index: number): void {
  const ligne = this.lignesVentes.at(index) as FormGroup;
  const prix = ligne.get('prixUnitaire')?.value || 0;
  const qte = ligne.get('quantite')?.value || 0;
  const total = prix * qte;
  
  // Si vous avez un champ total dans votre formGroup
  ligne.patchValue({ total: total });
}

  // Gestion des lignes
  get lignesVentes(): FormArray {
    return this.venteForm.get('lignesVentes') as FormArray;
  }

  

  addLigne(): void {
    this.lignesVentes.push(this.createLigneFormGroup());
     this.calculerMontantTotal();
  }

  removeLigne(index: number): void {
    this.lignesVentes.removeAt(index);
     this.calculerMontantTotal();
  }

  calculateLineTotal(ligne: AbstractControl): number {
    const prix = ligne.get('prixUnitaire')?.value || 0;
    const qte = ligne.get('quantite')?.value || 0;
    return prix * qte;
  }

 calculerMontantTotal(): void {
  let total = 0;
  this.lignesVentes.controls.forEach((ligne: AbstractControl) => {
    const prix = ligne.get('prixUnitaire')?.value || 0;
    const qte = ligne.get('quantite')?.value || 0;
    total += prix * qte;
  });

  this.venteForm.patchValue({ montantTotal: total });
}

annulerModification() {
   if (this.fromPage === 'form') {
          this.router.navigate(['/dashboard/ventes/pos']);
           } else {
           this.router.navigate(['/dashboard/ventes/liste']);
           }
 
}
}
