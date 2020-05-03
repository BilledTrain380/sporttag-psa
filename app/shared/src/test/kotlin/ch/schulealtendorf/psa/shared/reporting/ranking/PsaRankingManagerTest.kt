package ch.schulealtendorf.psa.shared.reporting.ranking

import ch.schulealtendorf.psa.dto.group.SimpleGroupDto
import ch.schulealtendorf.psa.dto.participation.BirthdayDto
import ch.schulealtendorf.psa.dto.participation.CompetitorDto
import ch.schulealtendorf.psa.dto.participation.GenderDto
import ch.schulealtendorf.psa.dto.participation.TownDto
import ch.schulealtendorf.psa.dto.participation.athletics.BALLWURF
import ch.schulealtendorf.psa.dto.participation.athletics.BALLZIELWURF
import ch.schulealtendorf.psa.dto.participation.athletics.DisciplineDto
import ch.schulealtendorf.psa.dto.participation.athletics.ResultDto
import ch.schulealtendorf.psa.dto.participation.athletics.SCHNELLLAUF
import ch.schulealtendorf.psa.dto.participation.athletics.SEILSPRINGEN
import ch.schulealtendorf.psa.dto.participation.athletics.UnitDto
import ch.schulealtendorf.psa.dto.participation.athletics.WEITSPRUNG
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag("unit-test")
internal class PsaRankingManagerTest {
    private lateinit var rankingManager: PsaRankingManager

    companion object {
        private const val FIRST_RANK = "1. rank"
        private const val SECOND_RANK = "2. rank"
        private const val THIRD_RANK = "3. rank"
    }

    @BeforeEach
    internal fun beforeEach() {
        rankingManager = PsaRankingManager()
    }

    @Nested
    internal inner class DisciplineGroupRanking {
        private val firstRankWithResults = competitorDtoOf(surname = FIRST_RANK, results = createHighestResult())
        private val thirdRankWithResults = competitorDtoOf(surname = THIRD_RANK, results = createLowestResults())

        @Test
        internal fun createRanking() {
            val competitors = listOf(
                thirdRankWithResults.copy(),
                competitorDtoOf(surname = FIRST_RANK, results = createHighestResult()),
                competitorDtoOf(surname = SECOND_RANK, results = createMiddleResults())
            )

            val ranking = rankingManager.createDisciplineGroupRanking(competitors)

            val expected = listOf(FIRST_RANK, SECOND_RANK, THIRD_RANK)
            assertThat(ranking.map { it.surname }).isEqualTo(expected)
        }

        @Test
        internal fun createRankingWhenSamePoints() {
            val competitors = listOf(
                thirdRankWithResults.copy(),
                firstRankWithResults.copy(),
                firstRankWithResults.copy(),
                firstRankWithResults.copy()
            )

            val ranking = rankingManager.createDisciplineGroupRanking(competitors)

            assertThat(ranking[0].rank).isEqualTo(1)
            assertThat(ranking[1].rank).isEqualTo(1)
            assertThat(ranking[2].rank).isEqualTo(1)
            assertThat(ranking[3].rank).isEqualTo(4)
        }

        private fun createLowestResults(): List<ResultDto> {
            return listOf(
                schnelllaufWithPoints(100),
                ballwurfWithPoints(100),
                weitsprungWithPoints(100)
            )
        }

        private fun createMiddleResults(): List<ResultDto> {
            return listOf(
                schnelllaufWithPoints(200),
                ballwurfWithPoints(200),
                weitsprungWithPoints(200)
            )
        }

        private fun createHighestResult(): List<ResultDto> {
            return listOf(
                schnelllaufWithPoints(300),
                ballwurfWithPoints(300),
                weitsprungWithPoints(300)
            )
        }
    }

    @Nested
    internal inner class DisciplineRanking {

        @Test
        internal fun createRanking() {
            val competitors = listOf(
                competitorDtoOf(surname = THIRD_RANK, results = listOf(ballzielwurfWithPoints(100))),
                competitorDtoOf(surname = FIRST_RANK, results = listOf(ballzielwurfWithPoints(300))),
                competitorDtoOf(surname = SECOND_RANK, results = listOf(ballzielwurfWithPoints(200)))
            )

            val discipline = DisciplineDto(
                name = BALLZIELWURF,
                unit = UnitDto(name = "", factor = 0),
                hasDistance = false,
                hasTrials = false
            )

            val ranking = rankingManager.createDisciplineRanking(competitors, discipline)

            val expected = listOf(FIRST_RANK, SECOND_RANK, THIRD_RANK)
            assertThat(ranking.map { it.surname }).isEqualTo(expected)
        }

        @Test
        internal fun createRankingWhenSamePoints() {
            val firstRank = competitorDtoOf(
                surname = FIRST_RANK,
                results = listOf(ballzielwurfWithPoints(100))
            )

            val competitors = listOf(
                competitorDtoOf(surname = "3. Rank", results = listOf(ballzielwurfWithPoints(50))),
                firstRank.copy(),
                firstRank.copy(),
                firstRank.copy()
            )

            val discipline = DisciplineDto(
                name = BALLZIELWURF,
                unit = UnitDto(name = "", factor = 0),
                hasDistance = false,
                hasTrials = false
            )

            val ranking = rankingManager.createDisciplineRanking(competitors, discipline)

            assertThat(ranking[0].rank).isEqualTo(1)
            assertThat(ranking[1].rank).isEqualTo(1)
            assertThat(ranking[2].rank).isEqualTo(1)
            assertThat(ranking[3].rank).isEqualTo(4)
        }
    }

    @Nested
    internal inner class TotalRanking {

        @Test
        internal fun createRanking() {
            val weitsprung = weitsprungWithPoints(100)
            val seilspringen = seilspringenWithPoints(300)

            val competitors = listOf(
                competitorDtoOf(
                    surname = THIRD_RANK, results = listOf(
                        weitsprung.copy(),
                        ballwurfWithPoints(100),
                        seilspringenWithPoints(100)
                    )
                ),
                competitorDtoOf(
                    surname = FIRST_RANK, results = listOf(
                        weitsprung.copy(),
                        ballwurfWithPoints(200),
                        seilspringen.copy()
                    )
                ),
                competitorDtoOf(
                    surname = SECOND_RANK, results = listOf(
                        weitsprung.copy(),
                        ballwurfWithPoints(150),
                        seilspringen.copy()
                    )
                )
            )

            val ranking = rankingManager.createTotalRanking(competitors)

            assertThat(ranking[0].total).isEqualTo(500)
            assertThat(ranking[0].deletedResult).isEqualTo(100)

            val expected = listOf(FIRST_RANK, SECOND_RANK, THIRD_RANK)
            assertThat(ranking.map { it.surname }).isEqualTo(expected)
        }

        @Test
        internal fun createRankingWhenSamePoints() {
            val firstRankResults = listOf(
                weitsprungWithPoints(100),
                ballwurfWithPoints(200),
                seilspringenWithPoints(300)
            )

            val firstRank = competitorDtoOf(surname = FIRST_RANK, results = firstRankResults)

            val competitors = listOf(
                competitorDtoOf(
                    surname = THIRD_RANK, results = listOf(
                        weitsprungWithPoints(100),
                        ballwurfWithPoints(100),
                        seilspringenWithPoints(100)
                    )
                ),
                firstRank.copy(),
                firstRank.copy(),
                firstRank.copy()
            )

            val ranking = rankingManager.createTotalRanking(competitors)

            assertThat(ranking[0].rank).isEqualTo(1)
            assertThat(ranking[1].rank).isEqualTo(1)
            assertThat(ranking[2].rank).isEqualTo(1)
            assertThat(ranking[3].rank).isEqualTo(4)
        }
    }

    private fun competitorDtoOf(
        id: Int = 1,
        startNumber: Int = 1,
        surname: String = "",
        prename: String = "",
        gender: GenderDto = GenderDto.MALE,
        birthday: BirthdayDto = BirthdayDto.ofMillis(0),
        absent: Boolean = false,
        address: String = "",
        zip: String = "",
        town: String = "",
        group: String = "",
        results: List<ResultDto> = listOf()
    ): CompetitorDto {
        val resultMap = results
            .map { it.discipline.name to it }
            .toMap()

        return CompetitorDto(
            startnumber = startNumber,
            results = resultMap,
            id = id,
            surname = surname,
            prename = prename,
            gender = gender,
            birthday = birthday,
            isAbsent = absent,
            address = address,
            town = TownDto(
                zip = zip,
                name = town
            ),
            group = SimpleGroupDto(
                name = group,
                coach = ""
            )
        )
    }

    private fun schnelllaufWithPoints(points: Int): ResultDto {
        return resultDtoOf(points = points, discipline = SCHNELLLAUF)
    }

    private fun ballwurfWithPoints(points: Int): ResultDto {
        return resultDtoOf(points = points, discipline = BALLWURF)
    }

    private fun weitsprungWithPoints(points: Int): ResultDto {
        return resultDtoOf(points = points, discipline = WEITSPRUNG)
    }

    private fun ballzielwurfWithPoints(points: Int): ResultDto {
        return resultDtoOf(points = points, discipline = BALLZIELWURF)
    }

    private fun seilspringenWithPoints(points: Int): ResultDto {
        return resultDtoOf(points = points, discipline = SEILSPRINGEN)
    }

    private fun resultDtoOf(
        id: Int = 1,
        value: Long = 0,
        points: Int = 0,
        discipline: String = ""
    ): ResultDto {
        return ResultDto(
            id = id,
            value = value,
            points = points,
            distance = "",
            discipline = DisciplineDto(
                name = discipline,
                unit = UnitDto(name = "", factor = 0),
                hasTrials = false,
                hasDistance = false
            )
        )
    }
}
