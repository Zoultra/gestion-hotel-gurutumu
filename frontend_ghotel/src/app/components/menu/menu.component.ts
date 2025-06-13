import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Menu } from './menu';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../components/services/auth/auth.service';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit {
  menuOpen = false;
   userRole: string = '';
  toggleMenu() {
    this.menuOpen = !this.menuOpen;
  }
  
  public menuProperties: Array<Menu> = [ 
    {
      id: '1',
      titre: 'Tableau de bord',
      icon: 'fa fa-line-chart',
      url: '',
      sousMenu: [
        {
          id: '1.1',
          titre: 'Vue d\'ensemble',
          icon: 'fa fa-chart-pie',
          url: 'dashboard/statistique'
        }
      ]
    },
    {
      id: '10',
      titre: 'Caisse',
      icon: 'fa fa-archive',
      url: '',
      sousMenu: [
        {
          id: '10.1',
          titre: 'Gestion',
          icon: 'fa fa-pencil-square-o',
          url: 'dashboard/caisses/gestion'
        },
         {
          id: '10.2',
          titre: 'Historique',
          icon: 'fa fa-history',
          url: 'dashboard/caisses/historiques'
        } ,
         {
          id: '10.3',
          titre: 'Opérations journalières',
          icon: 'fa fa-history',
          url: 'dashboard/mvtcaisse/liste'
        } 
      ]
    },
   
   
    {
      id: '2',
      titre: 'Client',
      icon: 'fa fa-users',
      url: '',
      sousMenu: [
        {
          id: '2.1',
          titre: 'Gestion',
          icon: 'fa fa-pencil-square-o',
          url: 'dashboard/clients/liste'
        } 
      ]
    },
    {
      id: '3',
      titre: 'Chambre',
      icon: 'fa fa-bed',
      url: '',
      sousMenu: [
        {
          id: '3.1',
          titre: 'Gestion',
          icon: 'fa fa-edit',
          url: 'dashboard/chambres/liste'
        } 
      ]
    },
    {
      id: '4',
      titre: 'Réservation',
      icon: 'fa fa-calendar-check-o',
      url: '',
      sousMenu: [
        {
          id: '4.1',
          titre: 'Gestion',
          icon: 'fa fa-edit',
          url: 'dashboard/reservations/liste'
        } 
      ]
    },
    {
      id: '5',
      titre: 'Restaurant',
      icon: 'fa fa-cutlery',
      url: '',
      sousMenu: [
        {
          id: '5.1',
          titre: 'Point de vente',
          icon: 'fa fa-shopping-cart',
           url: 'dashboard/ventes/pos'
        },
        {
          id: '5.1.1',
          titre: 'Liste vente',
          icon: 'fa fa-list',
           url: 'dashboard/ventes/liste'
        },
        {
          id: '5.2',
          titre: 'Gestion Articles',
          icon: 'fa fa-cutlery',
          url: 'dashboard/articles/liste'
        } 
      ]
    },
    {
      id: '6',
      titre: 'Salles',
      icon: 'fa fa-building-o',
      url: '',
      sousMenu: [
        {
          id: '6.1',
          titre: 'Gestion',
          icon: 'fa fa-edit',
          url: 'dashboard/salles/liste'
        }
      ]
    },
    {
      id: '7',
      titre: 'Dépenses',
      icon: 'fa fa-money',
      url: '',
      sousMenu: [
        {
          id: '7.1',
          titre: 'Gestion',
          icon: 'fa fa-edit',
          url: 'dashboard/depenses/liste'
        } 
      ]
    },
    {
      id: '9',
      titre: 'Comptes',
      icon: 'fa fa-user',
      url: '',
      adminOnly: true,
      sousMenu: [
        {
          id: '9.1',
          titre: 'Rôles',
          icon: 'fa fa-list',
          url: 'dashboard/roles/liste'
        },
        {
          id: '9.2',
          titre: 'Utilisateurs',
          icon: 'fa fa-users',
          url: 'dashboard/utilisateurs/liste'
        }
      ]
    }
  ];
  

  constructor(
    private router:Router,
      private authService: AuthService
  ) { }

  ngOnInit(): void {
      this.userRole = this.authService.getUserRole();
  }

  navigate(url?: string): void {
    this.router.navigate([url])
     
  }

   isMenuVisible(menu: Menu): boolean {
    if (!menu.adminOnly) return true;
    return this.userRole === 'ADMIN';
  }

  

}
