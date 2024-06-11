package gamification

import utils.XMLReader
import utils.XMLWriter

class GamificationManager(private val path: String) {



    private val allTitles = mutableListOf(
        Title("Basic Tester", 20),
        Title("Pro Tester", 500),
        Title("Master Tester", 1000),
    )

    private val allQuests = mutableListOf(
        Quest("xpathAbs10","Replace 10 absolute XPath locators with relative ones", 200, 10),
        Quest("xpathLength10", "Reduce the length of 10 XPath locators", 150, 10),
        Quest("xpathHeight10", "Reduce the height of 10 XPath locators", 150, 10),
        Quest("loc2css10", "Convert 10 non-CSS locators to a CSS ones", 150, 10),
        Quest("loc2xpath10", "Convert 10 non-XPath locators to XPath ones", 150, 10),
        Quest("loc2id10", "Convert 10 non-ID locators to ID ones", 150, 10),
        Quest("attrRef10", "Add a reference to 10 attributes within XPath locators", 150, 10),
        Quest("tableRef10", "Add a reference to 10 <table> tags within XPath locators", 150, 10),
        Quest("divRef10", "Add a reference to 10 <div> tags within XPath locators", 150, 10),
        Quest("formRef10", "Add a reference to 10 <form> tags within XPath locators", 150, 10),
        Quest("buttonRef10", "Add a reference to 10 <button> tags within XPath locators", 150, 10),
        Quest("linkRef10", "Add a reference to 10 <a> tags within XPath locators", 150, 10),
        Quest("spanRef10", "Add a reference to 10 <span> tags within XPath locators", 150, 10),
        Quest("change10", "Change 10 different locators", 100, 10),
        Quest("changeAllMethod", "Change all locators of a test method", 300, 999),
        Quest("changeAllTest", "Change all locators of a test class", 800, 999),
    )

    private val allAchievements = mutableListOf(
        Achievement("Is this a locator?", "Just a dummy achievement", 1),
        Achievement("Absolute XPath Destroyer", "Fix 100 absolute XPath locators", 100),
        Achievement("Length Reducer", "Reduce the length of 100 XPath locators", 100),
        Achievement("Height Reducer", "Reduce the height of 100 XPath locators", 100),
        Achievement("Stillness", "Do not change any locator for 48 hours", 999),
        Achievement("Master of Changes", "Change every locator at least once", 999),
        Achievement("I made a mistake", "Change a locator into something worse", 999), //e.g., relative to absolute, longer xpath, id to else, higher xpath
    )


    private fun initAchievements(userProfile: UserProfile) {
        for (ach in allAchievements) {
            val achProgress = AchievementProgress(ach, 0)
            userProfile.achievementProgresses.add(achProgress)
        }
    }

    private fun assignQuests(userProfile: UserProfile) {
        val quests = allQuests.shuffled().take(1)
        userProfile.assignQuests(quests)
    }

    //TODO: this will require to 1) be used to setup new users 2) retrieve persistent data from old users
    fun setupUserProfile(name: String): UserProfile {
        val xmlReader = XMLReader()
        var userProfile = xmlReader.createUserProfileFromXML(path, name)
        if(userProfile == null) {
            userProfile = UserProfile(
                name = name,
                level = 1,
                currentXP = 0,
                title = "Newbie Tester",
                achievementProgresses = mutableListOf(),
                dailyProgresses = mutableListOf(),
                questProgresses = mutableListOf(),
                achievements = mutableListOf()
            )
            DailyManager.setupDailies(userProfile)
            assignQuests(userProfile)
            initAchievements(userProfile)
        }
        return userProfile
    }





    fun updateProgresses(userProfile: UserProfile, results: Map<String, Int>) {
        DailyManager.updateDailies(userProfile, results)
        //updateQuests(userProfile, results)
        //updateAchievements(userProfile, results)
        updateTitleAndLvl(userProfile)
        val xmlWriter = XMLWriter()
        xmlWriter.updateXmlWithUserProfile(path, userProfile)
    }










    /*
    fun updateProgresses1(userProfile: UserProfile, results: Map<String, Int>){

        val numImprovedByLength = results["longXPathCount"] ?: 0
        val numImprovedByHeight = results["highXPathCount"] ?: 0
        val numImprovedByAbs = results["absXPathCount"] ?: 0


        if(numImprovedByLength > 0) {
            allDailies.find { it.name == "xpathLength" }?.let { updateDailyProgresses(userProfile, it, numImprovedByLength) }
            //allQuests.find { it.name == "xpathLength10" }?.let { updateQuestProgresses(userProfile, it, numImprovedByLength) }
            //allAchievements.find { it.name == "Length Reducer" }?.let { updateAchievementProgresses(userProfile, it, numImprovedByLength) }
        }
        if(numImprovedByHeight > 0) {
            allDailies.find { it.name == "xpathHeight" }?.let { updateDailyProgresses(userProfile, it, numImprovedByHeight) }
            //allQuests.find { it.name == "xpathHeight10" }?.let { updateQuestProgresses(userProfile, it, numImprovedByHeight) }
            //allAchievements.find { it.name == "Height Reducer" }?.let { updateAchievementProgresses(userProfile, it, numImprovedByHeight) }
        }
        if(numImprovedByAbs > 0) {
            allDailies.find { it.name == "xpathAbs" }?.let { updateDailyProgresses(userProfile, it, numImprovedByAbs) }
            //allQuests.find { it.name == "xpathAbs10" }?.let { updateQuestProgresses(userProfile, it, numImprovedByAbs) }
            //allAchievements.find { it.name == "Absolute XPath Destroyer" }?.let { updateAchievementProgresses(userProfile, it, numImprovedByAbs) }
        }
        //if(numImprovedByLength > 0 || numImprovedByHeight > 0 || numImprovedByAbs > 0)
        //    allAchievements.find { it.name == "Dummy Achievement" }?.let { updateAchievementProgresses(userProfile, it, 1) }
    }
    */


    /*private fun updateDailyProgresses(userProfile: UserProfile, daily: Daily, progress: Int){
        val dailyProgress = userProfile.dailyProgresses.find { it.daily.name == daily.name }
        dailyProgress?.let {
            dailyProgress.progress += progress
            if (dailyProgress.progress >= daily.target) {
                addXP(userProfile, daily.xp)
                removeDaily(userProfile, daily)
            }
        }
    }*/

    private fun updateQuestProgresses(userProfile: UserProfile, quest: Quest, progress: Int){
        val questProgress = userProfile.questProgresses.find { it.quest.name == quest.name }
        questProgress?.let {
            questProgress.progress += progress
            if (questProgress.progress >= quest.target){
                addXP(userProfile, quest.xp)
                removeQuest(userProfile, quest)
            }
        }
    }

    private fun updateAchievementProgresses(userProfile: UserProfile, ach: Achievement, progress: Int){
        val achProgress = userProfile.achievementProgresses.find { it.achievement.name == ach.name }
        achProgress?.let {
            achProgress.progress += progress
            if (achProgress.progress >= ach.target){
                addAchievement(userProfile, ach)
            }
        }
    }

    private fun addXP(userProfile: UserProfile, xp: Int) {
        userProfile.currentXP += xp
        updateTitleAndLvl(userProfile)
    }

    private fun updateTitleAndLvl(userProfile: UserProfile) {
        val newTitle = allTitles
            .filter { it.xp <= userProfile.currentXP }
            .maxByOrNull { it.xp }
        if (newTitle != null && userProfile.title != newTitle.name) {
            userProfile.title = newTitle.name
            userProfile.level++
        }
    }



    private fun removeQuest(userProfile: UserProfile, quest: Quest){
        userProfile.questProgresses.removeIf { it.quest.name == quest.name }
    }

    private fun addAchievement(userProfile: UserProfile, ach: Achievement){
        userProfile.achievements.add(ach)
    }

}
