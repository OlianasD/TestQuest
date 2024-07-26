package gamification

import listener.TestOutcome
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
            Title("Muggle", 0),
            Title("Mage Familiar", 20),
            Title("Tarnished Scholar", 50),
            Title("Gifted Acolyte", 100),
            Title("Mage Initiate", 200),
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
            Title("Veil Conjurer", 2000),
            Title("Mana Master", 2500),
            Title("Supreme Magus", 3000)
        )


        //called when a daily is removed, a user profile name is changed, or a propic is changed
        fun updateUserProfileAfterGUIChanges(userProfile: UserProfile){
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

        fun analyzeEvents(testOutcomes: List<TestOutcome>){
            var anyChange = false
            val checks = mutableListOf<TestOutcome>() //to collect the "good" outcomes between old and new locs
            for(testOutcome in testOutcomes){
                //case: changes on existing test methods
                //assumption: test methods always have locators
                if (testOutcome.locatorsOld.isNotEmpty()) {
                    //case: no changes
                    if (testOutcome.locatorsNew.size == testOutcome.locatorsOld.size &&
                        testOutcome.locatorsNew.toSet() == testOutcome.locatorsOld.toSet()) {
                        continue
                    }
                    anyChange = true
                    //case: changes by removing test method
                    //TODO
                    if (testOutcome.locatorsNew.isEmpty()){
                        continue
                    }
                    //case: changes over existing locators (edit types or values)
                    //assumption: same locator is in the same order which does not change
                    else if (testOutcome.locatorsNew.size == testOutcome.locatorsOld.size)
                        checks.add(testOutcome)
                    //case: changes by adding locators in existing test methods
                    //TODO
                    else if (testOutcome.locatorsNew.size > testOutcome.locatorsOld.size){
                        checks.add(testOutcome)
                    }
                    //case: changes by removing locators in existing test methods
                    //TODO
                    else{
                        continue
                    }
                }
                //case: changes by adding test methods
                //TODO
                else {
                    checks.add(testOutcome)
                }
            }
            if(anyChange)
                updateProgresses(checks, userProfile)
        }



        /***** aux functions *****/
        fun computeFragilityCoefficient(loc: Locator): Int{
            return 0//TODO:implement coefficient
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
