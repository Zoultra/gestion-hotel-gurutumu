import { Component, OnInit } from '@angular/core';
import { ClientService } from '../../../components/services/client/client.service';
import { Router, ActivatedRoute } from '@angular/router';
import { NgToastService } from 'ng-angular-popup';
import { FormsModule } from '@angular/forms';
import { AlertService } from '../../../components/services/alert/alert.service';
import { Client } from '../../../components/models/client';

@Component({
  selector: 'app-create-client',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './create-client.component.html',
  styleUrl: './create-client.component.scss'
})


export class CreateClientComponent implements OnInit {

  constructor( private route: ActivatedRoute, private router: Router, private toast: NgToastService, private clientService: ClientService, private alertService: AlertService) { }
    
  client : Client = new Client()

  isEditMode: boolean = false;

  selectedClient: any = null;
  
  fromPage: string = 'client'; // valeur par défaut

  errorMsgFromBackend: string | null = null;

 ngOnInit() {
   this.route.queryParams.subscribe(params => {
 
    this.fromPage = params['from'] || 'client';
    
  });
  const id = this.route.snapshot.params['id'];
  if (id) {
    this.isEditMode = true;
    this.clientService.getClientById(id).subscribe(data => {
      this.client = data;
    });
  }

}

onSubmit() {
  if (this.isEditMode) {
    this.clientService.updateClient(this.client.id, this.client).subscribe(() => {
      this.alertService.success('Client modifié avec succès !');
     
    });
  } else {
    this.clientService.createClient(this.client).subscribe({
    
     next: () => {  

      if (this.fromPage === 'reservation') {
          this.errorMsgFromBackend = null;
           this.alertService.confirmSuccess('Client enregistré avec succès !', 2000)
           this.router.navigate(['/dashboard/reservations/liste']);
         
           } else {
            this.alertService.confirmSuccess('Client enregistré avec succès !', 2000)
            this.router.navigate(['/dashboard/clients/liste']);
           }
    },
     error: err => {
      this.alertService.error(this.errorMsgFromBackend = err?.error?.message || 'Erreur inconnue.')
      
    }
      
    });
  }
}

resetForm() {
  this.isEditMode = false;
  this.selectedClient = null;
  this.goToClientList(); // rafraîchir la liste
}
    
          

goToClientList(){
    this.router.navigate(['/dashboard/clients/liste']); 
              }

}

  
 

