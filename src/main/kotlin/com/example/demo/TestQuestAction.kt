package com.example.demo

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import gamification.GamificationManager
import listener.locator.LocatorEditorListener
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
            val gamificationManager = GamificationManager()
            gamificationManager.showGUI()
            PluginData.userProfileId = "001" //TODO: change as a login
            gamificationManager.setupUserProfile(PluginData.userProfileId)
            //extract locators
            val extractor = LocatorsExtractor()
            locatorsNew = testFilePaths.flatMap { extractor.parseLocators(it) }
            //estimate overall fragility and show it on GUI

            val locEstimator = LocatorsFragilityCalculator()
            val estimation = locEstimator.calculateOverallFragility(locatorsNew)//TODO: this must be done after each change/run
            LocatorEditorListener().registerListener()

            GUIManager.showOverallLocsFragilityScore(estimation)
        } else {
            Messages.showMessageDialog(
                "Test files not found at $testFilePaths under project $project with action $actionText",
                actionText,
                Messages.getInformationIcon()
            )
        }
    }

    companion object {
        var locatorsOld: List<Locator> = listOf()
        var locatorsNew: List<Locator> = listOf()
    }
}
