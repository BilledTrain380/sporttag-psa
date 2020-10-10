package ch.schulealtendorf.sporttagpsa.business.user.validation

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PSAPasswordValidatorTest {
    companion object {
        const val VALID_PASSWORD = "Psa12345$"
    }

    private lateinit var passwordValidator: PSAPasswordValidator

    @BeforeEach
    internal fun beforeEach() {
        passwordValidator = PSAPasswordValidator()
    }

    @Test
    internal fun validateWhenAllRulesFulfilled() {
        val validationResult = passwordValidator.validate(VALID_PASSWORD)

        assertThat(validationResult.isValid)
            .withFailMessage("Expected password to be valid")
            .isTrue

        assertThat(validationResult.messages).isEmpty()
    }

    @Test
    internal fun validateEqualsWhenPasswordIsValid() {
        val validationResult = passwordValidator.validateEquals(VALID_PASSWORD, VALID_PASSWORD)

        assertThat(validationResult.isValid)
            .withFailMessage("Expected password to be valid")
            .isTrue

        assertThat(validationResult.messages).isEmpty()
    }

    @Test
    internal fun validateEqualsWhenPasswordsDontMatch() {
        val validationResult = passwordValidator.validateEquals(VALID_PASSWORD, "no match")

        assertThat(validationResult.isValid)
            .withFailMessage("Expected password to be invalid")
            .isFalse

        assertThat(validationResult.messages).hasSize(1)
        assertThat(validationResult.messages[0]).isEqualTo("Passwords do not match.")
    }
}
