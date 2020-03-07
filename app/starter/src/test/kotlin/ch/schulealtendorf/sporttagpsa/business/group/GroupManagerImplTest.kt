package ch.schulealtendorf.sporttagpsa.business.group

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
    internal fun testSomething() {

        assertThat(groupManager.getGroups()).isEmpty()
    }
}