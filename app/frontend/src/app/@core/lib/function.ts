export type Consumer<T> = (value: T) => void;
export type Supplier<T> = () => T;
export type UFunction<T, R> = (value: T) => R;
export type Predicate<T> = (value: T) => boolean;
