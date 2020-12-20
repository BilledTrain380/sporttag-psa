package ch.schulealtendorf.psa.desktop

import java.awt.Component
import java.awt.Font
import javax.swing.BoxLayout
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JPanel

class HeaderPanel(
    title: String,
    logo: ImageIcon
) : JPanel() {

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)

        add(
            JLabel(logo).apply {
                alignmentX = Component.CENTER_ALIGNMENT
            }
        )
        add(
            JLabel(title).apply {
                alignmentX = Component.CENTER_ALIGNMENT
                font = Font(Font.SANS_SERIF, 1, 20)
            }
        )
    }
}
