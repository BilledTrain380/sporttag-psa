package ch.schulealtendorf.psa.core.setup

import ch.schulealtendorf.psa.core.user.USER_ADMIN
import ch.schulealtendorf.psa.core.user.UserManagerImpl
import ch.schulealtendorf.psa.core.user.validation.PSAPasswordValidator
import ch.schulealtendorf.psa.setup.SetupRepository
import ch.schulealtendorf.psa.setup.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ActiveProfiles("test")
@Tag("db-test")
@ExtendWith(SpringExtension::class)
@Import(
    StatefulSetupManager::class,
    UserManagerImpl::class,
    PSAPasswordValidator::class
)
@DataJpaTest(properties = ["spring.flyway.locations=classpath:/db/migration"])
@FlywayTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
internal class StatefulSetupManagerTest {

    @Autowired
    private lateinit var setupManager: StatefulSetupManager

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var setupRepository: SetupRepository

    @Test
    internal fun initialize() {
        val setup = SetupInformation("gibbiX12345$")
        setupManager.initialize(setup)

        val setupEntity = setupRepository.getSetup()
        assertThat(setupEntity.initialized).isTrue
        assertThat(setupEntity.jwtSecret).isNotEmpty

        assertThat(setupManager.jwtSecret).isEqualTo(setupEntity.jwtSecret)
        assertThat(setupManager.isInitialized).isTrue

        val admin = userRepository.findByUsername(USER_ADMIN)
        assertThat(admin).isNotEmpty

        val isPasswordMatch = BCryptPasswordEncoder(4)
            .matches(setup.adminPassword, admin.get().password)
        assertThat(isPasswordMatch).isTrue
    }

    @Test
    internal fun initializeWhenAlreadyInitialized() {
        val setup = SetupInformation("gibbiX12345$")
        setupManager.initialize(setup)

        val exception = assertThrows<IllegalStateException> {
            setupManager.initialize(setup)
        }
        assertThat(exception).hasMessage("Setup is already initialized")
    }
}
