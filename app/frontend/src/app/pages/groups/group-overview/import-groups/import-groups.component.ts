import { Component, OnDestroy, OnInit } from "@angular/core";
import { AbstractControl, FormBuilder, FormGroup, Validators } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";

import { FormControlsObject } from "../../../../@core/forms/form-util";
import { FileValidators } from "../../../../@core/forms/form-validators";
import { FILE_SIZE_1MB } from "../../../../@core/lib/files";
import { Alert } from "../../../../modules/alert/alert";
import { AbstractSubmitModalComponent } from "../../../../modules/modal/submit-modal/abstract-submit-modal.component";
import { clearImportGroupsAlertAction, importGroupsAction } from "../../../../store/group/group.action";
import { selectImportAlert } from "../../../../store/group/group.selector";

@Component({
             selector: "app-import-groups",
             templateUrl: "./import-groups.component.html",
             styleUrls: ["./import-groups.component.scss"],
           })
export class ImportGroupsComponent extends AbstractSubmitModalComponent implements OnInit, OnDestroy {
  form?: FormGroup;
  formControls: FormControlsObject = {
    file: "file",
  };

  get fileControl(): AbstractControl | undefined {
    return this.form?.controls[this.formControls.file];
  }

  protected readonly alert$: Observable<Alert | undefined> = this.store.select(selectImportAlert);

  constructor(
    activeModal: NgbActiveModal,
    private readonly store: Store,
    private readonly formBuilder: FormBuilder,
  ) {
    super(activeModal);
  }

  ngOnInit(): void {
    super.ngOnInit();
    this.form = this.formBuilder.group(
      {
        [this.formControls.file]: [
          undefined,
          [
            Validators.required,
            FileValidators.maxFileSize(FILE_SIZE_1MB),
            FileValidators.accepts(".csv"),
          ],
        ],
      });
  }

  ngOnDestroy(): void {
    super.ngOnDestroy();
    this.store.dispatch(clearImportGroupsAlertAction({}));
  }

  submit(): void {
    const file = this.form?.value[this.formControls.file];

    if (file) {
      this.store.dispatch(importGroupsAction({file}));
    }
  }
}
