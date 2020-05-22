import { createAction, props } from "@ngrx/store";

import { GroupStatusType, OverviewGroupDto } from "../../dto/group";
import { Alert } from "../../modules/alert/alert";

export interface LoadOverviewGroupsProps {
  readonly statusType?: GroupStatusType;
}

export const loadOverviewGroupsAction = createAction(
  "[GroupOverview] Load overview groups",
  props<LoadOverviewGroupsProps>(),
);

export interface SetOverviewGroupsProps {
  readonly groups: ReadonlyArray<OverviewGroupDto>;
}

export const setOverviewGroupsAction = createAction(
  "[GroupReducer] Set overview groups",
  props<SetOverviewGroupsProps>(),
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
