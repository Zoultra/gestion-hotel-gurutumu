export class Paiement {
    id!: number;
     
    montant!: number;
    datePaiement!: Date;
    modePaiement?: string;
    commentaire?: string;
  }