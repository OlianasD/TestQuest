package listener.locator

import LocatorTooltipListener
import com.example.demo.TestQuestAction
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.editor.event.EditorFactoryEvent
import locator.LocatorsFragilityCalculator

class LocatorEditorListener : EditorFactoryListener, Disposable {

    override fun editorCreated(event: EditorFactoryEvent) {//left empty since it is otherwise activated BEFORE plugin
    }

    fun registerListener() {
        EditorFactory.getInstance().allEditors.forEach { editor ->
            val locatorScores = loadLocatorScores()
            val listener = LocatorTooltipListener(editor, locatorScores)
            editor.contentComponent.addMouseMotionListener(listener)
        }
        EditorFactory.getInstance().addEditorFactoryListener(this, this)
    }

    private fun loadLocatorScores(): Map<String, Double> {
        val locatorsNew = TestQuestAction.locatorsNew
        val locatorScores = mutableMapOf<String, Double>()
        for (locator in locatorsNew) {
            val locatorName = locator.locatorName
            if (locatorName != null) {
                val locFragilityCalc = LocatorsFragilityCalculator()
                val score = locFragilityCalc.calculateFragility(locator)
                locatorScores[locatorName] = score
            }
        }
        return locatorScores
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }
}
