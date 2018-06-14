/*
 * Copyright (c) 2017 by Nicolas Märchy
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

package ch.schulealtendorf.sporttagpsa.business.parsing

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * @author nmaerchy
 * *
 * @version 0.0.1
 */
object CompetitorFileReaderSpec : Spek ({
    
    var competitorFileReader: CompetitorFileReader = CompetitorFileReader()
    
    beforeEachTest { 
        competitorFileReader = CompetitorFileReader()
    }

    fun getDate(date: String): Date {

        val format: DateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN)
        return format.parse(date)
    }
    
    describe("a CompetitorFileReader") {

        val exampleFile: MultipartFile = Mockito.mock(MultipartFile::class.java)
        
        on("parse a competitor file") {

            val testInputStream: InputStream = Thread.currentThread().contextClassLoader.getResourceAsStream("parsing/test-competitor.csv")

            `when` (exampleFile.contentType).thenReturn("text/csv")
            `when` (exampleFile.isEmpty).thenReturn(false)
            `when`(exampleFile.inputStream).thenReturn(testInputStream)
            
            val result: List<FlatCompetitor> = competitorFileReader.parseToCompetitor(exampleFile)
            
            it("should return a list of FlatCompetitor objects") {
                val expected: List<FlatCompetitor> = Stream.of(
                        FlatCompetitor("Muster", "Hans", true, getDate("07.09.2017"), "Musterstrasse 1a", "8000", "Musterhausen", "1a", "Marry Müller"),
                        FlatCompetitor("Wirbelwind", "Will", false, getDate("08.12.2015"), "Wirbelstrasse 16", "4000", "Willhausen", "1a", "Hans Müller")
                ).collect(Collectors.toList())
                
                Assert.assertEquals(expected, result)
            }
        }
        
        on("parse an empty file") {

            `when` (exampleFile.contentType).thenReturn("text/csv")
            `when` (exampleFile.isEmpty).thenReturn(true)
            
            it("should throw an IllegalArgumentException that the file is empty") {
                
                val exception: IllegalArgumentException = assertFailsWith(IllegalArgumentException::class) {
                    competitorFileReader.parseToCompetitor(exampleFile)
                }

                assertEquals("Competitor input file is empty.", exception.message)
            }
            
        }
        
        on("parse a input file with no header line") {

            val testInputStream: InputStream = Thread.currentThread().contextClassLoader.getResourceAsStream("parsing/test-competitor-no-header.csv")

            `when` (exampleFile.contentType).thenReturn("text/csv")
            `when` (exampleFile.isEmpty).thenReturn(false)
            `when`(exampleFile.inputStream).thenReturn(testInputStream)
            
            it("should throw an IllegalArgumentException that an error occurred") {

                val exception: IllegalArgumentException = assertFailsWith(IllegalArgumentException::class) {
                    competitorFileReader.parseToCompetitor(exampleFile)
                }

                assertEquals("Error during CSV parsing.", exception.message)
            }
        }
        
        on("parse a competitor file with an invalid date format") {
            
            val testInputStream: InputStream = Thread.currentThread().contextClassLoader.getResourceAsStream("parsing/test-competitor-invalid-date.csv")

            `when` (exampleFile.contentType).thenReturn("text/csv")
            `when` (exampleFile.isEmpty).thenReturn(false)
            `when`(exampleFile.inputStream).thenReturn(testInputStream)
            
            it("should throw an IllegalArgumentException that an error occurred") {
                
                val exception: IllegalArgumentException = assertFailsWith(IllegalArgumentException::class) {
                    competitorFileReader.parseToCompetitor(exampleFile)
                }

                assertEquals("Error during CSV parsing.", exception.message)
            }
        }
        
        on("parse not a text/csv file") {

            `when` (exampleFile.contentType).thenReturn("text/html")
            
            it("should throw an IllegalArgumentException that an the file MUST be a text/csv file") {
                
                val exception: IllegalArgumentException = assertFailsWith(IllegalArgumentException::class) {
                    competitorFileReader.parseToCompetitor(exampleFile)
                }

                assertEquals("The input file MUST be the mime type \"text/csv\".", exception.message)
            }
        }
    }
})
