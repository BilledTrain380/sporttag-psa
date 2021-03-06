import { Action, createReducer, on } from "@ngrx/store";

import { ifNotNullOrUndefined } from "../../@core/lib/lib";
import { OverviewGroupDto, SimpleGroupDto } from "../../dto/group";
import { ParticipantDto, ParticipantDtoBuilder } from "../../dto/participation";
import { Alert } from "../../modules/alert/alert";

import {
  addParticipantAction,
  clearActiveGroupAction,
  clearActiveParticipantAction,
  clearImportGroupsAlertAction,
  clearOverviewGroupsAction,
  clearParticipantAlertAction,
  deleteParticipantAction,
  setActiveGroupAction,
  setActiveParticipantAction,
  setImportGroupsAlertAction,
  setOverviewGroupsAction,
  setParticipantAlertAction,
  updateParticipantAction,
} from "./group.action";

export interface GroupState {
  readonly overviewGroups: ReadonlyArray<OverviewGroupDto>;
  readonly importAlert?: Alert;
  readonly activeGroup?: SimpleGroupDto;
  readonly participants: ReadonlyArray<ParticipantDto>;
  readonly participantAlert?: Alert;
  readonly activeParticipant?: ParticipantDto;
}

const initialState: GroupState = {
  overviewGroups: [],
  participants: [],
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
      activeGroup: action.group,
      participants: action.participants,
    }
  )),
  on(clearActiveGroupAction, (state, _) => ({...state, activeGroup: undefined})),
  on(setParticipantAlertAction, (state, action) => (
    {
      ...state,
      participantAlert: action.alert,
    }
  )),
  on(clearParticipantAlertAction, (state, _) => (
    {
      ...state,
      participantAlert: undefined,
    }
  )),
  on(setActiveParticipantAction, (state, action) => (
    {
      ...state,
      activeParticipant: action.participant,
    }
  )),
  on(clearActiveParticipantAction, (state, _) => (
    {
      ...state,
      activeParticipant: undefined,
    }
  )),
  on(addParticipantAction, (state, action) => {
    if (action.participant.id === 0) {
      return {...state};
    }

    const participants = [...state.participants, action.participant];

    return {
      ...state,
      participants,
    };
  }),
  on(updateParticipantAction, (state, action) => {
    const participants = state.participants
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
      participants,
    };
  }),
  on(deleteParticipantAction, (state, action) => {
    const participants = state.participants
      .filter(participant => participant.id !== action.participant_id);

    return {
      ...state,
      participants,
    };
  }),
);

export function groupReducer(state: GroupState | undefined, action: Action): GroupState {
  return reducer(state, action);
}
