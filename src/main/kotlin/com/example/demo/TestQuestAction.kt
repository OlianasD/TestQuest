package com.example.demo

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import gamification.GamificationManager
import locator.Locator
import locator.LocatorsExtractor
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
            // wait for login to update
            val extractor = LocatorsExtractor()
            locatorsNew = testFilePaths.flatMap { extractor.parseLocators(it) }
        } else {
            Messages.showMessageDialog(
                "Test files not found",
                actionText,
                Messages.getInformationIcon()
            )
        }
    }

    companion object {
        var locatorsNew: List<Locator> = listOf()
    }
}
