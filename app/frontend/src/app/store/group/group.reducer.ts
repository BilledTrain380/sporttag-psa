import { GroupDto } from "../../@core/service/api/group-api";
import { Action, createReducer, on } from "@ngrx/store";
import { setGroups } from "./group.action";

export interface GroupState {
  readonly groups: ReadonlyArray<GroupDto>;
}

const initialState: GroupState = {
  groups: [],
};

const reducer = createReducer(
  initialState,
  on(setGroups, ((_, action) => ({groups: action.groups}))),
);

export function groupReducer(state: GroupState | undefined, action: Action): GroupState {
  return reducer(state, action);
}
