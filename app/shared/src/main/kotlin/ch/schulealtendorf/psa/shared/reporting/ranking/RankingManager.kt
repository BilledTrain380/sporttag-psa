package ch.schulealtendorf.psa.shared.reporting.ranking

import ch.schulealtendorf.psa.dto.participation.CompetitorDto
import ch.schulealtendorf.psa.dto.participation.athletics.DisciplineDto

/**
 * Describes a manager to create various rankings.
 */
interface RankingManager {

    /**
     * Creates the discipline ranking for the given [competitors] with the given [discipline].
     *
     * @param competitors the competitors to create the ranking for
     * @param discipline the discipline to create the ranking
     *
     * @return a list containing the disclipline ranking data
     */
    fun createDisciplineRanking(
        competitors: Collection<CompetitorDto>,
        discipline: DisciplineDto
    ): List<DisciplineRankingDataSet>

    /**
     * Creates the discipline group ranking for the given [competitors].
     *
     * @param competitors the competitors to create the ranking for
     *
     * @return a list containing the discipline group ranking data
     */
    fun createDisciplineGroupRanking(competitors: Collection<CompetitorDto>): List<DisciplineGroupRankingDataSet>

    /**
     * Creates the total ranking for the given [competitors].
     *
     * @param competitors the competitors to create the ranking for
     *
     * @return a list containing the total ranking data
     */
    fun createTotalRanking(competitors: Collection<CompetitorDto>): List<TotalRankingDataSet>
}