import { Action, createReducer, on } from "@ngrx/store";

import { SimpleGroupDto } from "../../dto/group";

import {
  downloadEventSheetsAction,
  downloadParticipantListAction,
  downloadStartlistAction,
  finishEventSheetsFileAction,
  finishParticipantFileAction,
  finishStartlistFileAction,
  setEventSheetsDataAction,
} from "./event-sheets.action";

export interface EventSheetsState {
  readonly groups: ReadonlyArray<SimpleGroupDto>;
  readonly isParticipationOpen: boolean;
  readonly isParticipantListDownloading: boolean;
  readonly isEventSheetsDownloading: boolean;
  readonly isStartlistDownloading: boolean;
}

const initialState: EventSheetsState = {
  groups: [],
  isParticipationOpen: true,
  isParticipantListDownloading: false,
  isEventSheetsDownloading: false,
  isStartlistDownloading: false,
};

const reducer = createReducer(
  initialState,
  on(setEventSheetsDataAction, (state, action) => (
    {
      ...state,
      groups: action.groups,
      isParticipationOpen: action.isParticipationOpen,
    }
  )),
  on(downloadParticipantListAction, state => (
    {
      ...state,
      isParticipantListDownloading: true,
    }
  )),
  on(finishParticipantFileAction, state => (
    {
      ...state,
      isParticipantListDownloading: false,
    }
  )),
  on(downloadEventSheetsAction, state => (
    {
      ...state,
      isEventSheetsDownloading: true,
    }
  )),
  on(finishEventSheetsFileAction, state => (
    {
      ...state,
      isEventSheetsDownloading: false,
    }
  )),
  on(downloadStartlistAction, state => (
    {
      ...state,
      isStartlistDownloading: true,
    }
  )),
  on(finishStartlistFileAction, state => (
    {
      ...state,
      isStartlistDownloading: false,
    }
  ))
);

export function eventSheetsReducer(state: EventSheetsState | undefined, action: Action): EventSheetsState {
  return reducer(state, action);
}
