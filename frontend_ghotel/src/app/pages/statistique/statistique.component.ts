import { Component } from '@angular/core';
import { NgChartsModule } from 'ng2-charts';
import { ChartOptions, ChartData, ChartType } from 'chart.js';
import Swal from 'sweetalert2';
import { DashboardService } from '../../components/services/dashboard/dashboard.service';
import { ReactiveFormsModule,  FormBuilder, FormGroup} from '@angular/forms';
import { CommonModule, formatDate } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { AlertService } from '../../components/services/alert/alert.service';

@Component({
  selector: 'app-statistique',
  standalone: true,
  imports: [NgChartsModule, ReactiveFormsModule, CommonModule ],  
  templateUrl: './statistique.component.html',
  styleUrl: './statistique.component.scss'
})
export class StatistiqueComponent {

constructor( private dashboardService: DashboardService, private fb: FormBuilder, private alerteService: AlertService) { }
 
 nombreChambresTotal: number = 0;
 nombreChambresDispo: number = 0;
 nombreSalles: number = 0;
 nombreSallesDispo: number = 0;
 
   
   //RECHERCHE
   filterForm!: FormGroup;
  // TOTAL ENTREE
  startDate: string = '';
  endDate: string = '';
  totalEntrees: number | null = null;
  totalDepenses: number | null = null;
  benefice: number | null = null;
  soldeReel: number | null = null;
  totalEntreeResto: number | null = null;
  totalEntreeReservation: number | null = null;

 ngOnInit(): void {

  
  this.dashboardService.getNombreChambresDispo().subscribe({
    next: (data) => this.nombreChambresDispo = data,
    error: (err) => console.error('Erreur chargement nombre chambres', err)
  });

   this.dashboardService.getNombreChambresTotal().subscribe({
    next: (data) => this.nombreChambresTotal = data,
    error: (err) => console.error('Erreur chargement nombre chambres', err)
  });

   this.dashboardService.getNombreSalles().subscribe({
    next: (data) => this.nombreSalles = data,
    error: (err) => console.error('Erreur chargement nombre salles', err)
  });

   this.dashboardService.getNombreSallesDispo().subscribe({
    next: (data) => this.nombreSallesDispo = data,
    error: (err) => console.error('Erreur chargement nombre salles', err)
  });

    const now = new Date();

  const startOfDay = new Date(now);
  startOfDay.setHours(0, 0, 0, 0); // 00:00

  const isoStart = startOfDay.toISOString().slice(0, 16); // yyyy-MM-ddTHH:mm
  const isoEnd = now.toISOString().slice(0, 16);           // heure actuelle

  this.filterForm = this.fb.group({
    startDate: [isoStart],
    endDate: [isoEnd]
  });
 
  

 this.loadAllsData()
 
   
}

  loadAllsData(){

    this.alerteService.showLoading('Chargement des données en cours...', 4000);
          setTimeout(() => {
            this.alerteService.closeLoading();
            
            this.loadTotalEntrees()
            this.loadTotalDepenses()
            this.loadSoldeReel()
            this.loadTotalEntreeResto()
            this.loadTotalEntreeReservation()

            this.alerteService.confirmSuccess('Chargement effectué', 1000);
            
          }, 2000);

    
  }

 loadTotalEntrees2(): void {

    const start = this.filterForm.value.startDate;
    const end = this.filterForm.value.endDate;
   

    if (!this.filterForm.value.startDate || !this.filterForm.value.endDate) {
      alert("Veuillez sélectionner une plage de dates.");
      return;
    }

    if(this.filterForm.value.startDate > this.filterForm.value.endDate){
      this.alerteService.error('Veuillez sélectionner une plage de dates valide, la date de fin est inferieur à la date de debut.')
        return;
    }


    this.dashboardService.getTotalEntrees(this.filterForm.value)
    
      .subscribe({
        next: (res) => {
                     this.totalEntrees = res;
                     this.calculerBenefice();
                      },
        error: (err) => console.error('Erreur lors du chargement des entrées', err)
      });

        
  }


  loadTotalEntrees(): void {
  const startRaw = this.filterForm.value.startDate;
  const endRaw = this.filterForm.value.endDate;

 

  const startDate = new Date(startRaw);
  const endDate = new Date(endRaw);

  if (startDate > endDate) {
    this.alerteService.error("Veuillez sélectionner une plage de dates valide, la date de fin est inférieure à la date de début.");
    return;
  }

  const startDateTime = startDate.toISOString();
  const endDateTime = endDate.toISOString();

 if (!startDateTime || !endDateTime) {
    alert("Veuillez sélectionner une plage de dates.");
    return;
  }
  this.dashboardService.getTotalEntrees({ startDateTime, endDateTime })
    .subscribe({
      next: (res) => {
        this.totalEntrees = res;
        this.calculerBenefice();
      },
      error: (err) => console.error('Erreur lors du chargement des entrées', err)
    });
}



   loadTotalDepenses(): void {

    const startRaw = this.filterForm.value.startDate;
    const endRaw = this.filterForm.value.endDate;

    const startDate = new Date(startRaw);
    const endDate = new Date(endRaw);

    const startDateTime = startDate.toISOString();
    const endDateTime = endDate.toISOString();

    console.log('Recherche entre DEPENSES :', startDateTime, 'et', endDateTime);
    
    this.dashboardService.getTotalDepenses({ startDateTime, endDateTime })
      .subscribe({
        next: (res) => {
                         this.totalDepenses = res;
                         console.log('TOTAL DEPENSES : ', this.totalDepenses)
                         this.calculerBenefice();
                      },
        error: (err) => console.error('Erreur lors du chargement des entrées', err)
      }); 
  }
  
   calculerBenefice(): void {
            if (this.totalEntrees !== null && this.totalDepenses !== null) {
            this.benefice = this.totalEntrees - this.totalDepenses;
            }
         }

    loadSoldeReel() {
    this.dashboardService.getSoldeReel().subscribe({
      next: (response: any) => {
        console.log('Données reçues :', response);
        if (response) {
         
             this.soldeReel = response
            }
         },
          error: (err) => {
            console.error('Erreur lors du chargement du solde', err);
            }
         });
      }

  loadTotalEntreeResto(): void {
   const startRaw = this.filterForm.value.startDate;
    const endRaw = this.filterForm.value.endDate;

    const startDate = new Date(startRaw);
    const endDate = new Date(endRaw);

    const startDateTime = startDate.toISOString();
    const endDateTime = endDate.toISOString();

    console.log('Recherche entre RESTO :', startDateTime, 'et', endDateTime);

    this.dashboardService.getTotalEntreeResto({ startDateTime, endDateTime })
      .subscribe({
        next: (res) => {
                         this.totalEntreeResto = res;
                          console.log('resto', res)
                         
                      },
        error: (err) => console.error('Erreur lors du chargement des entrées', err)
      }); 
  }

  loadTotalEntreeReservation(): void {
   const startRaw = this.filterForm.value.startDate;
    const endRaw = this.filterForm.value.endDate;

    const startDate = new Date(startRaw);
    const endDate = new Date(endRaw);

    const startDateTime = startDate.toISOString();
    const endDateTime = endDate.toISOString();

    console.log('Recherche entre RESERVATION :', startDateTime, 'et', endDateTime);
    
    this.dashboardService.getTotalEntreeReservation({ startDateTime, endDateTime })
      .subscribe({
        next: (res) => {
                         this.totalEntreeReservation = res;
                         
                      },
        error: (err) => console.error('Erreur lors du chargement des entrées', err)
      }); 
  }
  
  // Configuration du Bar Chart
public barChartOptions: ChartOptions = {
  responsive: true,
};

public barChartLabels: string[] = ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin', 'Juil', 'Août', 'Sept', 'Oct', 'Nov', 'Déc'];
public barChartData: ChartData<'bar'> = {
  labels: this.barChartLabels,
  datasets: [
    {
      label: 'Chambres occupées (nombre)',
      data: [50, 70, 120, 100, 90, 150, 170, 200, 210, 190, 160, 140], // Exemple de données d'occupation
      backgroundColor: 'rgba(54, 162, 235, 0.6)',
      borderColor: 'rgba(54, 162, 235, 1)',
      borderWidth: 1,
    }
  ]
};

public barChartType: ChartType = 'bar';

   public pieChartLabels: string[] = ['Chambres Standard', 'Chambres Deluxe', 'Suites', 'Chambres Familiales'];
public pieChartData: ChartData<'pie'> = {
  labels: this.pieChartLabels,
  datasets: [
    {
      data: [40, 30, 20, 10], // Exemple de données pour la répartition des chambres réservées
      backgroundColor: ['#ff6384', '#36a2eb', '#ffcd56', '#4bc0c0'],
    }
  ]
};

public pieChartType: ChartType = 'pie';

  
 public lineChartData: ChartData<'line'> = {
  labels: this.barChartLabels,
  datasets: [
    {
      label: 'Taux de réservation (%)',
      data: [10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 100, 90], // Exemple de données de taux de réservation
      borderColor: '#ff6384',
      backgroundColor: 'rgba(255, 99, 132, 0.2)',
      fill: true,
    }
  ]
};

public lineChartType: ChartType = 'line';
  
  
  
    showAlert() {
      Swal.fire({
        title: 'Alerte !',
        text: 'Ceci est une alerte avec SweetAlert2.',
        icon: 'warning',
        confirmButtonText: 'OK'
      });
    }
  
    showLoading(message: string = 'Chargement en cours...') {
      Swal.fire({
        title: message,
        didOpen: () => {
          Swal.showLoading(); // Affiche l'indicateur de chargement
        },
        allowOutsideClick: false, // Empêche la fermeture en cliquant à l'extérieur
        allowEscapeKey: false, // Empêche la fermeture avec la touche Échap
      });
    }
  
    // Fermer l'indicateur de chargement
    closeLoading() {
      Swal.close();
    }
  
  

}
