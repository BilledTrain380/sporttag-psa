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

package ch.schulealtendorf.sporttagpsa.business.rules

import com.deliveredtechnologies.rulebook.FactMap
import com.deliveredtechnologies.rulebook.NameValueReferableMap

/**
 * Acts as a wrapper for a {@link List}. The list can be looped with facts for a rulebook.
 * 
 * @author nmaerchy
 * @version 1.0.0
 */
class RulableListObject<out T> private constructor(
        val list: List<T>
){
    
    companion object {

        /**
         * Creates an instance by the passed in {@code list}.
         * @return the resulting instance
         */
        fun <T> of(list: List<T>) = RulableListObject(list)
    }

    /**
     * Loops to the list and provides each element with facts for a rulebook.
     * Facts will be cleared after each element.
     * 
     * @param condition the condition the will be set to the facts
     * @param action provides the element and the facts
     */
    inline fun useFacts(condition: String, action: (element: T, facts: NameValueReferableMap<Any>) -> Unit) {
        
        val facts: NameValueReferableMap<Any> = FactMap()
        val ruleTarget = RuleTarget(condition, RuleTarget.Members())
        
        for (element in list) {
            facts.setValue("target", ruleTarget)
            action(element, facts)
            facts.clear()
        }
    }
}