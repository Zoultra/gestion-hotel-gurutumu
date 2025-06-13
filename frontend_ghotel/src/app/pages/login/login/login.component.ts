import { Component } from '@angular/core';
import { AuthService } from '../../../components/services/auth/auth.service';
import { Router } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule,CommonModule,ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: []
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  errorMessage: string = '';
  successMessage: string = ''
  passwordVisible: boolean = false;
  loading: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  onLogin() {

    this.loading = true;  // Active le spinner
    this.errorMessage = ''; // Réinitialise le message d'erreur avant de commencer

   

    
    setTimeout(() => {
      this.loading = false; // Désactive le spinner après la réponse

      this.authService.login(this.username, this.password).subscribe({
        next: (response) => {
          this.authService.saveToken(response.token);
          this.authService.saveUser(response.user);
          this.successMessage = "Connexion Reussie !!! "
           console.log(this.successMessage, response.token)
          this.router.navigate(['/dashboard/statistique']); // Rediriger après connexion
        },
        error: (err) => {
          this.errorMessage = 'Identifiants incorrects. Veuillez réessayer.';
          console.error(err);
        }
      });

    }, 2000);

   

  }

  togglePasswordVisibility() {
    this.passwordVisible = !this.passwordVisible;
  }
}

 