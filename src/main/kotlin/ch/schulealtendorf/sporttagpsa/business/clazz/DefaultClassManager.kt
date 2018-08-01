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

import ch.schulealtendorf.sporttagpsa.entity.ClazzEntity
import ch.schulealtendorf.sporttagpsa.model.Clazz
import ch.schulealtendorf.sporttagpsa.model.Coach
import ch.schulealtendorf.sporttagpsa.repository.ClazzRepository
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
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
        private val classRepository: ClazzRepository,
        private val competitorRepository: CompetitorRepository
): ClassManager {

    /**
     * @return all classes which are available
     */
    override fun getAllClasses(): List<Clazz> {
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
    override fun getClass(name: String): Optional<Clazz> {

        val clazz = classRepository.findByName(name)

        return clazz.map { it.map() }
    }

    /**
     * Saves the given {@code clazz}.
     *
     * If the name of the class exists already, it will be updated, otherwise created.
     *
     * @param clazz the class to save
     */
    override fun saveClass(clazz: Clazz) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun ClazzEntity.pendingParticipation() = competitorRepository.findByClazzName(this.name).any { it.sport == null }

    private fun ClazzEntity.map(): Clazz {
        return Clazz(
                name,
                Coach(coach.id!!, coach.name),
                pendingParticipation())
    }
}