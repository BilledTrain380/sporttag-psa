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

package ch.schulealtendorf.sporttagpsa.controller.rest.group

import ch.schulealtendorf.sporttagpsa.business.group.GroupManager
import ch.schulealtendorf.sporttagpsa.controller.rest.NotFoundException
import ch.schulealtendorf.sporttagpsa.model.Group
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

/**
 * Rest controller for {@link Group}.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@RestController
@RequestMapping("/api/rest")
class GroupController(
        private val groupManager: GroupManager
) {

    @PreAuthorize("#oauth2.hasScope('group_read')")
    @GetMapping("/group/{group_name}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getGroup(@PathVariable("group_name") name: String): Group {
        return groupManager.getGroup(name)
                .orElseThrow { NotFoundException("Could not find group: name=$name") }
    }

    @PreAuthorize("#oauth2.hasScope('group_read')")
    @GetMapping("/groups", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getGroups(
            @RequestParam("competitive", required = false) competitive: Boolean?,
            @RequestParam("pendingParticipation", required = false) pendingParticipation: Boolean?
    ): List<Group> {

        return groupManager.getGroups()
                .filter(competitive, pendingParticipation)
    }

    private fun Iterable<Group>.filter(competitive: Boolean?, pendingParticipation: Boolean?): List<Group> {

        return this
                .filter {
                    (competitive == null) ||
                    competitive && it.isCompetitive() ||
                    !competitive && !it.isCompetitive()
                }
                .filter {
                    (pendingParticipation == null) ||
                    pendingParticipation && it.hasPendingParticipation() ||
                    !pendingParticipation && !it.hasPendingParticipation()
                }
    }

    private fun Group.isCompetitive() = groupManager.isCompetitive(this)

    private fun Group.hasPendingParticipation() = groupManager.hasPendingParticipation(this)
}