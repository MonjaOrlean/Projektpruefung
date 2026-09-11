import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { EinsatzplanSchicht } from '../models/einsatzplan-schicht';

@Injectable({
  providedIn: 'root'
})
export class EinsatzplanService {

  private readonly http = inject(HttpClient);

  private readonly apiUrl =
    'http://localhost:8080/api/einsatzplan';

  einsatzplanLaden(): Observable<EinsatzplanSchicht[]> {

    return this.http.get<EinsatzplanSchicht[]>(
      this.apiUrl
    );
  }

  einsatzplanExportieren(): Observable<Blob> {

    return this.http.get(
      `${this.apiUrl}/export`,
      {
        responseType: 'blob'
      }
    );
  }
}
