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

/**
 * {@link RuleTarget} can be used for facts in a {@link CoRRuleBook}.
 * The specific rule book MUST support this instance.
 * 
 * * T - the condition type that is used in a rule set
 * 
 * @author nmaerchy
 * @version 1.0.0
 */
class RuleTarget<out T>(
        val condition: T,
        val members: Members
) {
    
    class Members {

        private val members: MutableMap<String, Any> = hashMapOf()

        /**
         * Adds the ked value pair to this target.
         *
         * @param key the key to use
         * @param value the value to use
         */
        fun add(key: String, value: Any) = members.put(key, value)

        fun getAsBoolean(key: String) = members.getValue(key) as Boolean

        fun getAsInt(key: String) = members.getValue(key) as Int

        fun getAsDouble(key: String) = members.getValue(key) as Double

        fun getAsString(key: String)= members.getValue(key) as String
    }
}