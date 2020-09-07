import { Predicate } from "../../../@core/lib/function";
import { CompetitorDto } from "../../../dto/athletics";

export interface StatusFilterFormValues {
  readonly statusAbsent: boolean;
  readonly statusPresent: boolean;
}

export const initialStatusFilterFormValues: StatusFilterFormValues = {
  statusPresent: false,
  statusAbsent: false,
};

export function statusFilterPredicate(filters: StatusFilterFormValues): Predicate<CompetitorDto> {
  return dto => {
    const isAnyFilterConfigured = Object.values(filters)
      .some(it => it);

    if (!isAnyFilterConfigured) {
      return true;
    }

    if (!filters.statusAbsent && dto.absent) {
      return false;
    }

    if (!filters.statusPresent && !dto.absent) {
      return false;
    }

    return true;
  };
}
