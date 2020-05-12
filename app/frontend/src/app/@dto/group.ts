import { StatusDto } from "./status";

export class OverviewGroupDto {
  constructor(
    readonly group: SimpleGroupDto,
    readonly status: StatusDto,
  ) {
  }

  static parseJson(json: string): OverviewGroupDto {
    const overview = JSON.parse(json);

    const groupDto = SimpleGroupDto.fromJson(overview.group);
    const statusDto = StatusDto.fromJson(overview.status);

    return new OverviewGroupDto(groupDto, statusDto);
  }
}

export class SimpleGroupDto {
  constructor(
    readonly name: string,
    readonly coach: string,
  ) {
  }

  static parseJson(json: string): SimpleGroupDto {
    const group = JSON.parse(json);

    return new SimpleGroupDto(group.name, group.coach);
  }

  // tslint:disable-next-line: no-any
  static fromJson(json: any): SimpleGroupDto {
    return new SimpleGroupDto(json.name, json.group);
  }
}

export class CoachDto {
  constructor(
    readonly id: number,
    readonly name: string,
  ) {
  }

  static parseJson(json: string): CoachDto {
    const coach = JSON.parse(json);

    return new CoachDto(coach.id, coach.name);
  }
}

export enum GroupStatusType {
  OK = "OK",
  UNFINISHED_PARTICIPANTS = "UNFINISHED_PARTICIPANTS",
  GROUP_TYPE_COMPETITIVE = "GROUP_TYPE_COMPETITIVE",
  GROUP_TYPE_FUN = "GROUP_TYPE_FUN",
}
