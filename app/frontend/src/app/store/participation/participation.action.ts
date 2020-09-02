import { createAction, props } from "@ngrx/store";

import { ParticipationCommand, ParticipationDto } from "../../dto/participation";

export const loadParticipationStatusAction = createAction(
  "[ParticipationManagement] Load participation status",
  props(),
);

export interface SetParticipationStatusProps {
  readonly dto: ParticipationDto;
}

export const setParticipationStatusAction = createAction(
  "[ParticipationManagement] Set participation status",
  props<SetParticipationStatusProps>(),
);

export interface UpdateParticipationStatusProps {
  readonly command: ParticipationCommand;
}

export const updateParticipationStatusAction = createAction(
  "[ParticipationManagement] Update participation status",
  props<UpdateParticipationStatusProps>(),
);
