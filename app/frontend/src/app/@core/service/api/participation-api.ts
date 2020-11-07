import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { EventSheetData } from "../../../dto/event-sheets";

import { ParticipationCommand, ParticipationDto, SportDto } from "../../../dto/participation";
import { StatusEntry } from "../../../dto/status";
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

  getParticipationStatus(): Observable<StatusEntry> {
    this.log.info("Load participation status");

    return this.http.get<StatusEntry>(`${API_ENDPOINT}/participation-status`);
  }

  updateParticipation(command: ParticipationCommand): Observable<ParticipationDto> {
    this.log.info("Update participation: command =", command);

    // Explicit set headers, because the command is only a string and would be interpreted as text/plain
    const headers = new HttpHeaders({"Content-Type": "application/json"});

    return this.http.patch<ParticipationDto>(`${API_ENDPOINT}/participation`, `"${command}"`, {headers});
  }

  getSportTypes(): Observable<ReadonlyArray<SportDto>> {
    this.log.info("Load sport types");

    return this.http.get<ReadonlyArray<SportDto>>(`${API_ENDPOINT}/sports`);
  }

  createParticipantList(sportTypes: ReadonlyArray<SportDto>): Observable<Blob> {
    this.log.info("Create participant list: ", sportTypes);

    return this.http.post(`${API_ENDPOINT}/participation-list/download`, sportTypes, {responseType: "blob"});
  }

  createStartlist(): Observable<Blob> {
    this.log.info("Create start list");

    return this.http.get(`${API_ENDPOINT}/start-list/download`, {responseType: "blob"});
  }

  createEventSheets(data: ReadonlyArray<EventSheetData>): Observable<Blob> {
    this.log.info("Create event sheets");

    return this.http.post(`${API_ENDPOINT}/event-sheets/download`, data, {responseType: "blob"});
  }
}
