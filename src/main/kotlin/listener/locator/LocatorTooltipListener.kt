import testquest.TestQuestAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.popup.Balloon
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener
import javax.swing.Timer
import ui.GUIManager

class LocatorTooltipListener(
    private val editor: Editor,
    private var locatorScores: Map<String, Double>
) : MouseMotionListener {

    private var hoverTimer: Timer? = null
    private var currentBalloon: Balloon? = null

    override fun mouseMoved(e: MouseEvent) {
        //close any actual balloon if present
        hoverTimer?.stop()
        currentBalloon?.hide()

        //retrieve mouse position to hook balloon with
        val mousePoint = e.point
        val logicalPosition = editor.xyToLogicalPosition(mousePoint)
        val currentLine = logicalPosition.line + 1

        //if mouse position is a locator, show the score and balloon
        val locatorName = TestQuestAction.locatorsNewStatic.find { it.line == currentLine }?.locatorName
        if (locatorName != null && locatorScores.containsKey(locatorName)) {
            val score = locatorScores[locatorName]!!
            hoverTimer = Timer(500) {
                GUIManager.showBalloon(e, locatorName, score)
            }
            hoverTimer?.start()
        }
    }

    override fun mouseDragged(e: MouseEvent?) {
    }

    fun updateLocatorScores(newScores: Map<String, Double>) {
        locatorScores = newScores
    }
}
