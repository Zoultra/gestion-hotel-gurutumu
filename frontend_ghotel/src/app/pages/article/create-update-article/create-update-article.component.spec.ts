import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateUpdateArticleComponent } from './create-update-article.component';

describe('CreateUpdateArticleComponent', () => {
  let component: CreateUpdateArticleComponent;
  let fixture: ComponentFixture<CreateUpdateArticleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateUpdateArticleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateUpdateArticleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
