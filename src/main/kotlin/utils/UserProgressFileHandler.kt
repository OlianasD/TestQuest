package utils

import extractor.locator.Locator
import extractor.pageobject.PageObject
import extractor.test.PageObjectCall
import testquest.PluginData
import testquest.TestQuestAction
import java.io.*
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.Files
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

//this class manage the progress made by user that still needs to be validated through test execution

object UserProgressFileHandler {




    fun saveOldData() {
        try {
            val file = FilePathSolver.getSavedOldDataFile(PluginData.userProfileId)
            ObjectOutputStream(FileOutputStream(file)).use { oos ->
                oos.writeObject(TestQuestAction.locatorsOld)
                oos.writeObject(TestQuestAction.POsOld)
                oos.writeObject(TestQuestAction.POCallsOld)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadOldData() {
        try {
            val file = FilePathSolver.getSavedOldDataFile(PluginData.userProfileId)
            ObjectInputStream(FileInputStream(file)).use { ois ->
                val locatorsOld = ois.readObject() as List<Locator>
                val POsOld = ois.readObject() as List<PageObject>
                val POCallsOld = ois.readObject() as Map<String, List<PageObjectCall>>
                TestQuestAction.locatorsOld = locatorsOld
                TestQuestAction.POsOld = POsOld
                TestQuestAction.POCallsOld = POCallsOld
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun destroySavedData() {
        var file = FilePathSolver.getSavedPendingLocsFile(PluginData.userProfileId)
        if (file.exists())
            file.delete()
        file = FilePathSolver.getSavedOldDataFile(PluginData.userProfileId)
        if (file.exists())
            file.delete()
        file = FilePathSolver.getSavedPendingPOsFile(PluginData.userProfileId)
        if (file.exists())
            file.delete()
    }



    fun saveFixedAndPendingLocsData(targetedFixedAndPendingLocators:  MutableMap<String, MutableList<Locator>>) {
        try {
            val file = FilePathSolver.getSavedPendingLocsFile(PluginData.userProfileId)
            ObjectOutputStream(FileOutputStream(file)).use { oos ->
                oos.writeObject(targetedFixedAndPendingLocators)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadFixedAndPendingLocsData(): MutableMap<String, MutableList<Locator>>? {
        var loadedData: MutableMap<String, MutableList<Locator>>? = null
        try {
            val file = FilePathSolver.getSavedPendingLocsFile(PluginData.userProfileId)
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

    fun saveFixedAndPendingPOsData(targetedFixedAndPendingPOs:  MutableMap<String, MutableList<Any>>) {
        try {
            val file = FilePathSolver.getSavedPendingPOsFile(PluginData.userProfileId)
            ObjectOutputStream(FileOutputStream(file)).use { oos ->
                oos.writeObject(targetedFixedAndPendingPOs)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadFixedAndPendingPOsData(): MutableMap<String, MutableList<Any>>? {
        var loadedData: MutableMap<String, MutableList<Any>>? = null
        try {
            val file = FilePathSolver.getSavedPendingPOsFile(PluginData.userProfileId)
            ObjectInputStream(FileInputStream(file)).use { ois ->
                loadedData = ois.readObject() as? MutableMap<String, MutableList<Any>>
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return loadedData
    }







    fun getMostRecentSavedData(): String {
        val progressFile = FilePathSolver.getSavedOldDataFile(PluginData.userProfileId)
        val pendingLocsFile = FilePathSolver.getSavedPendingLocsFile(PluginData.userProfileId)
        val pendingPOsFile = FilePathSolver.getSavedPendingPOsFile(PluginData.userProfileId)
        val progressCreationTime = getFileCreationTime(progressFile)
        val fixedPendingCreationTime = getFileCreationTime(pendingLocsFile)
        val pendingPOsCreationTime = getFileCreationTime(pendingPOsFile)
        val mostRecentTime = listOfNotNull(progressCreationTime, fixedPendingCreationTime, pendingPOsCreationTime)
            .maxOrNull()
        return mostRecentTime?.let { formatDate(it) } ?: ""
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
