import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoriqueCaisseComponent } from './historique-caisse.component';

describe('HistoriqueCaisseComponent', () => {
  let component: HistoriqueCaisseComponent;
  let fixture: ComponentFixture<HistoriqueCaisseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HistoriqueCaisseComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HistoriqueCaisseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
