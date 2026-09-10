import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Schicht } from '../models/schicht';

@Injectable({
  providedIn: 'root'
})
export class SchichtService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/schichten';

  alleSchichtenLaden(): Observable<Schicht[]> {
    return this.http.get<Schicht[]>(this.apiUrl);
  }

  schichtNachIdLaden(id: number): Observable<Schicht> {
    return this.http.get<Schicht>(
      `${this.apiUrl}/${id}`
    );
  }

  schichtAnlegen(schicht: {
    name: string;
    datum: string;
    startzeit: string;
    endzeit: string;
    benoetigtePersonen: number;
    beschreibung?: string;
    veranstaltungId?: number | null;
  }): Observable<Schicht> {

    return this.http.post<Schicht>(
      this.apiUrl,
      schicht
    );
  }

  schichtBearbeiten(
    id: number,
    schicht: {
      name: string;
      datum: string;
      startzeit: string;
      endzeit: string;
      benoetigtePersonen: number;
      beschreibung?: string;
      veranstaltungId?: number | null;
    }
  ): Observable<Schicht> {

    return this.http.put<Schicht>(
      `${this.apiUrl}/${id}`,
      schicht
    );
  }

  schichtLoeschen(id: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${id}`
    );
  }
}
