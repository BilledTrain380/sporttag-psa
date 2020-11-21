package ch.schulealtendorf.psa.service.standard.manager

import ch.schulealtendorf.psa.service.standard.entity.ParticipantEntity

interface DefaultResultManager {
    fun saveAsCompetitor(participant: ParticipantEntity)

    fun deleteAsCompetitor(participant: ParticipantEntity)
}
