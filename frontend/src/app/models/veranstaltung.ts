export interface Veranstaltung {
  id?: number;
  name: string;
  datum: string;
  startzeit?: string;
  endzeit?: string;
  ort?: string;
  beschreibung?: string;
  aktiv: boolean;
}
