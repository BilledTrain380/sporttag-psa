import { createAction, props } from "@ngrx/store";

import { BuildInfoDto } from "../../dto/about";

export const loadMetadataAction = createAction(
  "[AppComponent] Load metadata",
);

export interface SetMetadataProps {
  readonly buildInfo: BuildInfoDto;
}

export const setMetadataAction = createAction(
  "[MetadataEffects] Set metadata",
  props<SetMetadataProps>(),
);
