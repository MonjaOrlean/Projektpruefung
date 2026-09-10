import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Verfuegbarkeit } from '../models/verfuegbarkeit';

@Injectable({
  providedIn: 'root'
})
export class VerfuegbarkeitService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    'http://localhost:8080/api/verfuegbarkeiten';

  alleVerfuegbarkeitenLaden(): Observable<Verfuegbarkeit[]> {
    return this.http.get<Verfuegbarkeit[]>(this.apiUrl);
  }

  verfuegbarkeitNachIdLaden(
    id: number
  ): Observable<Verfuegbarkeit> {
    return this.http.get<Verfuegbarkeit>(
      `${this.apiUrl}/${id}`
    );
  }

  verfuegbarkeitAnlegen(verfuegbarkeit: {
    datum: string;
    startzeit: string;
    endzeit: string;
    bemerkung?: string;
    mitgliedId: number;
  }): Observable<Verfuegbarkeit> {

    return this.http.post<Verfuegbarkeit>(
      this.apiUrl,
      verfuegbarkeit
    );
  }

  verfuegbarkeitBearbeiten(
    id: number,
    verfuegbarkeit: {
      datum: string;
      startzeit: string;
      endzeit: string;
      bemerkung?: string;
      mitgliedId: number;
    }
  ): Observable<Verfuegbarkeit> {

    return this.http.put<Verfuegbarkeit>(
      `${this.apiUrl}/${id}`,
      verfuegbarkeit
    );
  }

  verfuegbarkeitLoeschen(id: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${id}`
    );
  }
}
