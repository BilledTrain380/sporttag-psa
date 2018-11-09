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

package ch.schulealtendorf.sporttagpsa.controlpanel

import ch.schulealtendorf.sporttagpsa.SporttagPsaApplication
import javafx.application.HostServices
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.Label
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext
import java.net.URL
import java.util.*

class FXMLControlPanelController: Initializable {

    private var hostServices: Optional<HostServices> = Optional.empty()

    private var context: Optional<ConfigurableApplicationContext> = Optional.empty()

    @FXML
    private var statusText: Label? = null

    @FXML
    private var startButton: Button? = null

    @FXML
    private var stopButton: Button? = null

    @FXML
    private var launchButton: Button? = null

    private val webappAddress = "http://localhost:8080"

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        stopButton?.isVisible = false
        launchButton?.isDisable = true

        Runtime.getRuntime().addShutdownHook(Thread {
            stop()
        })
    }

    fun setHostServices(hostServices: HostServices) {
        this.hostServices = Optional.of(hostServices)
    }

    @FXML
    fun start() {

        Thread {

            starting()

            val builder = SpringApplicationBuilder(SporttagPsaApplication::class.java)
            context = Optional.of(builder.run())

            running()

        }.start()
    }

    @FXML
    fun stop() {

        Thread {

            stopping()

            context.ifPresent { it.close() }
            context = Optional.empty()

            stopped()


        }.start()
    }

    @FXML
    fun launch() {
        hostServices.ifPresent { it.showDocument(webappAddress) }
    }

    @FXML
    fun quit() {
        stop()
        System.exit(0)
    }

    private fun starting() {

        Platform.runLater {
            statusText?.text = "STARTING"
            startButton?.isDisable = true
        }

    }

    private fun running() {

        Platform.runLater {

            statusText?.text = "RUNNING"
            statusText?.styleClass?.addAll("text", "success")

            startButton?.isVisible = false
            stopButton?.isVisible = true
            launchButton?.isDisable = false
        }
    }

    private fun stopping() {

        Platform.runLater {

            statusText?.text = "STOPPING"
            statusText?.styleClass?.removeAll("text", "success")

            stopButton?.isVisible = false
            startButton?.isVisible = true
            launchButton?.isDisable = true
        }
    }

    private fun stopped() {

        Platform.runLater {
            statusText?.text = "STOPPED"
            startButton?.isDisable = false
        }
    }
}