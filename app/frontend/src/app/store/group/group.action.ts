import { createAction, props } from "@ngrx/store";

import { GroupStatusType, OverviewGroupDto } from "../../dto/group";

export interface LoadGroupsOverviewProps {
  readonly statusType?: GroupStatusType;
}

export const loadGroupsOverviewAction = createAction(
  "[GroupOverview] Load groups overview",
  props<LoadGroupsOverviewProps>(),
);

export interface PropsSetGroup {
  readonly groups: ReadonlyArray<OverviewGroupDto>;
}

export const setGroupsAction = createAction(
  "[GroupReducer] Set Groups",
  props<PropsSetGroup>(),
);
