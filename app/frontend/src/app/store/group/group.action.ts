import { createAction, props } from "@ngrx/store";

import { GroupDto } from "../../@core/service/api/group-api";

export interface PropsLoadGroups {
  readonly competitive?: boolean;
  readonly includePendingParticipationStatus?: boolean;
}

export const loadGroups = createAction(
  "[GroupOverview] Load Groups",
  props<PropsLoadGroups>(),
);

export interface PropsSetGroup {
  readonly groups: ReadonlyArray<GroupDto>;
}

export const setGroups = createAction(
  "[GroupReducer] Set Groups",
  props<PropsSetGroup>(),
);
