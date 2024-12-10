
package listener.locator

import LocatorTooltipListener
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileEvent
import com.intellij.openapi.vfs.VirtualFileListener
import com.intellij.openapi.vfs.VirtualFileManager
import gamification.GamificationManager
import locator.Locator
import locator.LocatorsExtractor
import locator.LocatorsFragilityCalculator
import testquest.TestQuestAction
import ui.GUIManager
import utils.TestFilesExtractor


class LocatorChangeListener private constructor() : EditorFactoryListener, Disposable {

    companion object {
        private val instance = LocatorChangeListener()

        fun registerListener(project: Project) {
            instance.registerListenerInternal(project)
        }
    }

    private val tooltipListeners = mutableMapOf<Editor, LocatorTooltipListener>()
    private lateinit var proj: Project
    private var isRegistered = false

    fun registerListenerInternal(project: Project) {
        proj = project
        //this to save only test-related listeners
        if (!isRegistered) {
            EditorFactory.getInstance().allEditors.forEach { editor ->
                if (!tooltipListeners.containsKey(editor))
                    registerMouseListener(editor)
            }
            EditorFactory.getInstance().addEditorFactoryListener(this, this)
            manageChanges()
            isRegistered = true
        }
    }


    private fun registerMouseListener(testFile: Editor) {
        val virtualFile = FileDocumentManager.getInstance().getFile(testFile.document)
        if (virtualFile == null || !isTestFile(virtualFile.path) || tooltipListeners.containsKey(testFile)) {
            return
        }
        //compute locators score to associate with mouse tooltip for each test file
        val locatorScores = loadLocatorScores()
        val listener = LocatorTooltipListener(testFile, locatorScores)
        tooltipListeners[testFile] = listener
        testFile.contentComponent.addMouseMotionListener(listener)
    }



    private fun manageChanges() {
        //extract new locs, update scores, and update target dailies on changes over test files
        VirtualFileManager.getInstance().addVirtualFileListener(object : VirtualFileListener {
            override fun contentsChanged(event: VirtualFileEvent) {
                val filePath = event.file.path
                if (isTestFile(filePath)) {
                    val testFilePaths = TestFilesExtractor.findTestFilePaths(proj)
                    val extractor = LocatorsExtractor()
                    TestQuestAction.locatorsOldStatic = TestQuestAction.locatorsNewStatic
                    TestQuestAction.locatorsNewStatic = testFilePaths.flatMap { extractor.parseLocators(it) }
                    val updatedScores = loadLocatorScores()
                    tooltipListeners.values.forEach { listener ->
                        listener.updateLocatorScores(updatedScores)
                    }
                    if (GamificationManager.mode == GamificationManager.DailyAssignmentMode.TARGETED) {
                        GamificationManager.assignTargetDailies()
                    }
                }
            }
        }, this)
    }









    private fun isTestFile(filePath: String): Boolean {
        return filePath.endsWith(".java") || filePath.endsWith(".kt")
    }

    private fun loadLocatorScores(): Map<Locator, Double> {
        val locEstimator = LocatorsFragilityCalculator()
        val locatorScores = mutableMapOf<Locator, Double>()
        for (locator in TestQuestAction.locatorsNewStatic) {
            val score = locEstimator.calculateFragility(locator)
            locatorScores[locator] = score
        }
        val estimation = locEstimator.calculateOverallFragility(TestQuestAction.locatorsNewStatic)
        GUIManager.showOverallLocsFragilityScore(estimation)
        GUIManager.showLocatorScores(proj, locatorScores)
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
