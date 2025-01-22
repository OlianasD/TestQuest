package testquest

import listener.changes.CodeChangeListener
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import gamification.GamificationManager
import extractor.locator.Locator
import extractor.locator.LocatorsExtractor
import locator.LocatorsFragilityCalculator
import extractor.pageobject.PageObject
import extractor.pageobject.PageObjectExtractor
import extractor.test.PageObjectCall
import extractor.test.PageObjectCallExtractor
import ui.GUIManager
import utils.TestFilesExtractor

object PluginData {
    var userProfileId: String = ""
}

class TestQuestAction : AnAction() {


    override fun actionPerformed(e: AnActionEvent) {

        //get Test.java and Page.java files from project
        val project = e.project ?: return
        val testFilePaths = TestFilesExtractor.findTestFilePaths(project)

        if (testFilePaths.isNotEmpty()) {

            //extract locators (i.e., from classes named as _Test.java or _Page.java)
            val locExtractor = LocatorsExtractor()
            locatorsNewStatic = testFilePaths.flatMap { locExtractor.parseLocators(it) }
            locatorsOldDynamic = locatorsNewStatic //old dynamic locators store original info before test execution

            //extract PageObjects (i.e., from classes named as _Page.java)
            val poExtractor = PageObjectExtractor()
            POsNew = testFilePaths
                .filter { it.fileName.toString().endsWith("Page.java") }
                .map { filePath -> poExtractor.parsePageObject(filePath, locatorsNewStatic) }

            //extract PageObjects calls about PO usages in Tests (if any, from classes named as _Test.java)
            val poCallsExtractor = PageObjectCallExtractor()
            POCallsNew = testFilePaths
                .filter { it.fileName.toString().endsWith("Test.java") }
                .flatMap { filePath ->
                    poCallsExtractor.parsePOCalls(filePath.toFile()).entries
                }
                .associate { it.key to it.value }

            //setup gamification profile
            val gamificationManager = GamificationManager()
            gamificationManager.showGUI()
            PluginData.userProfileId = "003" //TODO: change as a login
            gamificationManager.setupUserProfile(PluginData.userProfileId)

            //estimate overall fragility and show it on GUI
            val locEstimator = LocatorsFragilityCalculator()
            val estimation = locEstimator.calculateOverallFragility(locatorsNewStatic)
            CodeChangeListener.registerListener(project)
            GUIManager.showOverallLocsFragilityScore(estimation)
        }

        else {
            Messages.showMessageDialog(
                "Test files not found at $testFilePaths under project $project",
                e.presentation.text,
                Messages.getInformationIcon()
            )
        }
    }

    companion object {
        //this to store changes that may affect fragility computation (computed statically)
        var locatorsOldStatic: List<Locator> = listOf()
        var locatorsNewStatic: List<Locator> = listOf()
        //this to store changes used for most tasks before-after test execution (computed dynamically)
        var locatorsOldDynamic: List<Locator> = listOf()
        var locatorsNewDynamic: List<Locator> = listOf()
        //this to store changes on PageObjects before-after test execution (computed dynamically)
        var POsNew: List<PageObject> = listOf()
        var POsOld: List<PageObject> = listOf()
        //this to store PO calls in tests before-after changes
        var POCallsNew: Map<String, List<PageObjectCall>> = emptyMap()
        var POCallsOld: Map<String, List<PageObjectCall>> = emptyMap()



    }
}
