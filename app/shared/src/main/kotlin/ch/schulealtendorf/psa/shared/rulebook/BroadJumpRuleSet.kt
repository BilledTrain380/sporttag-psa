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

import ch.schulealtendorf.psa.dto.participation.GenderDto
import ch.schulealtendorf.psa.dto.participation.athletics.WEITSPRUNG
import ch.schulealtendorf.psa.shared.rulebook.rules.RuleSet

/**
 * Defines all the rules that can be applied to a borad jump.
 *
 * @author nmaerchy
 * @version 1.0.0
 */
class BroadJumpRuleSet : RuleSet<FormulaModel, Int>() {

    /**
     * @return true if the rules of this rule set can be used, otherwise false
     */
    override val whenever: (FormulaModel) -> Boolean = { it.discipline == WEITSPRUNG }

    init {

        addRule(
            object : FormulaRule() {
                override val formula: (Double) -> Int =
                    { if (it < 1.81) 1 else ((220.628792 * (((it * 100) - 180) / 100)) pow 1.0).toInt() }

                override val whenever: (FormulaModel) -> Boolean = { it.gender == GenderDto.FEMALE }
            }
        )

        addRule(
            object : FormulaRule() {
                override val formula: (Double) -> Int =
                    { if (it < 1.91) 1 else ((180.85908 * (((it * 100) - 190) / 100)) pow 1.0).toInt() }

                override val whenever: (FormulaModel) -> Boolean = { it.gender == GenderDto.MALE }
            }
        )
    }
}
