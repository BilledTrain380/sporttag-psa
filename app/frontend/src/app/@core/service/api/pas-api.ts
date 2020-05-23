import { HttpParams } from "@angular/common/http";

export const API_ENDPOINT = "/api";

export interface ApiParameters {
  // tslint:disable-next-line: no-any
  [key: string]: any;

  buildParameters(): HttpParams;
}
