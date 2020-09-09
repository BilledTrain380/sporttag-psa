import { Action, createReducer, on } from "@ngrx/store";

import { CompetitorDto, CompetitorDtoBuilder } from "../../dto/athletics";
import { SimpleGroupDto } from "../../dto/group";
import { ParticipationStatusType } from "../../dto/participation";
import { StatusEntry, StatusSeverity } from "../../dto/status";
import { Alert } from "../../modules/alert/alert";

import {
  clearAthleticsAlertAction,
  setAthleticsAlertAction,
  setCompetitorsAction,
  setGroupsAction,
  setParticipationStatusAction,
  updateCompetitorRelationAction,
} from "./athletics.action";

export interface AthleticsState {
  readonly groups: ReadonlyArray<SimpleGroupDto>;
  readonly competitors: ReadonlyArray<CompetitorDto>;
  readonly participationStatus: StatusEntry;
  readonly alert?: Alert;
}

const initialState: AthleticsState = {
  groups: [],
  competitors: [],
  participationStatus: {
    severity: StatusSeverity.INFO,
    type: ParticipationStatusType.CLOSED,
  },
};

const reducer = createReducer(
  initialState,
  on(setAthleticsAlertAction, (state, action) => (
    {
      ...state,
      alert: action.alert,
    }
  )),
  on(clearAthleticsAlertAction, (state, _) => (
    {
      ...state,
      alert: undefined,
    }
  )),
  on(setParticipationStatusAction, (state, action) => (
    {
      ...state,
      participationStatus: action.status,
    }
  )),
  on(setGroupsAction, (state, action) => (
    {
      ...state,
      groups: action.groups,
    }
  )),
  on(setCompetitorsAction, (state, action) => (
    {
      ...state,
      competitors: action.competitors,
    }
  )),
  on(updateCompetitorRelationAction, (state, action) => {
    const competitors = state.competitors
      .map(competitor => {
        if (competitor.id === action.competitorId) {
          const builder = CompetitorDtoBuilder.newBuilder(competitor);
          action.results.forEach(result => builder.setResult(result));

          return builder.build();
        }

        return competitor;
      });

    return {
      ...state,
      competitors,
    };
  }),
);

export function athleticsReducer(state: AthleticsState | undefined, action: Action): AthleticsState {
  return reducer(state, action);
}
