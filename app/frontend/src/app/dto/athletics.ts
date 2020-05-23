export const SCHNELLLAUF = "Schnelllauf";
export const WEITSPRUNG = "Weitsprung";
export const BALLWURF = "Ballwurf";
export const BALLZIELWURF = "Ballzielwurf";
export const SEILSPRINGEN = "Seilspringen";
export const KORBEINWURF = "Korbeinwurf";

export interface ResultDto {
  readonly id: number;
  readonly value: number;
  readonly points: number;
  readonly discipline: DisciplineDto;
  readonly distance?: string;
  readonly relativeValue: string;
}

export interface ResultElement {
  readonly id: number;
  readonly value: number;
}

export interface DisciplineDto {
  readonly name: string;
  readonly unit: UnitDto;
  readonly hasTrials: boolean;
  readonly hasDistance: boolean;
}

export interface UnitDto {
  readonly name: string;
  readonly factor: number;
}
