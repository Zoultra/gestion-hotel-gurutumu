import { Injectable } from '@angular/core';
 

import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Vente } from '../../models/vente';
import { LigneVente } from '../../models/ligne-vente';

@Injectable({
  providedIn: 'root'
})
export class VenteService {

  private apiURL = `${environment.apiUrl}/ventes`;

  constructor(private httpClient: HttpClient) { }

  // Récupérer toutes les ventes
  getVenteList(): Observable<Vente[]> {
    return this.httpClient.get<Vente[]>(this.apiURL);
  }

  // Créer une nouvelle vente
  

  createVente(vente: Vente): Observable<Object> {
    const payload = {
      dateVente: vente.dateVente,
      etatVente: vente.etatVente,
      code: vente.code,
      montantTotal: vente.montantTotal,
      monnaie: vente.monnaie,
      montantPaye: vente.montantPaye,
      tableResto: vente.tableResto,
      lignes: vente.lignes.map(ligne => ({
        article_id: ligne.article.id,
        nomArticle: ligne.article.designation,
        quantite: ligne.quantite,
        prixUnitaire: ligne.prixUnitaire
      }))
    };
  console.log(vente)
    return this.httpClient.post(this.apiURL, payload);
  }
  
  // Récupérer une vente par ID
  getVenteById(id: number): Observable<Vente> {
    return this.httpClient.get<Vente>(`${this.apiURL}/${id}`);
  }

  // Supprimer une vente (optionnel)
  deleteVente(id: number): Observable<Object> {
    return this.httpClient.delete(`${this.apiURL}/${id}`);
  }

  

  //Recupperation des lignees d'une vente

  getLignesByVenteId(id: number): Observable<LigneVente[]> {
    return this.httpClient.get<LigneVente[]>(`${this.apiURL}/${id}/lignes`);
  }

  // Récupérer les 5 dernières ventes
  getLast50VenteList(): Observable<Vente[]> {
    return this.httpClient.get<Vente[]>(`${this.apiURL}/last-50-ventes`);
  }

  updateVente(id: number, vente: Vente): Observable<Vente> {
      return this.httpClient.put<Vente>(`${this.apiURL}/${id}`, vente);
    }
}
