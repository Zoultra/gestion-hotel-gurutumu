import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Salle } from '../../models/salle';

@Injectable({
  providedIn: 'root'
})
export class SalleService {

  
  private apiURL = `${environment.apiUrl}/salles`;

  constructor(private httpClient: HttpClient) { }

  getSalleList(): Observable<Salle[]> {
    return this.httpClient.get<Salle[]>(this.apiURL);
  }

  createSalle(salle: Salle): Observable<Object> {
    return this.httpClient.post(this.apiURL, salle);
  }

  updateSalle(id: number, salle: Salle): Observable<Object> {
    return this.httpClient.put(`${this.apiURL}/${id}`, salle);
  }

  deleteSalle(id: number): Observable<Object> {
    return this.httpClient.delete(`${this.apiURL}/${id}`);
  }

  getSalleById(id: number): Observable<Salle> {
    return this.httpClient.get<Salle>(`${this.apiURL}/${id}`);
  }
}
