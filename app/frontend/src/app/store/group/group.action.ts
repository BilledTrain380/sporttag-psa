import { createAction, props } from "@ngrx/store";
import { Alert } from "../../modules/alert/alert";

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

export interface ImportGroupsProps {
  readonly file: File;
}

export const importGroupsAction = createAction(
  "[ImportGroups] Import groups",
  props<ImportGroupsProps>(),
);

export interface ImportAlertProps {
  readonly alert: Alert;
}

export const setImportGroupsAlertAction = createAction(
  "[ImportGroups] Import groups alert",
  props<ImportAlertProps>(),
);

export const clearImportGroupsAlertAction = createAction(
  "[ImportGroups] Clear alert",
  props(),
);
