package ch.schulealtendorf.sporttagpsa.lib

import ch.schulealtendorf.psa.dto.BirthdayDto
import ch.schulealtendorf.sporttagpsa.entity.CompetitorEntity
import ch.schulealtendorf.sporttagpsa.entity.ParticipantEntity

fun ageOf(entity: ParticipantEntity): Int = BirthdayDto.ofMillis(entity.birthday).age

fun ageOf(entity: CompetitorEntity): Int = BirthdayDto.ofMillis(entity.participant.birthday).age
