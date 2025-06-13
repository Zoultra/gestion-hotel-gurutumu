import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private apiURL = `${environment.apiUrl}/dashboard`;
  filterForm: any;

  constructor(private http: HttpClient) {}

  getNombreChambresDispo(): Observable<number> {
  return this.http.get<number>(`${this.apiURL}/nombre-chambres-dispo`);
   }

    getNombreChambresTotal(): Observable<number> {
  return this.http.get<number>(`${this.apiURL}/nombre-chambres-total`);
   }

   getNombreSalles(): Observable<number> {
    return this.http.get<number>(`${this.apiURL}/nombre-salles`);
   }

   getNombreSallesDispo(): Observable<number> {
    return this.http.get<number>(`${this.apiURL}/nombre-salles-dispo`);
   }

    getTotalEntree2s(startDate: string, endDate: string): Observable<number> {
     return this.http.get<number>(`${this.apiURL}/total-entrees2`, {
         params: {
        startDate: this.filterForm.value.startDate,  // '2025-05-23'
        endDate: this.filterForm.value.endDate       // '2025-05-23'
        }
       });
     }

     getTotalEntrees(dates: { startDateTime: string, endDateTime: string }): Observable<any> {
       const { startDateTime, endDateTime } = dates;
        console.log('Recherche entre :', dates);
       return this.http.get(`${this.apiURL}/total-entrees`, {
       params: {
                  startDateTime,
                  endDateTime
                }
     });
   }

    getTotalDepenses(dates: { startDate: string, endDate: string }): Observable<any> {
       const { startDate, endDate } = dates;
       return this.http.get(`${this.apiURL}/total-depenses`, {
       params: {
                  startDate,
                  endDate
                }
     });
   }


    getSoldeReel(): Observable<number> {
    return this.http.get<number>(`${this.apiURL}/solde`);
   }

  getTotalEntreeResto(dates: { startDate: string, endDate: string }): Observable<number> {
  const { startDate, endDate } = dates;
  return this.http.get<number>(`${this.apiURL}/total-entrees-resto`, {
    params: { startDate, endDate }
  });
}

    getTotalEntreeReservation(dates: { startDate: string, endDate: string }): Observable<any> {
       const { startDate, endDate } = dates;
       return this.http.get(`${this.apiURL}/total-entrees-reservation`, {
       params: {
                  startDate,
                  endDate
                }
     });
   }
}
