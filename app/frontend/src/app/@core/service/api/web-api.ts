import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { mapTo } from "rxjs/operators";

import { getLogger } from "../../logging";

import { API_ENDPOINT } from "./pas-api";

@Injectable({
              providedIn: "root",
            })
export class WebApi {
  private readonly log = getLogger("WebApi");

  constructor(
    private readonly http: HttpClient,
  ) {
  }

  importGroups(file: File): Observable<void> {
    this.log.info("Import groups with file:", file.name);

    const formData = new FormData();
    formData.append("group-input", file);

    return this.http.post(`${API_ENDPOINT}/import-group`, formData, {responseType: "text"})
      .pipe(mapTo(undefined));
  }
}
