package utils

import extractor.locator.Locator
import extractor.pageobject.PageObject
import extractor.test.PageObjectCall
import testquest.PluginData
import testquest.TestQuestAction
import java.io.*
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

//this class manage the progress made by user that still needs to be validated through test execution

object UserProgressFileHandler {




    fun saveProgressData() {
        try {
            val file = FilePathSolver.getSavedProgressFile(PluginData.userProfileId)
            ObjectOutputStream(FileOutputStream(file)).use { oos ->
                oos.writeObject(TestQuestAction.locatorsOld)
                oos.writeObject(TestQuestAction.POsOld)
                oos.writeObject(TestQuestAction.POCallsOld)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadProgressData() {
        try {
            val file = FilePathSolver.getSavedProgressFile(PluginData.userProfileId)
            ObjectInputStream(FileInputStream(file)).use { ois ->
                val locatorsOld = ois.readObject() as List<Locator>
                val POsOld = ois.readObject() as List<PageObject>
                val POCallsOld = ois.readObject() as Map<String, List<PageObjectCall>>
                //TestQuestAction.locatorsOldStatic = locatorsOldStatic
                TestQuestAction.locatorsOld = locatorsOld
                TestQuestAction.POsOld = POsOld
                TestQuestAction.POCallsOld = POCallsOld
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun destroySavedData() {
        var file = FilePathSolver.getSavedPendingsFile(PluginData.userProfileId)
        if (file.exists())
            file.delete()
        file = FilePathSolver.getSavedProgressFile(PluginData.userProfileId)
        if (file.exists())
            file.delete()
    }



    fun saveFixedAndPendingData(targetedFixedAndPendingLocators:  MutableMap<String, MutableList<Locator>>) {
        try {
            val file = FilePathSolver.getSavedPendingsFile(PluginData.userProfileId)
            ObjectOutputStream(FileOutputStream(file)).use { oos ->
                oos.writeObject(targetedFixedAndPendingLocators)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadFixedAndPendingData(): MutableMap<String, MutableList<Locator>>? {
        var loadedData: MutableMap<String, MutableList<Locator>>? = null
        try {
            val file = FilePathSolver.getSavedPendingsFile(PluginData.userProfileId)
            ObjectInputStream(FileInputStream(file)).use { ois ->
                loadedData = ois.readObject() as? MutableMap<String, MutableList<Locator>>
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return loadedData
    }



    fun getMostRecentSavedData(): String {
        val progressFile = FilePathSolver.getSavedProgressFile(PluginData.userProfileId)
        val pendingsFile = FilePathSolver.getSavedPendingsFile(PluginData.userProfileId)
        val progressCreationTime = getFileCreationTime(progressFile)
        val fixedPendingCreationTime = getFileCreationTime(pendingsFile)
        if (progressCreationTime != null) {
            if (fixedPendingCreationTime != null) {
                if (progressCreationTime.isAfter(fixedPendingCreationTime)) {
                    return formatDate(progressCreationTime)
                } else if (progressCreationTime.isBefore(fixedPendingCreationTime)) {
                    return formatDate(fixedPendingCreationTime)
                } else {
                    return formatDate(progressCreationTime)
                }
            }
            else
                return formatDate(progressCreationTime)
        }
        else if (fixedPendingCreationTime!=null)
            return formatDate(fixedPendingCreationTime)
        return ""
    }

    private fun getFileCreationTime(file: File): Instant? {
        return try {
            val path = file.toPath()
            if (Files.exists(path)) {
                val attrs = Files.readAttributes(path, BasicFileAttributes::class.java)
                attrs.creationTime().toInstant()
            } else {
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun formatDate(instant: Instant): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
            .withZone(ZoneId.systemDefault())
        return formatter.format(instant)
    }



}
