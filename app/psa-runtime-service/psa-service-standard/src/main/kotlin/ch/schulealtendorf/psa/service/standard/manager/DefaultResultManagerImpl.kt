package ch.schulealtendorf.psa.service.standard.manager

import ch.schulealtendorf.psa.service.standard.ageOf
import ch.schulealtendorf.psa.service.standard.entity.CompetitorEntity
import ch.schulealtendorf.psa.service.standard.entity.ParticipantEntity
import ch.schulealtendorf.psa.service.standard.entity.ResultEntity
import ch.schulealtendorf.psa.service.standard.repository.CompetitorRepository
import ch.schulealtendorf.psa.service.standard.repository.DisciplineRepository
import ch.schulealtendorf.psa.shared.rulebook.CategoryModel
import ch.schulealtendorf.psa.shared.rulebook.CategoryRuleBook
import org.springframework.stereotype.Component

@Component
class DefaultResultManagerImpl(
    private val competitorRepository: CompetitorRepository,
    private val disciplineRepository: DisciplineRepository,
    private val categoryRuleBook: CategoryRuleBook
) : DefaultResultManager {
    override fun saveAsCompetitor(participant: ParticipantEntity) {
        val competitor = competitorRepository.save(CompetitorEntity(participant = participant))
        competitor.createDefaultResults()
        competitorRepository.save(competitor)
    }

    private fun CompetitorEntity.createDefaultResults() {
        val disciplines = disciplineRepository.findAll()

        this.results = disciplines.map { discipline ->
            ResultEntity(
                distance = categoryRuleBook.getDistance(CategoryModel(ageOf(this), discipline.name)),
                discipline = discipline,
                value = 1.toLong() * discipline.unit.factor
            ).also {
                it.competitor = this
            }
        }.toSet()
    }

    override fun deleteAsCompetitor(participant: ParticipantEntity) {
        val competitor = competitorRepository.findByParticipantId(participant.id ?: 0)

        competitor.ifPresent {
            competitorRepository.delete(it)
        }
    }
}