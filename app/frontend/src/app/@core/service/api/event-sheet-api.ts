import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { EventSheetData, EventSheetExport } from "../../../dto/event-sheets";
import { SportDto } from "../../../dto/participation";
import { getLogger } from "../../logging";

import { API_ENDPOINT } from "./pas-api";

@Injectable({
              providedIn: "root",
            })
export class EventSheetApi {
  private readonly log = getLogger("EventSheetApi");

  constructor(
    private readonly http: HttpClient,
  ) {
  }

  getData(): Observable<EventSheetData> {
    this.log.info("Get group names");

    return this.http.get<EventSheetData>(`${API_ENDPOINT}/event-sheets/data`);
  }

  createParticipantList(sportTypes: ReadonlyArray<SportDto>): Observable<Blob> {
    this.log.info("Create participant list: ", sportTypes);

    return this.http.post(`${API_ENDPOINT}/event-sheets/download/participant-list`, sportTypes, {responseType: "blob"});
  }

  createStartlist(): Observable<Blob> {
    this.log.info("Create start list");

    return this.http.get(`${API_ENDPOINT}/event-sheets/download/startlist`, {responseType: "blob"});
  }

  createEventSheets(data: ReadonlyArray<EventSheetExport>): Observable<Blob> {
    this.log.info("Create event sheets");

    return this.http.post(`${API_ENDPOINT}/event-sheets/download/sheets`, data, {responseType: "blob"});
  }
}
