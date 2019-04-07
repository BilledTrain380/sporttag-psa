/*
 * Copyright (c) 2019 by Nicolas Märchy
 *
 * This file is part of Sporttag PSA.
 *
 * Sporttag PSA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sporttag PSA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sporttag PSA.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Diese Datei ist Teil von Sporttag PSA.
 *
 * Sporttag PSA ist Freie Software: Sie können es unter den Bedingungen
 * der GNU General Public License, wie von der Free Software Foundation,
 * Version 3 der Lizenz oder (nach Ihrer Wahl) jeder späteren
 * veröffentlichten Version, weiterverbreiten und/oder modifizieren.
 *
 * Sporttag PSA wird in der Hoffnung, dass es nützlich sein wird, aber
 * OHNE JEDE GEWÄHRLEISTUNG, bereitgestellt; sogar ohne die implizite
 * Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
 * Siehe die GNU General Public License für weitere Details.
 *
 * Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
 * Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 *
 *
 */

package ch.schulealtendorf.sporttagpsa

import ch.schulealtendorf.psa.dto.BirthdayDto
import ch.schulealtendorf.psa.dto.CoachDto
import ch.schulealtendorf.psa.dto.CompetitorDto
import ch.schulealtendorf.psa.dto.DisciplineDto
import ch.schulealtendorf.psa.dto.GroupDto
import ch.schulealtendorf.psa.dto.ParticipantDto
import ch.schulealtendorf.psa.dto.ResultDto
import ch.schulealtendorf.psa.dto.SportDto
import ch.schulealtendorf.psa.dto.TownDto
import ch.schulealtendorf.psa.dto.UnitDto
import ch.schulealtendorf.psa.dto.UserDto
import ch.schulealtendorf.sporttagpsa.entity.CoachEntity
import ch.schulealtendorf.sporttagpsa.entity.CompetitorEntity
import ch.schulealtendorf.sporttagpsa.entity.DisciplineEntity
import ch.schulealtendorf.sporttagpsa.entity.GroupEntity
import ch.schulealtendorf.sporttagpsa.entity.ParticipantEntity
import ch.schulealtendorf.sporttagpsa.entity.ResultEntity
import ch.schulealtendorf.sporttagpsa.entity.SportEntity
import ch.schulealtendorf.sporttagpsa.entity.TownEntity
import ch.schulealtendorf.sporttagpsa.entity.UnitEntity
import ch.schulealtendorf.sporttagpsa.entity.UserEntity

infix fun UserDto.Companion.from(entity: UserEntity): UserDto {
    return UserDto(entity.id ?: 0, entity.username, entity.authorities.map { it.role }, entity.enabled)
}

infix fun CoachDto.Companion.from(entity: CoachEntity): CoachDto {
    return CoachDto(entity.id ?: 0, entity.name)
}

infix fun GroupDto.Companion.from(entity: GroupEntity): GroupDto {
    return GroupDto(entity.name, CoachDto from entity.coach)
}

infix fun TownDto.Companion.from(entity: TownEntity): TownDto {
    return TownDto(entity.zip, entity.name)
}

infix fun SportDto.Companion.from(entity: SportEntity?): SportDto? {
    if (entity == null) return null
    return SportDto(entity.name)
}

infix fun ParticipantDto.Companion.from(entity: ParticipantEntity): ParticipantDto {
    return ParticipantDto(
            entity.id ?: 0,
            entity.surname,
            entity.prename,
            entity.gender,
            BirthdayDto(entity.birthday),
            entity.absent,
            entity.address,
            TownDto from entity.town,
            GroupDto from entity.group,
            SportDto from entity.sport)
}

infix fun UnitDto.Companion.from(entity: UnitEntity): UnitDto {
    return UnitDto(entity.name, entity.factor)
}

infix fun DisciplineDto.Companion.from(entity: DisciplineEntity): DisciplineDto {
    return DisciplineDto(entity.name, UnitDto from entity.unit)
}

infix fun ResultDto.Companion.from(entity: ResultEntity): ResultDto {
    return ResultDto(
            entity.id ?: 0,
            entity.value,
            entity.points,
            DisciplineDto from entity.discipline,
            entity.distance)
}

infix fun CompetitorDto.Companion.from(entity: CompetitorEntity): CompetitorDto {
    return CompetitorDto(
            entity.participant.id ?: 0,
            entity.startnumber!!,
            entity.participant.surname,
            entity.participant.prename,
            entity.participant.gender,
            BirthdayDto(entity.participant.birthday),
            entity.participant.absent,
            entity.participant.address,
            TownDto from entity.participant.town,
            GroupDto from entity.participant.group,
            entity.results.map { ResultDto from it })
}