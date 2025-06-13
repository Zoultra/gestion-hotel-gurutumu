import { Component } from '@angular/core';
import { RoleService } from '../../../components/services/role/role.service';
import { AlertService } from '../../../components/services/alert/alert.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Role } from '../../../components/models/role';
import { FilterPipe } from '../../../pipes/filter.pipe';
import { NgxPaginationModule } from 'ngx-pagination';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

declare var window: any;

@Component({
  selector: 'app-gestion-role',
  standalone: true,
  imports: [NgxPaginationModule,FormsModule,CommonModule,FilterPipe],
  templateUrl: './gestion-role.component.html',
  styleUrl: './gestion-role.component.scss'
})
export class GestionRoleComponent {
 
  roles: Role[] = [];
  selectedRole: Role = { id: null, nomrole: '' };
  isEditMode = false;
  roleModal: any;
  id!: number
  myFilterText!:any
  title = 'Pagination'
  page: number = 1
  count: number = 0;
  tableSize: number = 5;
  tableSizes: any = [5, 10, 15, 20]
  
constructor(private roleService: RoleService,private alertService: AlertService,private route: ActivatedRoute, private router: Router) { }
   
ngOnInit(): void {

  this.getRoleList()
  this.roleModal = new window.bootstrap.Modal(document.getElementById('roleModal'));

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

/* reload*/
reloadPage(){
  this.router.routeReuseStrategy.shouldReuseRoute= () => false;
  this.router.onSameUrlNavigation = 'reload';
  this.router.navigate(['./'], {
    relativeTo: this.route
  })
}


onTableDataChange(event: any){
  this.page = event;
  this.getRoleList()
}

onTableSizeChange(event: any){
  this.tableSize = event.target.value;
  this.page = 1;
  this.getRoleList()
}

 

goToAddRole() {
  this.selectedRole = { id: null, nomrole: '' };
  this.isEditMode = false;
  this.roleModal.show();
}

updateRole(role: Role) {
  this.selectedRole = { ...role };
  this.isEditMode = true;
  this.roleModal.show();
}

onSaveRole() {
  if (this.isEditMode) {
    // Créer un objet contenant uniquement les champs nécessaires
    const updatedData = { nomrole: this.selectedRole.nomrole };

    this.roleService.updateRole(this.selectedRole.id!, updatedData).subscribe(() => {
      this.alertService.success('Rôle modifié avec succès !');
      this.getRoleList();
      this.roleModal.hide();
      console.log(updatedData)
    }, () => {
      this.alertService.error('La mise à jour a échoué.');
    });

  } else {
    this.roleService.createRole(this.selectedRole).subscribe(() => {
      this.alertService.success("Rôle créé avec succès !");
      this.getRoleList();
      this.roleModal.hide();
    }, () => {
      this.alertService.error('La création a échoué.');
    });
  }
}


 

 
deleteRole(id: number) {
  
  this.alertService.confirmDelete(() => {
    this.roleService.deleteRole(id).subscribe({
      
      next: () => {
      this.alertService.success("Utilisateur supprimé avec succès !");
      this.getRoleList(); // Rafraîchir la liste
    },  error: () =>  {
      this.alertService.error("La suppression a échoué.");
    }});
  });
}

}
 
