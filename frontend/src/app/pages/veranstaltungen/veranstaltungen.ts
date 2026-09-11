import {
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Veranstaltung } from '../../models/veranstaltung';
import { VeranstaltungService } from '../../services/veranstaltung.service';

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

  private readonly cdr =
    inject(ChangeDetectorRef);

  veranstaltungen: Veranstaltung[] = [];

  neueVeranstaltung: Veranstaltung = {
    name: '',
    datum: '',
    startzeit: '',
    endzeit: '',
    ort: '',
    beschreibung: '',
    aktiv: true
  };

  bearbeiteteVeranstaltung: Veranstaltung | null = null;

  fehlermeldung = '';

  ngOnInit(): void {
    this.veranstaltungenLaden();
  }

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

    this.fehlermeldung = '';

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

          this.fehlermeldung = '';

          this.veranstaltungenLaden();

          this.cdr.markForCheck();
        },

        error: (fehler) => {

          console.error(
            'Veranstaltung konnte nicht angelegt werden:',
            fehler
          );

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

          this.cdr.markForCheck();
        }
      });
  }

  bearbeitungStarten(
    veranstaltung: Veranstaltung
  ): void {

    this.fehlermeldung = '';

    this.bearbeiteteVeranstaltung = {
      ...veranstaltung
    };

    this.cdr.markForCheck();
  }

  bearbeitungAbbrechen(): void {

    this.fehlermeldung = '';

    this.bearbeiteteVeranstaltung = null;

    this.cdr.markForCheck();
  }

  veranstaltungSpeichern(): void {

    this.fehlermeldung = '';

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

          this.fehlermeldung = '';

          this.veranstaltungenLaden();

          this.cdr.markForCheck();
        },

        error: (fehler) => {

          console.error(
            'Veranstaltung konnte nicht bearbeitet werden:',
            fehler
          );

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

          this.cdr.markForCheck();
        }
      });
  }

  veranstaltungLoeschen(
    id?: number
  ): void {

    this.fehlermeldung = '';

    if (id === undefined) {
      return;
    }

    this.veranstaltungService
      .veranstaltungLoeschen(id)
      .subscribe({

        next: () => {

          this.fehlermeldung = '';

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
}
