import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { StatistiqueComponent } from './pages/statistique/statistique.component';
import { LoginComponent } from './pages/login/login/login.component';
import { AuthGuard } from './components/guards/auth.guard';
import { CreateUtilisateurComponent } from './pages/utilisateur/create-utilisateur/create-utilisateur.component';
import { ListeUtilisateurComponent } from './pages/utilisateur/liste-utilisateur/liste-utilisateur.component';
import { UpdateUtilisateurComponent } from './pages/utilisateur/update-utilisateur/update-utilisateur.component';
import { GestionRoleComponent } from './pages/role/gestion-role/gestion-role.component';
import { ListClientComponent } from './pages/client/list-client/list-client.component';
import { CreateClientComponent } from './pages/client/create-client/create-client.component';
import { CreateUpdateChambreComponent } from './pages/chambre/create-update-chambre/create-update-chambre.component';
import { ListChambreComponent } from './pages/chambre/list-chambre/list-chambre.component';
import { CreateUpdateSalleComponent } from './pages/salle/create-update-salle/create-update-salle.component';
import { ListSalleComponent } from './pages/salle/list-salle/list-salle.component';
import { ListDepenseComponent } from './pages/depense/list-depense/list-depense.component';
import { CreateUpdateDepenseComponent } from './pages/depense/create-update-depense/create-update-depense.component';
import { CreateUpdateArticleComponent } from './pages/article/create-update-article/create-update-article.component';
import { ListArticleComponent } from './pages/article/list-article/list-article.component';
import { GestionArticleComponent } from './pages/article/gestion-article/gestion-article.component';
import { VenteFormComponent } from './pages/vente/vente-form/vente-form.component';
import { ListVenteComponent } from './pages/vente/list-vente/list-vente.component';
import { ListReservationComponent } from './pages/reservation/list-reservation/list-reservation.component';
import { ReservationFormComponent } from './pages/reservation/reservation-form/reservation-form.component';
import { MouvementCaisseComponent } from './pages/mouvement-caisse/mouvement-caisse/mouvement-caisse.component';
import { UpdateReservationComponent } from './pages/reservation/update-reservation/update-reservation.component';
import { UpdateVenteComponent } from './pages/vente/update-vente/update-vente.component';
import { GestionCaisseComponent } from './pages/caisse/gestion-caisse/gestion-caisse.component';
import { OuvertureCaisseComponent } from './pages/caisse/ouverture-caisse/ouverture-caisse.component';
import { HistoriqueCaisseComponent } from './pages/caisse/historique-caisse/historique-caisse.component';
 
export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },

  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard], 
    children: [
      { path: '', redirectTo: 'statistique', pathMatch: 'full' },
      { path: 'statistique', component: StatistiqueComponent },
      { path: 'utilisateurs/add', component: CreateUtilisateurComponent },
      { path: 'utilisateurs/liste', component: ListeUtilisateurComponent },
      { path: 'utilisateurs/update/:id', component: UpdateUtilisateurComponent },

      { path: 'roles/liste', component: GestionRoleComponent },

      { path: 'clients/liste', component: ListClientComponent },
      { path: 'clients/add', component: CreateClientComponent },
      { path: 'clients/update/:id', component: CreateClientComponent },
      { path: 'chambres/liste',  component: ListChambreComponent },
      { path: 'chambres/add',  component: CreateUpdateChambreComponent },
      { path: 'chambres/update/:id', component: CreateUpdateChambreComponent },
      { path: 'salles/add', component: CreateUpdateSalleComponent },
      { path: 'salles/update/:id', component: CreateUpdateSalleComponent },
      { path: 'salles/liste', component: ListSalleComponent },
      { path: 'depenses/liste', component: ListDepenseComponent },
      { path: 'depenses/add', component: CreateUpdateDepenseComponent },
      { path: 'depenses/update/:id', component: CreateUpdateDepenseComponent },
      { path: 'articles/add', component: CreateUpdateArticleComponent },
      { path: 'articles/update/:id', component: CreateUpdateArticleComponent },
      { path: 'articles/liste', component: ListArticleComponent },
      { path: 'ventes/pos', component: VenteFormComponent },
      { path: 'ventes/liste', component: ListVenteComponent },
      { path: 'ventes/update/:id', component: UpdateVenteComponent },
      { path: 'reservations/liste', component: ListReservationComponent },
      { path: 'reservations/add', component: ReservationFormComponent },
      { path: 'reservations/update/:id', component: UpdateReservationComponent },
      { path: 'mvtcaisse/liste', component: MouvementCaisseComponent },
      { path: 'caisses/gestion', component: GestionCaisseComponent },
      { path: 'caisses/ouverture', component: OuvertureCaisseComponent },
       { path: 'caisses/historiques', component: HistoriqueCaisseComponent },

      
    ] 
  },

  { path: '**', redirectTo: 'login' } // Redirection si la route est invalide
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
