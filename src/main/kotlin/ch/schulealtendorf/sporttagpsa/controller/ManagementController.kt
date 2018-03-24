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

import ch.schulealtendorf.sporttagpsa.business.export.ParticipantExportManager
import ch.schulealtendorf.sporttagpsa.business.export.ParticipantExportModel
import ch.schulealtendorf.sporttagpsa.business.participation.ParticipationManager
import ch.schulealtendorf.sporttagpsa.business.participation.ParticipationStatus
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import java.io.FileInputStream
import javax.validation.Valid

/**
 * @author nmaerchy
 * @version 0.0.1
 */
@Controller
class ManagementController(
        private val participationManager: ParticipationManager,
        private val participationStatus: ParticipationStatus,
        private val participantExportManager: ParticipantExportManager
) {
    
    companion object {
        const val BASIC = "/management/basic"
        const val FINISH_PARTICIPATION = "/management/finish-participation"
    }
    
    @GetMapping(BASIC)
    fun basic(model: Model): String {

        model.addAttribute("participationStatus", participationStatus.isFinished())
        
        return "competitor/management-basic"
    }
    
    @GetMapping(FINISH_PARTICIPATION)
    fun finishParticipation(redirectAttributes: RedirectAttributes): String {
        
        participationManager.finishParticipation()
        
        redirectAttributes.addFlashAttribute("participationInfo", true)
        
        return "redirect:$BASIC"
    }
    
    @GetMapping("/management/participant-list")
    fun participantList(model: Model): String {
        
        model.addAttribute("participantModel", participantExportManager.getPreparedModel())
        
        return "competitor/management-participantlist"
    }
    
    @PostMapping("/management/participant-list")
    fun exportParticipantList(@Valid @ModelAttribute("participantModel") model: ParticipantExportModel): ResponseEntity<InputStreamResource> {

        val zip = participantExportManager.generateZip(model)

        val respHeaders = HttpHeaders()
        respHeaders.contentType = MediaType.APPLICATION_OCTET_STREAM
        respHeaders.contentLength = zip.length()
        respHeaders.setContentDispositionFormData("attachment", zip.name)

        val isr = InputStreamResource(FileInputStream(zip))
        return ResponseEntity(isr, respHeaders, HttpStatus.OK)
    }
}