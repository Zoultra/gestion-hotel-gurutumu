// article.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
interface ArticleDto {
  id: number;
  designation: string;
  prixVente: number;
  imageUrl: string;
}

@Injectable({ providedIn: 'root' })
export class ArticleService1 {

 private apiURL = `${environment.apiUrl}/articles`;

  constructor(private http: HttpClient) {}

  getArticles(): Observable<ArticleDto[]> {
    return this.http.get<ArticleDto[]>(this.apiURL);
  }

  getArticleImage(id: number): Observable<Blob> {
    return this.http.get(`${this.apiURL}/${id}/image`, { 
      responseType: 'blob' 
    });
  }
}