import { createSelector } from "@ngrx/store";

import { AppState } from "../app";

const selectEventSheets = (state: AppState) => state.eventSheets;

export const selectEventSheetsGroups = createSelector(
  selectEventSheets,
  state => state.groups,
);

export const selectIsParticipantListDownloading = createSelector(
  selectEventSheets,
  state => state.isParticipantListDownloading,
);
