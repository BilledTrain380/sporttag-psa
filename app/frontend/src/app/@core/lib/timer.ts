import { Observable, Subject } from "rxjs";
import { debounceTime } from "rxjs/operators";

import { Duration, HALF_SECOND_IN_MILLIS, QUARTER_SECOND_IN_MILLIS, SECOND_IN_MILLIS, TemporalUnit } from "./time";

export class Timer<R> {
  get run$(): Observable<R> {
    return this.runSubject$.asObservable()
      .pipe(debounceTime(this.delay.get(TemporalUnit.MILLIS)));
  }

  private constructor(
    private readonly delay: Duration,
  ) {
  }

  private readonly runSubject$ = new Subject<R>();

  static ofDelay<T>(delay: Duration): Timer<T> {
    return new Timer<T>(delay);
  }

  static ofOneSecond<T>(): Timer<T> {
    return new Timer<T>(Duration.ofMillis(SECOND_IN_MILLIS));
  }

  static ofFiveSeconds<T>(): Timer<T> {
    // tslint:disable-next-line:no-magic-numbers
    return new Timer<T>(Duration.ofSeconds(5));
  }

  static ofHalfSecond<T>(): Timer<T> {
    return new Timer<T>(Duration.ofMillis(HALF_SECOND_IN_MILLIS));
  }

  static ofQuarterSecond<T>(): Timer<T> {
    return new Timer<T>(Duration.ofMillis(QUARTER_SECOND_IN_MILLIS));
  }

  trigger(value: R): void {
    this.runSubject$.next(value);
  }

  abort(): void {
    this.runSubject$.complete();
  }
}
