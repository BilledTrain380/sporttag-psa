export interface StatusDto {
  readonly severity: StatusSeverity;
  readonly entries: ReadonlyArray<StatusEntry>;
}

export enum StatusSeverity {
  OK = "OK",
  INFO = "INFO",
  WARNING = "WARNING",
}

export interface StatusType {
  readonly code: number;
  readonly text: string;
}

export interface StatusEntry {
  readonly severity: StatusSeverity;
  readonly type: StatusType;
}
