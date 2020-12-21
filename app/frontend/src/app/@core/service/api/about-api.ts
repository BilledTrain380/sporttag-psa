import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { BuildInfoDto } from "../../../dto/about";
import { getLogger } from "../../logging";

import { API_ENDPOINT } from "./pas-api";

@Injectable({
              providedIn: "root",
            })
export class AboutApi {
  private readonly log = getLogger("AboutApi");

  constructor(
    private readonly http: HttpClient,
  ) {
  }

  getBuildInfo(): Observable<BuildInfoDto> {
    this.log.info("Get build info");

    return this.http.get<BuildInfoDto>(`${API_ENDPOINT}/build-info`);
  }
}
