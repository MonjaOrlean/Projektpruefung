import {
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Mitglied } from '../../models/mitglied';
import { Verfuegbarkeit } from '../../models/verfuegbarkeit';

import { MitgliedService } from '../../services/mitglied.service';
import { VerfuegbarkeitService } from '../../services/verfuegbarkeit.service';

@Component({
  selector: 'app-verfuegbarkeiten',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './verfuegbarkeiten.html',
  styleUrl: './verfuegbarkeiten.scss'
})
export class Verfuegbarkeiten implements OnInit {

  private readonly verfuegbarkeitService =
    inject(VerfuegbarkeitService);

  private readonly mitgliedService =
    inject(MitgliedService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  verfuegbarkeiten: Verfuegbarkeit[] = [];

  mitglieder: Mitglied[] = [];

  neueVerfuegbarkeit = {
    datum: '',
    startzeit: '',
    endzeit: '',
    bemerkung: '',
    mitgliedId: null as number | null
  };

  bearbeiteteVerfuegbarkeit: {
    id: number;
    datum: string;
    startzeit: string;
    endzeit: string;
    bemerkung: string;
    mitgliedId: number;
  } | null = null;

  fehlermeldung = '';

  ngOnInit(): void {
    this.verfuegbarkeitenLaden();
    this.mitgliederLaden();
  }

  verfuegbarkeitenLaden(): void {

    this.verfuegbarkeitService
      .alleVerfuegbarkeitenLaden()
      .subscribe({
        next: (daten) => {

          this.verfuegbarkeiten = [...daten];

          this.cdr.detectChanges();
        },

        error: (fehler) => {

          console.error(
            'Verfügbarkeiten konnten nicht geladen werden:',
            fehler
          );

          this.fehlermeldung =
            'Die Verfügbarkeiten konnten nicht geladen werden.';

          this.cdr.detectChanges();
        }
      });
  }

  mitgliederLaden(): void {

    this.mitgliedService
      .alleMitgliederLaden()
      .subscribe({
        next: (daten) => {

          this.mitglieder = [...daten];

          this.cdr.detectChanges();
        },

        error: (fehler) => {

          console.error(
            'Mitglieder konnten nicht geladen werden:',
            fehler
          );

          this.cdr.detectChanges();
        }
      });
  }

  verfuegbarkeitAnlegen(): void {

    this.fehlermeldung = '';

    if (!this.neueVerfuegbarkeit.datum) {

      this.fehlermeldung =
        'Bitte wähle ein Datum aus.';

      this.cdr.detectChanges();

      return;
    }

    if (!this.neueVerfuegbarkeit.startzeit) {

      this.fehlermeldung =
        'Bitte gib eine Startzeit ein.';

      this.cdr.detectChanges();

      return;
    }

    if (!this.neueVerfuegbarkeit.endzeit) {

      this.fehlermeldung =
        'Bitte gib eine Endzeit ein.';

      this.cdr.detectChanges();

      return;
    }

    if (
      this.neueVerfuegbarkeit.endzeit <
      this.neueVerfuegbarkeit.startzeit
    ) {

      this.fehlermeldung =
        'Die Endzeit darf nicht vor der Startzeit liegen.';

      this.cdr.detectChanges();

      return;
    }

    if (
      this.neueVerfuegbarkeit.mitgliedId === null
    ) {

      this.fehlermeldung =
        'Bitte wähle ein Mitglied aus.';

      this.cdr.detectChanges();

      return;
    }

    this.verfuegbarkeitService
      .verfuegbarkeitAnlegen({
        datum:
        this.neueVerfuegbarkeit.datum,

        startzeit:
        this.neueVerfuegbarkeit.startzeit,

        endzeit:
        this.neueVerfuegbarkeit.endzeit,

        bemerkung:
        this.neueVerfuegbarkeit.bemerkung,

        mitgliedId:
        this.neueVerfuegbarkeit.mitgliedId
      })
      .subscribe({
        next: () => {

          this.neueVerfuegbarkeit = {
            datum: '',
            startzeit: '',
            endzeit: '',
            bemerkung: '',
            mitgliedId: null
          };

          this.verfuegbarkeitenLaden();

          this.cdr.detectChanges();
        },

        error: (fehler) => {

          console.error(
            'Verfügbarkeit konnte nicht angelegt werden:',
            fehler
          );

          if (fehler.status === 400) {

            if (
              fehler.error?.zeitspanneGueltig
            ) {

              this.fehlermeldung =
                fehler.error.zeitspanneGueltig;

            } else if (
              fehler.error?.datum
            ) {

              this.fehlermeldung =
                fehler.error.datum;

            } else if (
              fehler.error?.startzeit
            ) {

              this.fehlermeldung =
                fehler.error.startzeit;

            } else if (
              fehler.error?.endzeit
            ) {

              this.fehlermeldung =
                fehler.error.endzeit;

            } else if (
              fehler.error?.mitgliedId
            ) {

              this.fehlermeldung =
                fehler.error.mitgliedId;

            } else {

              this.fehlermeldung =
                'Bitte überprüfe deine Eingaben.';
            }

          } else {

            this.fehlermeldung =
              'Die Verfügbarkeit konnte nicht gespeichert werden.';
          }

          this.cdr.detectChanges();
        }
      });
  }

  bearbeitungStarten(
    verfuegbarkeit: Verfuegbarkeit
  ): void {

    this.fehlermeldung = '';

    if (
      verfuegbarkeit.id === undefined ||
      verfuegbarkeit.mitglied.id === undefined
    ) {

      return;
    }

    this.bearbeiteteVerfuegbarkeit = {
      id:
      verfuegbarkeit.id,

      datum:
      verfuegbarkeit.datum,

      startzeit:
      verfuegbarkeit.startzeit,

      endzeit:
      verfuegbarkeit.endzeit,

      bemerkung:
        verfuegbarkeit.bemerkung ?? '',

      mitgliedId:
      verfuegbarkeit.mitglied.id
    };

    this.cdr.detectChanges();
  }

  bearbeitungAbbrechen(): void {

    this.fehlermeldung = '';

    this.bearbeiteteVerfuegbarkeit = null;

    this.cdr.detectChanges();
  }

  verfuegbarkeitSpeichern(): void {

    this.fehlermeldung = '';

    if (!this.bearbeiteteVerfuegbarkeit) {
      return;
    }

    if (
      !this.bearbeiteteVerfuegbarkeit.datum
    ) {

      this.fehlermeldung =
        'Bitte wähle ein Datum aus.';

      this.cdr.detectChanges();

      return;
    }

    if (
      !this.bearbeiteteVerfuegbarkeit.startzeit
    ) {

      this.fehlermeldung =
        'Bitte gib eine Startzeit ein.';

      this.cdr.detectChanges();

      return;
    }

    if (
      !this.bearbeiteteVerfuegbarkeit.endzeit
    ) {

      this.fehlermeldung =
        'Bitte gib eine Endzeit ein.';

      this.cdr.detectChanges();

      return;
    }

    if (
      this.bearbeiteteVerfuegbarkeit.endzeit <
      this.bearbeiteteVerfuegbarkeit.startzeit
    ) {

      this.fehlermeldung =
        'Die Endzeit darf nicht vor der Startzeit liegen.';

      this.cdr.detectChanges();

      return;
    }

    this.verfuegbarkeitService
      .verfuegbarkeitBearbeiten(
        this.bearbeiteteVerfuegbarkeit.id,
        {
          datum:
          this.bearbeiteteVerfuegbarkeit.datum,

          startzeit:
          this.bearbeiteteVerfuegbarkeit.startzeit,

          endzeit:
          this.bearbeiteteVerfuegbarkeit.endzeit,

          bemerkung:
          this.bearbeiteteVerfuegbarkeit.bemerkung,

          mitgliedId:
          this.bearbeiteteVerfuegbarkeit.mitgliedId
        }
      )
      .subscribe({
        next: () => {

          this.bearbeiteteVerfuegbarkeit =
            null;

          this.verfuegbarkeitenLaden();

          this.cdr.detectChanges();
        },

        error: (fehler) => {

          console.error(
            'Verfügbarkeit konnte nicht bearbeitet werden:',
            fehler
          );

          if (fehler.status === 400) {

            if (
              fehler.error?.zeitspanneGueltig
            ) {

              this.fehlermeldung =
                fehler.error.zeitspanneGueltig;

            } else {

              this.fehlermeldung =
                'Bitte überprüfe deine Eingaben.';
            }

          } else {

            this.fehlermeldung =
              'Die Verfügbarkeit konnte nicht gespeichert werden.';
          }

          this.cdr.detectChanges();
        }
      });
  }

  verfuegbarkeitLoeschen(
    id?: number
  ): void {

    this.fehlermeldung = '';

    if (id === undefined) {
      return;
    }

    this.verfuegbarkeitService
      .verfuegbarkeitLoeschen(id)
      .subscribe({
        next: () => {

          this.verfuegbarkeitenLaden();

          this.cdr.detectChanges();
        },

        error: (fehler) => {

          console.error(
            'Verfügbarkeit konnte nicht gelöscht werden:',
            fehler
          );

          this.fehlermeldung =
            'Die Verfügbarkeit konnte nicht gelöscht werden.';

          this.cdr.detectChanges();
        }
      });
  }
}
