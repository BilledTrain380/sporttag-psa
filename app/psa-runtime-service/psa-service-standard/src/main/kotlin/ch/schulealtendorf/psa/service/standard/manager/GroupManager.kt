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

package ch.schulealtendorf.psa.service.standard.manager

import ch.schulealtendorf.psa.dto.group.GroupStatusType
import ch.schulealtendorf.psa.dto.group.OverviewGroupDto
import ch.schulealtendorf.psa.dto.group.SimpleGroupDto
import java.util.Optional

/**
 * Describes a manager for domain classes related to a {@link Group}.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
interface GroupManager {

    /**
     * @return true if the given {@code group} has participant, which do not participate in any sport, otherwise false
     */
    fun hasPendingParticipation(group: SimpleGroupDto): Boolean

    /**
     * @return true whenever any participant of the given group is a competitor, otherwise false
     */
    fun isCompetitive(group: SimpleGroupDto): Boolean

    fun getGroups(): List<SimpleGroupDto>

    fun getGroupsBy(filter: GroupStatusType): List<SimpleGroupDto>

    fun getOverview(): List<OverviewGroupDto>

    fun getOverviewBy(filter: GroupStatusType): List<OverviewGroupDto>

    /**
     * Gets the group matching the given {@code name}.
     *
     * @param name tha name of the group
     *
     * @return an Optional containing the group, or empty if the group could not be found
     */
    fun getGroup(name: String): Optional<SimpleGroupDto>
}
