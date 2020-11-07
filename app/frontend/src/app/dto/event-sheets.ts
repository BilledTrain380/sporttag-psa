import { GenderDto } from "./participation";

export interface EventSheetData {
  readonly discipline: string;
  readonly group: string;
  readonly gender: GenderDto;
}
