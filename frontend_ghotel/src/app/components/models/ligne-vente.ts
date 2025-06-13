import { Article } from '../models/article';

export class LigneVente {
  id?: number;
  article!: Article;
  quantite: number = 1;
  prixUnitaire: number = 0;
}