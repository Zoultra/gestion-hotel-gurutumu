 
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';
import {jwtDecode} from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
    
  private apiURL = `${environment.apiUrl}/auth/login`;
  
  constructor(private http: HttpClient,  private router: Router) {}

  login(username: string, password: string): Observable<any> {
    console.log(this.apiURL)
    return this.http.post<any>(this.apiURL, { username, password });
  }

  saveToken(token: string): void {
    localStorage.setItem('auth_token', token);
  }

  saveUser(utilisateur: string): void {
    localStorage.setItem('userInfo', JSON.stringify(utilisateur)); // Stocker l'utilisateur
  }

  getUser(): any {
    const utilisateur = localStorage.getItem('userInfo');
    return utilisateur ? JSON.parse(utilisateur) : null;
    
  }

   // Décoder le token pour récupérer les infos utilisateur
   getUserInfo(): any {
    const token = this.getToken();
    if (token) {
      try {
        return jwtDecode(token);
      } catch (error) {
        console.error('Erreur de décodage du token', error);
        return null;
      }
    }
    return null;
  }

  getUserFullName(): string {
    const userInfo = this.getUserInfo();
    return userInfo ? `${userInfo.prenom} ${userInfo.nom} ${userInfo.role}` : '';
  }

   getUserName(): string {
    const userInfo = this.getUserInfo();
    return userInfo ? `${userInfo.prenom} ${userInfo.nom}` : '';
  }

  getToken(): string | null {
    return localStorage.getItem('auth_token');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  
  logout(): void {
    localStorage.removeItem('auth_token'); // Supprime le token
   // sessionStorage.removeItem('token'); // Supprime si stocké en session
    localStorage.removeItem('userInfo'); // Supprimer les infos de l'utilisateur
    this.router.navigate(['/login']); // Redirige vers la page de connexion
  }

  isAuthenticated(): boolean {
    return localStorage.getItem('auth_token') !== null; 
  }

  getUserRole(): string {
  const userInfo = this.getUserInfo();
  return userInfo ? userInfo.role : '';
}


isTokenExpired(): boolean {
  const token = localStorage.getItem('auth_token');
  if (!token) return true;

  const decoded: any = jwtDecode(token);
  const currentTime = Math.floor(Date.now() / 1000);

  return decoded.exp < currentTime;
}

}
