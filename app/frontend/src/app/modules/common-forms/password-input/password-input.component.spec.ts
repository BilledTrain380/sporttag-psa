import { CommonModule } from "@angular/common";
import { Component, ViewChild } from "@angular/core";
import { async, ComponentFixture, TestBed } from "@angular/core/testing";
import { FormControl, ReactiveFormsModule, Validators } from "@angular/forms";
import { By } from "@angular/platform-browser";
import { FontAwesomeTestingModule } from "@fortawesome/angular-fontawesome/testing";

import { PasswordInputComponent } from "./password-input.component";

describe("PasswordInputComponent", () => {
  let component: PasswordInputWrapperComponent;
  let fixture: ComponentFixture<PasswordInputWrapperComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
                                     declarations: [
                                       PasswordInputComponent,
                                       PasswordInputWrapperComponent,
                                     ],
                                     imports: [
                                       CommonModule,
                                       ReactiveFormsModule,
                                       FontAwesomeTestingModule,
                                     ],
                                   })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PasswordInputWrapperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should have an invalid form when password is not valid", () => {
    // It is required, so this is not valid
    component.control.setValue("");

    expect(component.passwordInputComponent?.formControl.invalid)
      .withContext("Invalid state")
      .toBeTrue();
  });

  it("should have a valid form when password is valid", () => {
    // It is required, so this is valid
    component.control.setValue("secret");

    expect(component.passwordInputComponent?.formControl.valid)
      .withContext("Valid state")
      .toBeTrue();
  });

  it("should show / hide the password when show password (eye) button is clicked", () => {
    function getPasswordTypeAttribute(): string | undefined {
      const passwordInputAttribute = fixture.debugElement.query(By.css("app-password-input input"));
      const typeAttribute = (passwordInputAttribute.nativeElement.attributes as NamedNodeMap).getNamedItem("type");

      return typeAttribute?.value;
    }

    component.control.setValue("show me");

    const showPasswordButton = fixture.debugElement.query(By.css('[data-test-selector="showPasswordButton"]'));
    showPasswordButton.nativeElement.click();

    expect(getPasswordTypeAttribute())
      .toBe("text");

    showPasswordButton.nativeElement.click();

    expect(getPasswordTypeAttribute())
      .toBe("password");
  });
});

@Component({
             selector: "app-password-input-wrapper",
             template: `
               <app-password-input [formControl]="control"></app-password-input>`,
           })
class PasswordInputWrapperComponent {

  @ViewChild(PasswordInputComponent, {static: false})
  passwordInputComponent?: PasswordInputComponent;

  readonly control = new FormControl("", Validators.required);
}
