import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { By } from "@angular/platform-browser";

import { FormHintDirective } from "./form-hint.directive";

describe("FormHintDirective", () => {
  let fixture: ComponentFixture<FormHintWrapperComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
                                     declarations: [
                                       FormHintWrapperComponent,
                                       FormHintDirective,
                                     ],
                                     imports: [
                                       CommonModule,
                                     ],
                                   })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FormHintWrapperComponent);
    fixture.detectChanges();
  });

  it("should add the form hints as first child to the form tag", () => {
    const hint = fixture.debugElement.query(By.css('[data-test-selector="form-hint"]'));

    expect(hint)
      .not
      .toBeNull();
  });
});

@Component({
             template: "<form appFormHint><div>Input</div></form>",
           })
export class FormHintWrapperComponent {
}
