import { Routes } from '@angular/router';

import { Mitglieder } from './pages/mitglieder/mitglieder';
import { Veranstaltungen } from './pages/veranstaltungen/veranstaltungen';
import { Aufgaben } from './pages/aufgaben/aufgaben';
import { Schichten } from './pages/schichten/schichten';
import { Verfuegbarkeiten } from './pages/verfuegbarkeiten/verfuegbarkeiten';
import { Einsatzplan } from './pages/einsatzplan/einsatzplan';

export const routes: Routes = [
  {
    path: 'mitglieder',
    component: Mitglieder
  },
  {
    path: 'veranstaltungen',
    component: Veranstaltungen
  },
  {
    path: 'aufgaben',
    component: Aufgaben
  },
  {
    path: 'schichten',
    component: Schichten
  },
  {
    path: 'verfuegbarkeiten',
    component: Verfuegbarkeiten
  },
  {
    path: 'einsatzplan',
    component: Einsatzplan
  },
  {
    path: '',
    redirectTo: 'mitglieder',
    pathMatch: 'full'
  }
];
