/*
 * Copyright (c) 2018 by Nicolas Märchy
 *
 * This file is part of Sporttag PSA.
 *
 * Sporttag PSA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sporttag PSA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sporttag PSA.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Diese Datei ist Teil von Sporttag PSA.
 *
 * Sporttag PSA ist Freie Software: Sie können es unter den Bedingungen
 * der GNU General Public License, wie von der Free Software Foundation,
 * Version 3 der Lizenz oder (nach Ihrer Wahl) jeder späteren
 * veröffentlichten Version, weiterverbreiten und/oder modifizieren.
 *
 * Sporttag PSA wird in der Hoffnung, dass es nützlich sein wird, aber
 * OHNE JEDE GEWÄHRLEISTUNG, bereitgestellt; sogar ohne die implizite
 * Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
 * Siehe die GNU General Public License für weitere Details.
 *
 * Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
 * Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 *
 *
 */

package ch.schulealtendorf.sporttagpsa.business.group

import ch.schulealtendorf.sporttagpsa.model.Birthday
import ch.schulealtendorf.sporttagpsa.model.Gender
import com.nhaarman.mockito_kotlin.whenever
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.Mockito
import org.mockito.Mockito.reset
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * @author nmaerchy
 *
 * @version 1.0.0
 * @since 1.0.0
 */
object GroupFileParserImplSpec : Spek ({

    fun convertDate(date: String): Date {

        val format: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN)
        return format.parse(date)
    }

    describe("a group file parser impl") {

        val parser = GroupFileParserImpl()


        val mockFile: MultipartFile = Mockito.mock(MultipartFile::class.java)

        beforeEachTest {
            reset(mockFile)
        }

        given("a multipart csv file to parse") {

            on("parsing a csv") {

                val testInputStream: InputStream = GroupFileParserImplSpec.javaClass.getResourceAsStream("/parsing/test-group-import.csv")

                whenever(mockFile.contentType).thenReturn("text/csv")
                whenever(mockFile.isEmpty).thenReturn(false)
                whenever(mockFile.inputStream).thenReturn(testInputStream)


                val result = parser.parseCSV(mockFile)


                it("should return  list of csv participants") {
                    val expected = listOf(
                            FlatParticipant("Muster", "Hans", Gender.MALE, Birthday(convertDate("07.09.2017")), "Musterstrasse 1a", "8000", "Musterhausen", "1a", "Marry Müller"),
                            FlatParticipant("Wirbelwind", "Will", Gender.FEMALE, Birthday(convertDate("08.12.2015")), "Wirbelstrasse 16", "4000", "Willhausen", "1a", "Hans Müller")
                    )
                    assertEquals(expected, result)
                }
            }

            on("an empty file") {

                whenever(mockFile.contentType).thenReturn("text/csv")
                whenever(mockFile.isEmpty).thenReturn(true)


                it("should throw an illegal argument exception, indicating that an empty file can not be parsed") {
                    val exception = assertFailsWith<IllegalArgumentException> {
                        parser.parseCSV(mockFile)
                    }
                    assertEquals("Can not parse empty file", exception.message)
                }
            }

            on("invalid date format") {

                val testInputStream: InputStream = GroupFileParserImplSpec.javaClass.getResourceAsStream("/parsing/test-group-import-invalid-date.csv")

                whenever(mockFile.contentType).thenReturn("text/csv")
                whenever(mockFile.isEmpty).thenReturn(false)
                whenever(mockFile.inputStream).thenReturn(testInputStream)


                it("should throw an parse exception exception, indicating that the date format is invalid") {
                    val exception = assertFailsWith<CSVParsingException> {
                        parser.parseCSV(mockFile)
                    }
                    assertEquals("Can not parse birthday: value=08. Juni 2015", exception.message)
                    assertEquals(1, exception.line)
                    assertEquals(54, exception.column)
                }
            }

            on("invalid gender value") {

                val testInputStream: InputStream = GroupFileParserImplSpec.javaClass.getResourceAsStream("/parsing/test-group-import-invalid-gender.csv")

                whenever(mockFile.contentType).thenReturn("text/csv")
                whenever(mockFile.isEmpty).thenReturn(false)
                whenever(mockFile.inputStream).thenReturn(testInputStream)


                it("should throw an parse exception, indicating that the gender value is invalid") {
                    val exception = assertFailsWith<CSVParsingException> {
                        parser.parseCSV(mockFile)
                    }
                    assertEquals("Can not parse gender: value=r", exception.message)
                    assertEquals(0, exception.line)
                    assertEquals(15, exception.column)
                }
            }

            on("non csv file") {

                whenever(mockFile.contentType).thenReturn("application/pdf")


                it("should throw an illegal argument exception, indicating that the file type is invalid") {
                    val exception = assertFailsWith<IllegalArgumentException> {
                        parser.parseCSV(mockFile)
                    }
                    assertEquals("Invalid file type: type=application/pdf", exception.message)
                }
            }
        }
    }
})
