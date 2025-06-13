import { Component, OnInit } from '@angular/core';
import { Client } from '../../../components/models/client';
import { ClientService } from '../../../components/services/client/client.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxPaginationModule } from 'ngx-pagination';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FilterPipe } from '../../../pipes/filter.pipe';
import { AlertService } from '../../../components/services/alert/alert.service';
import { AuthService } from '../../../components/services/auth/auth.service';
 

@Component({
  selector: 'app-list-client',
  standalone: true,
  imports: [NgxPaginationModule,FormsModule,CommonModule,FilterPipe],
  templateUrl: './list-client.component.html',
  styleUrl: './list-client.component.scss'
})
 

export class ListClientComponent implements  OnInit {

  role!: string
  clients: any[] = [];
  id!: number
  myFilterText!:any

  title = 'Pagination'
  page: number = 1
  count: number = 0;
  tableSize: number = 5;
  tableSizes: any = [5, 10, 15, 20]
  

  constructor(private clientService: ClientService,
    private alertService: AlertService,
    private route: ActivatedRoute, 
    private router: Router,
    private authService: AuthService) { }
   

  ngOnInit(): void {
    this.getClientList();
    this.role = this.authService.getUserRole();
  }

  private getClientList(){
    this.clientService.getClientList().subscribe({
      next: (response: any) => {
        console.log('Données reçues :', response); // 🔍 Vérifier la structure dans la console
        if (response) {
          this.clients = response; // ✅ Extraire `data`
        }
      },
      error: (err) => {
        console.error('Erreur lors du chargement des utilisateurs', err);
      }
    });

  }

  

  

  deleteClient(id: number) {
    this.alertService.confirmDelete(() => {
      this.clientService.deleteClient(id).subscribe(() => {
        this.alertService.success("Client supprimé avec succès !");
        this.getClientList(); // Rafraîchir la liste
      }, error => {
        this.alertService.error("La suppression a échoué.");
      });
    });
  }
  

  onTableDataChange(event: any){
    this.page = event;
    this.getClientList()
  }

  onTableSizeChange(event: any){
    this.tableSize = event.target.value;
    this.page = 1;
    this.getClientList()
  }

  
  updateClient(id: number){
    this.router.navigate(['dashboard/clients/update', id]);
  }

   
/* reload*/
reloadPage(){
  this.router.routeReuseStrategy.shouldReuseRoute= () => false;
  this.router.onSameUrlNavigation = 'reload';
  this.router.navigate(['./'], {
    relativeTo: this.route
  })
}

 
 goToAddClient() {
     this.router.navigate(['/dashboard/clients/add'], {
     queryParams: { from: 'client' }
    });

  }
 
} 
