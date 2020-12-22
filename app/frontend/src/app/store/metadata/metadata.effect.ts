import { Injectable } from "@angular/core";
import { Actions, createEffect, ofType } from "@ngrx/effects";
import { EMPTY } from "rxjs";
import { catchError, map, switchMap } from "rxjs/operators";
import { getLogger } from "../../@core/logging";

import { AboutApi } from "../../@core/service/api/about-api";

import { loadMetadataAction, setMetadataAction } from "./metadata.action";

@Injectable()
export class MetadataEffects {
  readonly loadMetadata = createEffect(() => this.actions$
    .pipe(ofType(loadMetadataAction.type))
    .pipe(switchMap(() =>
                      this.aboutApi.getBuildInfo()
                        .pipe(map(buildInfo => setMetadataAction({buildInfo})))
                        .pipe(catchError(err => {
                          this.log.warn("Could not load metadata", err);

                          return EMPTY;
                        })),
    )));

  private readonly log = getLogger("MetadataEffects");

  constructor(
    private readonly actions$: Actions,
    private readonly aboutApi: AboutApi,
  ) {
  }
}
