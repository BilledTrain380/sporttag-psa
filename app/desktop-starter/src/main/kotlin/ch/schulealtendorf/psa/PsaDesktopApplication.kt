package ch.schulealtendorf.psa

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ConfigurableApplicationContext
import java.awt.BorderLayout
import java.awt.Component
import java.awt.Desktop
import java.awt.Font
import java.awt.GridLayout
import java.net.URI
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
import kotlin.system.exitProcess

@SpringBootApplication
class PsaApplication

class ControlPanel(
    private val args: Array<String>
) : JFrame("PSA Control Panel") {
    private val statusLabel = JLabel("Status")
    private val statusText = JLabel("STOPPED")
    private val startButton = JButton("Start")
    private val stopButton = JButton("Stop")
    private val launchButton = JButton("Launch")
    private val quitButton = JButton("Quit")

    private val logo = ImageIcon().apply {
        image = ImageIO.read(ControlPanel::class.java.getResourceAsStream("/psa-logo.png"))
    }

    private var context: ConfigurableApplicationContext? = null

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

                add(
                    JLabel(logo).apply {
                        alignmentX = Component.CENTER_ALIGNMENT
                    }
                )
                add(
                    JLabel("PSA Control Panel").apply {
                        alignmentX = Component.CENTER_ALIGNMENT
                        font = Font(Font.SANS_SERIF, 1, 20)
                    }
                )
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

        setSize(400, 230)
        isResizable = false

        add(panel)
        isVisible = true
        defaultCloseOperation = EXIT_ON_CLOSE
    }

    private fun start() {
        thread {
            starting()
            context = SpringApplicationBuilder(PsaApplication::class.java)
                .profiles("standalone")
                .run(*args)
            running()
        }
    }

    private fun stop() {
        thread {
            stopping()
            context?.close()
            context = null
            stopped()
        }
    }

    private fun launch() {
        val desktop = if (Desktop.isDesktopSupported()) Desktop.getDesktop() else null
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
            desktop.browse(URI(webappAddress))
    }

    private fun quit() {
        stop()
        exitProcess(0)
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
        ControlPanel(args)
    }
}
