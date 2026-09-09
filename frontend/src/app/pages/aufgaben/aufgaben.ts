import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Aufgabe } from '../../models/aufgabe';
import { Veranstaltung } from '../../models/veranstaltung';

import { AufgabeService } from '../../services/aufgabe.service';
import { VeranstaltungService } from '../../services/veranstaltung.service';

@Component({
  selector: 'app-aufgaben',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './aufgaben.html',
  styleUrl: './aufgaben.scss'
})
export class Aufgaben implements OnInit {

  private readonly aufgabeService = inject(AufgabeService);
  private readonly veranstaltungService = inject(VeranstaltungService);

  aufgaben: Aufgabe[] = [];
  veranstaltungen: Veranstaltung[] = [];

  neueAufgabe = {
    titel: '',
    beschreibung: '',
    veranstaltungId: null as number | null
  };

  bearbeiteteAufgabe: {
    id: number;
    titel: string;
    beschreibung: string;
    erledigt: boolean;
    veranstaltungId: number | null;
  } | null = null;

  fehlermeldung = '';

  ngOnInit(): void {
    this.aufgabenLaden();
    this.veranstaltungenLaden();
  }

  aufgabenLaden(): void {
    this.aufgabeService
      .alleAufgabenLaden()
      .subscribe({
        next: (daten) => {
          this.aufgaben = daten;
        },
        error: (fehler) => {
          console.error(
            'Aufgaben konnten nicht geladen werden:',
            fehler
          );

          this.fehlermeldung =
            'Die Aufgaben konnten nicht geladen werden.';
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

  aufgabeAnlegen(): void {

    this.fehlermeldung = '';

    if (!this.neueAufgabe.titel.trim()) {
      this.fehlermeldung =
        'Bitte gib einen Titel für die Aufgabe ein.';

      return;
    }

    this.aufgabeService
      .aufgabeAnlegen(this.neueAufgabe)
      .subscribe({
        next: () => {

          this.aufgabenLaden();

          this.neueAufgabe = {
            titel: '',
            beschreibung: '',
            veranstaltungId: null
          };
        },

        error: (fehler) => {

          console.error(
            'Aufgabe konnte nicht angelegt werden:',
            fehler
          );

          if (fehler.status === 400) {

            if (fehler.error?.titel) {
              this.fehlermeldung =
                fehler.error.titel;
            } else {
              this.fehlermeldung =
                'Bitte überprüfe deine Eingaben.';
            }

          } else {

            this.fehlermeldung =
              'Die Aufgabe konnte nicht gespeichert werden.';
          }
        }
      });
  }

  bearbeitungStarten(aufgabe: Aufgabe): void {

    this.fehlermeldung = '';

    if (aufgabe.id === undefined) {
      return;
    }

    this.bearbeiteteAufgabe = {
      id: aufgabe.id,
      titel: aufgabe.titel,
      beschreibung: aufgabe.beschreibung ?? '',
      erledigt: aufgabe.erledigt,
      veranstaltungId:
        aufgabe.veranstaltung?.id ?? null
    };
  }

  bearbeitungAbbrechen(): void {
    this.fehlermeldung = '';
    this.bearbeiteteAufgabe = null;
  }

  aufgabeSpeichern(): void {

    this.fehlermeldung = '';

    if (!this.bearbeiteteAufgabe) {
      return;
    }

    if (!this.bearbeiteteAufgabe.titel.trim()) {

      this.fehlermeldung =
        'Bitte gib einen Titel für die Aufgabe ein.';

      return;
    }

    this.aufgabeService
      .aufgabeBearbeiten(
        this.bearbeiteteAufgabe.id,
        {
          titel: this.bearbeiteteAufgabe.titel,
          beschreibung:
          this.bearbeiteteAufgabe.beschreibung,
          erledigt:
          this.bearbeiteteAufgabe.erledigt,
          veranstaltungId:
          this.bearbeiteteAufgabe.veranstaltungId
        }
      )
      .subscribe({
        next: () => {

          this.bearbeiteteAufgabe = null;

          this.aufgabenLaden();
        },

        error: (fehler) => {

          console.error(
            'Aufgabe konnte nicht bearbeitet werden:',
            fehler
          );

          if (fehler.status === 400) {

            if (fehler.error?.titel) {
              this.fehlermeldung =
                fehler.error.titel;
            } else {
              this.fehlermeldung =
                'Bitte überprüfe deine Eingaben.';
            }

          } else {

            this.fehlermeldung =
              'Die Aufgabe konnte nicht gespeichert werden.';
          }
        }
      });
  }

  aufgabeLoeschen(id?: number): void {

    this.fehlermeldung = '';

    if (id === undefined) {
      return;
    }

    this.aufgabeService
      .aufgabeLoeschen(id)
      .subscribe({
        next: () => {
          this.aufgabenLaden();
        },

        error: (fehler) => {

          console.error(
            'Aufgabe konnte nicht gelöscht werden:',
            fehler
          );

          this.fehlermeldung =
            'Die Aufgabe konnte nicht gelöscht werden.';
        }
      });
  }
}
