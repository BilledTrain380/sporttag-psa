import { Action, createReducer, on } from "@ngrx/store";

import { OverviewGroupDto, SimpleGroupDto } from "../../dto/group";
import { ParticipantDto } from "../../dto/participation";
import { Alert } from "../../modules/alert/alert";

import {
  clearActiveGroupAction,
  clearImportGroupsAlertAction,
  clearOverviewGroupsAction,
  setActiveGroupAction,
  setImportGroupsAlertAction,
  setOverviewGroupsAction,
} from "./group.action";

export interface GroupState {
  readonly overviewGroups: ReadonlyArray<OverviewGroupDto>;
  readonly importAlert?: Alert;
  readonly activeGroup?: ActiveGroup;
}

export interface ActiveGroup {
  readonly group: SimpleGroupDto;
  readonly participants: ReadonlyArray<ParticipantDto>;
}

const initialState: GroupState = {
  overviewGroups: [],
};

const reducer = createReducer(
  initialState,
  on(setOverviewGroupsAction, (state, action) => ({...state, overviewGroups: action.groups})),
  on(clearOverviewGroupsAction, (state, _) => ({...state, overviewGroups: []})),
  on(setImportGroupsAlertAction, (state, action) => (
    {
      ...state,
      importAlert: action.alert,
    }
  )),
  on(clearImportGroupsAlertAction, (state, _) => (
    {
      ...state,
      importAlert: undefined,
    }
  )),
  on(setActiveGroupAction, (state, action) => (
    {
      ...state,
      activeGroup: {
        group: action.group,
        participants: action.participants,
      },
    }
  )),
  on(clearActiveGroupAction, (state, _) => ({...state, activeGroup: undefined})),
);

export function groupReducer(state: GroupState | undefined, action: Action): GroupState {
  return reducer(state, action);
}
