import { createAction, props } from "@ngrx/store";

import { ParticipationCommand, ParticipationDto } from "../../dto/participation";

export const loadParticipationAction = createAction(
  "[ParticipationManagement] Load participation status",
  props(),
);

export interface SetParticipationProps {
  readonly dto: ParticipationDto;
}

export const setParticipationAction = createAction(
  "[ParticipationManagement] Set participation status",
  props<SetParticipationProps>(),
);

export interface UpdateParticipationStatusProps {
  readonly command: ParticipationCommand;
}

export const updateParticipationAction = createAction(
  "[ParticipationManagement] Update participation status",
  props<UpdateParticipationStatusProps>(),
);
