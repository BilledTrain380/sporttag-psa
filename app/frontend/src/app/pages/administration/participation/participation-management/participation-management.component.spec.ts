import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ParticipationManagementComponent } from './participation-management.component';

describe('ParticipationManagementComponent', () => {
  let component: ParticipationManagementComponent;
  let fixture: ComponentFixture<ParticipationManagementComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ParticipationManagementComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ParticipationManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
