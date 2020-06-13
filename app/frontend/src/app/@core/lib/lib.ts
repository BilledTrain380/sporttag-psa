import { Consumer, Predicate, Supplier, UFunction } from "./function";

export class Optional<T> {
  // tslint:disable-next-line:no-any
  private static readonly EMPTY: Optional<any> = new Optional();

  private constructor(
    private readonly value?: T,
  ) {
  }

  static empty<T>(): Optional<T> {
    return Optional.EMPTY;
  }

  static of<T>(value: T): Optional<T> {
    requireNonNullOrUndefined(value);

    return new Optional<T>(value);
  }

  static ofUndefinable<T>(value: T | undefined): Optional<T> {
    return value ? Optional.of(value) : Optional.empty();
  }

  get(): T {
    if (isNullOrUndefined(this.value)) {
      throw new Error("No value present");
    }

    return this.value!;
  }

  isPresent(): boolean {
    return !isNullOrUndefined(this.value);
  }

  ifPresent(consumer: Consumer<T>): void {
    if (!isNullOrUndefined(this.value)) {
      consumer(this.value!);
    }
  }

  filter(predicate: Predicate<T>): Optional<T> {
    if (!this.isPresent()) {
      return this;
    }

    return predicate(this.value!) ? this : Optional.empty();
  }

  map<R>(mapper: UFunction<T, R | undefined>): Optional<R> {
    if (!this.isPresent()) {
      return Optional.empty();
    }

    return Optional.ofUndefinable(mapper(this.value!));
  }

  orElse(other: T): T {
    return this.isPresent() ? this.value! : other;
  }

  orElseGet(other: Supplier<T>): T {
    return this.isPresent() ? this.value! : other();
  }

  orElseThrow(error: Error): T {
    if (this.isPresent()) {
      return this.value!;
    }

    throw error;
  }
}

// tslint:disable-next-line: no-null-undefined-union
export function requireNonNullOrUndefined<T>(value: T | undefined | null): T {
  if (value === undefined || value === null) {
    throw new Error("Value must not be undefined or null");
  }

  return value;
}

// tslint:disable-next-line:no-any
export function isNullOrUndefined(value: any): boolean {
  return value === undefined || value === null;
}

export function ifNotNullOrUndefined<T>(value: T | undefined, consumer: Consumer<T>): void {
  if (!isNullOrUndefined(value)) {
    consumer(value!);
  }
}
