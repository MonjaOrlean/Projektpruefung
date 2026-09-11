export interface EinsatzplanSchicht {

  schichtId: number;

  schichtName: string;

  datum: string;

  startzeit: string;

  endzeit: string;

  veranstaltungName: string | null;

  benoetigtePersonen: number;

  zugewiesenePersonen: number;

  fehlendePersonen: number;

  unterbesetzt: boolean;

  mitglieder: string[];
}
