package com.example.demo

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.ui.Messages
import gamification.GamificationManager
import locator.LocatorsMonitor
import ui.GUIManager
import java.io.File
import java.nio.file.Path

class TestQuestAction : AnAction() {

    private var guiManager = GUIManager()

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val actionText = e.presentation.text
        val testFilePaths = findTestFilePaths(project)
        if (testFilePaths.isNotEmpty()) {
            guiManager.showGUI()
            // TODO: improve multiuser and DB-based persistency or make the path external as well as username
            val gamificationManager = GamificationManager("C:\\Users\\User\\Desktop\\users.xml")
            val userProfile = gamificationManager.setupUserProfile("John Doe")
            val monitor = LocatorsMonitor(gamificationManager, userProfile, guiManager, testFilePaths)
            monitor.startMonitoring()
        } else {
            Messages.showMessageDialog(
                "Test files not found",
                actionText,
                Messages.getInformationIcon()
            )
        }
    }

    private fun findTestFilePaths(project: Project): List<Path> {
        val module = ProjectRootManager.getInstance(project).contentRoots.firstOrNull() ?: return emptyList()
        val srcDirectory = File(module.path, "src")
        val testFilePaths = mutableListOf<Path>()
        exploreDirectory(srcDirectory, testFilePaths)
        return testFilePaths
    }

    private fun exploreDirectory(directory: File, testFilePaths: MutableList<Path>) {
        val children = directory.listFiles() ?: return
        for (child in children) {
            if (child.isDirectory) {
                exploreDirectory(child, testFilePaths)
            } else if (child.isFile && child.name.endsWith("Test.java")) { //TODO: only Test* files?
                testFilePaths.add(child.toPath())
            }
        }
    }
}
