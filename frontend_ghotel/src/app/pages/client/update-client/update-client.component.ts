import { Component, OnInit } from '@angular/core';
import { Utilisateur } from '../../../components/models/user';
import { UserService } from '../../../components/services/users/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AlertService } from '../../../components/services/alert/alert.service';

@Component({
  selector: 'app-update-client',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './update-client.component.html',
  styleUrl: './update-client.component.scss'
})


export class UpdateClientComponent implements OnInit {
  
  user : Utilisateur = new Utilisateur()
  id!: number
  role!: string
  constructor(private userService: UserService,private route: ActivatedRoute, private router: Router, private alertService: AlertService) { }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id']
    this.userService.getUserById(this.id).subscribe({
    next: (response: any) => {
      console.log('Données reçues :', response); // 🔍 Vérifier la structure dans la console
      if (response.success) {
        this.user = response.data; // ✅ Extraire `data`
      }
    },
    error: (err) => {
      console.error('Erreur lors du chargement des utilisateurs', err);
    }
  });
  
  }

  onSubmit(){
    this.updateUser()
   
  }

  updateUser(): void{
    this.userService.updateUser(this.id, this.user).subscribe(
      (data) => {
        this.alertService.showLoading('Mise en jour en cours...', 5000)
        setTimeout(() => {
                            // Ferme l'alerte de chargement
                           
                            this.alertService.closeLoading();
                            // Affiche une alerte de succès avec un symbole de vérification
                            // Afficher l'alerte de succès
                           
                            
                              this.alertService.confirmSuccess('Mise à jour effectuée avec succès', 2000);
                        
                              this.goToClientList()
                         
                            
                          }, 3000);
        console.log(this.user);
       
      },
      (error) => {

        this.alertService.showLoading('Mise en jour en cours...', 5000)
        setTimeout(() => {
                            // Ferme l'alerte de chargement
                           
                            this.alertService.closeLoading();
                            // Affiche une alerte de succès avec un symbole de vérification
                            // Afficher l'alerte de succès
                            this.alertService.confirmError('Echec de la mise à jour', 2000);
                          }, 3000);
        console.error('Erreur lors de la mise à jour :', error);
      }
    );
    
    }

    goToClientList(){
      this.router.navigate(['/dashboard/utilisateurs/liste']); 
    }

}
