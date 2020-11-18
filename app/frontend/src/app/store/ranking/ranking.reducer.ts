import { Action, createReducer, on } from "@ngrx/store";

import {
  downloadDisciplineRankingAction,
  downloadTotalRankingAction,
  downloadTriathlonRankingAction,
  downloadUbsCupRankingAction,
  finishDisciplineRankingFileAction,
  finishTotalRankingFileAction,
  finishTriathlonRankingFileAction,
  finishUbsCupRankingFileAction,
  setRankingDataAction,
} from "./ranking.action";

export interface RankingState {
  readonly isParticipationOpen: boolean;
  readonly isTotalRankingDownloading: boolean;
  readonly isTriathlonRankingDownloading: boolean;
  readonly isUbsCupRankingDownloading: boolean;
  readonly isDisciplineRankingDownloading: boolean;
}

const initialState: RankingState = {
  isParticipationOpen: true,
  isTotalRankingDownloading: false,
  isTriathlonRankingDownloading: false,
  isUbsCupRankingDownloading: false,
  isDisciplineRankingDownloading: false,
};

const reducer = createReducer(
  initialState,
  on(setRankingDataAction, (state, action) => (
    {
      ...state,
      isParticipationOpen: action.isParticipationOpen,
    }
  )),
  on(downloadTotalRankingAction, state => (
    {
      ...state,
      isTotalRankingDownloading: true,
    }
  )),
  on(finishTotalRankingFileAction, state => (
    {
      ...state,
      isTotalRankingDownloading: false,
    }
  )),
  on(downloadTriathlonRankingAction, state => (
    {
      ...state,
      isTriathlonRankingDownloading: true,
    }
  )),
  on(finishTriathlonRankingFileAction, state => (
    {
      ...state,
      isTriathlonRankingDownloading: false,
    }
  )),
  on(downloadUbsCupRankingAction, state => (
    {
      ...state,
      isUbsCupRankingDownloading: true,
    }
  )),
  on(finishUbsCupRankingFileAction, state => (
    {
      ...state,
      isUbsCupRankingDownloading: false,
    }
  )),
  on(downloadDisciplineRankingAction, state => (
    {
      ...state,
      isDisciplineRankingDownloading: true,
    }
  )),
  on(finishDisciplineRankingFileAction, state => (
    {
      ...state,
      isDisciplineRankingDownloading: false,
    }
  )),
);

export function rankingReducer(state: RankingState | undefined, action: Action): RankingState {
  return reducer(state, action);
}
