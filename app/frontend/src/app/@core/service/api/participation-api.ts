import { HttpClient } from "@angular/common/http";
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

  updateParticipation(command: ParticipationCommand): Observable<void> {
    this.log.info(`Update participation: command=${command}`);

    return this.http.patch<void>(`${API_ENDPOINT}/participation`, command);
  }
}
