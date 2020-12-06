import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { fromPromise } from "rxjs/internal-compatibility";
import { map } from "rxjs/operators";

import { environment } from "../../../../environments/environment";

const url = environment.production
  ? "/user/change-locale"
  : `${window.location.protocol}//${window.location.hostname}:8080/user/change-locale`;

@Injectable({
              providedIn: "root",
            })
export class LanguageService {

  changeLocale(locale: string): Observable<void> {
    const init: RequestInit = {
      method: "POST",
      mode: "cors",
      cache: "no-cache",
      credentials: "include",
      headers: {
        "Content-Type": "application/json",
      },
      redirect: "error",
      referrerPolicy: "no-referrer",
      body: locale,
    };

    return fromPromise(fetch(url, init))
      .pipe(map(() => undefined));
  }
}
