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

import com.deliveredtechnologies.rulebook.NameValueReferableTypeConvertibleMap
import com.deliveredtechnologies.rulebook.lang.RuleBuilder
import com.deliveredtechnologies.rulebook.model.Rule


/**
 * {@link RuleFormula} is the base class for all rules that can be used in a {@link RuleSet}.
 * 
 * A subclass has to override two properties.
 * * whenever - defines the condition, when the rule should be applied
 * * formula - the formula will be used, if the rule has to apply
 * 
 * @author nmaerchy
 * @version 1.0.0
 */
abstract class RuleFormula {
    
    val get: Rule<RuleTarget, Int>
    
    init {
        get = RuleBuilder.create().withFactType(RuleTarget::class.java).withResultType(Int::class.java)
                .`when` { 
                    val target = it.getValue("target")
                    whenever(target.condition, target.members)
                }
                .then { facts, result -> result.value = facts.result()
                }.build()
    }
    
    protected abstract val whenever: (condition: String, target: RuleTarget.Members) -> Boolean
    
    protected abstract val formula: (RuleTarget.Members) -> Int
    
    protected fun Boolean.isMale() = this

    protected fun Boolean.isFemale() = !this
    
    private fun NameValueReferableTypeConvertibleMap<RuleTarget>.result(): Int {
        val points = formula(getValue("target").members)
        return if (points > 1) points else 1
    }
}