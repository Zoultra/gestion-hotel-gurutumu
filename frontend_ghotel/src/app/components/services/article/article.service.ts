import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Article } from '../../models/article';  


import { map } from 'rxjs/operators';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';


@Injectable({
  providedIn: 'root'
})
export class ArticleService {

  private apiURL = `${environment.apiUrl}/articles`;
  //private apiURLUpload = `${environment.apiUrl}/articles/upload`;

  

  constructor(private httpClient: HttpClient) { }

  getArticleList(): Observable<Article[]> {
    return this.httpClient.get<Article[]>(this.apiURL);
  }
// Pour la version image mettre Formdata a la place de Article
  createArticle(article: Article): Observable<Object> {
    return this.httpClient.post(`${this.apiURL}`, article);
  }
  // Pour la version image mettre Formdata a la place de Article
  updateArticle(id: number, article: Article): Observable<Object> {
    return this.httpClient.put(`${this.apiURL}/${id}`, article);
  }

  deleteArticle(id: number): Observable<Object> {
    return this.httpClient.delete(`${this.apiURL}/${id}`);
  }

  getArticleById(id: number): Observable<Article> {
    return this.httpClient.get<Article>(`${this.apiURL}/${id}`);
  }

  getArticleImage(id: number): Observable<Blob> {
    return this.httpClient.get(`${this.apiURL}/${id}/image`, { responseType: 'blob' });
  }

  
}
