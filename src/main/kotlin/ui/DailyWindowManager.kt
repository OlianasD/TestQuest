package ui

import javax.swing.JButton
import javax.swing.JFrame

object DailyWindowManager {

    //list of opened frames about targeted dailies
    // they are saved here to keep track of those opened to close them when tests are run or code is changed
    val openDetailsFrames: MutableList<Pair<JFrame, JButton>> = mutableListOf()


    fun addWindow(frame: JFrame, button: JButton) {
        openDetailsFrames.add(Pair(frame, button))
    }

    fun removeWindow(frame: JFrame) {
        openDetailsFrames.removeAll { it.first == frame }
    }

    fun closeAllWindows() {
        openDetailsFrames.forEach { (frame, button) ->
            frame.dispose()
            button.text = "Show"
        }
        openDetailsFrames.clear()
    }
}
