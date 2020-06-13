import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { ParticipantDto, ParticipantElement, ParticipantInput, ParticipantRelation } from "../../../dto/participation";
import { getLogger, Logger } from "../../logging";

import { ApiParameters, API_ENDPOINT } from "./pas-api";

@Injectable({
              providedIn: "root",
            })
export class ParticipantApi {
  private readonly log: Logger = getLogger("ParticipantApi");

  constructor(
    private readonly http: HttpClient,
  ) {
  }

  getParticipants(parameters?: ParticipantParameters): Observable<ReadonlyArray<ParticipantDto>> {
    this.log.info(`Get participants: parameters=${parameters}`);

    const params = parameters?.buildParameters();

    return this.http.get<ReadonlyArray<ParticipantDto>>(`${API_ENDPOINT}/participants`, {params});
  }

  getParticipant(id: number): Observable<ParticipantDto> {
    this.log.info(`Get participant: id=${id}`);

    return this.http.get<ParticipantDto>(`${API_ENDPOINT}/participant/${id}`);
  }

  updateParticipant(participantElement: ParticipantElement): Observable<void> {
    this.log.info(`Update participant: id=${participantElement.id}`);

    return this.http.patch<void>(`${API_ENDPOINT}/participant/${participantElement.id}`, participantElement);
  }

  updateParticipantRelation(participantRelation: ParticipantRelation): Observable<void> {
    this.log.info(`Update participant relation: id=${participantRelation.id}`);

    return this.http.put<void>(`${API_ENDPOINT}/participant/${participantRelation.id}`, participantRelation);
  }

  createParticipant(participantInput: ParticipantInput): Observable<void> {
    this.log.info(`Create participant: name=${participantInput.surname} ${participantInput.prename}`);

    return this.http.post<void>(`${API_ENDPOINT}/participants`, participantInput);
  }

  deleteParticipant(id: number): Observable<void> {
    this.log.info(`Delete participant: id=${id}`);

    return this.http.delete<void>(`${API_ENDPOINT}/participant/${id}`);
  }
}

export class ParticipantParameters implements ApiParameters {
  constructor(
    readonly group: string,
  ) {
  }

  buildParameters(): HttpParams {
    return new HttpParams()
      .set("group", this.group);
  }
}
