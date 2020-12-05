package ch.schulealtendorf.psa.components

import ch.schulealtendorf.psa.configuration.BuildInfo
import java.awt.BorderLayout
import javax.swing.JLabel
import javax.swing.JPanel

class VersionPanel : JPanel() {
    init {
        layout = BorderLayout()

        val versionLabel = JLabel("Version: ${BuildInfo.VERSION}")
        add(versionLabel, BorderLayout.WEST)
    }
}
