import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { EMPTY, Observable } from "rxjs";
import { mergeMap } from "rxjs/operators";

import { ParticipantDto, ParticipantElement, ParticipantInput, ParticipantRelation } from "../../../dto/participation";
import { getLogger, Logger } from "../../logging";

import { API_ENDPOINT, ApiParameters } from "./pas-api";

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

    return this.http.patch(`${API_ENDPOINT}/participant/${participantElement.id}`, participantElement)
      .pipe(mergeMap(() => EMPTY));
  }

  updateParticipantRelation(participantRelation: ParticipantRelation): Observable<void> {
    this.log.info(`Update participant relation: id=${participantRelation.id}`);

    return this.http.put(`${API_ENDPOINT}/participant/${participantRelation.id}`, participantRelation)
      .pipe(mergeMap(() => EMPTY));
  }

  createParticipant(participantInput: ParticipantInput): Observable<void> {
    this.log.info(`Create participant: name=${participantInput.surname} ${participantInput.prename}`);

    return this.http.post(`${API_ENDPOINT}/participants`, participantInput)
      .pipe(mergeMap(() => EMPTY));
  }

  deleteParticipant(id: number): Observable<void> {
    this.log.info(`Delete participant: id=${id}`);

    return this.http.delete(`${API_ENDPOINT}/participant/${id}`)
      .pipe(mergeMap(() => EMPTY));
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
