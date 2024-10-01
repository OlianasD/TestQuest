package gamification

import listener.TestOutcome
import ui.GUIManager
import utils.XMLReader
import utils.XMLWriter
import java.util.*

class GamificationManager() {


    // random = random daily assignment
    // targeted = daily assignment based on problems and bad practices observed by analysing test artifacts
    // inclusive = daily assignment to cover functionalities not covered by test artifacts, given the DOM
    enum class DailyAssignmentMode {
        random, targeted, inclusive
    }

    companion object {
        var usersDataFile: String = "C:\\Users\\User\\Desktop\\demo\\users.xml" //TODO: path
        var unknownUserPic : String = "C:\\Users\\User\\Desktop\\demo\\pics\\user\\default-user.png" //TODO: path
        lateinit var userProfile: UserProfile //the current user
        var mode: DailyAssignmentMode = DailyAssignmentMode.random //this flag is initially set to random and can be changed via GUI

        private val allTitles = mutableListOf(
            Title("Muggle", 0),
            Title("Mage Homunculus", 150),
            Title("Tarnished Scholar", 400),
            Title("Gifted Acolyte", 800),
            Title("Mage Initiate", 1600),
            Title("Journeyman", 3000),
            Title("Arcanist", 5000),
            Title("Sorcerer", 8000),
            Title("Magister", 12000),
            Title("Magus", 20000),
            Title("Master of Arcane Arts", 30000),
            Title("Archmage", 45000),
            Title("Grand Sorcerer", 65000),
            Title("High Arcanist", 100000),
            Title("Elder Magus", 150000),
            Title("Celestial Archmage", 220000),
            Title("Astral Wizard", 300000),
            Title("Veil Conjurer", 400000),
            Title("Mana Master", 550000),
            Title("Supreme Magus", 1000000)
        )


        //called when a daily expires or is discarded, a user profile name is changed, or a propic is changed
        fun updateUserProfile(userProfile: UserProfile){
            val xmlWriter = XMLWriter()
            xmlWriter.saveUserProfileToXML(usersDataFile, userProfile)
        }

        private fun updateProgresses(testOutcomes: List<TestOutcome>, userProfile: UserProfile) {
            val dailyUpdates = DailyManager.updateDailies(userProfile, testOutcomes)//check for each assigned daily
            val achUpdates = AchievementManager.updateAchievements(userProfile, testOutcomes)
            if(dailyUpdates || achUpdates) {
                val xmlWriter = XMLWriter()
                updateTitleAndLvl(userProfile)
                xmlWriter.saveUserProfileToXML(usersDataFile, userProfile)
                GUIManager.updateGUI(userProfile, true)
            }
            GUIManager.updateGUI(userProfile, false)
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

        fun analyzeEvents(testOutcomes: List<TestOutcome>){
            val checks = mutableListOf<TestOutcome>() //to collect the "good" outcomes between old and new locs
            for(testOutcome in testOutcomes)
                checks.add(testOutcome)
            updateProgresses(checks, userProfile)
            //TODO: ragionare su casi in cui locators nuovi o vecchi sono vuoti (es. a seguito di remove)
            //e su altri casi se serve (add method, remove method, ...)
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
        GUIManager.updateGUI(userProfile, false)
    }

    fun generateUniqueId(): String {
        val uuid = UUID.randomUUID()
        return uuid.toString()
    }

    fun showGUI() {
        GUIManager.showGUI()
    }




}
