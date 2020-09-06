import { requireNonNullOrUndefined } from "../@core/lib/lib";

import { SimpleGroupDto } from "./group";
import { BirthdayDto, GenderDto, TownDto } from "./participation";

export const SCHNELLLAUF = "Schnelllauf";
export const WEITSPRUNG = "Weitsprung";
export const BALLWURF = "Ballwurf";
export const BALLZIELWURF = "Ballzielwurf";
export const SEILSPRINGEN = "Seilspringen";
export const KORBEINWURF = "Korbeinwurf";

export interface CompetitorDto {
  readonly startnumber: number;
  readonly id: number;
  readonly surname: string;
  readonly prename: string;
  readonly gender: GenderDto;
  readonly birthday: BirthdayDto;
  readonly absent: boolean;
  readonly address: string;
  readonly town: TownDto;
  readonly group: SimpleGroupDto;
  readonly results: ReadonlyMap<string, ResultDto>;
}

export class CompetitorDtoBuilder {
  private competitorDto?: CompetitorDto;
  private readonly results: Array<ResultDto> = [];

  private constructor() {
  }

  static newBuilder(dto?: CompetitorDto): CompetitorDtoBuilder {
    const builder = new CompetitorDtoBuilder();

    if (dto) {
      builder.copy(dto);
    }

    return builder;
  }

  copy(dto: CompetitorDto): CompetitorDtoBuilder {
    this.competitorDto = dto;

    return this;
  }

  setResult(result: ResultDto): CompetitorDtoBuilder {
    this.results.push(result);

    return this;
  }

  build(): CompetitorDto {
    const dto = requireNonNullOrUndefined(this.competitorDto);

    const results = new Map(dto.results.entries());
    this.results.forEach(result => results.set(result.discipline.name, result));

    return {
      startnumber: dto.startnumber,
      id: dto.id,
      surname: dto.surname,
      prename: dto.prename,
      gender: dto.gender,
      birthday: dto.birthday,
      absent: dto.absent,
      address: dto.address,
      town: dto.town,
      group: dto.group,
      results,
    };
  }
}

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
