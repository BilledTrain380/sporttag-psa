package ch.schulealtendorf.sporttagpsa.lib

import ch.schulealtendorf.psa.dto.group.SimpleGroupDto
import ch.schulealtendorf.psa.dto.participation.BirthdayDto
import ch.schulealtendorf.psa.dto.participation.CompetitorDto
import ch.schulealtendorf.psa.dto.participation.ParticipantDto
import ch.schulealtendorf.psa.dto.participation.TownDto
import ch.schulealtendorf.psa.dto.participation.athletics.DisciplineDto
import ch.schulealtendorf.psa.dto.participation.athletics.ResultDto
import ch.schulealtendorf.psa.dto.participation.athletics.UnitDto
import ch.schulealtendorf.psa.dto.user.UserDto
import ch.schulealtendorf.sporttagpsa.entity.CompetitorEntity
import ch.schulealtendorf.sporttagpsa.entity.DisciplineEntity
import ch.schulealtendorf.sporttagpsa.entity.GroupEntity
import ch.schulealtendorf.sporttagpsa.entity.ParticipantEntity
import ch.schulealtendorf.sporttagpsa.entity.ResultEntity
import ch.schulealtendorf.sporttagpsa.entity.TownEntity
import ch.schulealtendorf.sporttagpsa.entity.UnitEntity
import ch.schulealtendorf.sporttagpsa.entity.UserEntity

fun ageOf(entity: CompetitorEntity): Int = BirthdayDto.ofDate(entity.participant.birthday).age

fun participantDtoOf(participantEntity: ParticipantEntity): ParticipantDto {
    return ParticipantDto(
        id = participantEntity.id!!,
        surname = participantEntity.surname,
        prename = participantEntity.prename,
        gender = participantEntity.gender,
        birthday = BirthdayDto.ofDate(participantEntity.birthday),
        isAbsent = participantEntity.absent,
        address = participantEntity.address,
        town = townDtoOf(participantEntity.town),
        group = simpleGroupDtoOf(participantEntity.group),
        sportType = participantEntity.sport?.name
    )
}

fun townDtoOf(townEntity: TownEntity): TownDto {
    return TownDto(
        zip = townEntity.zip,
        name = townEntity.name
    )
}

fun simpleGroupDtoOf(groupEntity: GroupEntity): SimpleGroupDto {
    return SimpleGroupDto(
        name = groupEntity.name,
        coach = groupEntity.coach.name
    )
}

fun competitorDtoOf(competitorEntity: CompetitorEntity): CompetitorDto {
    val results = competitorEntity.results
        .map { it.discipline.name to resultDtoOf(it) }
        .toMap()

    return CompetitorDto(
        startnumber = competitorEntity.startnumber!!,
        id = competitorEntity.participant.id!!,
        surname = competitorEntity.participant.surname,
        prename = competitorEntity.participant.prename,
        gender = competitorEntity.participant.gender,
        birthday = BirthdayDto.ofDate(competitorEntity.participant.birthday),
        isAbsent = competitorEntity.participant.absent,
        address = competitorEntity.participant.address,
        town = townDtoOf(competitorEntity.participant.town),
        group = simpleGroupDtoOf(competitorEntity.participant.group),
        results = results
    )
}

fun resultDtoOf(resultEntity: ResultEntity): ResultDto {
    return ResultDto(
        id = resultEntity.id!!,
        value = resultEntity.value,
        points = resultEntity.points,
        discipline = disciplineDtoOf(resultEntity.discipline),
        distance = resultEntity.distance
    )
}

fun disciplineDtoOf(disciplineEntity: DisciplineEntity): DisciplineDto {
    return DisciplineDto(
        name = disciplineEntity.name,
        hasDistance = disciplineEntity.hasDistance,
        hasTrials = disciplineEntity.hasTrials,
        unit = unitDtoOf(disciplineEntity.unit)
    )
}

fun unitDtoOf(unitEntity: UnitEntity): UnitDto {
    return UnitDto(
        name = unitEntity.name,
        factor = unitEntity.factor
    )
}

fun userDtoOf(userEntity: UserEntity): UserDto {
    return UserDto(
        id = userEntity.id!!,
        username = userEntity.username,
        authorities = userEntity.authorities.map { it.role },
        enabled = userEntity.enabled
    )
}
