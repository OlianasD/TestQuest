package testquest

import listener.changes.CodeChangeListener
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import gamification.GamificationManager
import extractor.locator.Locator
import extractor.locator.LocatorsExtractor
import analyzer.locator.LocatorsFragilityCalculator
import extractor.pageobject.MethodInfo
import extractor.pageobject.PageObject
import extractor.pageobject.PageObjectExtractor
import extractor.test.PageObjectCall
import extractor.test.PageObjectCallExtractor
import listener.test.TestExecutionListener
import ui.GUIManager
import utils.FilePathSolver
import utils.TestFilesExtractor
import utils.ProgressFileHandler
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object PluginData {
    var userProfileId: String = ""
}


object WindowStateManager {
    var isWindowOpen = false
}





class TestQuestAction : AnAction() {


    override fun actionPerformed(e: AnActionEvent) {

        //to avoid re-opening the plugin
        if (WindowStateManager.isWindowOpen) {
            Messages.showMessageDialog("Test Quest is already open.", "Warning", Messages.getWarningIcon())
            return
        }
        WindowStateManager.isWindowOpen = true

        //get Test.java and Page.java files from project
        val project = e.project ?: return
        val testFilePaths = TestFilesExtractor.findTestFilePaths(project)

        //if test files exist
        if (testFilePaths.isNotEmpty()) {

            //extract locators (i.e., from classes named as _Test.java or _Page.java)
            val locExtractor = LocatorsExtractor()
            locatorsNew = testFilePaths.flatMap { locExtractor.parseLocators(it) }

            //extract PageObjects (i.e., from classes named as _Page.java)
            val poExtractor = PageObjectExtractor()
            POsNew = testFilePaths
                .filter { it.fileName.toString().endsWith("Page.java") }
                //.map { filePath -> poExtractor.parsePageObject(filePath, locatorsNewStatic) }
                .map { filePath -> poExtractor.parsePageObject(filePath, locatorsNew) }

            //extract PageObjects calls about PO usages in Tests (if any, from classes named as _Test.java)
            val poCallsExtractor = PageObjectCallExtractor()
            POCallsNew = testFilePaths
                .filter { it.fileName.toString().endsWith("Test.java") }
                .flatMap { filePath ->
                    poCallsExtractor.parsePOCalls(filePath.toFile()).entries
                }
                .associate { it.key to it.value }

            //asks the user if they want locator-based gamification mode (i.e., only dailies about locators)
            //or advanced gamification mode (i.e., all dailies)
            val locatorMode = GUIManager.showGamificationModeChoice()
            if (locatorMode)
                GamificationManager.gamificationMode = GamificationManager.GamificationMode.LOCATOR
            else
                GamificationManager.gamificationMode = GamificationManager.GamificationMode.ADVANCED

            //setup gamification profile
            val gamificationManager = GamificationManager()
            gamificationManager.showGUI()
            PluginData.userProfileId = "001" //TODO: change as a login
            gamificationManager.setupUserProfile(PluginData.userProfileId)

            //previous untested progress is loaded from file and considered as "old" state to compare changes with, if exists and the user wants to.
            // if not, old is set to new
            val savedDataTime = ProgressFileHandler.getMostRecentSavedData()
            val useSavedData = GUIManager.showWindowStoredDataChoice(savedDataTime)
            if (useSavedData) {
                ProgressFileHandler.loadOldData()
                if (locatorsOld.isEmpty())
                    locatorsOld = locatorsNew
                if (POsOld.isEmpty())
                    POsOld = POsNew
                if (POCallsOld.isEmpty())
                    POCallsOld = POCallsNew
            } else {
                ProgressFileHandler.destroySavedData()
                locatorsOld = locatorsNew
                POsOld = POsNew
                POCallsOld = POCallsNew
            }
            ProgressFileHandler.saveOldData()

            //estimate overall fragility and show it on GUI
            val locEstimator = LocatorsFragilityCalculator()
            val estimation = locEstimator.calculateOverallFragility(locatorsNew)
            GUIManager.showOverallLocsFragilityScore(estimation)

            //create locators snapshot folder for logging purposes, if not exists
            FilePathSolver.createSnapshotsFolder(PluginData.userProfileId)

            //register listeners
            CodeChangeListener.registerListener(project)
            TestExecutionListener.registerListener(project)
        } else {
            Messages.showMessageDialog(
                "Test files not found at $testFilePaths under project $project",
                e.presentation.text,
                Messages.getInformationIcon()
            )
        }
    }

    companion object {
        //this to store locators info before-after changes
        var locatorsOld: List<Locator> = listOf()
        var locatorsNew: List<Locator> = listOf()
        //this to store PO info before-after changes
        var POsOld: List<PageObject> = listOf()
        var POsNew: List<PageObject> = listOf()
        var emptyPOs : ArrayList<PageObject> = arrayListOf()
        var emptyReturnType : MutableSet<MethodInfo> = mutableSetOf()
        var duplicatedMethods : MutableSet<MethodInfo> = mutableSetOf()
        //this to store PO calls in tests before-after changes
        var POCallsOld: Map<String, List<PageObjectCall>> = emptyMap()
        var POCallsNew: Map<String, List<PageObjectCall>> = emptyMap()

    }
}
