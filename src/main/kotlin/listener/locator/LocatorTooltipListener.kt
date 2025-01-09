import testquest.TestQuestAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.util.PsiTreeUtil
import locator.Locator
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionListener
import javax.swing.Timer
import ui.GUIManager

class LocatorTooltipListener(
    private val editor: Editor,
    private var locatorScores: Map<Locator, Double>
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

        //retrieve class name of mouse position
        val project = editor.project ?: return
        val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.document) ?: return
        val topLevelClass = PsiTreeUtil.findChildOfType(psiFile, PsiClass::class.java)
        val className = topLevelClass?.name ?: "Unknown Class"

        //if mouse position is a locator (identified by mouse position and class), show the score and balloon
        //TODO: beware of multiple test class files with same name
        val locator = TestQuestAction.locatorsNewStatic.find { it.line == currentLine && it.className == className }
        if (locatorScores.containsKey(locator)) {
            val score = locatorScores[locator]!!
            hoverTimer = Timer(500) {
                GUIManager.showBalloon(e, locator!!.locatorName, score)
            }
            hoverTimer?.start()
        }
    }

    override fun mouseDragged(e: MouseEvent?) {
    }

    fun updateLocatorScores(newScores: Map<Locator, Double>) {
        locatorScores = newScores
    }
}
