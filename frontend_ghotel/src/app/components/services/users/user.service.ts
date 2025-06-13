import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Utilisateur } from '../../models/user';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})

export class UserService {
   
 
  
  private apiURL = `${environment.apiUrl}/utilisateurs`;

  constructor(private httpClient: HttpClient) { }

  
  getUserList(): Observable<Utilisateur[]> {
    
   
    return this.httpClient.get<Utilisateur[]>(this.apiURL);

    
  }

  createUser(utilisateur: Utilisateur): Observable<Object>{
    return this.httpClient.post(`${this.apiURL}`, utilisateur);
  }
  
  updateUser(id: number, utilisateur: Utilisateur): Observable<Object>{
    return this.httpClient.put(`${this.apiURL}/${id}`, utilisateur);
  }

  deleteUser(id: number): Observable<Object>{
    return this.httpClient.delete(`${this.apiURL}/${id}`);
  }
  getUserById(id: number): Observable<Utilisateur>{
    return this.httpClient.get<Utilisateur>(`${this.apiURL}/${id}`);
  }
 
}

