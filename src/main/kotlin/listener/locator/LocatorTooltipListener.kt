import com.example.demo.TestQuestAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseMotionListener
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.awt.RelativePoint
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener

class LocatorTooltipListener(
    private val editor: Editor,
    private val locatorScores: Map<String, Double>
) : EditorMouseMotionListener, MouseMotionListener {

    override fun mouseMoved(event: EditorMouseEvent) {
        val caret = editor.caretModel.currentCaret
        val currentLine = caret.logicalPosition.line
        val locatorName = getLocatorNameFromLine(currentLine)
        if (locatorName != null && locatorScores.containsKey(locatorName)) {
            val score = locatorScores[locatorName]!!
            showTooltip(event.mouseEvent, score)
        } else {
            // Optionally show a tooltip when there's no locator score.
            showTooltip(event.mouseEvent, 666.0) // or consider not showing anything
        }
    }

    private fun showTooltip(event: MouseEvent, score: Double) {
        val tooltipText = "Fragility Score: ${String.format("%.2f", score)}"
        val popup = JBPopupFactory.getInstance().createMessage(tooltipText)
        val point = RelativePoint(event)
        popup.showInScreenCoordinates(event.component, point.screenPoint)
    }

    override fun mouseDragged(e: MouseEvent?) {
        TODO("Not yet implemented")
    }

    override fun mouseMoved(e: MouseEvent) {
        //val caret = editor.caretModel.currentCaret
        //val currentLine = caret.logicalPosition.line
        //val locatorName = getLocatorNameFromLine(currentLine)


        val mousePoint = e.point
        val logicalPosition = editor.xyToLogicalPosition(mousePoint)
        val currentLine = logicalPosition.line
        val locatorName = getLocatorNameFromLine(currentLine)

        if (locatorName != null && locatorScores.containsKey(locatorName)) {
            val score = locatorScores[locatorName]!!
            showTooltip(e, score)
        }
    }


    private fun getLocatorNameFromLine(line: Int): String? {
        return TestQuestAction.locatorsNew.find { it.line == line }?.locatorName
    }


}