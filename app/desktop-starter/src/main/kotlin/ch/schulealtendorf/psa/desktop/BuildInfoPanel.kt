package ch.schulealtendorf.psa.desktop

import ch.schulealtendorf.psa.configuration.BuildInfo
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.util.ResourceBundle
import javax.swing.BorderFactory
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

class BuildInfoPanel : JPanel() {
    private var gridYPosition = 0

    private val i18n = ResourceBundle.getBundle("build-info-panel")

    init {
        layout = GridBagLayout()
        border = BorderFactory.createEmptyBorder(20, 20, 20, 20)

        addInfo(i18n.getString("buildinfo.version"), BuildInfo.VERSION)
        addInfo(i18n.getString("buildinfo.hash"), BuildInfo.HASH)
        addInfo(i18n.getString("buildinfo.buildtime"), BuildInfo.BUILD_TIME.toString())
        addInfo(i18n.getString("buildinfo.environment"), BuildInfo.ENVIRONMENT)
    }

    private fun addInfo(label: String, info: String) {
        val jLabel = JLabel(label, SwingConstants.RIGHT)
        val infoLabel = JLabel(info, SwingConstants.LEFT)
        add(
            jLabel,
            GridBagConstraints().apply {
                weightx = 0.5
                insets = Insets(0, 0, 0, 20)
                fill = GridBagConstraints.HORIZONTAL
                gridx = 0
                gridy = gridYPosition
            }
        )
        add(
            infoLabel,
            GridBagConstraints().apply {
                weightx = 0.5
                fill = GridBagConstraints.HORIZONTAL
                gridx = 1
                gridy = gridYPosition
            }
        )

        gridYPosition++
    }
}
