 
import { LigneVente } from '../models/ligne-vente';

export class Vente {
  id?: number;
  code?: string;
  dateVente: Date = new Date();
  lignes: LigneVente[] = [];
  montantTotal: number = 0;
  montantPaye: number = 0;
  monnaie: number = 0;
  etatVente!: string;
  tableResto!: string;
  ligneVentes?: LigneVente[];
  showDetails?: boolean;
}

