import { Action, createReducer, on } from "@ngrx/store";

import { OverviewGroupDto } from "../../dto/group";
import { Alert } from "../../modules/alert/alert";

import { clearImportGroupsAlertAction, setImportGroupsAlertAction, setOverviewGroupsAction } from "./group.action";

export interface GroupState {
  readonly overviewGroups: ReadonlyArray<OverviewGroupDto>;
  readonly importAlert?: Alert;
}

const initialState: GroupState = {
  overviewGroups: [],
};

const reducer = createReducer(
  initialState,
  on(setOverviewGroupsAction, (state, action) => ({...state, overviewGroups: action.groups})),
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
