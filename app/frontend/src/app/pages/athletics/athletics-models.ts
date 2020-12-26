import { FormControl, Validators } from "@angular/forms";

import { hashcodeOf, requireNonNullOrUndefined } from "../../@core/lib/lib";
import { CompetitorDto, ResultDto, ResultElement, UnitDto } from "../../dto/athletics";
import { GenderDto } from "../../dto/participation";
import { floatNumberValidator, intNumberValidator } from "../../modules/common-forms/validatiors/number-validators";

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

  hashcode(): number {
    const prime = 31;
    let result = 1;
    result = prime * result + this.id;
    result = prime * result + this.startnumber;
    result = prime * result + hashcodeOf(this.surname);
    result = prime * result + hashcodeOf(this.prename);
    result = prime * result + hashcodeOf(this.gender);
    result = prime * result + (this.isAbsent ? 1 : 0);
    result = prime * result + this.result.hashcode();

    return result;
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
      validators.push(floatNumberValidator());
    } else {
      validators.push(intNumberValidator());
    }

    this.displayValueControl = new FormControl(displayValue, validators);
  }

  static fromDto(dto: ResultDto): ResultModel {
    const prependText = dto.discipline.hasDistance
      // tslint:disable-next-line
      ? $localize`Distance: ` + dto.distance
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

  hashcode(): number {
    const prime = 31;
    let result = 1;
    result = prime * result + this.id;
    result = prime * result + hashcodeOf(this.prependText);
    result = prime * result + hashcodeOf(this.appendText);
    result = prime * result + this.points;
    result = prime * result + hashcodeOf(this.unit.name);
    result = prime * result + this.unit.factor;

    return result;
  }
}
