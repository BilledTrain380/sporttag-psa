import { createAction, props } from "@ngrx/store";

import { EventSheetExport } from "../../dto/event-sheets";
import { SportDto } from "../../dto/participation";

export const loadEventSheetDataAction = createAction(
  "[EventSheetsSelectionComponent] Load event sheets data",
);

export interface SetEventSheetsDataProps {
  readonly isParticipationOpen: boolean;
  readonly groupNames: ReadonlyArray<string>;
}

export const setEventSheetsDataAction = createAction(
  "[EventSheetsEffects] Set event sheets data",
  props<SetEventSheetsDataProps>(),
);

export interface DownloadParticipantListProps {
  readonly data: ReadonlyArray<SportDto>;
}

export const downloadParticipantListAction = createAction(
  "[EventSheetsEffects] Download participant list",
  props<DownloadParticipantListProps>(),
);

export const finishParticipantFileAction = createAction(
  "[EventSheetsEffects] Finish participant list file",
);

export interface DownloadEventSheetsProps {
  readonly data: ReadonlyArray<EventSheetExport>;
}

export const downloadEventSheetsAction = createAction(
  "[EventSheetsEffects] Download event sheets",
  props<DownloadEventSheetsProps>(),
);

export const finishEventSheetsFileAction = createAction(
  "[EventSheetsEffects] Finish event sheets file",
);

export const downloadStartlistAction = createAction(
  "[EventSheetsEffects] Download start list",
);

export const finishStartlistFileAction = createAction(
  "[EventSheetsEffects] Finish start list file",
);
