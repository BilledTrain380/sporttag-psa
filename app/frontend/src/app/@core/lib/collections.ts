import { BinaryOperator } from "./function";

export function flatMapping<T>(): BinaryOperator<Array<T>> {
  return (accumulator, current) => {
    accumulator.push(...current);

    return accumulator;
  };
}
