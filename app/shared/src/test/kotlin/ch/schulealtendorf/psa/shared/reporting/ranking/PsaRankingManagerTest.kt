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

    @BeforeEach
    internal fun beforeEach() {
        rankingManager = PsaRankingManager()
    }

    @Nested
    internal inner class DisciplineGroupRanking {

        @Test
        internal fun createRanking() {
            val competitors = listOf(
                competitorDtoOf(
                    surname = "3. rank", results = listOf(
                        resultDtoOf(points = 100, discipline = SCHNELLLAUF),
                        resultDtoOf(points = 100, discipline = BALLWURF),
                        resultDtoOf(points = 100, discipline = WEITSPRUNG)
                    )
                ),
                competitorDtoOf(
                    surname = "1. rank", results = listOf(
                        resultDtoOf(points = 300, discipline = SCHNELLLAUF),
                        resultDtoOf(points = 300, discipline = BALLWURF),
                        resultDtoOf(points = 300, discipline = WEITSPRUNG)
                    )
                ),
                competitorDtoOf(
                    surname = "2. rank", results = listOf(
                        resultDtoOf(points = 200, discipline = SCHNELLLAUF),
                        resultDtoOf(points = 200, discipline = BALLWURF),
                        resultDtoOf(points = 200, discipline = WEITSPRUNG)
                    )
                )
            )

            val ranking = rankingManager.createDisciplineGroupRanking(competitors)

            val expected = listOf("1. rank", "2. rank", "3. rank")
            assertThat(ranking.map { it.surname }).isEqualTo(expected)
        }

        @Test
        internal fun createRankingWhenSamePoints() {
            val competitors = listOf(
                competitorDtoOf(
                    surname = "3. rank", results = listOf(
                        resultDtoOf(points = 100, discipline = SCHNELLLAUF),
                        resultDtoOf(points = 100, discipline = BALLWURF),
                        resultDtoOf(points = 100, discipline = WEITSPRUNG)
                    )
                ),
                competitorDtoOf(
                    surname = "1. rank", results = listOf(
                        resultDtoOf(points = 300, discipline = SCHNELLLAUF),
                        resultDtoOf(points = 300, discipline = BALLWURF),
                        resultDtoOf(points = 300, discipline = WEITSPRUNG)
                    )
                ),
                competitorDtoOf(
                    surname = "1. rank", results = listOf(
                        resultDtoOf(points = 300, discipline = SCHNELLLAUF),
                        resultDtoOf(points = 300, discipline = BALLWURF),
                        resultDtoOf(points = 300, discipline = WEITSPRUNG)
                    )
                ),
                competitorDtoOf(
                    surname = "1. rank", results = listOf(
                        resultDtoOf(points = 300, discipline = SCHNELLLAUF),
                        resultDtoOf(points = 300, discipline = BALLWURF),
                        resultDtoOf(points = 300, discipline = WEITSPRUNG)
                    )
                )
            )

            val ranking = rankingManager.createDisciplineGroupRanking(competitors)

            assertThat(ranking[0].rank).isEqualTo(1)
            assertThat(ranking[1].rank).isEqualTo(1)
            assertThat(ranking[2].rank).isEqualTo(1)
            assertThat(ranking[3].rank).isEqualTo(4)
        }
    }

    @Nested
    internal inner class DisciplineRanking {

        @Test
        internal fun createRanking() {
            val competitors = listOf(
                competitorDtoOf(
                    surname = "3. rank", results = listOf(
                        resultDtoOf(points = 100, discipline = BALLZIELWURF)
                    )
                ),
                competitorDtoOf(
                    surname = "1. rank", results = listOf(
                        resultDtoOf(points = 300, discipline = BALLZIELWURF)
                    )
                ),
                competitorDtoOf(
                    surname = "2. rank", results = listOf(
                        resultDtoOf(points = 200, discipline = BALLZIELWURF)
                    )
                )
            )

            val discipline = DisciplineDto(
                name = BALLZIELWURF,
                unit = UnitDto(name = "", factor = 0),
                hasDistance = false,
                hasTrials = false
            )

            val ranking = rankingManager.createDisciplineRanking(competitors, discipline)

            val expected = listOf("1. rank", "2. rank", "3. rank")
            assertThat(ranking.map { it.surname }).isEqualTo(expected)
        }

        @Test
        internal fun createRankingWhenSamePoints() {
            val competitors = listOf(
                competitorDtoOf(
                    surname = "3. Rank", results = listOf(
                        resultDtoOf(points = 50, discipline = BALLZIELWURF)
                    )
                ),
                competitorDtoOf(
                    surname = "1. rank", results = listOf(
                        resultDtoOf(points = 100, discipline = BALLZIELWURF)
                    )
                ),
                competitorDtoOf(
                    surname = "1. rank", results = listOf(
                        resultDtoOf(points = 100, discipline = BALLZIELWURF)
                    )
                ),
                competitorDtoOf(
                    surname = "1. rank", results = listOf(
                        resultDtoOf(points = 100, discipline = BALLZIELWURF)
                    )
                )
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
            val competitors = listOf(
                competitorDtoOf(
                    surname = "3. rank", results = listOf(
                        resultDtoOf(points = 100, discipline = WEITSPRUNG),
                        resultDtoOf(points = 100, discipline = BALLWURF),
                        resultDtoOf(points = 100, discipline = SEILSPRINGEN)
                    )
                ),
                competitorDtoOf(
                    surname = "1. rank", results = listOf(
                        resultDtoOf(points = 100, discipline = WEITSPRUNG),
                        resultDtoOf(points = 200, discipline = BALLWURF),
                        resultDtoOf(points = 300, discipline = SEILSPRINGEN)
                    )
                ),
                competitorDtoOf(
                    surname = "2. rank", results = listOf(
                        resultDtoOf(points = 100, discipline = WEITSPRUNG),
                        resultDtoOf(points = 150, discipline = BALLWURF),
                        resultDtoOf(points = 300, discipline = SEILSPRINGEN)
                    )
                )
            )

            val ranking = rankingManager.createTotalRanking(competitors)

            assertThat(ranking[0].total).isEqualTo(500)
            assertThat(ranking[0].deletedResult).isEqualTo(100)

            val expected = listOf("1. rank", "2. rank", "3. rank")
            assertThat(ranking.map { it.surname }).isEqualTo(expected)
        }

        @Test
        internal fun createRankingWhenSamePoints() {
            val competitors = listOf(
                competitorDtoOf(
                    surname = "3. rank", results = listOf(
                        resultDtoOf(points = 100, discipline = WEITSPRUNG),
                        resultDtoOf(points = 100, discipline = BALLWURF),
                        resultDtoOf(points = 100, discipline = SEILSPRINGEN)
                    )
                ),
                competitorDtoOf(
                    surname = "1. rank", results = listOf(
                        resultDtoOf(points = 200, discipline = WEITSPRUNG),
                        resultDtoOf(points = 100, discipline = BALLWURF),
                        resultDtoOf(points = 300, discipline = SEILSPRINGEN)
                    )
                ),
                competitorDtoOf(
                    surname = "1. rank", results = listOf(
                        resultDtoOf(points = 100, discipline = WEITSPRUNG),
                        resultDtoOf(points = 200, discipline = BALLWURF),
                        resultDtoOf(points = 300, discipline = SEILSPRINGEN)
                    )
                ),
                competitorDtoOf(
                    surname = "1. rank", results = listOf(
                        resultDtoOf(points = 100, discipline = WEITSPRUNG),
                        resultDtoOf(points = 200, discipline = BALLWURF),
                        resultDtoOf(points = 300, discipline = SEILSPRINGEN)
                    )
                )
            )

            val ranking = rankingManager.createTotalRanking(competitors)

            assertThat(ranking[0].rank).isEqualTo(1)
            assertThat(ranking[1].rank).isEqualTo(1)
            assertThat(ranking[2].rank).isEqualTo(1)
            assertThat(ranking[3].rank).isEqualTo(4)
        }
    }

    internal fun competitorDtoOf(
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

    internal fun resultDtoOf(
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
