import { Action, createReducer, on } from "@ngrx/store";

import { ifNotNullOrUndefined } from "../../@core/lib/lib";
import { OverviewGroupDto, SimpleGroupDto } from "../../dto/group";
import { ParticipantDto, ParticipantDtoBuilder } from "../../dto/participation";
import { Alert } from "../../modules/alert/alert";

import {
  clearActiveGroupAction,
  clearActiveGroupAlertAction,
  clearImportGroupsAlertAction,
  clearOverviewGroupsAction,
  setActiveGroupAction,
  setActiveGroupAlertAction,
  setImportGroupsAlertAction,
  setOverviewGroupsAction,
  updateParticipantAction,
} from "./group.action";

export interface GroupState {
  readonly overviewGroups: ReadonlyArray<OverviewGroupDto>;
  readonly importAlert?: Alert;
  readonly activeGroup?: ActiveGroup;
}

export interface ActiveGroup {
  readonly group: SimpleGroupDto;
  readonly participants: ReadonlyArray<ParticipantDto>;
  readonly alert?: Alert;
}

const initialState: GroupState = {
  overviewGroups: [],
};

const reducer = createReducer(
  initialState,
  on(setOverviewGroupsAction, (state, action) => ({...state, overviewGroups: action.groups})),
  on(clearOverviewGroupsAction, (state, _) => ({...state, overviewGroups: []})),
  on(setImportGroupsAlertAction, (state, action) => (
    {
      ...state,
      importAlert: action.alert,
    }
  )),
  on(clearImportGroupsAlertAction, (state, _) => (
    {
      ...state,
      importAlert: undefined,
    }
  )),
  on(setActiveGroupAction, (state, action) => (
    {
      ...state,
      activeGroup: {
        group: action.group,
        participants: action.participants,
      },
    }
  )),
  on(clearActiveGroupAction, (state, _) => ({...state, activeGroup: undefined})),
  on(setActiveGroupAlertAction, (state, action) => (
    {
      ...state,
      activeGroup: !state.activeGroup ? undefined : {
        group: state.activeGroup.group,
        participants: state.activeGroup.participants,
        alert: action.alert,
      },
    }
  )),
  on(clearActiveGroupAlertAction, (state, _) => (
    {
      ...state,
      activeGroup: !state.activeGroup ? undefined : {
        group: state.activeGroup.group,
        participants: state.activeGroup.participants,
        alert: undefined,
      },
    }
  )),
  on(updateParticipantAction, (state, action) => {
    const participants = state.activeGroup?.participants
      .map(participant => {
        if (participant.id === action.participant.id) {
          const builder = ParticipantDtoBuilder.newBuilder(participant);
          ifNotNullOrUndefined(action.participant.surname, value => builder.setSurname(value));
          ifNotNullOrUndefined(action.participant.prename, value => builder.setPrename(value));
          ifNotNullOrUndefined(action.participant.gender, value => builder.setGender(value));
          ifNotNullOrUndefined(action.participant.birthday, value => builder.setBirthday(value));
          ifNotNullOrUndefined(action.participant.address, value => builder.setAddress(value));
          ifNotNullOrUndefined(action.participant.isAbsent, value => builder.setAbsent(value));
          ifNotNullOrUndefined(action.participant.town, value => builder.setTown(value));

          return builder.build();
        }

        return participant;
      });

    return {
      ...state,
      activeGroup: !state.activeGroup ? undefined : {
        ...state.activeGroup,
        participants: participants ? participants : [],
      },
    };
  }),
);

export function groupReducer(state: GroupState | undefined, action: Action): GroupState {
  return reducer(state, action);
}
