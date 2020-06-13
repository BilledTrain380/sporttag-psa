import { UFunction } from "./function";

export const DAY_IN_MILLIS = 86_400_000;
export const HOUR_IN_MILLIS = 3_600_000;
export const MINUTE_IN_MILLIS = 60_000;
export const SECOND_IN_MILLIS = 1000;
export const HALF_SECOND_IN_MILLIS = 500;
export const QUARTER_SECOND_IN_MILLIS = 250;

export enum TemporalUnit {
  DAYS = "DAYS",
  HOURS = "HOURS",
  MINUTES = "MINUTES",
  SECONDS = "SECONDS",
  MILLIS = "MILLIS",
}

export class Duration {
  static readonly ZERO = new Duration(0);

  private static ROUND_PRECISION = 1000_000;

  private static UNIT_MAP: ReadonlyMap<TemporalUnit, UFunction<number, number>> =
    new Map([
              [TemporalUnit.DAYS, millis => millis / DAY_IN_MILLIS],
              [TemporalUnit.HOURS, millis => millis / HOUR_IN_MILLIS],
              [TemporalUnit.MINUTES, millis => millis / MINUTE_IN_MILLIS],
              [TemporalUnit.SECONDS, millis => millis / SECOND_IN_MILLIS],
              [TemporalUnit.MILLIS, millis => millis],
            ]);

  private constructor(
    private readonly millis: number,
  ) {
  }

  static ofDays(days: number): Duration {
    return Duration.create(days, DAY_IN_MILLIS);
  }

  static ofHours(hours: number): Duration {
    return Duration.create(hours, HOUR_IN_MILLIS);
  }

  static ofMinutes(minutes: number): Duration {
    return Duration.create(minutes, MINUTE_IN_MILLIS);
  }

  static ofSeconds(seconds: number): Duration {
    return Duration.create(seconds, SECOND_IN_MILLIS);
  }

  static ofMillis(millis: number): Duration {
    return new Duration(millis);
  }

  private static create(value: number, factor: number): Duration {
    return new Duration(Math.floor(value * factor));
  }

  get(unit: TemporalUnit): number {
    const calculateFn = Duration.UNIT_MAP.get(unit);

    if (calculateFn) {
      const raw = calculateFn(this.millis);

      return Math.round((raw + Number.EPSILON) * Duration.ROUND_PRECISION) / Duration.ROUND_PRECISION;
    }

    throw new Error(`Unsupported temporal unit: ${unit}`);
  }

  isZero(): boolean {
    return this.millis === 0;
  }

  equalsTo(other: Duration): boolean {
    return this.millis === other.get(TemporalUnit.MILLIS);
  }
}
