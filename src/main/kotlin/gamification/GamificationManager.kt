package gamification

import locator.Locator
import ui.GUIManager
import utils.XMLReader
import utils.XMLWriter
import java.util.*

class GamificationManager() {



    companion object {
        var usersDataFile: String = "C:\\Users\\User\\Desktop\\demo\\users.xml"
        var unknownUserPic : String = "C:\\Users\\User\\Desktop\\demo\\pics\\user\\default-user.png"
        lateinit var userProfile: UserProfile //the current user
        var guiManager: GUIManager = GUIManager()

        private val allTitles = mutableListOf(
            Title("Novice", 0),
            Title("Apprentice", 20),
            Title("Adept", 50),
            Title("Initiate", 100),
            Title("Acolyte", 200),
            Title("Journeyman", 300),
            Title("Arcanist", 400),
            Title("Sorcerer", 500),
            Title("Magister", 600),
            Title("Magus", 700),
            Title("Master of Arcane Arts", 800),
            Title("Archmage", 900),
            Title("Grand Sorcerer", 1000),
            Title("High Arcanist", 1200),
            Title("Elder Magus", 1400),
            Title("Celestial Archmage", 1600),
            Title("Astral Wizard", 1800),
            Title("Epic Conjurer", 2000),
            Title("Mana Creator", 2500),
            Title("Supreme Magus", 3000)
        )


        //called when a daily is removed, a user profile name is changed, or a propic is changed
        fun updateUserProfileAfterGUIChanges(userProfile: UserProfile){
            val xmlWriter = XMLWriter()
            xmlWriter.saveUserProfileToXML(usersDataFile, userProfile)
        }

        fun updateProgresses(locatorsOld: List<Locator>, locatorsNew: List<Locator>, userProfile: UserProfile) {
            val xmlWriter = XMLWriter()
            val dailyUpdates = DailyManager.updateDailies(userProfile, locatorsOld, locatorsNew)//for each assigned daily, check
            val achUpdates = AchievementManager.updateAchievements(userProfile, locatorsOld, locatorsNew)
            if(dailyUpdates || achUpdates) {
                updateTitleAndLvl(userProfile)
                xmlWriter.saveUserProfileToXML(usersDataFile, userProfile)
                guiManager.updateGUI(GamificationManager.userProfile, true)
            }
            guiManager.updateGUI(GamificationManager.userProfile, false)
        }

        private fun updateTitleAndLvl(userProfile: UserProfile) {
            val newTitle = allTitles
                .filter { it.xp <= userProfile.currentXP }
                .maxByOrNull { it.xp }
            if (newTitle != null && userProfile.title != newTitle.name) {
                userProfile.title = newTitle.name
                userProfile.level++
                val currentIndex = allTitles.indexOfFirst { it.name == userProfile.title }
                if (currentIndex != -1 && currentIndex + 1 < allTitles.size)
                    userProfile.nextXP = allTitles[currentIndex + 1].xp
            }
            else if (newTitle == null)
                userProfile.nextXP = Int.MAX_VALUE
        }

    }


    //upload user profile data from file if they exist or create a new user profile if they do not
    fun setupUserProfile(ID: String) {
        val xmlReader = XMLReader()
        val xmlWriter = XMLWriter()
        val tempUserProfile = xmlReader.loadUserProfileFromXML(usersDataFile, ID)
        if(tempUserProfile == null) {
            userProfile = UserProfile(
                id = ID,
                name = "John Doe",
                level = 1,
                currentXP = allTitles[0].xp,
                nextXP = allTitles[1].xp,
                title = allTitles[0].name,
                achievementProgresses = mutableListOf(),
                dailyProgresses = mutableListOf(),
                completedAchievements = mutableListOf(),
                propic = unknownUserPic
            )
            DailyManager.setupDailies(userProfile)
            AchievementManager.setupAchievements(userProfile)
            xmlWriter.addNewUserProfileToXML(usersDataFile, userProfile)
        }
        else
            userProfile = tempUserProfile
        guiManager.updateGUI(userProfile, false)
    }

    fun generateUniqueId(): String {
        val uuid = UUID.randomUUID()
        return uuid.toString()
    }

    fun showGUI() {
        guiManager.showGUI()
    }


}
