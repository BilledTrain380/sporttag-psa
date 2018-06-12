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

package ch.schulealtendorf.sporttagpsa.controller

import ch.schulealtendorf.sporttagpsa.business.competitors.CompetitorListConsumer
import ch.schulealtendorf.sporttagpsa.business.parsing.FileReader
import ch.schulealtendorf.sporttagpsa.controller.participant.importpage.ImportController
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes

/**
 * @author nmaerchy
 * @version 0.0.1
 */
@RunWith(JUnitPlatform::class)
object ImportControllerSpec: Spek({

    var mockFileReader: FileReader = mock(FileReader::class.java)
    var mockCompetitorConsumer: CompetitorListConsumer = mock(CompetitorListConsumer::class.java)

    var importCtrl: ImportController = ImportController(mockFileReader, mockCompetitorConsumer)

    beforeEachTest {
        mockFileReader = mock(FileReader::class.java)
        mockCompetitorConsumer = mock(CompetitorListConsumer::class.java)
        importCtrl = ImportController(mockFileReader, mockCompetitorConsumer)
    }
    
    describe("an ImportController") {

        val mockFile: MultipartFile = Mockito.mock(MultipartFile::class.java)
        val mockRedirectedAttr: RedirectAttributes = mock(RedirectAttributes::class.java)
        
        on("on handling a valid file") {

            `when` (mockFileReader.parseToCompetitor(mockFile)).thenReturn(emptyList())

            val result: String = importCtrl.handleFileUpload(mockFile, mockRedirectedAttr)
            
            it("should parse the input file") {
                verify(mockFileReader, times(1)).parseToCompetitor(mockFile)
            }
            
            it("should delegate the parsed file result to the competitor consumer") {
                verify(mockCompetitorConsumer, times(1)).accept(emptyList())
            }
            
            it("should redirect the user to the competitor import page") {
                Assert.assertEquals("redirect:/participant/import", result)
            }
            
            it("should send a success message to the redirected page") {
                verify(mockRedirectedAttr, times(1)).addFlashAttribute("success", true)
            }
        }
        
        on("handling an invalid file") {

            `when` (mockFileReader.parseToCompetitor(mockFile)).thenThrow(IllegalArgumentException::class.java)

            val result: String = importCtrl.handleFileUpload(mockFile, mockRedirectedAttr)
            
            it("should redirect the user to the import page") {
                Assert.assertEquals("redirect:/participant/import", result)
            }
            
            it("should send an error message to the redirected page") {
                verify(mockRedirectedAttr, times(1)).addFlashAttribute("success", false)
            }
        }
    }
})