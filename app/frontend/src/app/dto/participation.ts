import { SimpleGroupDto } from "./group";
import { StatusDto } from "./status";

export const ATHLETICS = "Athletics";
export const SCHATZSUCHE = "Schatzsuche";
export const BRENNBALL = "Brennball";
export const VELO_ROLLERBLADES = "Velo- Rollerblades";

export interface ParticipantDto {
  readonly id: number;
  readonly surname: string;
  readonly prename: string;
  readonly gender: GenderDto;
  readonly birthday: BirthdayDto;
  readonly isAbsent: boolean;
  readonly address: string;
  readonly town: TownDto;
  readonly group: SimpleGroupDto;
  readonly sportType?: string;
}

export enum ParticipationCommand {
  CLOSE = "CLOSE",
  RESET = "RESET",
}

export interface ParticipantElement {
  readonly id: number;
  readonly surname?: string;
  readonly prename?: string;
  readonly gender?: GenderDto;
  readonly birthday?: BirthdayDto;
  readonly isAbsent?: boolean;
  readonly address?: string;
  readonly town?: TownDto;
}

export interface ParticipantInput {
  readonly surname: string;
  readonly prename: string;
  readonly gender: GenderDto;
  readonly birthday: BirthdayDto;
  readonly address: string;
  readonly town: TownDto;
  readonly group: string;
}

export interface ParticipantRelation {
  readonly id: number;
  readonly sportType?: string;
}

export interface ParticipationDto {
  readonly status: StatusDto;
  readonly unfinishedGroups: ReadonlyArray<SimpleGroupDto>;
}

export enum GenderDto {
  MALE = "MALE",
  FEMALE = "FEMALE",
}

export function translateGender(gender: GenderDto): string {
  switch (gender) {
    case GenderDto.MALE:
      return $localize`Male`;
    case GenderDto.FEMALE:
      return $localize`Female`;
    default:
      throw new Error(`Can not translate unknown gender: gender=${gender}`);
  }
}

export interface BirthdayDto {
  readonly time: number;
}

export interface TownDto {
  readonly zip: string;
  readonly name: string;
}

export enum ParticipationStatusType {
  OPEN = "OPEN",
  CLOSED = "CLOSED",
}

export interface SportDto {
  readonly name: string;
}
