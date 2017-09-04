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

import ch.schulealtendorf.sporttagpsa.business.rules.books.CategoryRuleBook
import ch.schulealtendorf.sporttagpsa.business.rules.books.ResultRuleBook
import com.deliveredtechnologies.rulebook.lang.RuleBookBuilder
import com.deliveredtechnologies.rulebook.model.rulechain.cor.CoRRuleBook

/**
 * {@link DefaultRuleBookFactory} provides {@link CoRRuleBook} instances.
 * 
 * @author nmaerchy
 * @version 1.0.0
 */
class DefaultRuleBookFactory: RuleBookFactory {

    /**
     * @return a PSA rulebook
     */
    override fun getResultRuleBook(): CoRRuleBook<Int> = RuleBookBuilder.create(ResultRuleBook::class.java)
            .withResultType(Int::class.java)
            .withDefaultResult(1)
            .build() as CoRRuleBook<Int>

    /**
     * @return a category rulebook
     */
    override fun getCategoryRuleBook(): CoRRuleBook<String?> = RuleBookBuilder.create(CategoryRuleBook::class.java)
            .withResultType(String::class.java)
            .withDefaultResult(null)
            .build() as CoRRuleBook<String?>
}