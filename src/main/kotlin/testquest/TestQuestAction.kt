package testquest

import listener.locator.LocatorScoreListener
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import gamification.GamificationManager
import locator.Locator
import locator.LocatorsExtractor
import locator.LocatorsFragilityCalculator
import ui.GUIManager
import utils.TestFilesExtractor

object PluginData {
    var userProfileId: String = ""
}

class TestQuestAction : AnAction() {


    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val actionText = e.presentation.text
        val testFilePaths = TestFilesExtractor.findTestFilePaths(project)
        if (testFilePaths.isNotEmpty()) {

            //setup gamification profile
            val gamificationManager = GamificationManager()
            gamificationManager.showGUI()
            PluginData.userProfileId = "001" //TODO: change as a login
            gamificationManager.setupUserProfile(PluginData.userProfileId)

            //extract locators
            val extractor = LocatorsExtractor()
            locatorsNewStatic = testFilePaths.flatMap { extractor.parseLocators(it) }
            locatorsOldDynamic = locatorsNewStatic //old dynamic = locators at the beginning or those after each run

            //estimate overall fragility and show it on GUI
            val locEstimator = LocatorsFragilityCalculator()
            val estimation = locEstimator.calculateOverallFragility(locatorsNewStatic)
            LocatorScoreListener.registerListener(project)
            GUIManager.showOverallLocsFragilityScore(estimation)
        }
        else {
            Messages.showMessageDialog(
                "Test files not found at $testFilePaths under project $project with action $actionText",
                actionText,
                Messages.getInformationIcon()
            )
        }
    }

    companion object {
        //this to store static changes that may affect fragility computation but are not considered for most
        //tasks as they need to be dynamically validated first
        var locatorsOldStatic: List<Locator> = listOf()
        var locatorsNewStatic: List<Locator> = listOf()
        //this to store dynamic changes used for most tasks before-after test execution
        var locatorsOldDynamic: List<Locator> = listOf()
        var locatorsNewDynamic: List<Locator> = listOf()


    }
}
