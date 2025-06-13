import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AjoutPaiementModalComponent } from './ajout-paiement-modal.component';

describe('AjoutPaiementModalComponent', () => {
  let component: AjoutPaiementModalComponent;
  let fixture: ComponentFixture<AjoutPaiementModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AjoutPaiementModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AjoutPaiementModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
