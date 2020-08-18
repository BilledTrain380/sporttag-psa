import { NgbModalOptions } from "@ng-bootstrap/ng-bootstrap";

export enum ConfirmType {
  CONFIRM = "CONFIRM",
  CANCEL = "CANCEL",
}

export const confirmModalOptions: NgbModalOptions = {
  size: "md",
  backdrop: "static",
};
