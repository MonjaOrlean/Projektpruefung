import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Veranstaltung } from '../models/veranstaltung';

@Injectable({
  providedIn: 'root'
})
export class VeranstaltungService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/veranstaltungen';

  alleVeranstaltungenLaden(): Observable<Veranstaltung[]> {
    return this.http.get<Veranstaltung[]>(this.apiUrl);
  }

  veranstaltungNachIdLaden(id: number): Observable<Veranstaltung> {
    return this.http.get<Veranstaltung>(`${this.apiUrl}/${id}`);
  }

  veranstaltungAnlegen(
    veranstaltung: Omit<Veranstaltung, 'id'>
  ): Observable<Veranstaltung> {
    return this.http.post<Veranstaltung>(
      this.apiUrl,
      veranstaltung
    );
  }

  veranstaltungBearbeiten(
    id: number,
    veranstaltung: Omit<Veranstaltung, 'id'>
  ): Observable<Veranstaltung> {
    return this.http.put<Veranstaltung>(
      `${this.apiUrl}/${id}`,
      veranstaltung
    );
  }

  veranstaltungLoeschen(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
