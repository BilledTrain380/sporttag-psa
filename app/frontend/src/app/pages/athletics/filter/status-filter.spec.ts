import { now } from "../../../@core/lib/time";
import { CompetitorDto } from "../../../dto/athletics";
import { SimpleGroupDto } from "../../../dto/group";
import { BirthdayDto, BirthdayDtoImpl, GenderDto, TownDto } from "../../../dto/participation";

import { initialStatusFilterFormValues, StatusFilterFormValues, statusFilterPredicate } from "./status-filter";

describe("statusFilterPredicate", () => {
  const BIRTHDAY: BirthdayDto = BirthdayDtoImpl.of(now());
  const GROUP: SimpleGroupDto = {
    name: "2a",
    coach: "Willi Wirbelwind",
  };
  const TOWN: TownDto = {
    zip: "8000",
    name: "Zurich",
  };

  const ABSENT_COMPETITOR: CompetitorDto = {
    startnumber: 1,
    id: 1,
    absent: true,
    gender: GenderDto.MALE,
    surname: "Muster",
    prename: "Hans",
    address: "Musterstreet 18",
    results: new Map(),
    birthday: BIRTHDAY,
    group: GROUP,
    town: TOWN,
  };

  const PRESENT_COMPETITOR: CompetitorDto = {
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
    const predicate = statusFilterPredicate(initialStatusFilterFormValues);

    expect(predicate(ABSENT_COMPETITOR))
      .withContext("Filter result with absent competitor")
      .toBeTrue();

    expect(predicate(PRESENT_COMPETITOR))
      .withContext("Filter result with present competitor")
      .toBeTrue();
  });

  it("should return false when only absent competitor filters are configured", () => {
    const filters: StatusFilterFormValues = {
      statusAbsent: true,
      statusPresent: false,
    };

    const predicate = statusFilterPredicate(filters);

    expect(predicate(ABSENT_COMPETITOR))
      .withContext("Filter result with absent competitor")
      .toBeTrue();

    expect(predicate(PRESENT_COMPETITOR))
      .withContext("Filter result with present competitor")
      .toBeFalse();
  });

  it("should return false when only present competitor filters are configured", () => {
    const filters: StatusFilterFormValues = {
      statusAbsent: false,
      statusPresent: true,
    };

    const predicate = statusFilterPredicate(filters);

    expect(predicate(ABSENT_COMPETITOR))
      .withContext("Filter result with absent competitor")
      .toBeFalse();

    expect(predicate(PRESENT_COMPETITOR))
      .withContext("Filter result with present competitor")
      .toBeTrue();
  });

  it("should return true when both status filters are configured", () => {
    const filters: StatusFilterFormValues = {
      statusAbsent: true,
      statusPresent: true,
    };

    const predicate = statusFilterPredicate(filters);

    expect(predicate(ABSENT_COMPETITOR))
      .withContext("Filter result with absent competitor")
      .toBeTrue();

    expect(predicate(PRESENT_COMPETITOR))
      .withContext("Filter result with present competitor")
      .toBeTrue();
  });
});
