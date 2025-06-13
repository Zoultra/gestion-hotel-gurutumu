import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateUpdateDepenseComponent } from './create-update-depense.component';

describe('CreateUpdateDepenseComponent', () => {
  let component: CreateUpdateDepenseComponent;
  let fixture: ComponentFixture<CreateUpdateDepenseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateUpdateDepenseComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateUpdateDepenseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
