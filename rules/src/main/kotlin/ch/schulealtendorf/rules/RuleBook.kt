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

package ch.schulealtendorf.rules

import com.deliveredtechnologies.rulebook.FactMap
import com.deliveredtechnologies.rulebook.NameValueReferableMap
import com.deliveredtechnologies.rulebook.lang.RuleBookBuilder
import com.deliveredtechnologies.rulebook.lang.RuleBuilder
import com.deliveredtechnologies.rulebook.model.RuleBook
import kotlin.reflect.KClass

/**
 * Describes a rule book to add rules and run facts.
 * 
 * @author nmaerchy
 * @version 1.0.0
 */
open class RuleBook<T: Any, K: Any>(
        private val fact: KClass<T>,
        private val result: KClass<K>
) {
    
    private val ruleBook: RuleBook<K> = RuleBookBuilder.create()
            .withResultType(result.java)
            .withDefaultResult(Any() as K)
            .build()
    
    fun addRule(rule: Rule<T, K>) {
        
        val ruleModel: com.deliveredtechnologies.rulebook.model.Rule<T, K> = RuleBuilder.create()
                .withFactType(fact.java)
                .withResultType(result.java)
                .`when` { rule.whenever(it.getValue("input")) }
                .then { facts, result -> result.value = rule.then(facts.getValue("input")) }
                .build()
        
        ruleBook.addRule(ruleModel)
    }
    
    fun addRuleSet(ruleSet: RuleSet<T, K>) {
        ruleSet.getRules().forEach { addRule(it) }
    }
    
    fun run(fact: T): K {

        val facts: NameValueReferableMap<Any> = FactMap()
        facts.setValue("input", fact)

        ruleBook.run(facts)

        if (ruleBook.result.isPresent) {
            return ruleBook.result.get().value
        }
        
        throw IllegalStateException("No result is available.")
    }
}