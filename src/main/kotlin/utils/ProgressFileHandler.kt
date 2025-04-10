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
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

//this class manage the progress made by user that still needs to be validated through test execution

object ProgressFileHandler {




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
        val userId = PluginData.userProfileId
        val filesToDelete = listOf(
            FilePathSolver.getSavedPendingLocsFile(userId),
            FilePathSolver.getSavedOldDataFile(userId),
            FilePathSolver.getSavedPendingPOsFile(userId),
            FilePathSolver.getSavedInfeasibleLocatorsFile(userId),
        )
        filesToDelete.forEach { file ->
            if (file.exists()) {
                file.delete()
            }
        }
        val snapshotsFolder = FilePathSolver.getSnapshotFolder(userId)
        if (snapshotsFolder.exists() && snapshotsFolder.isDirectory) {
            snapshotsFolder.listFiles()?.forEach { it.delete() }
            snapshotsFolder.delete()
        }
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








    //this method saves locator snapshots after each change
    fun saveLocatorsSnapshot(updatedScores: Map<Locator, Double>, overallEstimation: Double) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
        val timestamp = LocalDateTime.now().format(formatter)
        val file = File(FilePathSolver.SNAPSHOTS_FOLDER, "$timestamp.txt")
        try {
            FileWriter(file).use { writer ->
                for (locator in TestQuestAction.locatorsNew) {
                    val score = updatedScores[locator] ?: "N/A"
                    writer.write("Locator Name: ${locator.locatorName}\n")
                    writer.write("Locator Type: ${locator.locatorType}\n")
                    writer.write("Locator Value: ${locator.locatorValue}\n")
                    writer.write("Class Name: ${locator.className}\n")
                    writer.write("Method Name: ${locator.methodName}\n")
                    writer.write("Line: ${locator.line}\n")
                    writer.write("Count Changes: ${locator.countChanges}\n")
                    writer.write("Score: $score\n")
                    writer.write("\n")
                }
                writer.write("\n# TOTAL LOCATORS: ${TestQuestAction.locatorsNew.size}")
                writer.write("\n# OVERALL FRAGILITY: $overallEstimation\n")
            }
        } catch (_: IOException) {
        }
    }




    fun updateLocatorsCounterFromLatestSnapshot() {
        val snapshotsFolder = File(FilePathSolver.SNAPSHOTS_FOLDER.toString())
        val latestFile = snapshotsFolder.listFiles()?.maxByOrNull { it.lastModified() } ?: return
        val extractedLocators = mutableListOf<Locator>()
        var currentLocator: Locator? = null
        //build locators from saved data
        latestFile.forEachLine { line ->
            when {
                line.startsWith("Locator Name:") -> {
                    val locatorName = line.substringAfter(": ").trim()
                    currentLocator = Locator(
                        locatorName = if (locatorName == "null") null else locatorName,
                        locatorType = "",
                        locatorValue = "",
                        className = "",
                        methodName = "",
                        line = 0,
                        locatorPosition = 0,
                        filePath = "",
                        countChanges = 0
                    )
                }
                line.startsWith("Locator Type:") -> currentLocator = currentLocator?.copy(locatorType = line.substringAfter(": ").trim())
                line.startsWith("Locator Value:") -> currentLocator = currentLocator?.copy(locatorValue = line.substringAfter(": ").trim())
                line.startsWith("Class Name:") -> currentLocator = currentLocator?.copy(className = line.substringAfter(": ").trim())
                line.startsWith("Method Name:") -> currentLocator = currentLocator?.copy(methodName = line.substringAfter(": ").trim())
                line.startsWith("Line:") -> currentLocator = currentLocator?.copy(line = line.substringAfter(": ").trim().toIntOrNull() ?: 0)
                line.startsWith("Count Changes:") -> {
                    currentLocator = currentLocator?.copy(countChanges = line.substringAfter(": ").trim().toIntOrNull() ?: 0)
                }
                line.startsWith("Score:") -> {
                    if (currentLocator != null)
                        extractedLocators.add(currentLocator!!)
                    currentLocator = null
                }
            }
        }
        TestQuestAction.locatorsNew = TestQuestAction.locatorsNew.map { newLocator ->
            //get new loc corresponding to saved one
            val savedLocator = extractedLocators.find { it.hashCode() == newLocator.hashCode() }
            if (savedLocator != null) {
                //if locator type or value is changed
                if (savedLocator.locatorType != newLocator.locatorType || savedLocator.locatorValue != newLocator.locatorValue)
                    //update counter
                    newLocator.copy(countChanges = savedLocator.countChanges + 1)
                else
                    newLocator.copy(countChanges = savedLocator.countChanges)
            } else
                newLocator
        }
    }



    //this method saves infeasible locator to file (i.e., locators that cannot be improved for certain tasks)
    fun saveInfeasibleLocators(targetLoc: Locator) {
        //get all saved infeasible locs and keep those that still exist
        val savedInfeasibleLocs = loadInfeasibleLocators() ?: emptyList()
        val existingInfeasibleLocs = savedInfeasibleLocs
            .filter { saved ->
                TestQuestAction.locatorsNew.any { it.hashCode() == saved.hashCode() }
            }
            .toMutableList()
        val existingIndex = existingInfeasibleLocs.indexOfFirst { it.hashCode() == targetLoc.hashCode() }
        if (existingIndex >= 0) {
            //if target locator exists, update it
            val existing = existingInfeasibleLocs[existingIndex]
            existing.feasible.putAll(targetLoc.feasible)
            //if target locator is feasible in all tasks, remove it from the list
            if (existing.feasible.values.all { it }) {
                existingInfeasibleLocs.removeAt(existingIndex)
            }
        }
        else {
            //if target locator does not exist, add it to the list if it has at least a task as infeasible
            if (targetLoc.feasible.values.any { !it }) {
                existingInfeasibleLocs.add(targetLoc)
            }
        }
        try {
            val file = FilePathSolver.getSavedInfeasibleLocatorsFile(PluginData.userProfileId)
            ObjectOutputStream(FileOutputStream(file)).use { oos ->
                oos.writeObject(existingInfeasibleLocs)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun loadInfeasibleLocators(): List<Locator>? {
        var infeasibleLocs: List<Locator>? = null
        try {
            val file = FilePathSolver.getSavedInfeasibleLocatorsFile(PluginData.userProfileId)
            ObjectInputStream(FileInputStream(file)).use { ois ->
                infeasibleLocs = ois.readObject() as? List<Locator>
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return infeasibleLocs
    }











}
