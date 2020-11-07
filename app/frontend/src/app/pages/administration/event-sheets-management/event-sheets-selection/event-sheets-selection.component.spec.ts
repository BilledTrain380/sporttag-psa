import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EventSheetsSelectionComponent } from './event-sheets-selection.component';

describe('EventSheetsSelectionComponent', () => {
  let component: EventSheetsSelectionComponent;
  let fixture: ComponentFixture<EventSheetsSelectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EventSheetsSelectionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EventSheetsSelectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
