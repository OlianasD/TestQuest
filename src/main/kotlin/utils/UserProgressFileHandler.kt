package utils

import extractor.locator.Locator
import extractor.pageobject.PageObject
import extractor.test.PageObjectCall
import testquest.PluginData
import testquest.TestQuestAction
import java.io.*
import java.util.*


//this class manage the progress made by user that still needs to be validated through test execution

object UserProgressFileHandler {

    private const val FILE_PATH = "C:\\Users\\User\\Desktop\\demo\\progress" //TODO: check path


    fun writeProgress() {
        try {
            ObjectOutputStream(FileOutputStream(FILE_PATH + PluginData.userProfileId + ".txt")).use { oos ->
                oos.writeObject(TestQuestAction.locatorsOldStatic)
                oos.writeObject(TestQuestAction.POsOld)
                oos.writeObject(TestQuestAction.POCallsOld)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //TODO: put a condition whether reading is desired
    fun readProgress() {
        try {
            val file = File(FILE_PATH + PluginData.userProfileId + ".txt")
            if (file.exists()) {
                ObjectInputStream(FileInputStream(file)).use { ois ->
                    val locatorsOldStatic = ois.readObject() as List<Locator>
                    val POsOld = ois.readObject() as List<PageObject>
                    val POCallsOld = ois.readObject() as Map<String, List<PageObjectCall>>
                    TestQuestAction.locatorsOldStatic = locatorsOldStatic
                    TestQuestAction.POsOld = POsOld
                    TestQuestAction.POCallsOld = POCallsOld
                    file.delete()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }
}
