import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OuvertureCaisseComponent } from './ouverture-caisse.component';

describe('OuvertureCaisseComponent', () => {
  let component: OuvertureCaisseComponent;
  let fixture: ComponentFixture<OuvertureCaisseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OuvertureCaisseComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OuvertureCaisseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
