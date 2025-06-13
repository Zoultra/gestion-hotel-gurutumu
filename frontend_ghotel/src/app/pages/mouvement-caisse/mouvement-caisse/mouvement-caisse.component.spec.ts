import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MouvementCaisseComponent } from './mouvement-caisse.component';

describe('MouvementCaisseComponent', () => {
  let component: MouvementCaisseComponent;
  let fixture: ComponentFixture<MouvementCaisseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MouvementCaisseComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MouvementCaisseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
