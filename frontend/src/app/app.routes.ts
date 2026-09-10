import { Routes } from '@angular/router';

import { Mitglieder } from './pages/mitglieder/mitglieder';
import { Veranstaltungen } from './pages/veranstaltungen/veranstaltungen';
import { Aufgaben } from './pages/aufgaben/aufgaben';
import { Schichten } from './pages/schichten/schichten';

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
    path: '',
    redirectTo: 'mitglieder',
    pathMatch: 'full'
  }
];
