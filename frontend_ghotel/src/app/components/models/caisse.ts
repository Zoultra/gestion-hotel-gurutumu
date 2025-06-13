
import { Utilisateur } from '../models/user';
import { MouvementCaisse } from './mouvementCaisse';
export class Caisse {

    caisseId!: number;

    dateOuverture!: Date;

    dateFermeture!: Date;

    heureOuverture!: string;
    heureFermeture!: string;
 

    soldeInitial!: number;

    soldeFinalTheorique!: number;

    soldeFinal!: number;

    ouverte!: boolean;

    utilisateur!: Utilisateur;

    mouvements?: MouvementCaisse[];

    showDetails?: boolean;
  }