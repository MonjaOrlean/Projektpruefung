import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Aufgabe } from '../models/aufgabe';

@Injectable({
  providedIn: 'root'
})
export class AufgabeService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/aufgaben';

  alleAufgabenLaden(): Observable<Aufgabe[]> {
    return this.http.get<Aufgabe[]>(this.apiUrl);
  }

  aufgabeNachIdLaden(id: number): Observable<Aufgabe> {
    return this.http.get<Aufgabe>(`${this.apiUrl}/${id}`);
  }

  aufgabeAnlegen(aufgabe: {
    titel: string;
    beschreibung?: string;
    veranstaltungId?: number | null;
  }): Observable<Aufgabe> {
    return this.http.post<Aufgabe>(
      this.apiUrl,
      aufgabe
    );
  }

  aufgabeBearbeiten(
    id: number,
    aufgabe: {
      titel: string;
      beschreibung?: string;
      erledigt: boolean;
      veranstaltungId?: number | null;
    }
  ): Observable<Aufgabe> {
    return this.http.put<Aufgabe>(
      `${this.apiUrl}/${id}`,
      aufgabe
    );
  }

  aufgabeLoeschen(id: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${id}`
    );
  }
}
