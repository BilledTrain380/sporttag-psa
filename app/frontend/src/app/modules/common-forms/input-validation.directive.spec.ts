import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { async, ComponentFixture, TestBed } from "@angular/core/testing";
import { FormControl, ReactiveFormsModule, Validators } from "@angular/forms";
import { By } from "@angular/platform-browser";

import { InputValidationDirective } from "./input-validation.directive";

describe("InputValidationDirective", () => {
  let component: FormValidationWrapperComponent;
  let fixture: ComponentFixture<FormValidationWrapperComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
                                     declarations: [
                                       FormValidationWrapperComponent,
                                       InputValidationDirective,
                                     ],
                                     imports: [
                                       CommonModule,
                                       ReactiveFormsModule,
                                     ],
                                   })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FormValidationWrapperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should mark the input invalid, if validation errors exist", async(() => {
    component.control.markAsDirty();
    component.control.setValue("");

    fixture.detectChanges();
    fixture.whenStable()
      .then(() => {
        const classList: DOMTokenList = fixture.debugElement.query(By.css("input")).nativeElement.classList;
        expect(classList.contains("is-invalid"))
          .withContext("Contains is-invalid css class")
          .toBeTrue();
        expect(classList.contains("is-valid"))
          .withContext("Contains not is-valid css class")
          .toBeFalse();
      });
  }));

  it("should mark the input valid, when no validation errors exist", async(() => {
    component.control.markAsDirty();
    component.control.setValue("The value");

    fixture.detectChanges();
    fixture.whenStable()
      .then(() => {
        const classList: DOMTokenList = fixture.debugElement.query(By.css("input")).nativeElement.classList;
        expect(classList.contains("is-invalid"))
          .withContext("Contains not is-invalid css class")
          .toBeFalse();
        expect(classList.contains("is-valid"))
          .withContext("Contains is-valid css class")
          .toBeTrue();
      });
  }));
});

@Component({
             template: '<input [formControl]="control" appInputValidation>',
           })
export class FormValidationWrapperComponent {
  readonly control = new FormControl("test", Validators.required);
}
