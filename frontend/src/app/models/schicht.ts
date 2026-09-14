import { Veranstaltung } from './veranstaltung';

export interface Einsatzbereich {
  id?: number;
  name: string;
  beschreibung?: string;
  veranstaltung?: Veranstaltung | null;
}

export interface Schicht {
  id?: number;
  name: string;
  datum: string;
  startzeit: string;
  endzeit: string;
  benoetigtePersonen: number;
  beschreibung?: string;
  veranstaltung?: Veranstaltung | null;
  einsatzbereich?: Einsatzbereich | null;
}
