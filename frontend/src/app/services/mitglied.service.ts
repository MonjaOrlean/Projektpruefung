import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Mitglied } from '../models/mitglied';

@Injectable({
  providedIn: 'root'
})
export class MitgliedService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/api/mitglieder';

  alleMitgliederLaden(): Observable<Mitglied[]> {
    return this.http.get<Mitglied[]>(this.apiUrl);
  }

  mitgliedNachIdLaden(id: number): Observable<Mitglied> {
    return this.http.get<Mitglied>(`${this.apiUrl}/${id}`);
  }

  mitgliedAnlegen(mitglied: Omit<Mitglied, 'id'>): Observable<Mitglied> {
    return this.http.post<Mitglied>(this.apiUrl, mitglied);
  }

  mitgliedBearbeiten(id: number, mitglied: Omit<Mitglied, 'id'>): Observable<Mitglied> {
    return this.http.put<Mitglied>(`${this.apiUrl}/${id}`, mitglied);
  }

  mitgliedLoeschen(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
