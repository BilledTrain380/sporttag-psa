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

package ch.schulealtendorf.sporttagpsa.controller.tournament

import ch.schulealtendorf.sporttagpsa.business.export.*
import ch.schulealtendorf.sporttagpsa.business.provider.ClazzProvider
import ch.schulealtendorf.sporttagpsa.business.provider.DisciplineProvider
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.io.FileInputStream
import javax.validation.Valid

@Controller
@RequestMapping("/event-sheet")
class EventSheetController(
        private val exportManager: ExportManager,
        clazzProvider: ClazzProvider,
        disciplineProvider: DisciplineProvider
) {
    
    private val clazzes = clazzProvider.getAll()
    private val disciplines = disciplineProvider.getAll()
    
    @GetMapping("/")
    fun index(model: Model): String {
        
        val eventSheetForm = EventSheetForm(
                disciplines.map {

                    DisciplineEventSheetForm(
                            it.id,
                            it.name,
                            clazzes.map {
                                ClazzEventSheetForm(
                                        it.id,
                                        it.name
                                )
                            }
                    )
                }
        )
        
        model.addAttribute("eventSheetForm", eventSheetForm)
        
        return "tournament/event-sheet"
    }

    @PostMapping("/export")
    fun export(@Valid @ModelAttribute("eventSheetForm") eventSheetForm: EventSheetForm): ResponseEntity<InputStreamResource> {

        val zip = exportManager.generateArchive(eventSheetForm.toEventSheetExport())

        val respHeaders = HttpHeaders()
        respHeaders.contentType = MediaType.APPLICATION_OCTET_STREAM
        respHeaders.contentLength = zip.length()
        respHeaders.setContentDispositionFormData("attachment", zip.name)

        val isr = InputStreamResource(FileInputStream(zip))
        return ResponseEntity(isr, respHeaders, HttpStatus.OK)
    }
    
    private fun EventSheetForm.toEventSheetExport(): EventSheetExport {
        
        return EventSheetExport(
                disciplines.map {

                    listOf(
                            it.clazzes
                                    .filter { it.male }
                                    .map {clazz ->

                                        Triple(
                                                SimpleDiscipline(it.id, it.name),
                                                SimpleClazz(clazz.id, clazz.name),
                                                true
                                        )
                                    },

                            it.clazzes
                                    .filter { it.female }
                                    .map {clazz ->

                                        Triple(
                                                SimpleDiscipline(it.id, it.name),
                                                SimpleClazz(clazz.id, clazz.name),
                                                false
                                        )
                                    }
                    ).flatten()

                }.flatten()
                        .map {
                            EventSheetDisciplineExport(it.first, it.second, it.third)
                        }
        )
    }
}