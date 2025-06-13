import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Vente } from '../../models/vente';

@Injectable({
  providedIn: 'root'
})
export class PaiementService {

  private apiURL = `${environment.apiUrl}/paiement`;
   private apiURLVente = `${environment.apiUrl}/ventes`;

  constructor(private http: HttpClient) { }

 ajouterPaiement(factureId: number, paiementDto: any): Observable<any> {
  return this.http.post(`${this.apiURL}/facture/${factureId}`, paiementDto);
}

// paiement.service.ts
payerVente(venteId: number): Observable<Vente> {
  return this.http.put<Vente>(`${this.apiURLVente}/${venteId}/payer`, null); // Ou POST selon ton API
}


}
