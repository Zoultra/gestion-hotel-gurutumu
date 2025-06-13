import { Utilisateur } from "./user";

export class MouvementCaisse{

    id?: number | null;

    nomrole!: string;  

    type!: string;  

    description!: string;

    montant!: number

    utilisateur?: Utilisateur[];

    date!: string;
}

export interface RecapJournalierDto {
  dateOuverture: string;
  heureOuverture: string;
  dateFermeture: string;
  heureFermeture: string;
  soldeDepart: number;
  totalEntree: number;
  totalSortie: number;
  mouvements: MouvementCaisse[];
  soldeFinal: number;
  soldeFinalTheorique: number;
}
 