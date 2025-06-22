import { Component, OnInit } from '@angular/core';
import { Depense } from '../../../components/models/depense';
import { DepenseService } from '../../../components/services/depense/depense.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxPaginationModule } from 'ngx-pagination';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FilterPipe } from '../../../pipes/filter.pipe';
import { AlertService } from '../../../components/services/alert/alert.service';
import { AuthService } from '../../../components/services/auth/auth.service';

@Component({
  selector: 'app-list-depense',
  standalone: true,
  imports: [NgxPaginationModule, ReactiveFormsModule, FormsModule, CommonModule, FilterPipe],
  templateUrl: './list-depense.component.html',
  styleUrl: './list-depense.component.scss'
})
export class ListDepenseComponent implements OnInit {
  depenses: Depense[] = [];
  totalDepenses: number = 0;
  id!: number;
  myFilterText!: any;

  title = 'Pagination';
  page: number = 1;
  count: number = 0;
  tableSize: number = 5;
  tableSizes: any = [5, 10, 15, 20];
  role!: string
  filterForm!: FormGroup;
  startDate: string = '';
  endDate: string = '';

  constructor(
    private depenseService: DepenseService,
    private alertService: AlertService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
     private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    const now = new Date();
    const startOfDay = new Date(now);
    startOfDay.setHours(0, 0, 0, 0); // 00:00
    const isoStart = startOfDay.toISOString().slice(0, 16); // yyyy-MM-ddTHH:mm
    const isoEnd = now.toISOString().slice(0, 16);           // heure actuelle
    this.filterForm = this.fb.group({
    startDate: [isoStart],
    endDate: [isoEnd]
  });
 
    this.loadDepensesData();
    this.role = this.authService.getUserRole();
  }

  loadDepensesData(){
    this.DepenseList()
  }

 private DepenseList() {
  
   const startRaw = this.filterForm.value.startDate;
    const endRaw = this.filterForm.value.endDate;

    const startDate = new Date(startRaw);
    const endDate = new Date(endRaw);

    const startDateTime = startDate.toISOString();
    const endDateTime = endDate.toISOString();

  this.depenseService.getListDepensesEtTotal({ startDateTime, endDateTime }).subscribe({
    next: (response: any) => {
      console.log('Réponse reçue :', response);
      if (response) {
        this.depenses = response.liste;
        this.totalDepenses = response.montantTotal;
      }
    },
    error: (err) => {
      console.error('Erreur lors du chargement des dépenses', err);
    }
  });
}


  deleteDepense(id: number) {
    this.alertService.confirmDelete(() => {
      this.depenseService.deleteDepense(id).subscribe(() => {
        this.alertService.success("Dépense supprimée avec succès !");
        this.DepenseList();
      }, error => {
        this.alertService.error("La suppression a échoué.");
      });
    });
  }

  onTableDataChange(event: any) {
    this.page = event;
    this.DepenseList();
  }

  onTableSizeChange(event: any) {
    this.tableSize = event.target.value;
    this.page = 1;
    this.DepenseList();
  }

  updateDepense(id: number) {
    this.router.navigate(['/dashboard/depenses/update', id]);
  }

  reloadPage() {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigate(['./'], {
      relativeTo: this.route
    });
  }

  goToAddDepense() {
    this.router.navigate(['/dashboard/depenses/add']);
  }
}
