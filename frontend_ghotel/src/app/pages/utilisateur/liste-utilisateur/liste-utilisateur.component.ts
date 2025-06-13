import { Component, OnInit } from '@angular/core';
import { Utilisateur } from '../../../components/models/user';
import { UserService } from '../../../components/services/users/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxPaginationModule } from 'ngx-pagination';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FilterPipe } from '../../../pipes/filter.pipe';
import { AlertService } from '../../../components/services/alert/alert.service';
import { AuthService } from '../../../components/services/auth/auth.service';

@Component({
  selector: 'app-liste-utilisateur',
  standalone: true,
  imports: [NgxPaginationModule,FormsModule,CommonModule,FilterPipe],
  templateUrl: './liste-utilisateur.component.html',
  styleUrl: './liste-utilisateur.component.scss'
})
export class ListeUtilisateurComponent  implements  OnInit {

  
  users: any[] = [];
  //listusers: Array<any> = [];
    id!: number
  //dtOptions: DataTables.Settings = {};
  myFilterText!:any

  title = 'Pagination'
  page: number = 1
  count: number = 0;
  tableSize: number = 5;
  tableSizes: any = [5, 10, 15, 20]
  role!: string
  
  constructor(private authService: AuthService, private userService: UserService,private alertService: AlertService,private route: ActivatedRoute, private router: Router) { }
   

  ngOnInit(): void {
    this.getUserList();
     this.role = this.authService.getUserRole();
  }
  private getUserList() {
    this.userService.getUserList().subscribe({
      next: (users: Utilisateur[]) => {
        console.log('Utilisateurs reçus :', users);
        this.users = users; // 👈 directement la liste
      },
      error: (err) => {
        console.error('Erreur lors du chargement des utilisateurs', err);
      }
    });
  
    const token = localStorage.getItem('auth_token');
    console.log('Token utilisé :', token);
  }
  

  

  deleteUtilisateur(id: number) {
    this.alertService.confirmDelete(() => {
      this.userService.deleteUser(id).subscribe(() => {
        this.alertService.success("Utilisateur supprimé avec succès !");
        this.getUserList(); // Rafraîchir la liste
      }, error => {
        this.alertService.error("La suppression a échoué.");
      });
    });
  }
  

  onTableDataChange(event: any){
    this.page = event;
    this.getUserList()
  }

  onTableSizeChange(event: any){
    this.tableSize = event.target.value;
    this.page = 1;
    this.getUserList()
  }

  
  updateUtilisateur(id: number){
    this.router.navigate(['dashboard/utilisateurs/update', id]);
  }

   


/* reload*/
reloadPage(){
  this.router.routeReuseStrategy.shouldReuseRoute= () => false;
  this.router.onSameUrlNavigation = 'reload';
  this.router.navigate(['./'], {
    relativeTo: this.route
  })
}

goToAddUser() {
  this.router.navigate(['/dashboard/utilisateurs/add']); // ✅ Naviguer vers la liste des clients
}
 
 

}
