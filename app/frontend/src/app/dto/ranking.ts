import { GenderDto } from "./participation";

export interface RankingData {
  readonly total: ReadonlyArray<GenderDto>;
  readonly discipline: ReadonlyArray<DisciplineRanking>;
  readonly triathlon: ReadonlyArray<GenderDto>;
  readonly ubsCup: ReadonlyArray<GenderDto>;
}

export interface DisciplineRanking {
  readonly discipline: string;
  readonly gender: GenderDto;
}
