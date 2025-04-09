package ui

import javax.swing.JButton
import javax.swing.JFrame

object DailyWindowManager {
    // Lista delle finestre aperte
    val openDetailsFrames: MutableList<Pair<JFrame, JButton>> = mutableListOf()

    // Aggiungi una finestra e il relativo bottone alla lista
    fun addWindow(frame: JFrame, button: JButton) {
        openDetailsFrames.add(Pair(frame, button))
    }

    // Rimuovi una finestra dalla lista
    fun removeWindow(frame: JFrame) {
        openDetailsFrames.removeAll { it.first == frame }
    }

    // Chiudi tutte le finestre aperte e aggiorna i bottoni
    fun closeAllWindows() {
        openDetailsFrames.forEach { (frame, button) ->
            frame.dispose() // Chiude la finestra
            button.text = "Show" // Ripristina il testo del bottone a "Show"
        }
        openDetailsFrames.clear() // Pulisce la lista delle finestre
    }
}
