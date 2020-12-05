package ch.schulealtendorf.psa

import ch.schulealtendorf.psa.components.BuildInfoPanel
import ch.schulealtendorf.psa.components.ControlPanel
import ch.schulealtendorf.psa.components.HeaderPanel
import ch.schulealtendorf.psa.components.TrayPopup
import ch.schulealtendorf.psa.components.VersionPanel
import java.awt.BorderLayout
import java.awt.SystemTray
import java.awt.TrayIcon
import java.util.ResourceBundle
import javax.imageio.ImageIO
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTabbedPane
import javax.swing.SwingUtilities

class MainFrame(
    private val args: Array<String>
) : JFrame() {
    private val logo = ImageIcon().apply {
        image = ImageIO.read(MainFrame::class.java.getResourceAsStream("/psa-logo.png"))
    }

    private val i18n = ResourceBundle.getBundle("main-frame")

    init {
        title = i18n.getString("title")

        if (SystemTray.isSupported()) {
            val trayIcon = TrayIcon(logo.image)
            val tray = SystemTray.getSystemTray()
            trayIcon.popupMenu = TrayPopup()
            tray.add(trayIcon)
        }

        val mainPanel = JPanel().apply {
            border = BorderFactory.createEmptyBorder(15, 5, 5, 5)
            layout = BorderLayout()

            val headerPanel = HeaderPanel(title, logo)
            add(headerPanel, BorderLayout.NORTH)

            val tabbedPane = JTabbedPane().apply {
                add(i18n.getString("tab.application"), ControlPanel(args))
                add(i18n.getString("tab.buildinfo"), BuildInfoPanel())
            }
            add(tabbedPane, BorderLayout.CENTER)

            add(VersionPanel(), BorderLayout.SOUTH)
        }

        setSize(400, 500)
        isResizable = false

        add(mainPanel)
        isVisible = true
        defaultCloseOperation = EXIT_ON_CLOSE
    }
}

fun main(args: Array<String>) {
    SwingUtilities.invokeLater {
        MainFrame(args)
    }
}
