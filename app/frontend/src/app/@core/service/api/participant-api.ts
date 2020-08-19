import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { tap } from "rxjs/operators";

import { ParticipantDto, ParticipantElement, ParticipantInput, ParticipantRelation } from "../../../dto/participation";
import { parseDate } from "../../lib/time";
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

    return this.http.get<ReadonlyArray<ParticipantDto>>(`${API_ENDPOINT}/participants`, {params})
      .pipe(tap(participants => participants.forEach(participant => enhanceBirthdayDtoWithDateProperty(participant.birthday))));
  }

  getParticipant(id: number): Observable<ParticipantDto> {
    this.log.info(`Get participant: id=${id}`);

    return this.http.get<ParticipantDto>(`${API_ENDPOINT}/participant/${id}`)
      .pipe(tap(participant => enhanceBirthdayDtoWithDateProperty(participant.birthday)));
  }

  updateParticipant(participantElement: ParticipantElement): Observable<void> {
    this.log.info(`Update participant: id=${participantElement.id}`);

    return this.http.patch<void>(`${API_ENDPOINT}/participant/${participantElement.id}`, participantElement);
  }

  updateParticipantRelation(participantRelation: ParticipantRelation): Observable<void> {
    this.log.info(`Update participant relation: id=${participantRelation.id}`);

    return this.http.put<void>(`${API_ENDPOINT}/participant/${participantRelation.id}`, participantRelation);
  }

  createParticipant(participantInput: ParticipantInput): Observable<ParticipantDto> {
    this.log.info(`Create participant: name=${participantInput.surname} ${participantInput.prename}`);

    return this.http.post<ParticipantDto>(`${API_ENDPOINT}/participants`, participantInput);
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

/**
 * Since the json returned from the api will only have the value property and not a parsed date property,
 * this function will defined the `date` property to the given `dto`.
 *
 * @param dto the birthday dto to enhance
 */
// tslint:disable-next-line:no-any
function enhanceBirthdayDtoWithDateProperty(dto: any): void {
  dto.date = parseDate(dto.value);
}
