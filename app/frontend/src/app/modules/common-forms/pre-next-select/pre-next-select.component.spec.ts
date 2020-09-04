import { Component, OnInit } from "@angular/core";
import { async, ComponentFixture, TestBed } from "@angular/core/testing";
import { FormControl, FormGroup, ReactiveFormsModule } from "@angular/forms";
import { By } from "@angular/platform-browser";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";

import { PreNextSelectComponent } from "./pre-next-select.component";

interface ButtonState {
  readonly disabled: boolean;
}

describe("PreNextSelectComponent", () => {
  let component: PreNextSelectWrapperComponent;
  let fixture: ComponentFixture<PreNextSelectWrapperComponent>;

  const TITLE_SENSEI = 2;
  const PREVIOUS_BUTTON_SELECTOR = ".input-group-prepend button";
  const NEXT_BUTTON_SELECTOR = ".input-group-append button";

  beforeEach(async(() => {
    TestBed.configureTestingModule({
                                     declarations: [
                                       PreNextSelectWrapperComponent,
                                       PreNextSelectComponent,
                                     ],
                                     imports: [
                                       ReactiveFormsModule,
                                       FontAwesomeModule,
                                     ],
                                   })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PreNextSelectWrapperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  describe("previous / next button states", () => {
    function assertPreviousButton(state: ButtonState): void {
      const previousButton = fixture.debugElement.query(By.css(PREVIOUS_BUTTON_SELECTOR));
      let assertation = expect(Object.keys(previousButton.attributes))
        .withContext("Previous button disabled state");

      if (!state.disabled) {
        assertation = assertation.not;
      }

      assertation.toContain("disabled");
    }

    function assertNextButton(state: ButtonState): void {
      const nextButton = fixture.debugElement.query(By.css(NEXT_BUTTON_SELECTOR));
      let assertation = expect(Object.keys(nextButton.attributes))
        .withContext("Next button disabled state");

      if (!state.disabled) {
        assertation = assertation.not;
      }

      assertation.toContain("disabled");
    }

    it("should disable previous button when the first value is selected", async(async () => {
      component.form?.controls.title.setValue(component.values[0]);

      fixture.detectChanges();
      await fixture.whenStable();

      assertPreviousButton({disabled: true});
      assertNextButton({disabled: false});
    }));

    it("should disable next button when the last value is selected", async(async () => {
      component.form?.controls.title.setValue(component.values[component.values.length - 1]);

      fixture.detectChanges();
      await fixture.whenStable();

      assertPreviousButton({disabled: false});
      assertNextButton({disabled: true});
    }));

    it("should enable previous and next button when neither the first or the last value is selected", async(async () => {
      component.form?.controls.title.setValue(component.values[TITLE_SENSEI]);

      fixture.detectChanges();
      await fixture.whenStable();

      assertPreviousButton({disabled: false});
      assertNextButton({disabled: false});
    }));
  });

  it("should change the form control value to the previous value when the previous button is clicked", async(async () => {
    // Select the last value to enabled the previous button in the first place
    component.form?.controls.title.setValue(component.values[component.values.length - 1]);

    fixture.detectChanges();
    await fixture.whenStable();

    fixture.debugElement
      .query(By.css(PREVIOUS_BUTTON_SELECTOR))
      .nativeElement
      .click();

    fixture.detectChanges();
    await fixture.whenStable();

    const currentFormValue = component.form?.value.title;
    expect(currentFormValue)
      .toBe("Sensei");
  }));

  it("should change the form control value to the next value when the next button is clicked", async(async () => {
    // Select the first value to enabled the next button in the first place
    component.form?.controls.title.setValue(component.values[0]);

    fixture.detectChanges();
    await fixture.whenStable();

    fixture.debugElement
      .query(By.css(NEXT_BUTTON_SELECTOR))
      .nativeElement
      .click();

    fixture.detectChanges();
    await fixture.whenStable();

    const currentFormValue = component.form?.value.title;
    expect(currentFormValue)
      .toBe("Master");
  }));
});

@Component({
             selector: "app-pre-next-select-wrapper",
             template: `
               <form [formGroup]="form">
                 <app-pre-next-select formControlName="title" [values]="values" prependText="Titles"></app-pre-next-select>
               </form>
             `,
           })
class PreNextSelectWrapperComponent implements OnInit {

  readonly values: ReadonlyArray<string> = [
    "Dr",
    "Master",
    "Sensei",
    "Chief",
  ];

  form?: FormGroup;

  ngOnInit(): void {
    this.form = new FormGroup({
                                title: new FormControl(""),
                              });
  }
}
