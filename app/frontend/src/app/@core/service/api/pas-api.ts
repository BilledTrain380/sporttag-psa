import { HttpParams, HttpResponse } from "@angular/common/http";

import { requireNonNullOrUndefined } from "../../lib/lib";

export const API_ENDPOINT = "/api";

export interface ApiParameters {
  // tslint:disable-next-line: no-any
  [key: string]: any;

  buildParameters(): HttpParams;
}

export class SimpleFile {
  constructor(
    readonly name: string,
    readonly binary: Blob,
  ) {
  }

  static fromResponse(response: HttpResponse<Blob>): SimpleFile {
    const contentDisposition = response.headers.get("content-disposition");

    const fileName = getFileNameOfContentDisposition(contentDisposition ?? "");

    return new SimpleFile(
      fileName,
      requireNonNullOrUndefined(response.body),
    );
  }
}

const CONTENT_DISPOSITION_PATTERN = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;

export function getFileNameOfContentDisposition(disposition: string): string {
  if (disposition && disposition.indexOf("attachment") !== -1) {
    const matches = CONTENT_DISPOSITION_PATTERN.exec(disposition);
    if (matches && matches[1]) {
      return matches[1].replace(/['"]/g, "");
    }
  }

  return "Unknown-file";
}
