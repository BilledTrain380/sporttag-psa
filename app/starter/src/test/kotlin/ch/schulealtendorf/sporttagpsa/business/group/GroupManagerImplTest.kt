package ch.schulealtendorf.sporttagpsa.business.group

import ch.schulealtendorf.psa.dto.group.GroupStatusType
import ch.schulealtendorf.psa.dto.status.StatusSeverity
import org.assertj.core.api.Assertions.assertThat
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ActiveProfiles("test")
@Tag("db-test")
@ExtendWith(SpringExtension::class)
@Import(GroupManagerImpl::class)
@DataJpaTest
@FlywayTest
internal class GroupManagerImplTest {

    @Autowired
    lateinit var groupManager: GroupManagerImpl

    @Test
    internal fun hasPendingParticipation() {
        val pendingParticipationGroup = groupManager.getGroup("2a")
        val finishedParticipationGroup = groupManager.getGroup("2b")

        assertThat(pendingParticipationGroup).isPresent
        assertThat(finishedParticipationGroup).isPresent

        assertThat(groupManager.hasPendingParticipation(pendingParticipationGroup.get()))
            .withFailMessage("Expected group ${pendingParticipationGroup.get().name} to have pending participation")
            .isTrue()

        assertThat(groupManager.hasPendingParticipation(finishedParticipationGroup.get()))
            .withFailMessage("Expected group ${finishedParticipationGroup.get().name} to not have pending participation")
            .isFalse()
    }

    @Test
    internal fun isCompetitive() {
        val competitiveGroup = groupManager.getGroup("2a")
        val funGroup = groupManager.getGroup("2b")

        assertThat(groupManager.isCompetitive(competitiveGroup.get()))
            .withFailMessage("Expected group ${competitiveGroup.get().name} to be competitive")
            .isFalse()

        assertThat(groupManager.isCompetitive(funGroup.get()))
            .withFailMessage("Expected group ${funGroup.get().name} to be competitive")
            .isTrue()
    }

    @Test
    internal fun getGroups() {
        val groups = groupManager.getGroups()

        assertThat(groups).hasSize(2)

        val group2a = groups.find { it.name == "2a" }
        assertThat(group2a).isNotNull
        assertThat(group2a?.coach).isEqualTo("Peter Muster")

        val group2b = groups.find { it.name == "2b" }
        assertThat(group2b).isNotNull
        assertThat(group2b?.coach).isEqualTo("Willi Wirbelwind")
    }

    @Test
    internal fun getOverview() {
        val overviewList = groupManager.getOverview()

        assertThat(overviewList).hasSize(2)

        val group2a = overviewList.find { it.group.name == "2a" }
        assertThat(group2a).isNotNull
        assertThat(group2a?.status?.severity).isEqualTo(StatusSeverity.WARNING)

        assertThat(group2a?.status?.entries).hasSize(2)

        val warningEntry = group2a?.status?.entries?.find { it.severity == StatusSeverity.WARNING }
        assertThat(warningEntry).isNotNull
        assertThat(warningEntry?.type?.text).isEqualTo(GroupStatusType.UNFINISHED_PARTICIPANTS.name)

        val infoEntry = group2a?.status?.entries?.find { it.severity == StatusSeverity.INFO }
        assertThat(infoEntry).isNotNull
        assertThat(infoEntry?.type?.text).isEqualTo(GroupStatusType.FUN.name)
    }

    @Test
    internal fun getOverviewByFilter() {
        val overviewList = groupManager.getOverviewBy(GroupStatusType.COMPETITIVE)

        assertThat(overviewList).hasSize(1)

        val overview2b = overviewList[0]
        assertThat(overview2b.group.name).isEqualTo("2b")
        assertThat(overview2b.group.coach).isEqualTo("Willi Wirbelwind")
        assertThat(overview2b.status.severity).isEqualTo(StatusSeverity.OK)

        assertThat(overview2b.status.entries).hasSize(1)

        val status = overview2b.status.entries[0]
        assertThat(status.severity).isEqualTo(StatusSeverity.INFO)
        assertThat(status.type.text).isEqualTo(GroupStatusType.COMPETITIVE.name)
    }
}