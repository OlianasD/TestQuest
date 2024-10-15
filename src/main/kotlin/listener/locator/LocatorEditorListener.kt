import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileEvent
import com.intellij.openapi.vfs.VirtualFileListener
import com.intellij.openapi.vfs.VirtualFileManager
import locator.LocatorsExtractor
import locator.LocatorsFragilityCalculator
import testquest.TestQuestAction
import ui.GUIManager
import utils.TestFilesExtractor

object LocatorEditorListener : EditorFactoryListener, Disposable {

    private val tooltipListeners = mutableMapOf<Editor, LocatorTooltipListener>()
    private lateinit var proj: Project
    private var isRegistered = false

    fun registerListener(project: Project) {
        proj = project
        //this to save only test-related listeners
        if (!isRegistered) {
            EditorFactory.getInstance().allEditors.forEach { editor ->
                if (!tooltipListeners.containsKey(editor)) {
                    addTooltipListener(editor)
                }
            }
            EditorFactory.getInstance().addEditorFactoryListener(this, this)
            isRegistered = true
        }
    }

    private fun addTooltipListener(editor: Editor) {
        //check if listener is about test file
        val virtualFile = FileDocumentManager.getInstance().getFile(editor.document)
        if (virtualFile == null || !isTestFile(virtualFile.path))
            return
        //instantiate mouse listener, then retrieve static changes and evaluate fragility score
        val locatorScores = loadLocatorScores()
        val listener = LocatorTooltipListener(editor, locatorScores)
        tooltipListeners[editor] = listener
        editor.contentComponent.addMouseMotionListener(listener)
        VirtualFileManager.getInstance().addVirtualFileListener(object : VirtualFileListener {
            override fun contentsChanged(event: VirtualFileEvent) {
                val filePath = event.file.path
                if (isTestFile(filePath)) {
                    val testFilePaths = TestFilesExtractor.findTestFilePaths(proj)
                    val extractor = LocatorsExtractor()
                    TestQuestAction.locatorsOldStatic = TestQuestAction.locatorsNewStatic
                    TestQuestAction.locatorsNewStatic = testFilePaths.flatMap { extractor.parseLocators(it) }
                    val updatedScores = loadLocatorScores()
                    listener.updateLocatorScores(updatedScores)
                }
            }
        }, this)
    }

    private fun isTestFile(filePath: String): Boolean {
        return filePath.endsWith(".java") || filePath.endsWith(".kt")
    }

    private fun loadLocatorScores(): Map<String, Double> {
        val locatorScores = mutableMapOf<String, Double>()
        for (locator in TestQuestAction.locatorsNewStatic) {
            val locatorName = locator.locatorName
            if (locatorName != null) {
                val locFragilityCalc = LocatorsFragilityCalculator()
                val score = locFragilityCalc.calculateFragility(locator)
                locatorScores[locatorName] = score
            }
        }
        val locEstimator = LocatorsFragilityCalculator()
        val estimation = locEstimator.calculateOverallFragility(TestQuestAction.locatorsNewStatic)
        GUIManager.showOverallLocsFragilityScore(estimation)
        return locatorScores
    }

    override fun dispose() {
        tooltipListeners.forEach { (editor, listener) ->
            editor.contentComponent.removeMouseMotionListener(listener)
        }
        tooltipListeners.clear()
        isRegistered = false
    }
}
