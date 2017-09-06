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

import com.deliveredtechnologies.rulebook.model.Rule

/**
 * Base class for all rules, that can be used in a rule set.
 * 
 * * T - the condition type that is passed in the {@code whenever} method
 * * U - the result type of the rule
 * 
 * @author nmaerchy
 * @version 1.0.0
 */
abstract class BaseRule<T, U> {

    /**
     * @return the specific rule that is used in a {@link CoRRuleBook}.
     */
    abstract val get: Rule<RuleTarget<T>, U>

    /**
     * @return true if the rule should apply, otherwise false
     */
    protected abstract val whenever: (condition: T, target: RuleTarget.Members) -> Boolean

    /**
     * @return the result, if the rule applies
     */
    protected abstract val then: (RuleTarget.Members) -> U

    /**
     * Helper function to get the reference of generic objects.
     */
    protected inline fun <reified T: Any> typeRef() = T::class.java
}