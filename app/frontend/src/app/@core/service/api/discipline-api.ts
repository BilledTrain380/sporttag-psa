import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { DisciplineDto } from "../../../dto/athletics";
import { getLogger } from "../../logging";

import { API_ENDPOINT } from "./pas-api";

@Injectable()
export class DisciplineApi {
  private readonly log = getLogger("DisciplineApi");

  constructor(
    private readonly http: HttpClient,
  ) {
  }

  getDisciplines(): Observable<ReadonlyArray<DisciplineDto>> {
    this.log.info("Load disciplines");

    return this.http.get<ReadonlyArray<DisciplineDto>>(`${API_ENDPOINT}/disciplines`);
  }
}
