import { createSelector } from "@ngrx/store";

import { AppState } from "../app";

const selectEventSheets = (state: AppState) => state.eventSheets;

export const selectEventSheetsGroups = createSelector(
  selectEventSheets,
  state => state.groupNames,
);

export const selectIsParticipationOpen = createSelector(
  selectEventSheets,
  state => state.isParticipationOpen,
);

export const selectIsParticipantListDownloading = createSelector(
  selectEventSheets,
  state => state.isParticipantListDownloading,
);

export const selectIsEventSheetsDownloading = createSelector(
  selectEventSheets,
  state => state.isEventSheetsDownloading,
);

export const selectIsStartlistDownloading = createSelector(
  selectEventSheets,
  state => state.isStartlistDownloading,
);
