import { GenderDto } from "./participation";

export interface EventSheetExport {
  readonly discipline: string;
  readonly group: string;
  readonly gender: GenderDto;
}

export interface EventSheetData {
  readonly participationOpen: boolean;
  readonly groups: ReadonlyArray<string>;
}
