import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Chambre } from '../../models/chambre';

@Injectable({
  providedIn: 'root'
})
export class ChambreService {

  private apiURL = `${environment.apiUrl}/chambres`;

  constructor(private httpClient: HttpClient) { }

  getChambreList(): Observable<Chambre[]> {
    return this.httpClient.get<Chambre[]>(this.apiURL);
  }

  createChambre(chambre: Chambre): Observable<Object> {
    return this.httpClient.post(this.apiURL, chambre);
  }

  updateChambre(id: number, chambre: Chambre): Observable<Object> {
    return this.httpClient.put(`${this.apiURL}/${id}`, chambre);
  }

  deleteChambre(id: number): Observable<Object> {
    return this.httpClient.delete(`${this.apiURL}/${id}`);
  }

  getChambreById(id: number): Observable<Chambre> {
    return this.httpClient.get<Chambre>(`${this.apiURL}/${id}`);
  }
}
