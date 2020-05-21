import { OnInit } from "@angular/core";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { Observable, Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";

import { Alert } from "../../alert/alert";

export abstract class AbstractSubmitModalComponent implements OnInit {
  get alert(): Alert | undefined {
    return this._alert;
  }

  protected abstract readonly alert$: Observable<Alert | undefined>;
  protected readonly destroy$ = new Subject<void>();

  private _alert?: Alert;

  protected constructor(
    private readonly activeModal: NgbActiveModal,
  ) {
  }

  ngOnInit(): void {
    this.alert$
      .pipe(takeUntil(this.destroy$))
      .subscribe(alert => {
        if (alert?.isSuccess()) {
          this.activeModal.close(alert);
        } else {
          this._alert = alert;
        }
      });
  }

  abstract submit(): void;

  cancel(): void {
    this.activeModal.dismiss();
  }
}
