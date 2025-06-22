import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Depense } from '../../models/depense';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DepenseService {
   
  private apiURL = `${environment.apiUrl}/depenses`;
  constructor(private http: HttpClient) {}

  getDepensesList(): Observable<Depense[]> {
    return this.http.get<Depense[]>(this.apiURL);
  }

  getDepenseById(id: number): Observable<Depense> {
    return this.http.get<Depense>(`${this.apiURL}/${id}`);
  }

  createDepense(depense: Depense): Observable<Depense> {
    return this.http.post<Depense>(this.apiURL, depense);
  }

  updateDepense(id: number, depense: Depense): Observable<Depense> {
    return this.http.put<Depense>(`${this.apiURL}/${id}`, depense);
  }

  deleteDepense(id: number): Observable<any> {
    return this.http.delete(`${this.apiURL}/${id}`);
  }

  getListDepensesEtTotal(dates: { startDateTime: string, endDateTime: string }): Observable<{ liste: any[], montantTotal: number }> {
  const { startDateTime, endDateTime } = dates;
  return this.http.get<{ liste: any[], montantTotal: number }>(`${this.apiURL}/list-depenses-montant-interval`, {
    params: { startDate: startDateTime, endDate: endDateTime }
  });
}

}
