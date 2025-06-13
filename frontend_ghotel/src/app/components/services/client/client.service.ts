import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Client } from '../../models/client';

@Injectable({
  providedIn: 'root'
})
export class ClientService {

  private apiURL = `${environment.apiUrl}/clients`;

  constructor(private httpClient: HttpClient) { }

  getClientList(): Observable<Client[]> {
    return this.httpClient.get<Client[]>(this.apiURL);
  }

  createClient(client: Client): Observable<Object> {
    return this.httpClient.post(this.apiURL, client);
  }

  updateClient(id: number, client: Client): Observable<Object> {
    return this.httpClient.put(`${this.apiURL}/${id}`, client);
  }

  deleteClient(id: number): Observable<Object> {
    return this.httpClient.delete(`${this.apiURL}/${id}`);
  }

  getClientById(id: number): Observable<Client> {
    return this.httpClient.get<Client>(`${this.apiURL}/${id}`);
  }
}
