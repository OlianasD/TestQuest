package utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import java.nio.file.Path
import java.nio.file.Paths

object TestFilesExtractor {




    fun findTestFilePaths(project: Project): List<Path> {
        val rootDirectories = ProjectRootManager.getInstance(project).contentRoots
        val testFilePaths = mutableListOf<Path>()

        for (root in rootDirectories) {
            val srcDirectory = root.findChild("src")
            if (srcDirectory != null && srcDirectory.isDirectory) {
                exploreDirectory(srcDirectory, testFilePaths)
            } else {
                println("Folder 'src' not found in ${root.path}")
            }
        }
        return testFilePaths
    }

    private fun exploreDirectory(directory: VirtualFile, testFilePaths: MutableList<Path>) {
        for (child in directory.children) {
            if (child.isDirectory) {
                exploreDirectory(child, testFilePaths)
            } else if (child.name.endsWith("Test.java") || child.name.endsWith("Page.java")) {
                testFilePaths.add(Paths.get(child.path))
            }
        }
    }









}
