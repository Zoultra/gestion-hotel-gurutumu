import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { MouvementCaisse, RecapJournalierDto } from '../../models/mouvementCaisse';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class MouvementCaisseService {

    private apiURL = `${environment.apiUrl}/caisse`;
    
    constructor(private http: HttpClient) {}
  
    getMvtList(): Observable<MouvementCaisse[]> {
      return this.http.get<MouvementCaisse[]>(this.apiURL);
    }

    getRecapJournalier(date: string): Observable<RecapJournalierDto> {
    return this.http.get<RecapJournalierDto>(`${this.apiURL}/operations-journaliere?date=${date}`);
  }

   getListCaisseDuJour(): Observable<RecapJournalierDto> {
    return this.http.get<RecapJournalierDto>(`${this.apiURL}/caisse-du-jour`);
  }

   getListCaisse(): Observable<RecapJournalierDto> {
    return this.http.get<RecapJournalierDto>(`${this.apiURL}/toutes-caisses`);
  }
  ouvrirCaisse(data: any) {
    return this.http.post(`${this.apiURL}/ouverture`, data);
  }

  
fermerCaisse(payload: { caisseId: number, soldeFinal: number }) {
  return this.http.post(`${this.apiURL}/fermeture/${payload.caisseId}`, {
    soldeFinal: payload.soldeFinal
  });
}

getRecapitulatifEntreDates(dates: { startDateTime: string, endDateTime: string }): Observable<RecapJournalierDto[]> {
    const { startDateTime, endDateTime } = dates;
    console.log('Recherche entre :', dates);
    return this.http.get<RecapJournalierDto[]>(`${this.apiURL}/operations-journaliere-interval`, 
      {  params: {
                  startDateTime,
                  endDateTime
                } });
  }

   

}
