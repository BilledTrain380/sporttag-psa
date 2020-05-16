import { StatusDto, StatusSeverity } from "../../../dto/status";

import { StatusEntryModel } from "./status-detail/status-entry-model";

export class StatusModel {
  private static readonly CSS_BADGE_CLASS = 0;
  private static readonly LABEL = 1;
  private static readonly CSS_BASE_CLASSES = "badge status-badge mr-1";

  constructor(
    readonly cssClasses: string,
    readonly text: string,
    readonly entries: ReadonlyArray<StatusEntryModel>,
  ) {
  }

  public static fromDto(status: StatusDto): StatusModel {
    const params = StatusModel.createParameters(status);
    const cssClasses = `${StatusModel.CSS_BASE_CLASSES} ${params[StatusModel.CSS_BADGE_CLASS]}`;
    const entryModels = status.entries.map(entry => StatusEntryModel.fromDto(entry));

    return new StatusModel(
      cssClasses,
      params[StatusModel.LABEL],
      entryModels,
    );
  }

  private static createParameters(status: StatusDto): [string, string] {
    switch (status.severity) {
      case StatusSeverity.OK:
        return ["badge-success", $localize`OK`];
      case StatusSeverity.INFO:
        return ["badge-info", $localize`Info`];
      case StatusSeverity.WARNING:
        return ["badge-warning", $localize`Warning`];
      default:
        return ["badge-secondary", $localize`Unknown severity`];
    }
  }
}
