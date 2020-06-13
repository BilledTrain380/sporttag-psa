import { createAction, props } from "@ngrx/store";

import { GroupStatusType, OverviewGroupDto, SimpleGroupDto } from "../../dto/group";
import { ParticipantDto } from "../../dto/participation";
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
  "[GroupEffect] Set overview groups",
  props<SetOverviewGroupsProps>(),
);

export const clearOverviewGroupsAction = createAction(
  "[GroupOverview] Clear overview groups",
  props(),
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

export interface LoadGroupProps {
  readonly name: string;
}

export const loadGroupAction = createAction(
  "[GroupDetail] Load group",
  props<LoadGroupProps>(),
);

export interface SetActiveGroupProps {
  readonly group: SimpleGroupDto;
  readonly participants: ReadonlyArray<ParticipantDto>;
}

export const setActiveGroupAction = createAction(
  "[GroupEffect] Set active group",
  props<SetActiveGroupProps>(),
);

export const clearActiveGroupAction = createAction(
  "[GroupDetail] Clear active group",
  props(),
);
