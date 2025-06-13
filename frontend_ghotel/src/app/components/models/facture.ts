import { Paiement } from "./paiement";

export class Facture {
    id!: number;
    numero!: string;
    montantTotal!: number;
    dateFacture!: Date;
    montantNet?: number;
    montantPaye?: number;
    resteAPayer?: number;
    montantRemise?: number;

    paiements!: Paiement[];
  }