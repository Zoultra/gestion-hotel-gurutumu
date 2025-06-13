import { Component, OnInit } from '@angular/core';
import { Salle } from '../../../components/models/salle';
import { SalleService } from '../../../components/services/salle/salle.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxPaginationModule } from 'ngx-pagination';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FilterPipe } from '../../../pipes/filter.pipe';
import { AlertService } from '../../../components/services/alert/alert.service';
import { AuthService } from '../../../components/services/auth/auth.service';

@Component({
  selector: 'app-list-salle',
  standalone: true,
  imports: [NgxPaginationModule, FormsModule, CommonModule, FilterPipe],
  templateUrl: './list-salle.component.html',
  styleUrl: './list-salle.component.scss'
})
export class ListSalleComponent implements OnInit {

  salles: Salle[] = [];
  id!: number;
  myFilterText!: any;
  title = 'Pagination';
  page: number = 1;
  count: number = 0;
  tableSize: number = 5;
  tableSizes: any = [5, 10, 15, 20];
  role!: string

  constructor(
    private salleService: SalleService,
    private alertService: AlertService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.getSalleList();
    this.role = this.authService.getUserRole();
  }

  private getSalleList() {
    this.salleService.getSalleList().subscribe({
      next: (response: any) => {
        console.log('Données reçues :', response);
        if (response) {
          this.salles = response;
        }
      },
      error: (err) => {
        console.error('Erreur lors du chargement des salles', err);
      }
    });
  }

  deleteSalle(id: number) {
    this.alertService.confirmDelete(() => {
      this.salleService.deleteSalle(id).subscribe(() => {
        this.alertService.success("Salle supprimée avec succès !");
        this.getSalleList();
      }, error => {
        this.alertService.error("La suppression a échoué.");
      });
    });
  }

  onTableDataChange(event: any) {
    this.page = event;
    this.getSalleList();
  }

  onTableSizeChange(event: any) {
    this.tableSize = event.target.value;
    this.page = 1;
    this.getSalleList();
  }

  updateSalle(id: number) {
    this.router.navigate(['/dashboard/salles/update', id]);
  }

  reloadPage() {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigate(['./'], {
      relativeTo: this.route
    });
  }

  goToAddSalle() {
    this.router.navigate(['/dashboard/salles/add']);
  }
}
