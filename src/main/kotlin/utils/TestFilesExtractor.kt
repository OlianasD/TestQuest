package utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import java.io.File
import java.nio.file.Path

object TestFilesExtractor {

    fun findTestFilePaths(project: Project): List<Path> {
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
