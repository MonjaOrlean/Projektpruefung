import {
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { EinsatzplanSchicht } from '../../models/einsatzplan-schicht';
import { EinsatzplanService } from '../../services/einsatzplan.service';

@Component({
  selector: 'app-einsatzplan',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
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

  ausgewaehlteVeranstaltung = '';

  fehlermeldung = '';

  erfolgsmeldung = '';

  wirdGeladen = false;

  wirdExportiert = false;

  ngOnInit(): void {
    this.einsatzplanLaden();
  }

  einsatzplanLaden(): void {

    this.fehlermeldung = '';
    this.erfolgsmeldung = '';
    this.wirdGeladen = true;

    this.cdr.markForCheck();

    this.einsatzplanService
      .einsatzplanLaden()
      .subscribe({

        next: (daten) => {

          this.schichten = daten;

          if (
            !this.ausgewaehlteVeranstaltung &&
            this.veranstaltungen.length > 0
          ) {

            this.ausgewaehlteVeranstaltung =
              this.veranstaltungen[0];
          }

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

  get veranstaltungen(): string[] {

    return [
      ...new Set(
        this.schichten
          .map(
            schicht =>
              schicht.veranstaltungName
          )
          .filter(
            (name): name is string =>
              !!name
          )
      )
    ].sort();
  }

  get gefilterteSchichten(): EinsatzplanSchicht[] {

    if (!this.ausgewaehlteVeranstaltung) {
      return [];
    }

    return this.schichten.filter(
      schicht =>
        schicht.veranstaltungName ===
        this.ausgewaehlteVeranstaltung
    );
  }

  get einsatzbereiche(): string[] {

    return [
      ...new Set(
        this.gefilterteSchichten.map(
          schicht =>
            schicht.einsatzbereichName ||
            'Ohne Einsatzbereich'
        )
      )
    ].sort();
  }

  get mitglieder(): string[] {

    const namen =
      new Set<string>();

    for (
      const schicht of
      this.gefilterteSchichten
      ) {

      for (
        const mitglied of
        schicht.mitglieder
        ) {

        namen.add(mitglied);
      }
    }

    return Array
      .from(namen)
      .sort();
  }

  schichtenFuerMitgliedUndBereich(
    mitglied: string,
    einsatzbereich: string
  ): EinsatzplanSchicht[] {

    return this.gefilterteSchichten.filter(
      schicht => {

        const bereich =
          schicht.einsatzbereichName ||
          'Ohne Einsatzbereich';

        return (
          bereich === einsatzbereich &&
          schicht.mitglieder.includes(
            mitglied
          )
        );
      }
    );
  }

  hatKonflikt(
    mitglied: string,
    aktuelleSchicht: EinsatzplanSchicht
  ): boolean {

    const andereSchichten =
      this.gefilterteSchichten.filter(
        schicht =>
          schicht.schichtId !==
          aktuelleSchicht.schichtId &&
          schicht.mitglieder.includes(
            mitglied
          ) &&
          schicht.datum ===
          aktuelleSchicht.datum
      );

    return andereSchichten.some(
      andere =>
        aktuelleSchicht.startzeit <
        andere.endzeit &&
        aktuelleSchicht.endzeit >
        andere.startzeit
    );
  }

  statusKlasse(
    schicht: EinsatzplanSchicht
  ): string {

    if (
      schicht.zugewiesenePersonen === 0
    ) {
      return 'besetzung-fehlt';
    }

    if (schicht.unterbesetzt) {
      return 'besetzung-teilweise';
    }

    return 'besetzung-voll';
  }

  statusText(
    schicht: EinsatzplanSchicht
  ): string {

    if (
      schicht.zugewiesenePersonen === 0
    ) {
      return '✕ Nicht besetzt';
    }

    if (schicht.unterbesetzt) {
      return '⚠ Unterbesetzt';
    }

    return '✓ Vollständig besetzt';
  }

  einsatzplanExportieren(): void {

    this.fehlermeldung = '';
    this.erfolgsmeldung = '';
    this.wirdExportiert = true;

    this.cdr.markForCheck();

    this.einsatzplanService
      .einsatzplanExportieren()
      .subscribe({

        next: (dateipfad) => {

          console.log(
            'Einsatzplan exportiert:',
            dateipfad
          );

          this.erfolgsmeldung =
            '✓ Einsatzplan wurde erfolgreich gespeichert: ' +
            dateipfad;

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
