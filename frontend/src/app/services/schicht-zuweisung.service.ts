import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Mitglied } from '../models/mitglied';

import {
  SchichtBesetzungsstatus,
  SchichtZuweisung
} from '../models/schicht-zuweisung';

@Injectable({
  providedIn: 'root'
})
export class SchichtZuweisungService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    'http://localhost:8080/api/schicht-zuweisungen';

  alleZuweisungenLaden(): Observable<SchichtZuweisung[]> {

    return this.http.get<SchichtZuweisung[]>(
      this.apiUrl
    );
  }

  zuweisungenNachSchichtLaden(
    schichtId: number
  ): Observable<SchichtZuweisung[]> {

    return this.http.get<SchichtZuweisung[]>(
      `${this.apiUrl}/schicht/${schichtId}`
    );
  }

  zuweisungenNachMitgliedLaden(
    mitgliedId: number
  ): Observable<SchichtZuweisung[]> {

    return this.http.get<SchichtZuweisung[]>(
      `${this.apiUrl}/mitglied/${mitgliedId}`
    );
  }

  zuweisungAnlegen(
    schichtId: number,
    mitgliedId: number
  ): Observable<SchichtZuweisung> {

    return this.http.post<SchichtZuweisung>(
      this.apiUrl,
      {
        schichtId,
        mitgliedId
      }
    );
  }

  zuweisungLoeschen(
    id: number
  ): Observable<void> {

    return this.http.delete<void>(
      `${this.apiUrl}/${id}`
    );
  }

  besetzungsstatusLaden(
    schichtId: number
  ): Observable<SchichtBesetzungsstatus> {

    return this.http.get<SchichtBesetzungsstatus>(
      `${this.apiUrl}/schicht/${schichtId}/status`
    );
  }

  verfuegbareMitgliederFuerSchichtLaden(
    schichtId: number
  ): Observable<Mitglied[]> {

    return this.http.get<Mitglied[]>(
      `${this.apiUrl}/schicht/${schichtId}/verfuegbare-mitglieder`
    );
  }
}
