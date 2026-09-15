import {
  ChangeDetectorRef,
  Component,
  OnInit,
  inject
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Mitglied } from '../../models/mitglied';
import { MitgliedService } from '../../services/mitglied.service';

@Component({
  selector: 'app-mitglieder',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './mitglieder.html',
  styleUrl: './mitglieder.scss'
})
export class Mitglieder implements OnInit {

  private readonly mitgliedService =
    inject(MitgliedService);

  private readonly cdr =
    inject(ChangeDetectorRef);

  mitglieder: Mitglied[] = [];

  neuesMitglied: Mitglied = {
    vorname: '',
    nachname: '',
    email: '',
    telefon: '',
    aktiv: true
  };

  bearbeitetesMitglied: Mitglied | null = null;

  ngOnInit(): void {
    this.mitgliederLaden();
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

  mitgliedAnlegen(): void {

    this.mitgliedService
      .mitgliedAnlegen(
        this.neuesMitglied
      )
      .subscribe({
        next: () => {

          this.neuesMitglied = {
            vorname: '',
            nachname: '',
            email: '',
            telefon: '',
            aktiv: true
          };

          this.mitgliederLaden();

          this.cdr.detectChanges();
        },

        error: (fehler) => {

          console.error(
            'Mitglied konnte nicht angelegt werden:',
            fehler
          );

          this.cdr.detectChanges();
        }
      });
  }

  bearbeitungStarten(
    mitglied: Mitglied
  ): void {

    this.bearbeitetesMitglied = {
      ...mitglied
    };

    this.cdr.detectChanges();
  }

  bearbeitungAbbrechen(): void {

    this.bearbeitetesMitglied = null;

    this.cdr.detectChanges();
  }

  mitgliedSpeichern(): void {

    if (!this.bearbeitetesMitglied?.id) {
      return;
    }

    this.mitgliedService
      .mitgliedBearbeiten(
        this.bearbeitetesMitglied.id,
        this.bearbeitetesMitglied
      )
      .subscribe({
        next: () => {

          this.bearbeitetesMitglied = null;

          this.mitgliederLaden();

          this.cdr.detectChanges();
        },

        error: (fehler) => {

          console.error(
            'Mitglied konnte nicht bearbeitet werden:',
            fehler
          );

          this.cdr.detectChanges();
        }
      });
  }

  mitgliedLoeschen(
    id?: number
  ): void {

    if (id === undefined) {
      return;
    }

    this.mitgliedService
      .mitgliedLoeschen(id)
      .subscribe({
        next: () => {

          this.mitgliederLaden();

          this.cdr.detectChanges();
        },

        error: (fehler) => {

          console.error(
            'Mitglied konnte nicht gelöscht werden:',
            fehler
          );

          this.cdr.detectChanges();
        }
      });
  }
}
