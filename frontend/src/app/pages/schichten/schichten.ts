import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Schicht } from '../../models/schicht';
import { Veranstaltung } from '../../models/veranstaltung';

import { SchichtService } from '../../services/schicht.service';
import { VeranstaltungService } from '../../services/veranstaltung.service';

@Component({
  selector: 'app-schichten',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './schichten.html',
  styleUrl: './schichten.scss'
})
export class Schichten implements OnInit {

  private readonly schichtService = inject(SchichtService);
  private readonly veranstaltungService = inject(VeranstaltungService);

  schichten: Schicht[] = [];
  veranstaltungen: Veranstaltung[] = [];

  neueSchicht = {
    name: '',
    datum: '',
    startzeit: '',
    endzeit: '',
    benoetigtePersonen: 1,
    beschreibung: '',
    veranstaltungId: null as number | null
  };

  bearbeiteteSchicht: {
    id: number;
    name: string;
    datum: string;
    startzeit: string;
    endzeit: string;
    benoetigtePersonen: number;
    beschreibung: string;
    veranstaltungId: number | null;
  } | null = null;

  fehlermeldung = '';

  ngOnInit(): void {
    this.schichtenLaden();
    this.veranstaltungenLaden();
  }

  schichtenLaden(): void {
    this.schichtService
      .alleSchichtenLaden()
      .subscribe({
        next: (daten) => {
          this.schichten = daten;
        },

        error: (fehler) => {
          console.error(
            'Schichten konnten nicht geladen werden:',
            fehler
          );

          this.fehlermeldung =
            'Die Schichten konnten nicht geladen werden.';
        }
      });
  }

  veranstaltungenLaden(): void {
    this.veranstaltungService
      .alleVeranstaltungenLaden()
      .subscribe({
        next: (daten) => {
          this.veranstaltungen = daten;
        },

        error: (fehler) => {
          console.error(
            'Veranstaltungen konnten nicht geladen werden:',
            fehler
          );
        }
      });
  }

  schichtAnlegen(): void {

    this.fehlermeldung = '';

    if (!this.neueSchicht.name.trim()) {
      this.fehlermeldung =
        'Bitte gib einen Namen für die Schicht ein.';

      return;
    }

    if (!this.neueSchicht.datum) {
      this.fehlermeldung =
        'Bitte wähle ein Datum aus.';

      return;
    }

    if (!this.neueSchicht.startzeit) {
      this.fehlermeldung =
        'Bitte gib eine Startzeit ein.';

      return;
    }

    if (!this.neueSchicht.endzeit) {
      this.fehlermeldung =
        'Bitte gib eine Endzeit ein.';

      return;
    }

    if (
      this.neueSchicht.endzeit <
      this.neueSchicht.startzeit
    ) {
      this.fehlermeldung =
        'Die Endzeit darf nicht vor der Startzeit liegen.';

      return;
    }

    if (this.neueSchicht.benoetigtePersonen < 1) {
      this.fehlermeldung =
        'Es muss mindestens eine Person benötigt werden.';

      return;
    }

    this.schichtService
      .schichtAnlegen(this.neueSchicht)
      .subscribe({
        next: () => {

          this.schichtenLaden();

          this.neueSchicht = {
            name: '',
            datum: '',
            startzeit: '',
            endzeit: '',
            benoetigtePersonen: 1,
            beschreibung: '',
            veranstaltungId: null
          };
        },

        error: (fehler) => {

          console.error(
            'Schicht konnte nicht angelegt werden:',
            fehler
          );

          if (fehler.status === 400) {

            if (fehler.error?.zeitspanneGueltig) {
              this.fehlermeldung =
                fehler.error.zeitspanneGueltig;
            }
            else if (fehler.error?.name) {
              this.fehlermeldung =
                fehler.error.name;
            }
            else if (fehler.error?.datum) {
              this.fehlermeldung =
                fehler.error.datum;
            }
            else if (fehler.error?.startzeit) {
              this.fehlermeldung =
                fehler.error.startzeit;
            }
            else if (fehler.error?.endzeit) {
              this.fehlermeldung =
                fehler.error.endzeit;
            }
            else if (fehler.error?.benoetigtePersonen) {
              this.fehlermeldung =
                fehler.error.benoetigtePersonen;
            }
            else {
              this.fehlermeldung =
                'Bitte überprüfe deine Eingaben.';
            }

          } else {

            this.fehlermeldung =
              'Die Schicht konnte nicht gespeichert werden.';
          }
        }
      });
  }

  bearbeitungStarten(schicht: Schicht): void {

    this.fehlermeldung = '';

    if (schicht.id === undefined) {
      return;
    }

    this.bearbeiteteSchicht = {
      id: schicht.id,
      name: schicht.name,
      datum: schicht.datum,
      startzeit: schicht.startzeit,
      endzeit: schicht.endzeit,
      benoetigtePersonen:
      schicht.benoetigtePersonen,
      beschreibung:
        schicht.beschreibung ?? '',
      veranstaltungId:
        schicht.veranstaltung?.id ?? null
    };
  }

  bearbeitungAbbrechen(): void {
    this.fehlermeldung = '';
    this.bearbeiteteSchicht = null;
  }

  schichtSpeichern(): void {

    this.fehlermeldung = '';

    if (!this.bearbeiteteSchicht) {
      return;
    }

    if (!this.bearbeiteteSchicht.name.trim()) {
      this.fehlermeldung =
        'Bitte gib einen Namen für die Schicht ein.';

      return;
    }

    if (!this.bearbeiteteSchicht.datum) {
      this.fehlermeldung =
        'Bitte wähle ein Datum aus.';

      return;
    }

    if (!this.bearbeiteteSchicht.startzeit) {
      this.fehlermeldung =
        'Bitte gib eine Startzeit ein.';

      return;
    }

    if (!this.bearbeiteteSchicht.endzeit) {
      this.fehlermeldung =
        'Bitte gib eine Endzeit ein.';

      return;
    }

    if (
      this.bearbeiteteSchicht.endzeit <
      this.bearbeiteteSchicht.startzeit
    ) {
      this.fehlermeldung =
        'Die Endzeit darf nicht vor der Startzeit liegen.';

      return;
    }

    if (
      this.bearbeiteteSchicht.benoetigtePersonen < 1
    ) {
      this.fehlermeldung =
        'Es muss mindestens eine Person benötigt werden.';

      return;
    }

    this.schichtService
      .schichtBearbeiten(
        this.bearbeiteteSchicht.id,
        {
          name:
          this.bearbeiteteSchicht.name,
          datum:
          this.bearbeiteteSchicht.datum,
          startzeit:
          this.bearbeiteteSchicht.startzeit,
          endzeit:
          this.bearbeiteteSchicht.endzeit,
          benoetigtePersonen:
          this.bearbeiteteSchicht.benoetigtePersonen,
          beschreibung:
          this.bearbeiteteSchicht.beschreibung,
          veranstaltungId:
          this.bearbeiteteSchicht.veranstaltungId
        }
      )
      .subscribe({
        next: () => {

          this.bearbeiteteSchicht = null;

          this.schichtenLaden();
        },

        error: (fehler) => {

          console.error(
            'Schicht konnte nicht bearbeitet werden:',
            fehler
          );

          if (fehler.status === 400) {

            if (fehler.error?.zeitspanneGueltig) {
              this.fehlermeldung =
                fehler.error.zeitspanneGueltig;
            }
            else if (fehler.error?.name) {
              this.fehlermeldung =
                fehler.error.name;
            }
            else if (fehler.error?.datum) {
              this.fehlermeldung =
                fehler.error.datum;
            }
            else if (fehler.error?.startzeit) {
              this.fehlermeldung =
                fehler.error.startzeit;
            }
            else if (fehler.error?.endzeit) {
              this.fehlermeldung =
                fehler.error.endzeit;
            }
            else if (fehler.error?.benoetigtePersonen) {
              this.fehlermeldung =
                fehler.error.benoetigtePersonen;
            }
            else {
              this.fehlermeldung =
                'Bitte überprüfe deine Eingaben.';
            }

          } else {

            this.fehlermeldung =
              'Die Schicht konnte nicht gespeichert werden.';
          }
        }
      });
  }

  schichtLoeschen(id?: number): void {

    this.fehlermeldung = '';

    if (id === undefined) {
      return;
    }

    this.schichtService
      .schichtLoeschen(id)
      .subscribe({
        next: () => {
          this.schichtenLaden();
        },

        error: (fehler) => {

          console.error(
            'Schicht konnte nicht gelöscht werden:',
            fehler
          );

          this.fehlermeldung =
            'Die Schicht konnte nicht gelöscht werden.';
        }
      });
  }
}
