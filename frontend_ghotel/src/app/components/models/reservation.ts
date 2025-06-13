import { Client } from '../models/client';
import { LigneReservation } from '../models/ligne-reservation';
import { Facture } from './facture';

export class Reservation {
  id?: number;
  code!: string;
  dateReservation!: Date;
  dateDebut!: Date;
  dateFin!: Date;
  montantTotal!: number;
  montantRemise!: number;
  statut!: 'EN_COURS' | 'CONFIRMEE' | 'ANNULEE'; // ou string si libre
  client_id!: number;
  client!: Client;
  lignes!: LigneReservation[];
  showDetails?: boolean;
  facture!: Facture | null;
}
