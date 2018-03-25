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

package ch.schulealtendorf.sporttagpsa.entity

import ch.schulealtendorf.sporttagpsa.controller.model.SimpleCompetitorModel
import ch.schulealtendorf.sporttagpsa.controller.model.SportModel
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertEquals

/**
 * @author nmaerchy
 * @version 0.0.1
 */
@RunWith(JUnitPlatform::class)
object EntityMappingsKtSpec: Spek({

    val townEntity: TownEntity = TownEntity(1, "8000", "Musterhausen")
    val clazzEntity: ClazzEntity = ClazzEntity(1, "1a", TeacherEntity(1, "teacher"))
    val sportEntity: SportEntity = SportEntity(1, "Brennball")
    
    describe("an EntityMapping for a CompetitorEntity") {
        
        on("a CompetitorEntity with a SportEntity") {
            
            val competitorEntity: CompetitorEntity = CompetitorEntity(1, "Muster", "Hans", true, 1, "address",
                    townEntity,
                    clazzEntity,
                    sportEntity)
            
            it("should map the CompetitorEntity to a SimpleCompetitorModel with the SportModel") {
                val expected: SimpleCompetitorModel = SimpleCompetitorModel(1, "Muster", "Hans", true, "address", SportModel(1, "Brennball"))
                assertEquals(expected, competitorEntity.map())
            }
        }
        
        on("a CompetitorEntity with no SportEntity") {

            val competitorEntity: CompetitorEntity = CompetitorEntity(1, "Muster", "Hans", true, 1, "address",
                    townEntity,
                    clazzEntity,
                    null)
            
            it("should map the CompetitorEntity to a SimpleCompetitorModel with an empty SportModel") {
                val expected: SimpleCompetitorModel = SimpleCompetitorModel(1, "Muster", "Hans", true, "address", SportModel())
                assertEquals(expected, competitorEntity.map())
            }
        }
        
        on("a CompetitorEntity with no id") {

            val competitorEntity: CompetitorEntity = CompetitorEntity(null, "Muster", "Hans", true, 1, "address",
                    townEntity,
                    clazzEntity,
                    null)
            
            it("should map the CompetitorEntity to a SimpleCompetitorModel with 0 as id") {
                val expected: SimpleCompetitorModel = SimpleCompetitorModel(0, "Muster", "Hans", true, "address", SportModel())
                assertEquals(expected, competitorEntity.map())
            }
        }
        
        on("merging a SimpleCompetitorModel") {
            
            val competitorEntity: CompetitorEntity = CompetitorEntity(1, "Muster", "Hans", false, 1, "address", townEntity, clazzEntity, sportEntity)
            val competitorModel: SimpleCompetitorModel = SimpleCompetitorModel(2, "merged Muster", "merged Hans", true, "merged address")
            
            competitorEntity.merge(competitorModel)
            
            it("should only merge primitive properties without the id") {
                val expected: CompetitorEntity = CompetitorEntity(1, "merged Muster", "merged Hans", true, 1, "merged address", townEntity, clazzEntity, sportEntity)
                assertEquals(expected, competitorEntity)
            }
        }
    }
    
    describe("an EntityMapping for a SportEntity") {
        
        on("a SportEntity with no id") {

            val noIdsportEntity: SportEntity = SportEntity(null, "Brennball")
            
            it("should map the SportEntity to a SportModel with 0 as id") {
                val expected: SportModel = SportModel(0, "Brennball")
                assertEquals(expected, noIdsportEntity.map())
            }
        }
    }
})