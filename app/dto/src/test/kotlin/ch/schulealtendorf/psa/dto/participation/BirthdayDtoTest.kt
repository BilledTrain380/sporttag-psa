package ch.schulealtendorf.psa.dto.participation

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Year
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

internal class BirthdayDtoTest {
    companion object {
        private const val YEAR = 2020
        private const val MONTH = 8
        private const val DAY = 18
    }

    @Test
    internal fun parse() {
        val birthday = BirthdayDto.parse("2020-08-18")

        val age = Year.now().value - 2020
        assertThat(birthday.age).isEqualTo(age)
        assertThat(birthday.year).isEqualByComparingTo(Year.of(2020))
        assertThat(birthday.value).isEqualTo("2020-08-18")
    }

    @Test
    internal fun parseWithFormatter() {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMAN)
        val birthday = BirthdayDto.parse("18.08.2020", formatter)

        val age = Year.now().value - 2020
        assertThat(birthday.age).isEqualTo(age)
        assertThat(birthday.year).isEqualByComparingTo(Year.of(2020))
        assertThat(birthday.value).isEqualTo("2020-08-18")
    }

    @Test
    internal fun ofDate() {
        val date = LocalDate.of(YEAR, MONTH, DAY)
        val birthday = BirthdayDto.ofDate(date)

        val age = Year.now().value - YEAR
        assertThat(birthday.age).isEqualTo(age)
        assertThat(birthday.year).isEqualByComparingTo(Year.of(YEAR))
        assertThat(birthday.value).isEqualTo("2020-08-18")
    }

    @Test
    internal fun formatWithFormatPattern() {
        val birthday = BirthdayDto.parse("2020-08-18")
        val formattedDate = birthday.format("dd.MM.yyyy")

        assertThat(formattedDate).isEqualTo("18.08.2020")
    }

    @Test
    internal fun format() {
        val birthday = BirthdayDto.parse("2020-08-18")
        val formattedDate = birthday.format()

        val expectedDate = LocalDate.of(YEAR, MONTH, DAY)
        val defaultFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
        val expectedFormattedDate = expectedDate.format(defaultFormatter)
        assertThat(formattedDate).isEqualTo(expectedFormattedDate)
    }
}
