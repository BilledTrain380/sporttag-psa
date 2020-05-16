import { IconDefinition } from "@fortawesome/fontawesome-common-types";
import { faCheckCircle, faCircle, faExclamationCircle, faExclamationTriangle } from "@fortawesome/free-solid-svg-icons";

import { GroupStatusType } from "../../../../dto/group";
import { StatusEntry, StatusSeverity } from "../../../../dto/status";

export class StatusEntryModel {
  private static readonly ICON = 0;
  private static readonly CSS_CLASSES = 1;

  private constructor(
    readonly icon: IconDefinition,
    readonly cssClasses: string,
    readonly text: string,
  ) {
  }

  public static fromDto(entry: StatusEntry): StatusEntryModel {
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
    default:
      return $localize`Unknown status`;
  }
}
