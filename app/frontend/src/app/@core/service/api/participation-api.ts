import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { ParticipationCommand, ParticipationDto } from "../../../dto/participation";
import { getLogger, Logger } from "../../logging";

import { API_ENDPOINT } from "./pas-api";

@Injectable({
              providedIn: "root",
            })
export class ParticipationApi {
  private readonly log: Logger = getLogger("ParticipationApi");

  constructor(
    private readonly http: HttpClient,
  ) {
  }

  getParticipation(): Observable<ParticipationDto> {
    this.log.info("Load participation");

    return this.http.get<ParticipationDto>(`${API_ENDPOINT}/participation`);
  }

  updateParticipation(command: ParticipationCommand): Observable<ParticipationDto> {
    this.log.info("Update participation: command =", command);

    // Explicit set headers, because the command is only a string and would be interpreted as text/plain
    const headers = new HttpHeaders({"Content-Type": "application/json"});

    return this.http.patch<ParticipationDto>(`${API_ENDPOINT}/participation`, `"${command}"`, {headers});
  }
}
