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

package ch.schulealtendorf.sporttagpsa.macos

import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import java.net.InetAddress

/**
 * Very minimalistic UI for platform with a GUI.
 * 
 * @author nmaerchy
 * @version 1.0.0
 */
class MacOSApplication: Application() {

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     *
     *
     * NOTE: This method is called on the JavaFX Application Thread.
     *
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set. The primary stage will be embedded in
     * the browser if the application was launched as an applet.
     * Applications may create other stages, if needed, but they will not be
     * primary stages and will not be embedded in the browser.
     */
    override fun start(primaryStage: Stage?) {
        
        primaryStage?.title = "Sporttag PSA"

        val ip = InetAddress.getLocalHost().hostAddress

        val root = GridPane()
        root.padding = Insets(10.0)
        root.vgap = 10.0
        root.hgap = 10.0

        val status = Label("Status: Running")
        GridPane.setConstraints(status, 0, 0)
        
        
        val port = Label("Port: 8080")
        GridPane.setConstraints(port, 0, 1)
        
        val address = Label("IP Adresse: $ip")
        GridPane.setConstraints(address, 0, 2)
        
        
        val open = Label("Öffnen Sie folgende Adresse im Browser: $ip:8080")
        GridPane.setConstraints(open, 0, 5)
        
        
        val quit = Button("Beenden")
        quit.setOnAction {
            System.exit(0)
        }
        GridPane.setConstraints(quit, 0, 10)
        
        root.children.addAll(status, port, address, open, quit)
        
        val scene = Scene(root, 450.0, 220.0)
        primaryStage?.scene = scene
        primaryStage?.show()
        
        primaryStage?.setOnCloseRequest {
            System.exit(0)
        }
    }
}