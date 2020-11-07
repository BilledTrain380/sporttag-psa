import { Action, createReducer, on } from "@ngrx/store";

import { SimpleGroupDto } from "../../dto/group";

import { downloadParticipantListAction, finishParticipantFileAction, setEventSheetsDataAction } from "./event-sheets.action";

export interface EventSheetsState {
  readonly groups: ReadonlyArray<SimpleGroupDto>;
  readonly isParticipantListDownloading: boolean;
}

const initialState: EventSheetsState = {
  groups: [],
  isParticipantListDownloading: false,
};

const reducer = createReducer(
  initialState,
  on(setEventSheetsDataAction, (state, action) => (
    {
      ...state,
      groups: action.groups,
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
);

export function eventSheetsReducer(state: EventSheetsState | undefined, action: Action): EventSheetsState {
  return reducer(state, action);
}
