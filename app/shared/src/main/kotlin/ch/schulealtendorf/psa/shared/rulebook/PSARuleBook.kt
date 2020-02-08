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

package ch.schulealtendorf.psa.shared.rulebook

import ch.schulealtendorf.psa.shared.rulebook.rules.BasicRuleBook
import org.springframework.stereotype.Component

/**
 * Contains all the rules that are used in a PSA sport tournament.
 *
 * @author nmaerchy
 * @version 1.1.0
 */
@Component
class PSARuleBook : BasicRuleBook<FormulaModel, Int>(
        FormulaModel::class,
        Int::class
), ResultRuleBook {

    init {
        addRuleSet(SprintRuleSet())
        addRuleSet(SkippingRuleSet())
        addRuleSet(TargetThrowingRuleSet())
        addRuleSet(BroadJumpRuleSet())
        addRuleSet(BallThrowingRuleSet())
        addRuleSet(BasketThrowingRuleSet())
    }

    /**
     * Runs a fact in this rulebook and calculates
     * the result depending on it.
     *
     * The result is at least 1.
     *
     * @param fact the fact to run
     *
     * @return the resulting result
     */
    override fun calc(fact: FormulaModel): Int {
        val result = run(fact) ?: 1
        return if (result < 1) 1 else result
    }
}