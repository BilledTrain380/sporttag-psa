import { createAction, props } from "@ngrx/store";

import { GenderDto } from "../../dto/participation";
import { DisciplineRanking } from "../../dto/ranking";

export const loadRankingData = createAction(
  "[RankingExportComponent] Load ranking data",
);

export interface SetRankingDataProps {
  readonly isParticipationOpen: boolean;
}

export const setRankingDataAction = createAction(
  "[RankingEffects] Set ranking data",
  props<SetRankingDataProps>(),
);

export interface DownloadTotalRankingProps {
  readonly genders: ReadonlyArray<GenderDto>;
}

export const downloadTotalRankingAction = createAction(
  "[RankingExportComponent] Download total ranking",
  props<DownloadTotalRankingProps>(),
);

export const finishTotalRankingFileAction = createAction(
  "[RankingEffects] Finish total ranking download",
);

export interface DownloadTriathlonRankingProps {
  readonly genders: ReadonlyArray<GenderDto>;
}

export const downloadTriathlonRankingAction = createAction(
  "[RankingExportComponent] Download triathlon ranking",
  props<DownloadTriathlonRankingProps>(),
);

export const finishTriathlonRankingFileAction = createAction(
  "[RankingEffects] Finish triathlon ranking download",
);

export interface DownloadUbsCupRankingProps {
  readonly genders: ReadonlyArray<GenderDto>;
}

export const downloadUbsCupRankingAction = createAction(
  "[RankingExportComponent] Download UBS-Cup ranking",
  props<DownloadUbsCupRankingProps>(),
);

export const finishUbsCupRankingFileAction = createAction(
  "[RankingEffects] Finish UBS-Cup ranking download",
);

export interface DownloadDisciplineRankingProps {
  readonly disciplines: ReadonlyArray<DisciplineRanking>;
}

export const downloadDisciplineRankingAction = createAction(
  "[RankingExportComponent] Download discipline ranking",
  props<DownloadDisciplineRankingProps>(),
);

export const finishDisciplineRankingFileAction = createAction(
  "[RankingEffects] Finish discipline ranking download",
);
