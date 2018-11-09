/*
 * Copyright (c) 2018 by Nicolas Märchy
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

package ch.schulealtendorf.sporttagpsa

import ch.schulealtendorf.sporttagpsa.controlpanel.FXMLControlPanelController
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage


/**
 * JavaFX Application to enable the control panel.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
class FXApplication: Application() {

    override fun start(primaryStage: Stage?) {

        val loader = FXMLLoader(javaClass.getResource("/fxml/control-panel/control-panel.fxml"))
        val root: Parent = loader.load()
        loader.getController<FXMLControlPanelController>().setHostServices(hostServices)

        val scene = Scene(root, 300.0, 270.0)

        primaryStage?.title = "PSA Control Panel"
        primaryStage?.scene = scene
        primaryStage?.isResizable = false
        primaryStage?.show()

        primaryStage?.setOnCloseRequest {
            System.exit(0)
        }
    }
}

fun main(args: Array<String>) {
    Application.launch(FXApplication::class.java, *args)
}