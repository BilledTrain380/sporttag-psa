package ch.schulealtendorf.psa

import ch.schulealtendorf.psa.desktop.MainFrame
import javax.swing.SwingUtilities

fun main(args: Array<String>) {
    SwingUtilities.invokeLater {
        MainFrame(args)
    }
}
