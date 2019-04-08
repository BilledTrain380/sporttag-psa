/*
 * Copyright (c) 2019 by Nicolas Märchy
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

package ch.schulealtendorf.psa.shared.rulebook.rules

/**
 * Describes a basic rule with a when condition, then do..
 *
 * @author nmaerchy
 * @version 1.0.1
 */
abstract class Rule<T, out K> {

    /**
     * Used by a rule book. Combines the condition of the rule
     * and the condition of a rule set.
     * If the rule is not used in a rule set, the rule sets condition
     * will always return true
     *
     * @return true if this rule should be applied, otherwise false
     */
    internal val wheneverMod: (T) -> Boolean = { whenever(it) && wheneverSet(it) }

    /**
     * Additional whenever condition for a rule set
     */
    internal var wheneverSet: (T) -> Boolean = { true }

    /**
     * @return true, if this rule should be applied, otherwise false
     */
    abstract val whenever: (T) -> Boolean

    /**
     * @return the result for this rule
     */
    abstract val then: (T) -> K
}