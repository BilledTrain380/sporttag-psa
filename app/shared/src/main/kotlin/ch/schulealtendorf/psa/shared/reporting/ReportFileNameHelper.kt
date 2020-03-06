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

package ch.schulealtendorf.psa.shared.reporting

import ch.schulealtendorf.psa.dto.SportDto
import ch.schulealtendorf.psa.shared.reporting.participation.EventSheetConfig
import ch.schulealtendorf.psa.shared.reporting.ranking.DisciplineGroupConfig
import ch.schulealtendorf.psa.shared.reporting.ranking.DisciplineRankingConfig
import ch.schulealtendorf.psa.shared.reporting.ranking.TotalRankingConfig
import java.util.ResourceBundle

private val resourceBundle = ResourceBundle.getBundle("i18n.reporting")

fun pdfNameOf(config: SportDto) = "${resourceBundle.getString("file.name.participant-list")} ${config.name}.pdf"
fun pdfNameOf(config: EventSheetConfig) =
    "${resourceBundle.getString("file.name.event-sheets")} ${config.discipline.name} ${config.group.name} ${config.gender.text}.pdf"

fun pdfNameOf(config: DisciplineGroupConfig) =
    "${resourceBundle.getString("file.name.ranking")} ${config.gender.text} ${resourceBundle.getString("ranking.discipline-group")} ${config.year.value}.pdf"

fun csvNameOf(config: DisciplineGroupConfig) = "UBS - ${config.gender.text} - ${config.year.value}.csv"
fun pdfNameOf(config: DisciplineRankingConfig) =
    "${resourceBundle.getString("file.name.ranking")} ${config.gender.text} ${config.discipline.name} ${config.year.value}.pdf"

fun pdfNameOf(config: TotalRankingConfig) =
    "${resourceBundle.getString("file.name.ranking")} ${config.gender.text} ${resourceBundle.getString("ranking.total")} ${config.year.value}.pdf"
