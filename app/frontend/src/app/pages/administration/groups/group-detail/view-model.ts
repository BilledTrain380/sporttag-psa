import { ParticipantDto, translateGender } from "../../../../dto/participation";
import { ActiveGroup } from "../../../../store/group/group.reducer";

export class GroupViewModel {
  private static readonly GROUP_PREFIX = $localize`Group `;

  private constructor(
    readonly name: string,
    readonly coach: string,
    readonly participants: ReadonlyArray<ParticipantModel>,
  ) {
  }

  static fromState(activeGroup: ActiveGroup): GroupViewModel {
    const participantModels = activeGroup.participants
      .map(dto => ParticipantModel.fromDto(dto));

    return new GroupViewModel(
      GroupViewModel.GROUP_PREFIX + activeGroup.group.name,
      activeGroup.group.coach,
      participantModels,
    );
  }

  static empty(): GroupViewModel {
    return new GroupViewModel("UNKNOWN GROUP", "", []);
  }
}

export class ParticipantModel {
  private constructor(
    readonly id: number,
    readonly surname: string,
    readonly prename: string,
    readonly gender: string,
    public isAbsent: boolean,
    readonly address: string,
    readonly sportType?: string,
  ) {
  }

  static fromDto(dto: ParticipantDto): ParticipantModel {
    return new ParticipantModel(
      dto.id,
      dto.surname,
      dto.prename,
      translateGender(dto.gender),
      dto.isAbsent,
      dto.address,
      dto.sportType,
    );
  }
}
