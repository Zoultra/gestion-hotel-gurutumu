export interface LigneReservation {
  id?: number;
  type: 'CHAMBRE' | 'SALLE';
  idObjet: number;               // ID de la chambre ou salle
  nomObjet?: string;             // Optionnel : pour affichage dans la table
  prixUnitaire: number;
  nombreJours: number;


  chambre?: {
    id: number;
    numero: string;
    type: string;
    tarifParNuit: number;
    description?: string;
  };

  salle?: {
    id: number;
    numero: string;
    capacite: number;
    description?: string;
  };
   
}
