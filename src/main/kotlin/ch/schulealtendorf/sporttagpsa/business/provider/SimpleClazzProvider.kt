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

package ch.schulealtendorf.sporttagpsa.business.provider

import ch.schulealtendorf.sporttagpsa.entity.ClazzEntity
import ch.schulealtendorf.sporttagpsa.model.Clazz
import ch.schulealtendorf.sporttagpsa.repository.ClazzRepository
import org.springframework.stereotype.Component

/**
 * Provider for {@link ClazzEntity}.
 * 
 * @author nmaerchy
 * @version 1.1.0
 */
@Component
class SimpleClazzProvider(
        private val clazzRepository: ClazzRepository
): ClazzProvider {

    /**
     * @return all the data from this provider
     */
    override fun getAll(): Collection<Clazz> {
        return clazzRepository.findAll().map { 
            Clazz(it?.id!!, it.name, it.teacher.name)
        }
    }

    /**
     * @param id id of the class
     *
     * @return the class matching the given {@code id}
     * @throws IllegalArgumentException if the class with the given id does not exist
     */
    override fun getOne(id: Int): Clazz {
        
        val clazz: ClazzEntity = clazzRepository.findById(id)
                .orElseThrow { IllegalArgumentException("Class does not exits: id=$id") }!!

        return Clazz(clazz.id!!, clazz.name, clazz.teacher.name)
    }
}