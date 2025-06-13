import { Component, OnInit } from '@angular/core';
import { Article } from '../../../components/models/article';  // Modifié pour Article
import { ArticleService } from '../../../components/services/article/article.service';  // Modifié pour ArticleService
import { ActivatedRoute, Router } from '@angular/router';
import { NgxPaginationModule } from 'ngx-pagination';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FilterPipe } from '../../../pipes/filter.pipe';
import { AlertService } from '../../../components/services/alert/alert.service';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-list-article',  // Modifié pour ListArticleComponent
  standalone: true,
  imports: [NgxPaginationModule, FormsModule, CommonModule, FilterPipe],
  templateUrl: './list-article.component.html',  // Modifié pour list-article.component.html
  styleUrl: './list-article.component.scss'  // Modifié pour list-article.component.scss
})
export class ListArticleComponent implements OnInit {  // Modifié pour ListArticleComponent
  
  id!: number;
  myFilterText!: any;
  title = 'Pagination';
  page: number = 1;
  count: number = 0;
  tableSize: number = 5;
  tableSizes: any = [5, 10, 15, 20];
  imageUrl: SafeUrl | null = null;
  article: any;
  articles: any[] = [];
  isLoading = true;
  displayedColumns: string[] = ['image', 'id', 'designation', 'prixVente'];

  public apiURL = `${environment.apiUrl}/articles/images/`;
  
  constructor(
    private articleService: ArticleService,  // Modifié pour ArticleService
    private alertService: AlertService,
    private route: ActivatedRoute,
    private router: Router,
    private sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.getArticleList();  // Modifié pour getArticleList()
  }

 

  private getArticleList() {
    this.articleService.getArticleList().subscribe({
      next: (response: any) => {
        console.log('Données reçues :', response);
        if (response) {
          this.articles = response;  // Modifié pour articles
        }
      },
      error: (err) => {
        console.error('Erreur lors du chargement des articles', err);
      }
    });
  }

  deleteArticle(id: number) {  // Modifié pour deleteArticle()
    this.alertService.confirmDelete(() => {
      this.articleService.deleteArticle(id).subscribe(() => {
        this.alertService.success("Article supprimé avec succès !");
        this.getArticleList();  // Modifié pour getArticleList()
      }, error => {
        this.alertService.error("La suppression a échoué.");
      });
    });
  }

  onTableDataChange(event: any) {
    this.page = event;
    this.getArticleList();  // Modifié pour getArticleList()
  }

  onTableSizeChange(event: any) {
    this.tableSize = event.target.value;
    this.page = 1;
    this.getArticleList();  // Modifié pour getArticleList()
  }

  updateArticle(id: number) {  // Modifié pour updateArticle()
    this.router.navigate(['/dashboard/articles/update', id]);  // Modifié pour articles
  }

  reloadPage() {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigate(['./'], {
      relativeTo: this.route
    });
  }

  goToAddArticle() {  // Modifié pour goToAddArticle()
    this.router.navigate(['/dashboard/articles/add']);  // Modifié pour articles
  }

}
