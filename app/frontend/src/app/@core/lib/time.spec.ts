import { NgbDate } from "@ng-bootstrap/ng-bootstrap";
import { Duration, isoFormatOfDate, parseDate, SECOND_IN_MILLIS, TemporalUnit } from "./time";

interface ExpectedDurationValues {
  readonly days: number;
  readonly hours: number;
  readonly minutes: number;
  readonly seconds: number;
}

// tslint:disable: no-magic-numbers
describe("Time", () => {
  describe("Duration", () => {

    function assertDuration(duration: Duration, expected: ExpectedDurationValues): void {
      expect(duration.isZero())
        .toBeFalse();
      expect(duration.get(TemporalUnit.DAYS))
        .toBe(expected.days);
      expect(duration.get(TemporalUnit.HOURS))
        .toBe(expected.hours);
      expect(duration.get(TemporalUnit.MINUTES))
        .toBe(expected.minutes);
      expect(duration.get(TemporalUnit.SECONDS))
        .toBe(expected.seconds);
      expect(duration.get(TemporalUnit.MILLIS))
        .toBe(expected.seconds * SECOND_IN_MILLIS);
    }

    it("should create a duration by days", () => {
      const oneDay = Duration.ofDays(1);

      assertDuration(oneDay, {
        days: 1,
        hours: 24,
        minutes: 1440,
        seconds: 86_400,
      });
    });

    it("should create a duration by hours", () => {
      const twoHours = Duration.ofHours(2);

      assertDuration(twoHours, {
        days: 0.083333,
        hours: 2,
        minutes: 120,
        seconds: 7200,
      });
    });

    it("should create a duration by minutes", () => {
      const twentyMinutes = Duration.ofMinutes(20);

      assertDuration(twentyMinutes, {
        days: 0.013889,
        hours: 0.333333,
        minutes: 20,
        seconds: 1200,
      });
    });

    it("should create a duration by seconds", () => {
      const fourteenSeconds = Duration.ofSeconds(30);

      assertDuration(fourteenSeconds, {
        days: 0.000347,
        hours: 0.008333,
        minutes: 0.5,
        seconds: 30,
      });
    });

    it("should create a duration by millis", () => {
      const twoHundredMillis = Duration.ofMillis(500);

      assertDuration(twoHundredMillis, {
        days: 0.000006,
        hours: 0.000139,
        minutes: 0.008333,
        seconds: 0.5,
      });
    });

    it("should create a duration of zero", () => {
      const zero = Duration.ZERO;

      expect(zero.isZero())
        .toBeTrue();
    });
  });

  describe("parse date", () => {
    it("should parse a valid ISO date", () => {
      const date = parseDate("2008-04-28");

      expect(date.year)
        .toBe(2008);
      expect(date.month)
        .toBe(4);
      expect(date.day)
        .toBe(28);
    });

    it("should throw an error on invalid ISO date", () => {
      expect(() => parseDate("invalid"))
        .toThrowError("Invalid date value: invalid");
    });
  });

  describe("iso format date", () => {
    it("should format with two digit numbers when month or day is one digit", () => {
      const date = new NgbDate(2008, 5, 2);

      const format = isoFormatOfDate(date);

      expect(format)
        .toBe("2008-05-02");
    });

    it("should format with two digit numbers", () => {
      const date = new NgbDate(2008, 11, 28);

      const format = isoFormatOfDate(date);

      expect(format)
        .toBe("2008-11-28");
    });
  });
});
