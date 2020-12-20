import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { ProfileElement, PsaLocale } from "../../../dto/profile";
import { getLogger } from "../../logging";
import { API_ENDPOINT } from "../api/pas-api";

@Injectable({
              providedIn: "root",
            })
export class LanguageService {
  private readonly log = getLogger("LanguageService");

  constructor(
    private readonly http: HttpClient,
  ) {
  }

  changeLocale(locale: PsaLocale): Observable<void> {
    this.log.info("Change user locale to", locale);

    const profileElement: ProfileElement = {locale};

    return this.http.patch<void>(`${API_ENDPOINT}/profile`, profileElement);
  }
}
