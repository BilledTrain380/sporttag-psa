import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { UserDto, UserElement, UserInput, UserRelation } from "../../../dto/user";
import { getLogger } from "../../logging";

import { API_ENDPOINT } from "./pas-api";

@Injectable({
              providedIn: "root",
            })
export class UserApi {
  private readonly log = getLogger("UserApi");

  constructor(
    private readonly http: HttpClient,
  ) {
  }

  getUsers(): Observable<ReadonlyArray<UserDto>> {
    this.log.info("Get users");

    return this.http.get<ReadonlyArray<UserDto>>(`${API_ENDPOINT}/users`);
  }

  getUser(userId: number): Observable<UserDto> {
    this.log.info("Get user: userId =", userId);

    return this.http.get<UserDto>(`${API_ENDPOINT}/user/${userId}`);
  }

  createUser(user: UserInput): Observable<UserDto> {
    this.log.info("Create user: username =", user.username);

    return this.http.post<UserDto>(`${API_ENDPOINT}/users`, user);
  }

  updateUserElement(userId: number, user: UserElement): Observable<void> {
    this.log.info("Update user: userId =", userId);

    return this.http.patch<void>(`${API_ENDPOINT}/user/${userId}`, user);
  }

  updateUserRelation(userId: number, user: UserRelation): Observable<void> {
    this.log.info("Update user relation: userId =", userId);

    return this.http.put<void>(`${API_ENDPOINT}/user/${userId}`, user);
  }

  deleteUser(userId: number): Observable<void> {
    this.log.info("Delete user: userId =", userId);

    return this.http.delete<void>(`${API_ENDPOINT}/user/${userId}`);
  }
}
