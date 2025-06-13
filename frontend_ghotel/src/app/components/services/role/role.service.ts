import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Role } from '../../models/role';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})

export class RoleService {
   
   
  private baseURLrole = `${environment.apiUrl}/roles`;
  
  constructor(private httpClient: HttpClient) { }

  getRoleList(): Observable<Role[]> {
    return this.httpClient.get<Role[]>(this.baseURLrole);
  }

  createRole(role: Role): Observable<Object>{
    return this.httpClient.post(`${this.baseURLrole}`, role);
  }

  updateRole(id: number, data: any): Observable<any> {
    return this.httpClient.put(`${this.baseURLrole}/${id}`, data);
  }

  deleteRole(id: number): Observable<String>{
    return this.httpClient.delete(`${this.baseURLrole}/${id}`, { responseType: 'text' });
  }
  getRoleById(id: number): Observable<Role>{
    return this.httpClient.get<Role>(`${this.baseURLrole}/${id}`);
  }
 
}

