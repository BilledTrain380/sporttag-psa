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

package ch.schulealtendorf.sporttagpsa.business.clazz

import ch.schulealtendorf.sporttagpsa.entity.GroupEntity
import ch.schulealtendorf.sporttagpsa.model.Group
import ch.schulealtendorf.sporttagpsa.model.Coach
import ch.schulealtendorf.sporttagpsa.repository.GroupRepository
import ch.schulealtendorf.sporttagpsa.repository.ParticipantRepository
import org.springframework.stereotype.Component
import java.util.*

/**
 * Default class manager implementation which uses the repository classes to get its data.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@Component
class DefaultClassManager(
        private val classRepository: GroupRepository,
        private val competitorRepository: ParticipantRepository
): ClassManager {

    /**
     * @return all classes which are available
     */
    override fun getAllClasses(): List<Group> {
        return classRepository.findAll()
                .mapNotNull { it?.map() }
    }

    /**
     * An {@link Optional} containing the class matching
     * the {name} or an empty Optional if no class could be found.
     *
     * @param name the name of the class
     *
     * @return the resulting class in an Optional
     */
    override fun getClass(name: String): Optional<Group> {

        val clazz = classRepository.findByName(name)

        return clazz.map { it.map() }
    }

    /**
     * Saves the given {@code group}.
     *
     * If the name of the class exists already, it will be updated, otherwise created.
     *
     * @param clazz the class to save
     */
    override fun saveClass(clazz: Group) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * A pending participation means, if any participant of the given {@code group}
     * as no sport.
     *
     * @return true if the class has pending participation, otherwise false
     */
    override fun hasPendingParticipation(clazz: Group) = competitorRepository.findByGroupName(clazz.name).any { it.sport == null }

    private fun GroupEntity.map(): Group {
        return Group(
                name,
                Coach(coach.id!!, coach.name))
    }
}