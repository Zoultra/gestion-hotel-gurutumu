import { Component } from '@angular/core';
import { AuthService } from '../../components/services/auth/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AlertService } from '../services/alert/alert.service';
@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
   user: any;
   userFullName: string = '';
   role: string = '';
 
   constructor(private authService: AuthService, private router: Router,private alertService: AlertService) {}

  ngOnInit() {
    this.userFullName = this.authService.getUserFullName(); // Récupérer l'utilisateur connecté
    this.role = this.authService.getUserRole();
    console.log(this.user);
  }

 handleLogout() {
      this.alertService.confirmLogout(() => { 
      this.authService.logout();
       });

          }

          
  }
