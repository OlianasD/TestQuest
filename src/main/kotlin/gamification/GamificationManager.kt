package gamification

import listener.test.TestOutcome
import ui.GUIManager
import utils.FilePathSolver
import utils.XMLReader
import utils.XMLWriter
import java.io.File
import java.util.*

class GamificationManager {


    // random = random daily assignment
    // targeted = daily assignment based on problems and bad practices observed by analysing test artifacts
    // inclusive = daily assignment to cover functionalities not covered by test artifacts, given the DOM //TODO
    enum class DailyAssignmentMode {
        RANDOM, TARGETED, INCLUSIVE
    }


    //LOCATOR = only locator-based dailies can be assigned
    //ADVANCED = all type of dailies can be assigned
    enum class GamificationMode {
        LOCATOR, ADVANCED
    }


    companion object {

        fun fromString(value: String?): DailyAssignmentMode {
            return when (value?.uppercase()) {
                "INCLUSIVE" -> DailyAssignmentMode.INCLUSIVE
                "TARGETED" -> DailyAssignmentMode.TARGETED
                else -> DailyAssignmentMode.RANDOM
            }
        }

        var usersDataFile: File = FilePathSolver.getUserDataFile()//path to users file
        lateinit var userProfile: UserProfile //the current user
        var assignmentMode: DailyAssignmentMode = DailyAssignmentMode.RANDOM //this flag is initially set to random and can be changed via GUI
        var gamificationMode: GamificationMode = GamificationMode.LOCATOR//this flag is initially set to locator mode so to assign only locator-based dailiees


        //METRICS USED TO DETERMINE GOOD/BAD LOCATORS
        const val MAX_LENGTH = 50
        const val MAX_LEVEL = 5
        const val MAX_POS_PRED = 3
        val GOOD_PREDS = setOf(
            "@id", "@name", "@class", "@title", "@alt", "@value") //TODO: this is just a subset. more good predicates could be considered
        val BAD_PREDS = setOf(
            "@src", "@href", "@height", "@width")
        val BAD_JS = setOf("@onclick", "@onload",
            "@onmouseover", "@onmouseout", "@onchange", "@sonsubmit",
            "@onfocus", "@onkeydown")
        const val FRAGILITY_THRESHOLD = 0.5

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

        private fun getXpForTitle(titleName: String): Int {
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
            val achProgresses = AchievementManager.updateAchievementProgresses(userProfile, testOutcomes) //TODO: commented
            if (dailyProgresses.isNotEmpty()){// || achProgresses.isNotEmpty()){ //TODO: commented
                val totalXp = dailyProgresses.sumOf { it.first } //total xp gained
                var msg = ""
                if(dailyProgresses.isNotEmpty() && totalXp > 0) {
                    msg = "$totalXp XP gained from (partially) completed dailies\n"
                }
                if (achProgresses.isNotEmpty()) { //TODO: commented
                    val completedAchievements = achProgresses.filterNotNull().joinToString(", ") { it.name }
                    msg += "Achievements completed: $completedAchievements\n"
                }
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
            //e.g., if current user is level 3 and has 15/400 xp left, to find if new level is reached
            // we need to count 15 + base XP needed to reach level 3 to determine whole XP and then the next lvl title
            val newTitle = allTitles
                .maxByOrNull {
                    if (it.xp + getXpForTitle(userProfile.title) <= userProfile.currentXP + getXpForTitle(userProfile.title)) it.xp
                else Int.MIN_VALUE }

            if (newTitle != null && userProfile.title != newTitle.name) {//if a new title is found
                userProfile.title = newTitle.name
                val currentIndex = allTitles.indexOfFirst { it.name == userProfile.title }
                if(currentIndex + 1 >= allTitles.size) { //if max title/lvl reached
                    userProfile.nextXP = allTitles[allTitles.size - 1].xp
                    userProfile.currentXP = allTitles[allTitles.size - 1].xp
                }
                else {
                    userProfile.nextXP = allTitles[currentIndex + 1].xp
                    //userProfile.currentXP -= allTitles[currentIndex].xp //xp is restarted considering exceeding XP from lvl reached
                }
                userProfile.level = currentIndex + 1
                return true
            }
            /*else if (newTitle == null)
                userProfile.nextXP = allTitles[allTitles.size -1].xp*/
            return false
        }

        //this method is called either when
        // 1. user choose TARGETED mode for daily assignments
        // 2. a test file changes
        // 3. the plugin is opened
        fun assignTargetDailies(){
            DailyManager.assignTargetedDailies(userProfile)
        }

        //this is called to update the random dailies and substitute advanced ones with locator-based if locator mode
        //is selected as gamification mode
        fun updateRandomDailies(){
            DailyManager.updateRandomDailies(userProfile)
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
                propic = FilePathSolver.USER_PROPIC_PATH
            )
            DailyManager.setupRandomDailies(userProfile)
            AchievementManager.setupAchievements(userProfile)
            xmlWriter.addNewUserProfileToXML(usersDataFile, userProfile)
        }
        else {
            userProfile = tempUserProfile
            AchievementManager.updateUserProfile(userProfile)
            if(assignmentMode == DailyAssignmentMode.TARGETED)
                assignTargetDailies()
            else
                updateRandomDailies()
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
