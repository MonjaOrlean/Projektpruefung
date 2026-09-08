import { Routes } from '@angular/router';
import { Mitglieder } from './pages/mitglieder/mitglieder';

export const routes: Routes = [
  {
    path: 'mitglieder',
    component: Mitglieder
  },
  {
    path: '',
    redirectTo: 'mitglieder',
    pathMatch: 'full'
  }
];
