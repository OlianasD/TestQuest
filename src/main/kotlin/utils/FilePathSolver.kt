package utils

import com.intellij.openapi.application.PathManager
import java.io.File


object FilePathSolver {

    private val BASE_PLUGIN_PATH = File(PathManager.getConfigPath(), "testquest")
    const val USER_PROPIC_PATH = "pics/user/default-user.png"
    lateinit var SNAPSHOTS_FOLDER: File //locators snapshots folder to keep track of locs changes over time
    //TODO: change following paths so to manage multiple icons
    const val DAILY_PICS_PATH = "pics/daily/default-daily.png"
    const val ACHIEVEMENT_PICS_PATH = "pics/achievement/default-achievement.png"


    fun getUserDataFile(): File {
        BASE_PLUGIN_PATH.mkdirs()
        val file = File(BASE_PLUGIN_PATH, "users.xml")
        if (!file.exists()) {
            file.createNewFile()
            val xmlWriter = XMLWriter()
            xmlWriter.createUsersFile(file)
        }
        return file
    }

    fun getSavedOldDataFile(user: String): File {
        val userFolder = File(BASE_PLUGIN_PATH, user)
        if (!userFolder.exists())
            userFolder.mkdir()
        return File(userFolder, "oldData.txt")//old locs, POs, PO calls
    }

    fun getSavedPendingLocsFile(user: String): File {
        val userFolder = File(BASE_PLUGIN_PATH, user)
        if (!userFolder.exists())
            userFolder.mkdir()
        return File(userFolder, "pendingLocs.txt")
    }

    fun getSavedPendingPOsFile(user: String): File {
        val userFolder = File(BASE_PLUGIN_PATH, user)
        if (!userFolder.exists())
            userFolder.mkdir()
        return File(userFolder, "pendingPOs.txt")
    }


    fun createSnapshotsFolder(user: String) {
        val userFolder = File(BASE_PLUGIN_PATH, user)
        if (!userFolder.exists())
            userFolder.mkdir()
        SNAPSHOTS_FOLDER = File(userFolder, "snapshots")
        if (!SNAPSHOTS_FOLDER.exists())
            SNAPSHOTS_FOLDER.mkdir()
    }





}