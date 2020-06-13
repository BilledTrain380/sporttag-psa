import { Duration, SECOND_IN_MILLIS, TemporalUnit } from "./time";

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
});
