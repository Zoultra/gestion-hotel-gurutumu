import { Component, OnInit } from '@angular/core';
import { Chambre } from '../../../components/models/chambre';
import { ChambreService } from '../../../components/services/chambre/chambre.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxPaginationModule } from 'ngx-pagination';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FilterPipe } from '../../../pipes/filter.pipe';
import { AlertService } from '../../../components/services/alert/alert.service';
import { AuthService } from '../../../components/services/auth/auth.service';

@Component({
  selector: 'app-list-chambre',
  standalone: true,
  imports: [NgxPaginationModule, FormsModule, CommonModule, FilterPipe],
  templateUrl: './list-chambre.component.html',
  styleUrl: './list-chambre.component.scss'
})
export class ListChambreComponent implements OnInit {
  chambres: Chambre[] = [];

  id!: number;
  myFilterText!: any;

  title = 'Pagination';
  page: number = 1;
  count: number = 0;
  tableSize: number = 5;
  tableSizes: any = [5, 10, 15, 20];
  role!: string
  
  constructor(
    private chambreService: ChambreService,
    private alertService: AlertService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.getChambreList();
     this.role = this.authService.getUserRole();
  }

  private getChambreList() {
    this.chambreService.getChambreList().subscribe({
      next: (response: any) => {
        console.log('Données reçues :', response);
        if (response) {
          this.chambres = response;
        }
      },
      error: (err) => {
        console.error('Erreur lors du chargement des chambres', err);
      }
    });
  }

  deleteChambre(id: number) {
    this.alertService.confirmDelete(() => {
      this.chambreService.deleteChambre(id).subscribe(() => {
        this.alertService.success("Chambre supprimée avec succès !");
        this.getChambreList();
      }, error => {
        this.alertService.error("La suppression a échoué.");
      });
    });
  }

  onTableDataChange(event: any) {
    this.page = event;
    this.getChambreList();
  }

  onTableSizeChange(event: any) {
    this.tableSize = event.target.value;
    this.page = 1;
    this.getChambreList();
  }

  updateChambre(id: number) {
    this.router.navigate(['/dashboard/chambres/update', id]);
  }

  reloadPage() {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.router.onSameUrlNavigation = 'reload';
    this.router.navigate(['./'], {
      relativeTo: this.route
    });
  }

  goToAddChambre() {
    this.router.navigate(['/dashboard/chambres/add']);
  }
}
