import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewDeploymentComponent } from './new-deployment.component';

describe('NewDeploymentComponent', () => {
  let component: NewDeploymentComponent;
  let fixture: ComponentFixture<NewDeploymentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewDeploymentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewDeploymentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
