import { CommonModule } from "@angular/common";
import { Component, DebugElement, OnInit } from "@angular/core";
import { async, ComponentFixture, TestBed } from "@angular/core/testing";
import { FormControl, FormGroup, ReactiveFormsModule } from "@angular/forms";
import { By } from "@angular/platform-browser";
import { FontAwesomeTestingModule } from "@fortawesome/angular-fontawesome/testing";
import { NgbDatepickerModule } from "@ng-bootstrap/ng-bootstrap";

import { DateInputComponent } from "./date-input.component";

describe("DateInputComponent", () => {
  let component: DateInputWrapperComponent;
  let fixture: ComponentFixture<DateInputWrapperComponent>;

  let calendarButton: DebugElement | undefined;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
                                     declarations: [
                                       DateInputComponent,
                                       DateInputWrapperComponent,
                                     ],
                                     imports: [
                                       CommonModule,
                                       ReactiveFormsModule,
                                       NgbDatepickerModule,
                                       FontAwesomeTestingModule,
                                     ],
                                   })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DateInputWrapperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    calendarButton = fixture.debugElement.query(By.css("#date-input-calendar-btn"));
  });

  it("should create", () => {
    expect(component)
      .toBeDefined();
  });

  it("should toggle the date picker popup on calendar icon click", () => {
    calendarButton?.nativeElement?.click();
    fixture.detectChanges();

    let popup = fixture.debugElement.query(By.css("ngb-datepicker"));
    expect(popup)
      .not
      .toBeNull();

    calendarButton?.nativeElement?.click();
    fixture.detectChanges();

    popup = fixture.debugElement.query(By.css("ngb-datepicker"));
    expect(popup)
      .toBeNull();
  });
});

@Component({
             template: `
               <div [formGroup]="form">
                 <app-date-input formControlName="date"></app-date-input>
               </div>
             `,
           })
export class DateInputWrapperComponent implements OnInit {
  form?: FormGroup;

  ngOnInit(): void {
    this.form = new FormGroup({
                                date: new FormControl(new Date("2000-01-15")),
                              });
  }
}
