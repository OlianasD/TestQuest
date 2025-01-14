package gamification

import listener.test.TestOutcome
import ui.GUIManager
import utils.XMLReader
import utils.XMLWriter
import java.util.*

class GamificationManager() {


    // random = random daily assignment
    // targeted = daily assignment based on problems and bad practices observed by analysing test artifacts
    // inclusive = daily assignment to cover functionalities not covered by test artifacts, given the DOM
    enum class DailyAssignmentMode {
        RANDOM, TARGETED, INCLUSIVE
    }

    companion object {

        fun fromString(value: String?): DailyAssignmentMode {
            return when (value?.uppercase()) {
                "INCLUSIVE" -> DailyAssignmentMode.INCLUSIVE
                "TARGETED" -> DailyAssignmentMode.TARGETED
                else -> DailyAssignmentMode.RANDOM
            }
        }

        var usersDataFile: String = "C:\\Users\\User\\Desktop\\demo\\users.xml" //TODO: path
        var unknownUserPic : String = "C:\\Users\\User\\Desktop\\demo\\pics\\user\\default-user.png" //TODO: path
        lateinit var userProfile: UserProfile //the current user
        var mode: DailyAssignmentMode = DailyAssignmentMode.RANDOM //this flag is initially set to random and can be changed via GUI

        //PROPERTIES USED TO DETERMINE GOOD/BAD LOCATORS
        const val MAX_LENGTH = 50
        const val MAX_LEVEL = 5
        const val MAX_POS_PRED = 3
        val GOOD_PREDS = setOf(
            "@id", "@name", "@class", "@title", "@alt", "@value")
        val BAD_PREDS = setOf(
            "src", "href", "height", "width")
        val BAD_JS = setOf("onclick", "onload",
            "onmouseover", "onmouseout", "onchange", "onsubmit",
            "onfocus", "onkeydown")
        const val ROBUST_THRESHOLD = 0.5



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

        fun getXpForTitle(titleName: String): Int {
            return allTitles.find { it.name == titleName }?.xp ?: -1
        }

        fun analyzeEvents(testOutcomes: List<TestOutcome>){
            val checks = mutableListOf<TestOutcome>() //to collect the "good" outcomes between old and new locs
            for(testOutcome in testOutcomes)
                checks.add(testOutcome)
            updateProgresses(checks, userProfile)
            //TODO: ragionare su casi in cui locators nuovi o vecchi sono vuoti (es. a seguito di remove) e su altri casi se serve (add method, remove method, ...)
        }

        //called when a daily expires or is discarded, a user profile name is changed, or a propic is changed
        fun updateUserProfile(userProfile: UserProfile){
            val xmlWriter = XMLWriter()
            xmlWriter.saveUserProfileToXML(usersDataFile, userProfile)
        }

        private fun updateProgresses(testOutcomes: List<TestOutcome>, userProfile: UserProfile) {
            val dailyProgresses = DailyManager.updateDailyProgresses(userProfile, testOutcomes)//check for each assigned daily
            //val achUpdates = AchievementManager.updateAchievements(userProfile, testOutcomes)
            if (!dailyProgresses.isNullOrEmpty()) { //|| achUpdates.isNullOrEmpty
                val totalXp = dailyProgresses.sumOf { it.first } //total xp gained
                //val dailyDescriptions = dailyUpdates.mapNotNull { it.second?.description } //list of involved dailies
                var msg = "$totalXp XP gained from (partially) completed dailies\n"
                val xmlWriter = XMLWriter()
                val isNewTitle = updateTitleAndLvl(userProfile)
                xmlWriter.saveUserProfileToXML(usersDataFile, userProfile)
                DailyManager.assignTargetedDailies(userProfile) //this to update in case of broken locators and to update GUI
                if(isNewTitle)
                    msg += "New Level & Title reached!"
                GUIManager.updateGUI(userProfile, true, msg)
            }
            else
                GUIManager.updateGUI(userProfile, false)
        }

        private fun updateTitleAndLvl(userProfile: UserProfile): Boolean {
            //e.g., if current user is level 3 and has 15/400 xp, to find if new level is reached
            // we need to count 15 + base XP needed to reach level 3 to determine whole XP
            val newTitle = allTitles
                .maxByOrNull { if (it.xp + getXpForTitle(userProfile.title) <= userProfile.currentXP + getXpForTitle(userProfile.title)) it.xp
                else Int.MIN_VALUE }
            if (newTitle != null && userProfile.title != newTitle.name) {
                userProfile.title = newTitle.name
                userProfile.level++
                userProfile.currentXP -= userProfile.nextXP //xp is restarted considering exceeding XP from new lvl reached
                val currentIndex = allTitles.indexOfFirst { it.name == userProfile.title }
                if (currentIndex != -1 && currentIndex + 1 < allTitles.size)
                    userProfile.nextXP = allTitles[currentIndex + 1].xp
                return true
            }
            else if (newTitle == null)
                userProfile.nextXP = Int.MAX_VALUE
            return false
        }

        //this method is called either when
        // 1. user choose TARGETED mode for daily assignments
        // 2. a test file changes
        // 3. the plugin is opened
        fun assignTargetDailies(){
            DailyManager.assignTargetedDailies(userProfile)
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
            DailyManager.setupRandomDailies(userProfile)
            AchievementManager.setupAchievements(userProfile)
            xmlWriter.addNewUserProfileToXML(usersDataFile, userProfile)
        }
        else {
            userProfile = tempUserProfile
            if(mode == DailyAssignmentMode.TARGETED)
                assignTargetDailies()
        }
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
