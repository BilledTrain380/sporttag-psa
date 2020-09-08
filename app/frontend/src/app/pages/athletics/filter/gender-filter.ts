import { Predicate } from "../../../@core/lib/function";
import { CompetitorDto } from "../../../dto/athletics";
import { GenderDto } from "../../../dto/participation";

export interface GenderFilterFormValues {
  readonly genderMale: boolean;
  readonly genderFemale: boolean;
}

export const initialGenderFilterFormValues: GenderFilterFormValues = {
  genderFemale: false,
  genderMale: false,
};

export function genderFilterPredicate(filters: GenderFilterFormValues): Predicate<CompetitorDto> {
  return dto => {
    const isAnyFilterConfigured = Object.values(filters)
      .some(it => it);

    if (!isAnyFilterConfigured) {
      return true;
    }

    if (!filters.genderFemale && dto.gender === GenderDto.FEMALE) {
      return false;
    }

    if (!filters.genderMale && dto.gender === GenderDto.MALE) {
      return false;
    }

    return true;
  };
}
