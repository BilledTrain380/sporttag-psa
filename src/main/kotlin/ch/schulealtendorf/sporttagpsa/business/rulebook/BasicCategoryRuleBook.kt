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

package ch.schulealtendorf.sporttagpsa.business.rulebook

import ch.schulealtendorf.rules.BasicRuleBook
import ch.schulealtendorf.rules.Rule
import org.springframework.stereotype.Component

/**
 * Rulebook that determines the distance by the age and discipline.
 * 
 * @author nmaerchy
 * @version 1.0.0
 */
@Component
class BasicCategoryRuleBook : BasicRuleBook<CategoryModel, String>(
        CategoryModel::class,
        String::class
), CategoryRuleBook {
    
    init {
        addRule(
                object: Rule<CategoryModel, String>() {
                    
                    override val whenever: (CategoryModel) -> Boolean = { it.discipline == "Schnelllauf" }
                    
                    override val then: (CategoryModel) -> String = { "60m" }
                }
        )
        
        addRule(
                object: Rule<CategoryModel, String>() {
                    
                    override val whenever: (CategoryModel) -> Boolean = { it.age < 12 && it.discipline == "Ballzielwurf"}
                    
                    override val then: (CategoryModel) -> String = { "4m" }
                }
        )
        
        addRule(
                object: Rule<CategoryModel, String>() {
                    
                    override val whenever: (CategoryModel) -> Boolean = { it.age > 11 && it.discipline == "Ballzielwurf" }
                    
                    override val then: (CategoryModel) -> String = { "5m" }
                }
        )
        
        addRule(
                object: Rule<CategoryModel, String>() {
                    
                    override val whenever: (CategoryModel) -> Boolean = { it.age < 12 && it.discipline == "Korbeinwurf" }
                    
                    override val then: (CategoryModel) -> String = { "2m" }
                }
        )

        addRule(
                object: Rule<CategoryModel, String>() {

                    override val whenever: (CategoryModel) -> Boolean = { it.age > 11 && it.discipline == "Korbeinwurf" }

                    override val then: (CategoryModel) -> String = { "2.5m" }
                }
        )
    }

    /**
     * Defines the category depending on the given
     * {@code fact} and then determines the distance
     * by the rules of this rule book.
     *
     * @param fact the fact to use
     *
     * @return the resulting distance or null if no distance applies for the given {@code fact}
     */
    override fun getDistance(fact: CategoryModel): String? = run(fact)
}