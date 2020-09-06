import { FormControl, Validators } from "@angular/forms";

import { requireNonNullOrUndefined } from "../../@core/lib/lib";
import { CompetitorDto, ResultDto, ResultElement, UnitDto } from "../../dto/athletics";
import { GenderDto } from "../../dto/participation";
import { floatNumber, intNumber } from "../../modules/common-forms/validatiors/number-validators";

export class CompetitorModel {
  constructor(
    readonly id: number,
    readonly startnumber: number,
    readonly surname: string,
    readonly prename: string,
    readonly gender: GenderDto,
    readonly isAbsent: boolean,
    readonly result: ResultModel,
  ) {
  }

  static fromDtoWithDiscipline(dto: CompetitorDto, discipline: string): CompetitorModel {
    const result = requireNonNullOrUndefined(dto.results.get(discipline));

    return new CompetitorModel(
      dto.id,
      dto.startnumber,
      dto.surname,
      dto.prename,
      dto.gender,
      dto.absent,
      ResultModel.fromDto(result),
    );
  }
}

export class ResultModel {
  readonly displayValueControl: FormControl;

  constructor(
    readonly id: number,
    readonly prependText: string,
    readonly appendText: string,
    displayValue: string,
    readonly points: number,
    private readonly unit: UnitDto,
  ) {
    const validators = [Validators.required, Validators.min(1)];

    if (this.unit.factor > 1) {
      validators.push(floatNumber());
    } else {
      validators.push(intNumber());
    }

    this.displayValueControl = new FormControl(displayValue, validators);
  }

  static fromDto(dto: ResultDto): ResultModel {
    const prependText = dto.discipline.hasDistance
      ? $localize`Distance: ${dto.distance}`
      : "";

    const appendText = dto.discipline.unit.name;

    const displayValue = dto.value / dto.discipline.unit.factor;

    return new ResultModel(
      dto.id,
      prependText,
      appendText,
      displayValue.toString(),
      dto.points,
      dto.discipline.unit,
    );
  }

  toResultElement(): ResultElement {
    const absolutePoints = Math.floor(Number.parseFloat(this.displayValueControl.value) * this.unit.factor);

    return {
      id: this.id,
      value: absolutePoints,
    };
  }
}
