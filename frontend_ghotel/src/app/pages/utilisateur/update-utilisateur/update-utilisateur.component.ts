import { Component, OnInit } from '@angular/core';
import { AlertService } from '../../../components/services/alert/alert.service';
import { UserService } from '../../../components/services/users/user.service';
import { Utilisateur } from '../../../components/models/user';
import { Role } from '../../../components/models/role';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { RoleService } from '../../../components/services/role/role.service';

@Component({
  selector: 'app-update-utilisateur',
  standalone: true,
  imports: [FormsModule,ReactiveFormsModule,CommonModule],
  templateUrl: './update-utilisateur.component.html',
  styleUrl: './update-utilisateur.component.scss'
})
export class UpdateUtilisateurComponent implements OnInit {

  utilisateurForm!: FormGroup;
   
  utilisateur : Utilisateur = new Utilisateur()
  roles: any[] = [];
   id!: number
    
   constructor(
    private userService: UserService,
    private roleService: RoleService,
    private route: ActivatedRoute, 
    private router: Router, 
    private alertService: AlertService,
    private fb: FormBuilder
  ) { }
 
   ngOnInit(): void {
    
    this.utilisateurForm = this.fb.group({

      nom: ['', Validators.required],
      prenom: ['', Validators.required],
      email: ['', Validators.required],
      telephone: ['', Validators.required],
      role: ['', Validators.required],
       
      
    });

     this.id = this.route.snapshot.params['id']

     this.userService.getUserById(this.id).subscribe({

     next: (response: any) => {
      
      if (response) {
         this.utilisateur = response; // ✅ Extraire `data`
         console.log('Données reçues :', response); // 🔍 Vérifier la structure dans la console
         this.utilisateurForm.patchValue({

          nom: response.nom,
          prenom: response.prenom,
          email: response.email,
          telephone: response.telephone,
          role: response.role.id,
          username: response.username,
          password: response.password

        });
         console.log('Formulaire après injection :', this.utilisateurForm.value);
        }
     },
     error: (err) => {
       console.error('Erreur lors du chargement des utilisateurs', err);
     }
   });

   
   
      this.getRoleList()
   }
 
   onSubmit(){
     this.updateUser()
    
   }


   updateUser(): void {
 

  const formData = this.utilisateurForm.value;

  const updatedUser: Utilisateur = {
    id: this.id,
    nom: formData.nom,
    prenom: formData.prenom,
    email: formData.email,
    telephone: formData.telephone,
    username: this.utilisateur.username, // ajoute-le si nécessaire
    password: this.utilisateur.password,
     // ou garde l'ancien si non modifié
    role: {
      id: formData.role,
      nomrole: '' // optionnel, juste l'id suffit souvent côté backend
    }
  };
     console.log(" DONNEES SENDED", updatedUser)
  this.userService.updateUser(this.id, updatedUser).subscribe({
    next: () => {
      this.alertService.confirmSuccess('Mise à jour réussie', 2000);
      setTimeout(() => {
        this.alertService.closeLoading();
        
        this.goToUserList();
      }, 3000);
     
    },
    error: () => {
      this.alertService.confirmError('Erreur lors de la mise à jour', 2000);
    }
  });
  }
 
   
 
     goToUserList(){
       this.router.navigate(['/dashboard/utilisateurs/liste']); 
     }

     private getRoleList() {

      this.roleService.getRoleList().subscribe({
        next: (roles: Role[]) => {
          console.log('Roles reçus :', roles);
          this.roles = roles; // 👈 directement la liste
        },
        error: (err) => {
          console.error('Erreur lors du chargement des Roles', err);
        }
      });
    
      const token = localStorage.getItem('auth_token');
      console.log('Token utilisé :', token);
    }

}
