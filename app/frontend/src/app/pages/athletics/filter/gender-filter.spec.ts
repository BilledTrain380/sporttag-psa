import { now } from "../../../@core/lib/time";
import { CompetitorDto } from "../../../dto/athletics";
import { SimpleGroupDto } from "../../../dto/group";
import { BirthdayDto, BirthdayDtoImpl, GenderDto, TownDto } from "../../../dto/participation";

import { GenderFilterFormValues, genderFilterPredicate, initialGenderFilterFormValues } from "./gender-filter";

describe("genderFilterPredicate", () => {
  const BIRTHDAY: BirthdayDto = BirthdayDtoImpl.of(now());
  const GROUP: SimpleGroupDto = {
    name: "2a",
    coach: "Willi Wirbelwind",
  };
  const TOWN: TownDto = {
    zip: "8000",
    name: "Zurich",
  };

  const MALE_COMPETITOR: CompetitorDto = {
    startnumber: 1,
    id: 1,
    absent: false,
    gender: GenderDto.MALE,
    surname: "Muster",
    prename: "Hans",
    address: "Musterstreet 18",
    results: new Map(),
    birthday: BIRTHDAY,
    group: GROUP,
    town: TOWN,
  };

  const FEMALE_COMPETITOR: CompetitorDto = {
    startnumber: 2,
    id: 2,
    absent: false,
    gender: GenderDto.FEMALE,
    surname: "Bossi",
    prename: "Betty",
    address: "Bettybossystreet 21",
    results: new Map(),
    birthday: BIRTHDAY,
    group: GROUP,
    town: TOWN,
  };

  it("should return true when no filters are configured", () => {
    const predicate = genderFilterPredicate(initialGenderFilterFormValues);

    expect(predicate(FEMALE_COMPETITOR))
      .withContext("Filter result with female competitor")
      .toBeTrue();

    expect(predicate(MALE_COMPETITOR))
      .withContext("Filter result with male competitor")
      .toBeTrue();
  });

  it("should return false when only gender male is configured", () => {
    const filters: GenderFilterFormValues = {
      genderFemale: false,
      genderMale: true,
    };

    const predicate = genderFilterPredicate(filters);

    expect(predicate(FEMALE_COMPETITOR))
      .withContext("Filter result with female competitor")
      .toBeFalse();

    expect(predicate(MALE_COMPETITOR))
      .withContext("Filter result with male competitor")
      .toBeTrue();
  });

  it("should return false when only gender female is configured", () => {
    const filters: GenderFilterFormValues = {
      genderFemale: true,
      genderMale: false,
    };

    const predicate = genderFilterPredicate(filters);

    expect(predicate(FEMALE_COMPETITOR))
      .withContext("Filter result with female competitor")
      .toBeTrue();

    expect(predicate(MALE_COMPETITOR))
      .withContext("Filter result with male competitor")
      .toBeFalse();
  });

  it("should return true when both genders are configured", () => {
    const filters: GenderFilterFormValues = {
      genderFemale: true,
      genderMale: true,
    };

    const predicate = genderFilterPredicate(filters);

    expect(predicate(FEMALE_COMPETITOR))
      .withContext("Filter result with female competitor")
      .toBeTrue();

    expect(predicate(MALE_COMPETITOR))
      .withContext("Filter result with male competitor")
      .toBeTrue();
  });
});
