
package listener.changes

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
import extractor.locator.Locator
import extractor.locator.LocatorsExtractor
import analyzer.locator.LocatorsFragilityCalculator
import extractor.pageobject.PageObjectExtractor
import extractor.test.PageObjectCallExtractor
import testquest.TestQuestAction
import ui.DailyWindowManager
import ui.GUIManager
import utils.ProgressFileHandler
import utils.TestFilesExtractor


class CodeChangeListener private constructor() : EditorFactoryListener, Disposable {

    companion object {
        val instance = CodeChangeListener()

        fun registerListener(project: Project) {
            instance.registerListenerInternal(project)
        }

    }

    private val locatorTooltipListeners = mutableMapOf<Editor, LocatorTooltipListener>()
    private lateinit var proj: Project
    private var isRegistered = false
    private var fileListener: VirtualFileListener? = null

    private var initialSnapshot = false
    private var locatorScores: MutableMap<Locator, Double> = mutableMapOf()


    fun registerListenerInternal(project: Project) {
        proj = project
        //this to save only test-related listeners
        if (!isRegistered) {
            EditorFactory.getInstance().allEditors.forEach { editor ->
                if (!locatorTooltipListeners.containsKey(editor))
                    registerMouseListener(editor)
            }
            EditorFactory.getInstance().addEditorFactoryListener(this, this)
            manageChanges()
            isRegistered = true
        }
    }

    // this to update locator scores once mouse is moved
    private fun registerMouseListener(testFile: Editor) {
        val virtualFile = FileDocumentManager.getInstance().getFile(testFile.document)
        if (virtualFile == null || !isTestRelatedFile(virtualFile.path) || locatorTooltipListeners.containsKey(testFile)) {
            return
        }
        //compute locators score to associate with mouse tooltip for each test file and create initial locators snapshot
        if(!initialSnapshot) {
            val (locatorScores, overallEstimation) = loadLocatorScores()
            this.locatorScores = locatorScores
            ProgressFileHandler.updateLocatorsCounterFromLatestSnapshot()
            ProgressFileHandler.saveLocatorsSnapshot(locatorScores, overallEstimation)
        }
        initialSnapshot = true
        val listener = LocatorTooltipListener(testFile, locatorScores)
        locatorTooltipListeners[testFile] = listener
        testFile.contentComponent.addMouseMotionListener(listener)
    }

    //this to update all locator scores, and retrieve updated locators/PO info, once code changes occur
    private fun manageChanges() {
        //extract new locs and POs, update scores, and update target dailies on changes over test files
        //VirtualFileManager.getInstance().addVirtualFileListener(object : VirtualFileListener {
        fileListener = object : VirtualFileListener {
            override fun contentsChanged(event: VirtualFileEvent) {
                val filePath = event.file.path

                if (isTestRelatedFile(filePath)) {
                    val testFilePaths = TestFilesExtractor.findTestFilePaths(proj)

                    //updates locators/POs/PO calls from classes named as _Test.java or _Page.java, and compute new fragility scores
                    val extractor = LocatorsExtractor()
                    TestQuestAction.locatorsNew = testFilePaths.flatMap { extractor.parseLocators(it) }
                    val (updatedScores, overallEstimation) = loadLocatorScores()
                    locatorTooltipListeners.values.forEach { listener ->
                        listener.updateLocatorScores(updatedScores)
                    }

                    //extract PageObjects (i.e., from classes named as _Page.java)
                    val poExtractor = PageObjectExtractor()
                    TestQuestAction.POsNew = testFilePaths
                        .filter { it.fileName.toString().endsWith("Page.java") }
                        .map { po -> poExtractor.parsePageObject(po, TestQuestAction.locatorsNew) }

                    //needed for the daily emptyPOs
                    var newEmptyPOs = TestQuestAction.POsNew.filter {newPO ->
                        newPO.methods.isEmpty() && !(TestQuestAction.emptyPOs.any {it.name == newPO.name}) && !(TestQuestAction.POsOld.any{it.name == newPO.name})}
                    TestQuestAction.emptyPOs.addAll(newEmptyPOs)

                    //needed for the daily missingRetPOMethods
                    var voidReturnMethods = TestQuestAction.POsNew.flatMap { po ->
                        po.methods.filter { it.returnType.equals("void", ignoreCase = true) }.map { it }
                    }
                    TestQuestAction.emptyReturnType.addAll(voidReturnMethods)

                    //extract PageObject calls from Tests (if any, from classes named as _Test.java)
                    val poCallsExtractor = PageObjectCallExtractor()
                    TestQuestAction.POCallsNew = testFilePaths
                        .filter { it.fileName.toString().endsWith("Test.java") }
                        .flatMap { fp ->
                            poCallsExtractor.parsePOCalls(fp.toFile()).entries
                        }
                        .associate { it.key to it.value }

                    //reassign targeted dailies based on changes
                    if (GamificationManager.assignmentMode == GamificationManager.DailyAssignmentMode.TARGETED) {
                        GamificationManager.assignTargetDailies()
                    }

                    //update opened windows about targeted dailies
                    DailyWindowManager.closeAllWindows()

                    //update counters of changed locators and create a new snapshot
                    ProgressFileHandler.updateLocatorsCounterFromLatestSnapshot()
                    ProgressFileHandler.saveLocatorsSnapshot(updatedScores, overallEstimation)


                }
            }
        }
        VirtualFileManager.getInstance().addVirtualFileListener(fileListener!!, this)
    }


    //only changes over test cases and page objects are considered
    private fun isTestRelatedFile(filePath: String): Boolean {
        return filePath.endsWith("Test.java") || filePath.endsWith("Page.java")
    }

    private fun loadLocatorScores(): Pair<MutableMap<Locator, Double>, Double> {
        val locEstimator = LocatorsFragilityCalculator()
        val locatorScores = mutableMapOf<Locator, Double>()
        for (locator in TestQuestAction.locatorsNew) {
            val score = locEstimator.calculateFragility(locator)
            locatorScores[locator] = score
        }
        val estimation = locEstimator.calculateOverallFragility(TestQuestAction.locatorsNew)
        GUIManager.showOverallLocsFragilityScore(estimation)
        GUIManager.showLocatorScores(proj, locatorScores)
        return Pair(locatorScores, estimation)
    }

    override fun dispose() {
        locatorTooltipListeners.forEach { (editor, listener) ->
            listener.dispose()
            editor.contentComponent.removeMouseMotionListener(listener)
        }
        locatorTooltipListeners.clear()
        fileListener?.let { VirtualFileManager.getInstance().removeVirtualFileListener(it) }
        fileListener = null
        isRegistered = false
    }
}
