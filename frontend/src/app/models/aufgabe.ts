import { Veranstaltung } from './veranstaltung';

export interface Aufgabe {
  id?: number;
  titel: string;
  beschreibung?: string;
  erledigt: boolean;
  veranstaltung?: Veranstaltung | null;
}
