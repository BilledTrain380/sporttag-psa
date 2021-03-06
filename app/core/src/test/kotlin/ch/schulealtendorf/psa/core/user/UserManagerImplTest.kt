package ch.schulealtendorf.psa.core.user

import ch.schulealtendorf.psa.core.user.validation.InvalidPasswordException
import ch.schulealtendorf.psa.core.user.validation.PSAPasswordValidator
import ch.schulealtendorf.psa.dto.user.UserDto
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
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit.jupiter.SpringExtension

@ActiveProfiles("test")
@Tag("db-test")
@ExtendWith(SpringExtension::class)
@Import(
    UserManagerImpl::class,
    PSAPasswordValidator::class
)
@DataJpaTest
@FlywayTest
internal class UserManagerImplTest {
    companion object {
        private const val FHANSELER = "fhanseler"
    }

    @Autowired
    private lateinit var userManager: UserManagerImpl

    @Autowired
    private lateinit var userRepository: UserRepository

    private val encoder = BCryptPasswordEncoder(4)

    @Test
    @FlywayTest
    internal fun saveNewUser() {
        val user = UserDto(
            id = 0,
            username = "hmuster",
            enabled = true,
            authorities = listOf("ROLE_USER"),
            password = "gibbiX12345$"
        )

        val result = userManager.save(user)

        assertThat(result.username).isEqualTo(user.username)
        assertThat(result.enabled).isTrue()
        assertThat(result.password).isEqualTo("protected")
        assertThat(result.authorities).containsOnly("ROLE_USER")

        val userEntity = userRepository.findByUsername(user.username)
        assertThat(userEntity).isNotEmpty

        val isPasswordMatch = encoder.matches(user.password, userEntity.get().password)
        assertThat(isPasswordMatch).isTrue()
    }

    @Test
    @FlywayTest
    internal fun saveNewUserWithInvalidPassword() {
        val user = UserDto(
            id = 0,
            username = "hmüller",
            enabled = true,
            authorities = listOf("ROLE_USER"),
            password = "simplepass"
        )

        assertThrows<InvalidPasswordException> {
            userManager.save(user)
        }
    }

    @Test
    @FlywayTest
    @Sql("/db/user/add-user.sql")
    internal fun saveExistingUser() {
        val userOptional = userManager.getOne(FHANSELER)
        assertThat(userOptional).isNotEmpty

        val user = userOptional.get().toBuilder()
            .setEnabled(false)
            .setLocale("de")
            .build()

        val updatedUser = userManager.save(user)
        assertThat(updatedUser.enabled).isFalse

        val userEntity = userRepository.findByUsername(FHANSELER)
        assertThat(userEntity).isNotEmpty
        assertThat(userEntity.get().enabled).isFalse
        assertThat(userEntity.get().locale).isEqualTo("de")
    }

    @Test
    internal fun saveAdminUser() {
        val userOptional = userManager.getOne(USER_ADMIN)
        assertThat(userOptional).isNotEmpty

        val admin = userOptional.get().toBuilder()
            .setUsername("no-longer-admin")
            .setEnabled(false)
            .build()

        val exception = assertThrows<IllegalUserOperationException> {
            userManager.save(admin)
        }
        assertThat(exception).hasMessage("Not allowed to modify admin user")
    }

    @Test
    internal fun saveAdminUserLocaleOnly() {
        val userOptional = userManager.getOne(USER_ADMIN)
        assertThat(userOptional).isNotEmpty

        val admin = userOptional.get().toBuilder()
            .setLocale("de")
            .build()

        val updateAdmin = userManager.save(admin)
        assertThat(updateAdmin.locale).isEqualTo("de")

        val userEntityOptional = userRepository.findByUsername(USER_ADMIN)
        assertThat(userEntityOptional).isNotEmpty

        val userEntity = userEntityOptional.get()
        assertThat(userEntity.username).isEqualTo(USER_ADMIN)
        assertThat(userEntity.enabled).isTrue
        assertThat(userEntity.locale).isEqualTo("de")
    }

    @Test
    @FlywayTest
    @Sql("/db/user/add-user.sql")
    internal fun changePassword() {
        val newPass = "Secret12345$"

        val userOptional = userManager.getOne(FHANSELER)
        assertThat(userOptional).isNotEmpty

        userManager.changePassword(userOptional.get(), newPass)

        val userEntity = userRepository.findByUsername(FHANSELER)
        assertThat(userEntity).isNotEmpty

        val isPasswordMatch = encoder.matches(newPass, userEntity.get().password)
        assertThat(isPasswordMatch).isTrue()
    }

    @Test
    internal fun changePasswordWhenUserNotExisting() {
        val user = UserDto(
            id = 0,
            username = "hmuster",
            authorities = listOf("ROLE_USER")
        )

        val exception = assertThrows<UserNotFoundException> {
            userManager.changePassword(user, "newSecret1245$")
        }
        assertThat(exception).hasMessage("Could not find user: username=${user.username}")
    }

    @Test
    @Sql("/db/user/add-user.sql")
    internal fun changeInvalidPassword() {
        val userOptional = userManager.getOne(FHANSELER)
        assertThat(userOptional).isNotEmpty

        assertThrows<InvalidPasswordException> {
            userManager.changePassword(userOptional.get(), "simplepass")
        }
    }

    @Test
    @FlywayTest
    @Sql("/db/user/add-user.sql")
    internal fun deleteUser() {
        val user = userManager.getOne(FHANSELER)
        assertThat(user).isNotEmpty

        userManager.delete(user.get().id)

        val userOptional = userManager.getOne(FHANSELER)
        assertThat(userOptional).isEmpty

        val userEntity = userRepository.findByUsername(FHANSELER)
        assertThat(userEntity).isEmpty
    }

    @Test
    internal fun deleteAdminUser() {
        val admin = userManager.getOne(USER_ADMIN)
        assertThat(admin).isNotEmpty

        val exception = assertThrows<IllegalUserOperationException> {
            userManager.delete(admin.get().id)
        }
        assertThat(exception).hasMessage("Not allowed to delete admin user")

        val adminEntity = userRepository.findByUsername(USER_ADMIN)
        assertThat(adminEntity).isNotEmpty
    }
}
