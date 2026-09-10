import { Mitglied } from './mitglied';
import { Schicht } from './schicht';

export interface SchichtZuweisung {
  id?: number;
  schicht: Schicht;
  mitglied: Mitglied;
}

export interface SchichtBesetzungsstatus {
  schichtId: number;
  benoetigtePersonen: number;
  zugewiesenePersonen: number;
  fehlendePersonen: number;
  unterbesetzt: boolean;
}
