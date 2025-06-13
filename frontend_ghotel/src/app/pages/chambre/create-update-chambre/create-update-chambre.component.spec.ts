import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateUpdateChambreComponent } from './create-update-chambre.component';

describe('CreateUpdateChambreComponent', () => {
  let component: CreateUpdateChambreComponent;
  let fixture: ComponentFixture<CreateUpdateChambreComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateUpdateChambreComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateUpdateChambreComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
