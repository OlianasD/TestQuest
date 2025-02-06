package listener.daily

import testquest.PluginData
import com.intellij.openapi.components.Service
import gamification.DailyManager
import gamification.GamificationManager
import utils.FilePathSolver
import utils.XMLReader
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

@Service(Service.Level.PROJECT)
class DailyExpirationListener {

    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    private var waitTime: Long = 1
    private var dailyTS: Long = -1

    init {
        scheduleNextRun()
    }

    private fun scheduleNextRun() {
        scheduler.schedule({
            if (PluginData.userProfileId.isNotEmpty()) {
                removeExpiredDailiesFromXML()
            }
            scheduleNextRun()
        }, waitTime, TimeUnit.MILLISECONDS)
    }

    fun dispose() {
        scheduler.shutdown()
    }

    private fun removeExpiredDailiesFromXML() {
        val xmlFile = FilePathSolver.getUserDataFile()
        if (!xmlFile.exists()) {
            println("File not found: ${xmlFile.absolutePath}")
            return
        }
        //load userProfile
        val xmlReader = XMLReader()
        val tempUserProfile =
            xmlReader.loadUserProfileFromXML(GamificationManager.usersDataFile, PluginData.userProfileId) //TODO: test multiple users
        if(tempUserProfile==null)
            return
        //compute currentTime and dailyTime
        dailyTS = tempUserProfile.timestamp //expire daily time is retrieved
        val currentTime = System.currentTimeMillis()
        val twentyFourHoursInMillis = 24 * 60 * 60 * 1000
        val diffTime = currentTime - dailyTS
        //if dailies are expired, remove them and assign new ones, then updates the GUI
        if (diffTime > twentyFourHoursInMillis) {
            DailyManager.reassignRandomDailiesFromExpire(tempUserProfile)
            GamificationManager.userProfile = tempUserProfile
            waitTime = twentyFourHoursInMillis.toLong() //set next time to check TODO: test when dailies expire
        } else {
            waitTime = twentyFourHoursInMillis - diffTime //set next time to check TODO: test when dailies expire
        }
    }
}