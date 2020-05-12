export class StatusDto {
  constructor(
    readonly severity: StatusSeverity,
    readonly entries: ReadonlyArray<StatusEntry>,
  ) {
  }

  static parseJson(json: string): StatusDto {
    const status = JSON.parse(json);

    return new StatusDto(status.severity, status.entries);
  }

  // tslint:disable-next-line: no-any
  static fromJson(json: any): StatusDto {
    return new StatusDto(json.severity, json.entries);
  }

  contains(type: StatusType): boolean {
    return this.entries.some(entry => entry.type.text === type.text);
  }
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
