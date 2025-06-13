 
// article-list.component.ts
import { Component, OnInit } from '@angular/core';
 
import { ArticleService1 } from '../../../components/services/article/article1.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-gestion-article',
  standalone: true,
   
   imports: [FormsModule, CommonModule],
  templateUrl: './gestion-article.component.html',
  styleUrl: './gestion-article.component.scss'
})
export class GestionArticleComponent implements OnInit {
  articles: any[] = [];
  loading = true;

  constructor(private articleService1: ArticleService1) {}

  ngOnInit(): void {
    this.loadArticles();
  }

  loadArticles(): void {
    this.articleService1.getArticles().subscribe({
      next: (articles) => {
        this.articles = articles;
        this.loading = false;
        console.log(articles)
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  formatPrice(price: number): string {
    return price.toFixed(2) + ' FCFA';
  }
}
