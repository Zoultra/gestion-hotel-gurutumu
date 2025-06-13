import { Component, OnInit } from '@angular/core';
import { Depense } from '../../../components/models/depense';
import { DepenseService } from '../../../components/services/depense/depense.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgxPaginationModule } from 'ngx-pagination';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FilterPipe } from '../../../pipes/filter.pipe';
import { AlertService } from '../../../components/services/alert/alert.service';
import { AuthService } from '../../../components/services/auth/auth.service';

@Component({
  selector: 'app-list-depense',
  standalone: true,
  imports: [NgxPaginationModule, FormsModule, CommonModule, FilterPipe],
  templateUrl: './list-depense.component.html',
  styleUrl: './list-depense.component.scss'
})
export class ListDepenseComponent implements OnInit {
  depenses: Depense[] = [];

  id!: number;
  myFilterText!: any;

  title = 'Pagination';
  page: number = 1;
  count: number = 0;
  tableSize: number = 5;
  tableSizes: any = [5, 10, 15, 20];
  role!: string

  constructor(
    private depenseService: DepenseService,
    private alertService: AlertService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.DepenseList();
    this.role = this.authService.getUserRole();
  }

  private DepenseList() {
    this.depenseService.getDepensesList().subscribe({
      next: (response: any) => {
        console.log('Dépenses reçues :', response);
        if (response) {
          this.depenses = response;
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
