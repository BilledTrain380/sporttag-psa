export type Consumer<T> = (value: T) => void;
export type Supplier<T> = () => T;
export type UFunction<T, R> = (value: T) => R;
export type BiFunction<T, K, R> = (value1: T, value2: K) => R;
export type Predicate<T> = (value: T) => boolean;
export type Runnable = () => void;
