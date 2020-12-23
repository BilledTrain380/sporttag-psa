package ch.schulealtendorf.psa.desktop

import java.awt.Color
import java.awt.Desktop
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.net.URI
import java.util.ResourceBundle
import javax.swing.BorderFactory
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingUtilities
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class ControlPanel(
    private val args: Array<String>
) : JPanel() {
    private val i18n = ResourceBundle.getBundle("control-panel")
    private val webappAddress = "http://localhost:8080"

    private val statusLabel = JLabel(i18n.getString("label.status"))
    private val statusText = JLabel(i18n.getString("status.stopped"))
    private val startButton = JButton(i18n.getString("label.start"))
    private val stopButton = JButton(i18n.getString("label.stop"))
    private val launchButton = JButton(i18n.getString("label.launch"))
    private val quitButton = JButton(i18n.getString("label.quit"))
    private val openLogsButton = JButton(i18n.getString("label.logs"))

    init {
        setupActionListeners()
        border = BorderFactory.createEmptyBorder(20, 20, 20, 20)
        layout = GridBagLayout()

        add(
            statusLabel,
            GridBagConstraints().apply {
                weightx = 0.5
                anchor = GridBagConstraints.FIRST_LINE_START
                gridx = 0
                gridy = 0
            }
        )
        add(
            statusText,
            GridBagConstraints().apply {
                weightx = 0.5
                anchor = GridBagConstraints.FIRST_LINE_END
                gridx = 1
                gridy = 0
            }
        )
        add(
            startButton,
            GridBagConstraints().apply {
                weightx = 0.5
                gridwidth = 2
                insets = Insets(20, 0, 10, 0)
                fill = GridBagConstraints.HORIZONTAL
                gridx = 0
                gridy = 1
            }
        )
        add(
            stopButton,
            GridBagConstraints().apply {
                weightx = 0.5
                gridwidth = 2
                insets = Insets(10, 0, 10, 0)
                fill = GridBagConstraints.HORIZONTAL
                gridx = 0
                gridy = 2
            }
        )
        add(
            launchButton,
            GridBagConstraints().apply {
                weightx = 0.5
                gridwidth = 2
                insets = Insets(10, 0, 10, 0)
                fill = GridBagConstraints.HORIZONTAL
                gridx = 0
                gridy = 3
            }
        )

        add(
            openLogsButton,
            GridBagConstraints().apply {
                weightx = 0.5
                gridwidth = 2
                insets = Insets(10, 0, 10, 0)
                fill = GridBagConstraints.HORIZONTAL
                gridx = 0
                gridy = 4
            }
        )

        add(
            quitButton,
            GridBagConstraints().apply {
                weightx = 0.5
                gridwidth = 2
                insets = Insets(10, 0, 10, 0)
                fill = GridBagConstraints.HORIZONTAL
                gridx = 0
                gridy = 5
            }
        )
    }

    private fun setupActionListeners() {
        startButton.addActionListener { start() }

        stopButton.apply {
            isEnabled = false
            addActionListener { stop() }
        }

        launchButton.apply {
            isEnabled = false
            addActionListener { launch() }
        }

        openLogsButton.addActionListener {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(PsaApplicationContext.logsDirectory.toFile())
            }
        }

        quitButton.addActionListener { quit() }
    }

    private fun start() {
        thread {
            try {
                starting()
                PsaApplicationContext.start(args)
                running()
            } catch (ex: Exception) {
                failed()
            }
        }
    }

    private fun stop() {
        thread {
            stopping()
            PsaApplicationContext.stop()
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
            statusText.text = i18n.getString("status.starting")
            statusText.background = Color.ORANGE
            statusText.isOpaque = true
            startButton.isEnabled = false
        }
    }

    private fun running() {
        SwingUtilities.invokeLater {
            statusText.text = i18n.getString("status.running")
            statusText.background = Color.GREEN
            stopButton.isEnabled = true
            launchButton.isEnabled = true
        }
    }

    private fun stopping() {
        SwingUtilities.invokeLater {
            statusText.text = i18n.getString("status.stopping")
            statusText.background = Color.ORANGE
            stopButton.isEnabled = false
            statusText.isOpaque = false
            launchButton.isEnabled = false
        }
    }

    private fun stopped() {
        SwingUtilities.invokeLater {
            statusText.text = i18n.getString("status.stopped")
            statusText.isOpaque = false
            startButton.isEnabled = true
        }
    }

    private fun failed() {
        SwingUtilities.invokeLater {
            statusText.text = i18n.getString("status.failed")
            statusText.background = Color.RED
            statusText.isOpaque = true
            startButton.isEnabled = true
        }
    }
}
