import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { By } from "@angular/platform-browser";

import { FormHintRequiredDirective } from "./form-hint-required.directive";

describe("FormHintRequiredDirective", () => {
  let fixture: ComponentFixture<FormHintRequiredWrapperComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
                                     declarations: [
                                       FormHintRequiredWrapperComponent,
                                       FormHintRequiredDirective,
                                     ],
                                     imports: [
                                       CommonModule,
                                     ],
                                   })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FormHintRequiredWrapperComponent);
    fixture.detectChanges();
  });

  it("should add the * character at the end of the label", () => {
    const label: string = fixture.debugElement.query(By.css("label")).nativeElement.innerHTML;

    expect(label)
      .toEqual('Surname<span style="color: red;"> *</span>');
  });
});

@Component({
             template: "<label appFormHintRequired>Surname</label>",
           })
export class FormHintRequiredWrapperComponent {
}
