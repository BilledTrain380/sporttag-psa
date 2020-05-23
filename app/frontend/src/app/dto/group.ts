import { StatusDto } from "./status";

export interface OverviewGroupDto {
  readonly group: SimpleGroupDto;
  readonly status: StatusDto;
}

export interface SimpleGroupDto {
  readonly name: string;
  readonly coach: string;
}

export interface CoachDto {
  readonly id: number;
  readonly name: string;
}

export enum GroupStatusType {
  OK = "OK",
  UNFINISHED_PARTICIPANTS = "UNFINISHED_PARTICIPANTS",
  GROUP_TYPE_COMPETITIVE = "GROUP_TYPE_COMPETITIVE",
  GROUP_TYPE_FUN = "GROUP_TYPE_FUN",
}
