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

    private const val OLD_DATA_PATH = "C:\\Users\\User\\Desktop\\demo\\progress" //TODO: check path
    private const val FIXED_PENDING_PATH = "C:\\Users\\User\\Desktop\\demo\\pending" //TODO: check path


    fun saveOldData() {
        try {
            ObjectOutputStream(FileOutputStream(OLD_DATA_PATH + PluginData.userProfileId + ".txt")).use { oos ->
                //oos.writeObject(TestQuestAction.locatorsOldStatic)
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
            val file = File(OLD_DATA_PATH + PluginData.userProfileId + ".txt")
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

    fun destroyOldData() {
        var file = File(FIXED_PENDING_PATH + PluginData.userProfileId + ".txt")
        if (file.exists())
            file.delete()
        file = File(OLD_DATA_PATH + PluginData.userProfileId + ".txt")
        if (file.exists())
            file.delete()
    }



    fun saveFixedAndPendingLocators(targetedFixedAndPendingLocators:  MutableMap<String, MutableList<Locator>>) {
        try {
            ObjectOutputStream(FileOutputStream(FIXED_PENDING_PATH + PluginData.userProfileId + ".txt")).use { oos ->
                oos.writeObject(targetedFixedAndPendingLocators)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun loadFixedAndPendingLocators(): MutableMap<String, MutableList<Locator>>? {
        var loadedData: MutableMap<String, MutableList<Locator>>? = null
        try {
            val file = File(FIXED_PENDING_PATH + PluginData.userProfileId + ".txt")
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



    fun getMostRecentSavedDataTime(): String {
        val oldDataPath = Paths.get(OLD_DATA_PATH + PluginData.userProfileId + ".txt")
        val fixedPendingPath = Paths.get(FIXED_PENDING_PATH + PluginData.userProfileId + ".txt")
        val oldDataCreationTime = getFileCreationTime(oldDataPath)
        val fixedPendingCreationTime = getFileCreationTime(fixedPendingPath)
        if (oldDataCreationTime != null) {
            if (fixedPendingCreationTime != null) {
                if (oldDataCreationTime.isAfter(fixedPendingCreationTime)) {
                    return formatDate(oldDataCreationTime)
                } else if (oldDataCreationTime.isBefore(fixedPendingCreationTime)) {
                    return formatDate(fixedPendingCreationTime)
                } else {
                    return formatDate(oldDataCreationTime)
                }
            }
            else
                return formatDate(oldDataCreationTime)
        }
        else if (fixedPendingCreationTime!=null)
            return formatDate(fixedPendingCreationTime)
        return ""
    }

    private fun getFileCreationTime(path: java.nio.file.Path): Instant? {
        return try {
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
