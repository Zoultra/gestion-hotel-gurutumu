import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgToastService } from 'ng-angular-popup';
import { UserService } from '../../../components/services/users/user.service';
import { AlertService } from '../../../components/services/alert/alert.service';
import { Utilisateur } from '../../../components/models/user';
import { FormBuilder, FormGroup, FormsModule, Validators, ReactiveFormsModule} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RoleService } from '../../../components/services/role/role.service';
import { Role } from '../../../components/models/role';

@Component({
  selector: 'app-create-utilisateur',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './create-utilisateur.component.html',
  styleUrl: './create-utilisateur.component.scss'
})
export class CreateUtilisateurComponent {
         
  utilisateurForm!: FormGroup;
  
    constructor(

      private router: Router,
      private toast: NgToastService, 
      private userService: UserService,
      private alertService: AlertService, 
      private roleService: RoleService,
      private fb: FormBuilder) { }
      
    
    utilisateur: Utilisateur = {
      id: 0,
      nom: '',
      prenom: '',
      email: '',
      username: '',
      password: '',
      telephone: '',
      role: {
        id: 0,
        nomrole: ''
      }
    };
    
    roles: any[] = [];

    ngOnInit(): void {
      
       this.utilisateurForm = this.fb.group({
            nom: ['', Validators.required],
            prenom: ['', Validators.required],
            email: ['', Validators.required],
            role_id: ['', Validators.required],
            telephone: ['', Validators.required],
            username: ['', Validators.required],
            password: ['', Validators.required],
            
          });

      this.getRoleList()

    }
      
    onSubmit(){
               this.saveUser()
             }
  
      
         saveUser(){

          if (this.utilisateurForm.valid) {
  
            const formData = {
              ...this.utilisateurForm.value,
               
            };
            console.log(this.utilisateurForm.value);
            
            this.userService.createUser(formData).subscribe(
              data => {
                
                this.alertService.showLoading('Enregistrement en cours...', 3000);
                setTimeout(() => {
                  this.alertService.closeLoading();
                  this.alertService.confirmSuccess('Enregistrement effectué', 1000);
                  this.goToUserList();
                }, 2000);
                console.log(data);
              },
              error => {
                this.alertService.confirmError('Enregistrement échoué', 2000);
              }
            );
          }
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
  
              goToUserList(){
                this.router.navigate(['/dashboard/utilisateurs/liste']); 
              }
}
