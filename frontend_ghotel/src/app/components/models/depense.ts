export class Depense {
    id!: number;
    libelle!: string;
    montant!: number;
    categorie!: 'ACHAT' | 'ENTRETIEN' | 'SALAIRE' | 'AUTRE' | 'TRANSPORT' | string;
    dateDepense!: Date;
    description?: string;
  }