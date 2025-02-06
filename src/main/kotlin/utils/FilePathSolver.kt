package utils

import com.intellij.openapi.application.PathManager
import java.io.File


object FilePathSolver {

    private val BASE_PLUGIN_PATH = File(PathManager.getConfigPath(), "testquest")
    const val USER_PROPIC_PATH = "pics/user/default-user.png"
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


    fun getSavedProgressFile(user: String): File {
        val userFolder = File(BASE_PLUGIN_PATH, user)
        if (!userFolder.exists())
            userFolder.mkdir()
        return File(userFolder, "progress.txt")
    }

    fun getSavedPendingsFile(user: String): File {
        val userFolder = File(BASE_PLUGIN_PATH, user)
        if (!userFolder.exists())
            userFolder.mkdir()
        return File(userFolder, "pending.txt")
    }




}