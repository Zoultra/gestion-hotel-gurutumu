export class Salle {
    id!: number;
    numero!: string;
   // nom!: string
    tarif!: number;
    capaciteMax!: number;
    statut!: 'DISPONIBLE' | 'RESERVEE' | 'EN_MAINTENANCE' | 'NETTOYAGE' | 'HORS_SERVICE';
    type!: 'REUNION' | 'CONFERENCE' | 'BANQUET' | 'FORMATION' | 'POLYVALENTE';
    equipements!: string;
  }
 