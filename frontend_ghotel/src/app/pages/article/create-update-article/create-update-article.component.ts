import { Component, OnInit } from '@angular/core';
import { ArticleService } from '../../../components/services/article/article.service';
import { Router, ActivatedRoute } from '@angular/router';
import { NgToastService } from 'ng-angular-popup';
import { FormsModule, FormGroup, FormBuilder, ReactiveFormsModule} from '@angular/forms';
import { AlertService } from '../../../components/services/alert/alert.service';
import { Article } from '../../../components/models/article';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-create-update-article',
  standalone: true,
  imports: [FormsModule,ReactiveFormsModule,CommonModule],
  templateUrl: './create-update-article.component.html',
  styleUrls: ['./create-update-article.component.scss']
})
export class CreateUpdateArticleComponent implements OnInit {

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private toast: NgToastService,
    private articleService: ArticleService,
    private alertService: AlertService,
    private fb: FormBuilder, private http: HttpClient
  ) { this.articleForm = this.fb.group({
      designation: [''],
      prixUnitaire: ['']
    });}

  articleForm: FormGroup;
  selectedFile!: File;

  article: Article = new Article();
  isEditMode: boolean = false;
  selectedArticle: any = null;
  errorMsgFromBackend: string | null = null;

   

  ngOnInit() {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.isEditMode = true;
      this.articleService.getArticleById(id).subscribe(data => {
  this.article = data;
  this.articleForm.patchValue({
    designation: data.designation,
    prixUnitaire: data.prixUnitaire
  });
});
    }
  }

/**
 imageFile: File | null = null;
imagePreview: string | ArrayBuffer | null = null;

 
onFileChange(event: Event): void {
  const input = event.target as HTMLInputElement;
  if (input.files && input.files.length > 0) {
    const file = input.files[0];
    this.imageFile = file;

    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result;
    };
    reader.readAsDataURL(file);
  }
}

  * Creates an instance of documenter.
 
  onSubmitData() {
    const formData = new FormData();
    formData.append('designation', this.articleForm.get('designation')?.value);
    formData.append('prixUnitaire', this.articleForm.get('prixUnitaire')?.value);
    if (this.imageFile) {
    formData.append('image', this.imageFile);
  }

   if (this.isEditMode) {
    this.articleService.updateArticle(this.article.id, formData).subscribe({
      next: () => {
        this.alertService.success('Article modifié avec succès !');
        this.imageFile = null;
        this.imagePreview = null;
      },
      error: err => {
        this.alertService.error('Erreur lors de la modification.');
      }
    });
  } else{   
 
   this.articleService.createArticle(formData)
    .subscribe({
       next: () => {
      this.alertService.success('Article enregistré avec succès !');
      this.articleForm.reset();
      this.imageFile = null;
      this.imagePreview = null;
      setTimeout(() => {
          window.location.reload();
        }, 4000);
     
    },
    error: err => {
      this.alertService.error('Erreur lors de l\'enregistrement.');
    }
      });
}
  }
 */


     onSubmit() {
     if (this.isEditMode) {
       this.articleService.updateArticle(this.article.id, this.article).subscribe(() => {
      this.alertService.success('Article modifié avec succès !');
        this.resetForm();
       });
     }  else {
      
        this.articleService.createArticle(this.article).subscribe({
      next: () => {
         this.errorMsgFromBackend = null;
         this.alertService.success('Article enregistrée avec succès !');
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
    this.selectedArticle = null;
    this.goToArticleList();
  }

  goToArticleList() {
    this.router.navigate(['/dashboard/articles/liste']);
  }
}
