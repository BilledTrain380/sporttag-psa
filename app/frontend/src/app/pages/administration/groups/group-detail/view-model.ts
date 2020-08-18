import { SimpleGroupDto } from "../../../../dto/group";
import { ParticipantDto, translateGender } from "../../../../dto/participation";

export class GroupViewModel {
  private static readonly GROUP_PREFIX = $localize`Group `;

  private constructor(
    readonly name: string,
    readonly coach: string,
  ) {
  }

  static fromState(activeGroup: SimpleGroupDto): GroupViewModel {
    return new GroupViewModel(
      GroupViewModel.GROUP_PREFIX + activeGroup.name,
      activeGroup.coach,
    );
  }

  static empty(): GroupViewModel {
    return new GroupViewModel("UNKNOWN GROUP", "");
  }
}

export class ParticipantModel {
  get fullName(): string {
    return `${this.surname} ${this.prename}`;
  }

  private constructor(
    readonly id: number,
    readonly surname: string,
    readonly prename: string,
    readonly gender: string,
    public isAbsent: boolean,
    readonly address: string,
    public sportType?: string,
  ) {
  }

  static fromDto(dto: ParticipantDto): ParticipantModel {
    return new ParticipantModel(
      dto.id,
      dto.surname,
      dto.prename,
      translateGender(dto.gender),
      dto.absent,
      dto.address,
      dto.sportType ?? "",
    );
  }

  compareTo(other: ParticipantModel): number {
    if (this.fullName.localeCompare(other.fullName) > 0) {
      return 1;
    }

    if (this.fullName.localeCompare(other.fullName) < 0) {
      return -1;
    }

    return 0;
  }
}
