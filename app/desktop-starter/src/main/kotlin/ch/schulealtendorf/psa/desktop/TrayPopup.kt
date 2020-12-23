package ch.schulealtendorf.psa.desktop

import java.awt.MenuItem
import java.awt.PopupMenu
import java.util.ResourceBundle
import kotlin.system.exitProcess

class TrayPopup : PopupMenu() {
    private val i18n = ResourceBundle.getBundle("tray-popup")

    init {
        val quitItem = MenuItem(i18n.getString("label.quit")).apply {
            addActionListener {
                PsaApplicationContext.stop()
                exitProcess(0)
            }
        }
        add(quitItem)
    }
}
