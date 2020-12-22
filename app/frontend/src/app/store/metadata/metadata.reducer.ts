import { Action, createReducer, on } from "@ngrx/store";

import { BuildInfoDto } from "../../dto/about";

import { setMetadataAction } from "./metadata.action";

export interface MetadataState {
  readonly buildInfo?: BuildInfoDto;
}

const initialState: MetadataState = {};

const reducer = createReducer(
  initialState,
  on(setMetadataAction, (state, action) => (
    {
      ...state,
      buildInfo: action.buildInfo,
    }
  )),
);

export function metadataReducer(state: MetadataState | undefined, action: Action): MetadataState {
  return reducer(state, action);
}
