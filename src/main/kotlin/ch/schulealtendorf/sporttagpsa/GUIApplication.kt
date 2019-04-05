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

package ch.schulealtendorf.sporttagpsa

import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Desktop
import java.awt.Font
import java.awt.GridLayout
import java.net.URI
import java.util.*
import javax.imageio.ImageIO
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingUtilities
import kotlin.concurrent.thread

class ControlPanel : JFrame("PSA Control Panel") {

    private val statusLabel = JLabel("Status")
    private val statusText = JLabel("STOPPED")
    private val startButton = JButton("Start")
    private val stopButton = JButton("Stop")
    private val launchButton = JButton("Launch")
    private val quitButton = JButton("Quit")

    private val logo = ImageIcon().apply {
        image = ImageIO.read(ControlPanel::class.java.classLoader.getResourceAsStream("/img/psa-logo.png"))
    }

    private var context: Optional<ConfigurableApplicationContext> = Optional.empty()

    private val webappAddress = "http://localhost:8080"

    init {

        startButton.addActionListener { start() }

        stopButton.apply {
            isEnabled = false
            addActionListener { stop() }
        }

        launchButton.apply {
            isEnabled = false
            addActionListener { launch() }
        }

        quitButton.addActionListener { quit() }

        val panel = JPanel().apply {

            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)

            layout = BorderLayout()

            val north = JPanel().apply {

                layout = BoxLayout(this, BoxLayout.Y_AXIS)

                add(JLabel(logo).apply {
                    alignmentX = Component.CENTER_ALIGNMENT
                })
                add(JLabel("PSA Control Panel").apply {
                    alignmentX = Component.CENTER_ALIGNMENT
                    font = Font(Font.SANS_SERIF, 1, 20)
                })


            }


            val center = JPanel().apply {

                layout = GridLayout(1, 4)

                border = BorderFactory.createEmptyBorder(10, 5, 10, 5)

                add(statusLabel)
                add(statusText)
                add(startButton)
                add(stopButton)

            }

            val south = JPanel().apply {

                layout = GridLayout(2, 1)

                add(launchButton)

                add(quitButton)
            }

            add(north, BorderLayout.NORTH)
            add(center, BorderLayout.CENTER)
            add(south, BorderLayout.SOUTH)
        }

        setSize(350, 230)
        isResizable = false

        add(panel)
        isVisible = true
        defaultCloseOperation = EXIT_ON_CLOSE
    }

    private fun start() {

        thread {
            starting()

            context = Optional.of(
                    SpringApplicationBuilder(SporttagPsaApplication::class.java).run()
            )

            running()
        }
    }

    private fun stop() {
        thread {
            stopping()
            context.ifPresent { it.close() }
            context = Optional.empty()
            stopped()
        }
    }

    private fun launch() {

        val desktop = Optional.ofNullable(if (Desktop.isDesktopSupported()) Desktop.getDesktop() else null)
        if (desktop.isPresent && desktop.get().isSupported(Desktop.Action.BROWSE)) {
            desktop.get().browse(URI(webappAddress))
        }
    }

    private fun quit() {
        stop()
        System.exit(0)
    }

    private fun starting() {
        SwingUtilities.invokeLater {
            statusText.text = "STARTING..."
            startButton.isEnabled = false
        }
    }

    private fun running() {
        SwingUtilities.invokeLater {
            statusText.text = "RUNNING"
            stopButton.isEnabled = true
            launchButton.isEnabled = true
        }
    }

    private fun stopping() {
        SwingUtilities.invokeLater {
            statusText.text = "STOPPING..."
            stopButton.isEnabled = false
            launchButton.isEnabled = false
        }
    }

    private fun stopped() {
        SwingUtilities.invokeLater {
            statusText.text = "STOPPED"
            startButton.isEnabled = true
        }
    }
}

fun main(args: Array<String>) {
    SwingUtilities.invokeLater {
        ControlPanel()
    }
}
