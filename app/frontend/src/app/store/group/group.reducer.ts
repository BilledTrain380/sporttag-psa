import { Action, createReducer, on } from "@ngrx/store";

import { OverviewGroupDto } from "../../dto/group";

import { setGroupsAction } from "./group.action";

export interface GroupState {
  readonly groups: ReadonlyArray<OverviewGroupDto>;
}

const initialState: GroupState = {
  groups: [],
};

const reducer = createReducer(
  initialState,
  on(setGroupsAction, ((_, action) => ({groups: action.groups}))),
);

export function groupReducer(state: GroupState | undefined, action: Action): GroupState {
  return reducer(state, action);
}
