import { requireNonNullOrUndefined } from "../@core/lib/lib";
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

export class ParticipantDtoBuilder {
  private id?: number;
  private surname?: string;
  private prename?: string;
  private gender?: GenderDto;
  private birthday?: BirthdayDto;
  private isAbsent?: boolean;
  private address?: string;
  private town?: TownDto;
  private group?: SimpleGroupDto;
  private sportType?: string;

  private constructor() {
  }

  static newBuilder(dto?: ParticipantDto): ParticipantDtoBuilder {
    const builder = new ParticipantDtoBuilder();

    if (dto) {
      builder.copy(dto);
    }

    return builder;
  }

  copy(dto: ParticipantDto): ParticipantDtoBuilder {
    this.id = dto.id;
    this.surname = dto.surname;
    this.prename = dto.prename;
    this.gender = dto.gender;
    this.birthday = dto.birthday;
    this.isAbsent = dto.isAbsent;
    this.address = dto.address;
    this.town = dto.town;
    this.group = dto.group;
    this.sportType = dto.sportType;

    return this;
  }

  setId(id: number): ParticipantDtoBuilder {
    this.id = id;

    return this;
  }

  setSurname(surname: string): ParticipantDtoBuilder {
    this.surname = surname;

    return this;
  }

  setPrename(prename: string): ParticipantDtoBuilder {
    this.prename = prename;

    return this;
  }

  setGender(gender: GenderDto): ParticipantDtoBuilder {
    this.gender = gender;

    return this;
  }

  setBirthday(birthday: BirthdayDto): ParticipantDtoBuilder {
    this.birthday = birthday;

    return this;
  }

  setAbsent(isAbsent: boolean): ParticipantDtoBuilder {
    this.isAbsent = isAbsent;

    return this;
  }

  setAddress(address: string): ParticipantDtoBuilder {
    this.address = address;

    return this;
  }

  setTown(town: TownDto): ParticipantDtoBuilder {
    this.town = town;

    return this;
  }

  setSportType(sportType: string): ParticipantDtoBuilder {
    this.sportType = sportType;

    return this;
  }

  build(): ParticipantDto {
    return {
      id: requireNonNullOrUndefined(this.id),
      surname: requireNonNullOrUndefined(this.surname),
      prename: requireNonNullOrUndefined(this.prename),
      gender: requireNonNullOrUndefined(this.gender),
      birthday: requireNonNullOrUndefined(this.birthday),
      isAbsent: requireNonNullOrUndefined(this.isAbsent),
      address: requireNonNullOrUndefined(this.address),
      town: requireNonNullOrUndefined(this.town),
      group: requireNonNullOrUndefined(this.group),
      sportType: this.sportType,
    };
  }
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
  readonly value: Date;
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
