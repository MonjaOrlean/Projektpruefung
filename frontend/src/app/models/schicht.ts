import { Veranstaltung } from './veranstaltung';

export interface Schicht {
  id?: number;
  name: string;
  datum: string;
  startzeit: string;
  endzeit: string;
  benoetigtePersonen: number;
  beschreibung?: string;
  veranstaltung?: Veranstaltung | null;
}
