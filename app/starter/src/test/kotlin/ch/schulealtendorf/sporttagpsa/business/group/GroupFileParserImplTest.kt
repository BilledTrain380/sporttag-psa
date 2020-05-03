package ch.schulealtendorf.sporttagpsa.business.group

import ch.schulealtendorf.psa.dto.participation.BirthdayDto
import ch.schulealtendorf.psa.dto.participation.GenderDto
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

@ActiveProfiles("test")
@Tag("unit-test")
@Disabled("Migrate instant to LocalDate")
internal class GroupFileParserImplTest {

    private val parser = GroupFileParserImpl()

    private val mockFile: MultipartFile = mock()

    @BeforeEach
    internal fun beforeEach() {
        reset(mockFile)
    }

    @Test
    internal fun parsingCsv() {
        val testInputStream: InputStream =
            GroupFileParserImplTest::class.java.getResourceAsStream("/parsing/test-group-import.csv")

        whenever(mockFile.contentType).thenReturn("text/csv")
        whenever(mockFile.isEmpty).thenReturn(false)
        whenever(mockFile.inputStream).thenReturn(testInputStream)

        val result = parser.parseCSV(mockFile)
        assertThat(result).hasSize(2)
        assertThat(result).containsOnly(
            FlatParticipant(
                "Muster",
                "Hans",
                GenderDto.MALE,
                BirthdayDto.parse("2017-09-07T00:00:00+02:00[Europe/Zurich]"),
                "Musterstrasse 1a",
                "8000",
                "Musterhausen",
                "1a",
                "Marry Müller"
            ),
            FlatParticipant(
                "Wirbelwind",
                "Will",
                GenderDto.FEMALE,
                BirthdayDto.parse("2015-12-08T00:00:00+01:00[Europe/Zurich]"),
                "Wirbelstrasse 16",
                "4000",
                "Willhausen",
                "1a",
                "Hans Müller"
            )
        )
    }

    @Test
    internal fun parseEmptyFile() {
        whenever(mockFile.contentType).thenReturn("text/csv")
        whenever(mockFile.isEmpty).thenReturn(true)

        val exception = assertThrows<IllegalArgumentException> {
            parser.parseCSV(mockFile)
        }
        assertThat(exception).hasMessage("Can not parse empty file")
    }

    @Test
    internal fun parseInvalidDateFormat() {
        val testInputStream: InputStream =
            GroupFileParserImplTest::class.java.getResourceAsStream("/parsing/test-group-import-invalid-date.csv")

        whenever(mockFile.contentType).thenReturn("text/csv")
        whenever(mockFile.isEmpty).thenReturn(false)
        whenever(mockFile.inputStream).thenReturn(testInputStream)

        val exception = assertThrows<CSVParsingException> {
            parser.parseCSV(mockFile)
        }
        assertThat(exception).hasMessage("Can not parse birthday: value=08. Juni 2015")
        assertThat(exception.line).isOne()
        assertThat(exception.column).isEqualTo(54)
    }

    @Test
    internal fun parseInvalidGender() {
        val testInputStream: InputStream =
            GroupFileParserImplTest::class.java.getResourceAsStream("/parsing/test-group-import-invalid-gender.csv")

        whenever(mockFile.contentType).thenReturn("text/csv")
        whenever(mockFile.isEmpty).thenReturn(false)
        whenever(mockFile.inputStream).thenReturn(testInputStream)

        val exception = assertThrows<CSVParsingException> {
            parser.parseCSV(mockFile)
        }
        assertThat(exception).hasMessage("Can not parse gender: value=r")
        assertThat(exception.line).isZero()
        assertThat(exception.column).isEqualTo(15)
    }

    @Test
    internal fun parseInvalidFileExtension() {
        whenever(mockFile.contentType).thenReturn("application/pdf")

        val exception = assertThrows<IllegalArgumentException> {
            parser.parseCSV(mockFile)
        }
        assertThat(exception).hasMessage("Invalid file type: type=application/pdf")
    }

    @Test
    internal fun parseInvalidCsvLine() {
        val testInputStream: InputStream = "not,enough,values".byteInputStream()

        whenever(mockFile.contentType).thenReturn("text/csv")
        whenever(mockFile.isEmpty).thenReturn(false)
        whenever(mockFile.inputStream).thenReturn(testInputStream)

        val exception = assertThrows<CSVParsingException> {
            parser.parseCSV(mockFile)
        }
        assertThat(exception).hasMessage("Can not parse line: Missing values.")
        assertThat(exception.line).isZero()
        assertThat(exception.column).isZero()
    }
}
