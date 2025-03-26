package utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

object TestFilesExtractor {


    fun findTestFilePaths1(project: Project): List<Path> {
        val module = ProjectRootManager.getInstance(project).contentRoots.firstOrNull() ?: return emptyList()
        val srcDirectory = File(module.path, "src")
        val testFilePaths = mutableListOf<Path>()
        exploreDirectory1(srcDirectory, testFilePaths)
        return testFilePaths
    }


    private fun exploreDirectory1(directory: File, testFilePaths: MutableList<Path>) {
        val children = directory.listFiles() ?: return
        for (child in children) {
            if (child.isDirectory) {
                exploreDirectory1(child, testFilePaths)
            }
            //TODO: locators are currently extracted from files named _Test or _Page (in case of PageObjects)
            else if (child.isFile &&
                (child.name.endsWith("Test.java") || child.name.endsWith("Page.java"))
                ) {
                testFilePaths.add(child.toPath())
            }
        }
    }









    fun findTestFilePaths(project: Project): List<Path> {
        val rootDirectories = ProjectRootManager.getInstance(project).contentRoots
        val testFilePaths = mutableListOf<Path>()

        for (root in rootDirectories) {
            val srcDirectory = root.findChild("src")
            if (srcDirectory != null && srcDirectory.isDirectory) {
                exploreVirtualDirectory(srcDirectory, testFilePaths)
            } else {
                println("Folder 'src' not found in ${root.path}")
            }
        }
        return testFilePaths
    }

    private fun exploreVirtualDirectory(directory: VirtualFile, testFilePaths: MutableList<Path>) {
        for (child in directory.children) {
            if (child.isDirectory) {
                exploreVirtualDirectory(child, testFilePaths)
            } else if (child.name.endsWith("Test.java") || child.name.endsWith("Page.java")) {
                testFilePaths.add(Paths.get(child.path))
            }
        }
    }









}
