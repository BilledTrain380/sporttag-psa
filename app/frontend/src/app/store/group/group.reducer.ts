import { Action, createReducer, on } from "@ngrx/store";

import { OverviewGroupDto } from "../../dto/group";
import { Alert } from "../../modules/alert/alert";

import { clearImportGroupsAlertAction, setGroupsAction, setImportGroupsAlertAction } from "./group.action";

export interface GroupState {
  readonly groups: ReadonlyArray<OverviewGroupDto>;
  readonly importAlert?: Alert;
}

const initialState: GroupState = {
  groups: [],
};

const reducer = createReducer(
  initialState,
  on(setGroupsAction, (state, action) => ({...state, groups: action.groups})),
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
);

export function groupReducer(state: GroupState | undefined, action: Action): GroupState {
  return reducer(state, action);
}
