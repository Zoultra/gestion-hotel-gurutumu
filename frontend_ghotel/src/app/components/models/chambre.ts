export class Chambre {
    id!: number;
    numero!: string;
    type!: 'SIMPLE' | 'DOUBLE' | 'MINI_SUITE';
    tarifParNuit!: number;
    etat!: 'DISPONIBLE' | 'OCCUPEE' | 'MAINTENANCE';
    description!: string;
  }
  