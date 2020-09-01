import { IconDefinition } from "@fortawesome/fontawesome-common-types";
import { faCheckCircle, faCircle, faExclamationCircle, faExclamationTriangle } from "@fortawesome/free-solid-svg-icons";

import { GroupStatusType } from "../../dto/group";
import { ParticipationStatusType } from "../../dto/participation";
import { StatusDto, StatusEntry, StatusSeverity } from "../../dto/status";

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

  static fromDto(status: StatusDto): StatusModel {
    const params = StatusModel.createParameters(status);
    const cssClasses = `${StatusModel.CSS_BASE_CLASSES} ${params[StatusModel.CSS_BADGE_CLASS]}`;
    const entryModels = status.entries.map(entry => StatusEntryModel.fromDto(entry));

    return new StatusModel(
      cssClasses,
      params[StatusModel.LABEL],
      entryModels,
    );
  }

  static unknown(): StatusModel {
    return StatusModel.fromDto({
                                 severity: StatusSeverity.INFO,
                                 entries: [],
                               });
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

export class StatusEntryModel {
  private static readonly ICON = 0;
  private static readonly CSS_CLASSES = 1;

  private constructor(
    readonly icon: IconDefinition,
    readonly cssClasses: string,
    readonly text: string,
  ) {
  }

  static fromDto(entry: StatusEntry): StatusEntryModel {
    const params = StatusEntryModel.createParameters(entry);
    const label = labelOfStatusType(entry.type);

    return new StatusEntryModel(
      params[StatusEntryModel.ICON],
      params[StatusEntryModel.CSS_CLASSES],
      label,
    );
  }

  private static createParameters(entry: StatusEntry): [IconDefinition, string] {
    switch (entry.severity) {
      case StatusSeverity.OK:
        return [faCheckCircle, "text-success"];
      case StatusSeverity.INFO:
        return [faExclamationCircle, "text-info"];
      case StatusSeverity.WARNING:
        return [faExclamationTriangle, "text-warning"];
      default:
        return [faCircle, "text-secondary"];
    }
  }
}

function labelOfStatusType(type: string): string {
  switch (type) {
    case GroupStatusType.OK:
      return $localize`OK`;
    case GroupStatusType.UNFINISHED_PARTICIPANTS:
      return $localize`Contains participants with no sport type`;
    case GroupStatusType.GROUP_TYPE_COMPETITIVE:
      return $localize`Contains only competitive participants`;
    case GroupStatusType.GROUP_TYPE_FUN:
      return $localize`Contains non competitive participants`;
    case ParticipationStatusType.OPEN:
      return $localize`Participation is open`;
    case ParticipationStatusType.CLOSED:
      return $localize`Participation is closed`;
    default:
      return $localize`Unknown status`;
  }
}
