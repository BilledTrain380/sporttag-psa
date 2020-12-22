import { createSelector } from "@ngrx/store";

import { AppState } from "../app";

const selectMetadata = (state: AppState) => state.metadata;

export const selectBuildInfo = createSelector(
  selectMetadata,
  state => state.buildInfo,
);
