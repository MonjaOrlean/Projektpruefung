import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Einsatzbereich } from '../models/schicht';

@Injectable({
  providedIn: 'root'
})
export class EinsatzbereichService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    'http://localhost:8080/api/einsatzbereiche';

  alleEinsatzbereicheLaden(): Observable<Einsatzbereich[]> {
    return this.http.get<Einsatzbereich[]>(
      this.apiUrl
    );
  }

  einsatzbereicheFuerVeranstaltungLaden(
    veranstaltungId: number
  ): Observable<Einsatzbereich[]> {

    return this.http.get<Einsatzbereich[]>(
      `${this.apiUrl}/veranstaltung/${veranstaltungId}`
    );
  }

  einsatzbereichNachIdLaden(
    id: number
  ): Observable<Einsatzbereich> {

    return this.http.get<Einsatzbereich>(
      `${this.apiUrl}/${id}`
    );
  }

  einsatzbereichAnlegen(
    einsatzbereich: {
      name: string;
      beschreibung?: string;
      veranstaltungId: number;
    }
  ): Observable<Einsatzbereich> {

    return this.http.post<Einsatzbereich>(
      this.apiUrl,
      einsatzbereich
    );
  }

  einsatzbereichBearbeiten(
    id: number,
    einsatzbereich: {
      name: string;
      beschreibung?: string;
      veranstaltungId: number;
    }
  ): Observable<Einsatzbereich> {

    return this.http.put<Einsatzbereich>(
      `${this.apiUrl}/${id}`,
      einsatzbereich
    );
  }

  einsatzbereichLoeschen(
    id: number
  ): Observable<void> {

    return this.http.delete<void>(
      `${this.apiUrl}/${id}`
    );
  }
}
