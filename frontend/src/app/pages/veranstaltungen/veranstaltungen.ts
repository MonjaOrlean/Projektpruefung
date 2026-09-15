import {
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Veranstaltung } from '../../models/veranstaltung';
import { Einsatzbereich } from '../../models/schicht';

import { VeranstaltungService } from '../../services/veranstaltung.service';
import { EinsatzbereichService } from '../../services/einsatzbereich.service';

@Component({
  selector: 'app-veranstaltungen',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './veranstaltungen.html',
  styleUrl: './veranstaltungen.scss'
})
export class Veranstaltungen implements OnInit {

  private readonly veranstaltungService =
    inject(VeranstaltungService);

  private readonly einsatzbereichService =
    inject(EinsatzbereichService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  veranstaltungen: Veranstaltung[] = [];

  einsatzbereiche: Einsatzbereich[] = [];

  ausgewaehlteVeranstaltung: Veranstaltung | null = null;

  neueVeranstaltung: Veranstaltung = {
    name: '',
    datum: '',
    startzeit: '',
    endzeit: '',
    ort: '',
    beschreibung: '',
    aktiv: true
  };

  neuerEinsatzbereich = {
    name: '',
    beschreibung: ''
  };

  bearbeiteteVeranstaltung: Veranstaltung | null = null;

  bearbeiteterEinsatzbereich: Einsatzbereich | null = null;

  fehlermeldung = '';

  erfolgsmeldung = '';

  ngOnInit(): void {
    this.veranstaltungenLaden();
  }

  // =========================================================
  // VERANSTALTUNGEN
  // =========================================================

  veranstaltungenLaden(): void {

    this.veranstaltungService
      .alleVeranstaltungenLaden()
      .subscribe({

        next: (daten) => {

          this.veranstaltungen = daten;

          this.cdr.markForCheck();
        },

        error: (fehler) => {

          console.error(
            'Veranstaltungen konnten nicht geladen werden:',
            fehler
          );

          this.fehlermeldung =
            'Die Veranstaltungen konnten nicht geladen werden.';

          this.cdr.markForCheck();
        }
      });
  }

  veranstaltungAnlegen(): void {

    this.meldungenZuruecksetzen();

    if (!this.neueVeranstaltung.name.trim()) {

      this.fehlermeldung =
        'Bitte gib einen Namen für die Veranstaltung ein.';

      return;
    }

    if (!this.neueVeranstaltung.datum) {

      this.fehlermeldung =
        'Bitte wähle ein Datum für die Veranstaltung aus.';

      return;
    }

    if (
      this.neueVeranstaltung.startzeit &&
      this.neueVeranstaltung.endzeit &&
      this.neueVeranstaltung.endzeit <
      this.neueVeranstaltung.startzeit
    ) {

      this.fehlermeldung =
        'Die Endzeit darf nicht vor der Startzeit liegen.';

      return;
    }

    this.veranstaltungService
      .veranstaltungAnlegen(
        this.neueVeranstaltung
      )
      .subscribe({

        next: () => {

          this.neueVeranstaltung = {
            name: '',
            datum: '',
            startzeit: '',
            endzeit: '',
            ort: '',
            beschreibung: '',
            aktiv: true
          };

          this.erfolgsmeldung =
            'Die Veranstaltung wurde erfolgreich angelegt.';

          this.veranstaltungenLaden();

          this.cdr.markForCheck();
        },

        error: (fehler) => {

          console.error(
            'Veranstaltung konnte nicht angelegt werden:',
            fehler
          );

          this.veranstaltungsFehlerAuswerten(fehler);

          this.cdr.markForCheck();
        }
      });
  }

  bearbeitungStarten(
    veranstaltung: Veranstaltung
  ): void {

    this.meldungenZuruecksetzen();

    this.bearbeiteteVeranstaltung = {
      ...veranstaltung
    };

    this.cdr.markForCheck();
  }

  bearbeitungAbbrechen(): void {

    this.meldungenZuruecksetzen();

    this.bearbeiteteVeranstaltung = null;

    this.cdr.markForCheck();
  }

  veranstaltungSpeichern(): void {

    this.meldungenZuruecksetzen();

    if (!this.bearbeiteteVeranstaltung?.id) {
      return;
    }

    if (
      !this.bearbeiteteVeranstaltung
        .name
        .trim()
    ) {

      this.fehlermeldung =
        'Bitte gib einen Namen für die Veranstaltung ein.';

      return;
    }

    if (
      !this.bearbeiteteVeranstaltung.datum
    ) {

      this.fehlermeldung =
        'Bitte wähle ein Datum für die Veranstaltung aus.';

      return;
    }

    if (
      this.bearbeiteteVeranstaltung.startzeit &&
      this.bearbeiteteVeranstaltung.endzeit &&
      this.bearbeiteteVeranstaltung.endzeit <
      this.bearbeiteteVeranstaltung.startzeit
    ) {

      this.fehlermeldung =
        'Die Endzeit darf nicht vor der Startzeit liegen.';

      return;
    }

    this.veranstaltungService
      .veranstaltungBearbeiten(
        this.bearbeiteteVeranstaltung.id,
        this.bearbeiteteVeranstaltung
      )
      .subscribe({

        next: () => {

          this.bearbeiteteVeranstaltung = null;

          this.erfolgsmeldung =
            'Die Veranstaltung wurde erfolgreich gespeichert.';

          this.veranstaltungenLaden();

          this.cdr.markForCheck();
        },

        error: (fehler) => {

          console.error(
            'Veranstaltung konnte nicht bearbeitet werden:',
            fehler
          );

          this.veranstaltungsFehlerAuswerten(fehler);

          this.cdr.markForCheck();
        }
      });
  }

  veranstaltungLoeschen(
    id?: number
  ): void {

    this.meldungenZuruecksetzen();

    if (id === undefined) {
      return;
    }

    this.veranstaltungService
      .veranstaltungLoeschen(id)
      .subscribe({

        next: () => {

          if (
            this.ausgewaehlteVeranstaltung?.id === id
          ) {

            this.einsatzbereichVerwaltungSchliessen();
          }

          this.erfolgsmeldung =
            'Die Veranstaltung wurde gelöscht.';

          this.veranstaltungenLaden();

          this.cdr.markForCheck();
        },

        error: (fehler) => {

          console.error(
            'Veranstaltung konnte nicht gelöscht werden:',
            fehler
          );

          this.fehlermeldung =
            'Die Veranstaltung konnte nicht gelöscht werden.';

          this.cdr.markForCheck();
        }
      });
  }

  // =========================================================
  // EINSATZBEREICHE
  // =========================================================

  einsatzbereicheOeffnen(
    veranstaltung: Veranstaltung
  ): void {

    this.meldungenZuruecksetzen();

    if (!veranstaltung.id) {
      return;
    }

    this.ausgewaehlteVeranstaltung =
      veranstaltung;

    this.neuerEinsatzbereich = {
      name: '',
      beschreibung: ''
    };

    this.bearbeiteterEinsatzbereich = null;

    this.einsatzbereicheLaden(
      veranstaltung.id
    );
  }

  einsatzbereicheLaden(
    veranstaltungId: number
  ): void {

    this.einsatzbereichService
      .einsatzbereicheFuerVeranstaltungLaden(
        veranstaltungId
      )
      .subscribe({

        next: (daten) => {

          this.einsatzbereiche = daten;

          this.cdr.markForCheck();
        },

        error: (fehler) => {

          console.error(
            'Einsatzbereiche konnten nicht geladen werden:',
            fehler
          );

          this.fehlermeldung =
            'Die Einsatzbereiche konnten nicht geladen werden.';

          this.cdr.markForCheck();
        }
      });
  }

  einsatzbereichAnlegen(): void {

    this.meldungenZuruecksetzen();

    if (!this.ausgewaehlteVeranstaltung?.id) {
      return;
    }

    if (!this.neuerEinsatzbereich.name.trim()) {

      this.fehlermeldung =
        'Bitte gib einen Namen für den Einsatzbereich ein.';

      return;
    }

    const daten = {
      name: this.neuerEinsatzbereich.name.trim(),
      beschreibung:
      this.neuerEinsatzbereich.beschreibung,
      veranstaltungId:
      this.ausgewaehlteVeranstaltung.id
    };

    this.einsatzbereichService
      .einsatzbereichAnlegen(daten)
      .subscribe({

        next: () => {

          this.neuerEinsatzbereich = {
            name: '',
            beschreibung: ''
          };

          this.erfolgsmeldung =
            'Der Einsatzbereich wurde angelegt.';

          this.einsatzbereicheLaden(
            this.ausgewaehlteVeranstaltung!.id!
          );

          this.cdr.markForCheck();
        },

        error: (fehler) => {

          console.error(
            'Einsatzbereich konnte nicht angelegt werden:',
            fehler
          );

          this.fehlermeldung =
            'Der Einsatzbereich konnte nicht angelegt werden.';

          this.cdr.markForCheck();
        }
      });
  }

  einsatzbereichBearbeitenStarten(
    einsatzbereich: Einsatzbereich
  ): void {

    this.meldungenZuruecksetzen();

    this.bearbeiteterEinsatzbereich = {
      ...einsatzbereich
    };

    this.cdr.markForCheck();
  }

  einsatzbereichBearbeitungAbbrechen(): void {

    this.bearbeiteterEinsatzbereich = null;

    this.meldungenZuruecksetzen();

    this.cdr.markForCheck();
  }

  einsatzbereichSpeichern(): void {

    this.meldungenZuruecksetzen();

    if (
      !this.bearbeiteterEinsatzbereich?.id ||
      !this.ausgewaehlteVeranstaltung?.id
    ) {
      return;
    }

    if (
      !this.bearbeiteterEinsatzbereich.name.trim()
    ) {

      this.fehlermeldung =
        'Bitte gib einen Namen für den Einsatzbereich ein.';

      return;
    }

    const daten = {
      name:
        this.bearbeiteterEinsatzbereich
          .name
          .trim(),

      beschreibung:
        this.bearbeiteterEinsatzbereich
          .beschreibung ?? '',

      veranstaltungId:
      this.ausgewaehlteVeranstaltung.id
    };

    this.einsatzbereichService
      .einsatzbereichBearbeiten(
        this.bearbeiteterEinsatzbereich.id,
        daten
      )
      .subscribe({

        next: () => {

          this.bearbeiteterEinsatzbereich =
            null;

          this.erfolgsmeldung =
            'Der Einsatzbereich wurde gespeichert.';

          this.einsatzbereicheLaden(
            this.ausgewaehlteVeranstaltung!.id!
          );

          this.cdr.markForCheck();
        },

        error: (fehler) => {

          console.error(
            'Einsatzbereich konnte nicht gespeichert werden:',
            fehler
          );

          this.fehlermeldung =
            'Der Einsatzbereich konnte nicht gespeichert werden.';

          this.cdr.markForCheck();
        }
      });
  }

  einsatzbereichLoeschen(
    id?: number
  ): void {

    this.meldungenZuruecksetzen();

    if (
      id === undefined ||
      !this.ausgewaehlteVeranstaltung?.id
    ) {
      return;
    }

    this.einsatzbereichService
      .einsatzbereichLoeschen(id)
      .subscribe({

        next: () => {

          this.erfolgsmeldung =
            'Der Einsatzbereich wurde gelöscht.';

          this.einsatzbereicheLaden(
            this.ausgewaehlteVeranstaltung!.id!
          );

          this.cdr.markForCheck();
        },

        error: (fehler) => {

          console.error(
            'Einsatzbereich konnte nicht gelöscht werden:',
            fehler
          );

          this.fehlermeldung =
            'Der Einsatzbereich konnte nicht gelöscht werden. Möglicherweise wird er noch von einer Schicht verwendet.';

          this.cdr.markForCheck();
        }
      });
  }

  einsatzbereichVerwaltungSchliessen(): void {

    this.ausgewaehlteVeranstaltung = null;

    this.einsatzbereiche = [];

    this.neuerEinsatzbereich = {
      name: '',
      beschreibung: ''
    };

    this.bearbeiteterEinsatzbereich = null;

    this.meldungenZuruecksetzen();

    this.cdr.markForCheck();
  }

  // =========================================================
  // HILFSMETHODEN
  // =========================================================

  private meldungenZuruecksetzen(): void {

    this.fehlermeldung = '';

    this.erfolgsmeldung = '';
  }

  private veranstaltungsFehlerAuswerten(
    fehler: any
  ): void {

    if (fehler.status === 400) {

      if (
        fehler.error?.zeitspanneGueltig
      ) {

        this.fehlermeldung =
          fehler.error.zeitspanneGueltig;

      } else if (
        fehler.error?.name
      ) {

        this.fehlermeldung =
          fehler.error.name;

      } else if (
        fehler.error?.datum
      ) {

        this.fehlermeldung =
          fehler.error.datum;

      } else {

        this.fehlermeldung =
          'Bitte überprüfe deine Eingaben.';
      }

    } else {

      this.fehlermeldung =
        'Die Veranstaltung konnte nicht gespeichert werden.';
    }
  }
}
