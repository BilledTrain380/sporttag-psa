import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { mapTo } from "rxjs/operators";

import { getLogger } from "../../logging";

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

    return this.http.post("/web/import-group", formData)
      .pipe(mapTo(undefined));
  }
}
