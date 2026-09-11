import {
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';

import { EinsatzplanSchicht } from '../../models/einsatzplan-schicht';
import { EinsatzplanService } from '../../services/einsatzplan.service';

@Component({
  selector: 'app-einsatzplan',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './einsatzplan.html',
  styleUrl: './einsatzplan.scss'
})
export class Einsatzplan implements OnInit {

  private readonly einsatzplanService =
    inject(EinsatzplanService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  schichten: EinsatzplanSchicht[] = [];

  fehlermeldung = '';

  wirdGeladen = false;

  wirdExportiert = false;

  ngOnInit(): void {
    this.einsatzplanLaden();
  }

  einsatzplanLaden(): void {

    this.fehlermeldung = '';
    this.wirdGeladen = true;

    this.cdr.markForCheck();

    this.einsatzplanService
      .einsatzplanLaden()
      .subscribe({
        next: (daten) => {

          console.log(
            'Einsatzplan geladen:',
            daten
          );

          this.schichten = daten;

          this.wirdGeladen = false;

          this.cdr.markForCheck();
        },

        error: (fehler) => {

          console.error(
            'Einsatzplan konnte nicht geladen werden:',
            fehler
          );

          this.fehlermeldung =
            'Der Einsatzplan konnte nicht geladen werden.';

          this.wirdGeladen = false;

          this.cdr.markForCheck();
        }
      });
  }

  einsatzplanExportieren(): void {

    this.fehlermeldung = '';
    this.wirdExportiert = true;

    this.cdr.markForCheck();

    this.einsatzplanService
      .einsatzplanExportieren()
      .subscribe({
        next: (datei) => {

          const url =
            window.URL.createObjectURL(datei);

          const link =
            document.createElement('a');

          link.href = url;

          const heute = new Date()
            .toISOString()
            .slice(0, 10);

          link.download =
            `einsatzplan_${heute}.csv`;

          document.body.appendChild(link);

          link.click();

          document.body.removeChild(link);

          window.URL.revokeObjectURL(url);

          this.wirdExportiert = false;

          this.cdr.markForCheck();
        },

        error: (fehler) => {

          console.error(
            'Einsatzplan konnte nicht exportiert werden:',
            fehler
          );

          this.fehlermeldung =
            'Der Einsatzplan konnte nicht exportiert werden.';

          this.wirdExportiert = false;

          this.cdr.markForCheck();
        }
      });
  }
}
