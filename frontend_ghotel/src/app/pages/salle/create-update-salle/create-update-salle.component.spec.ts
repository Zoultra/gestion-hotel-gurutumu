import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateUpdateSalleComponent } from './create-update-salle.component';

describe('CreateUpdateSalleComponent', () => {
  let component: CreateUpdateSalleComponent;
  let fixture: ComponentFixture<CreateUpdateSalleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateUpdateSalleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateUpdateSalleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
