import { Mitglied } from './mitglied';

export interface Verfuegbarkeit {
  id?: number;
  datum: string;
  startzeit: string;
  endzeit: string;
  bemerkung?: string;
  mitglied: Mitglied;
}
