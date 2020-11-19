import { createSelector } from "@ngrx/store";

import { AppState } from "../app";

const selectRanking = (state: AppState) => state.ranking;

export const selectIsParticipationOpen = createSelector(
  selectRanking,
  state => state.isParticipationOpen,
);

export const selectIsTotalRankingDownloading = createSelector(
  selectRanking,
  state => state.isTotalRankingDownloading,
);

export const selectIsTriathlonRankingDownloading = createSelector(
  selectRanking,
  state => state.isTriathlonRankingDownloading,
);

export const selectIsUbsCupRankingDownloading = createSelector(
  selectRanking,
  state => state.isUbsCupRankingDownloading,
);

export const selectIsDisciplineRankingDownloading = createSelector(
  selectRanking,
  state => state.isDisciplineRankingDownloading,
);
