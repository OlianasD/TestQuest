package utils

import gamification.*
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory


class XMLReader {


    fun loadUserProfileFromXML(xmlFilePath: String, id: String): UserProfile? {
        val userProfileNode = findUserProfileNodeByID(xmlFilePath, id)
        if (userProfileNode != null) {
            val userProfile = UserProfile(
                id = userProfileNode.getElementsByTagName("id").item(0).textContent,
                name = userProfileNode.getElementsByTagName("name").item(0).textContent,
                level = userProfileNode.getElementsByTagName("level").item(0).textContent.toInt(),
                currentXP = userProfileNode.getElementsByTagName("currentXP").item(0).textContent.toInt(),
                nextXP = userProfileNode.getElementsByTagName("nextXP").item(0).textContent.toInt(),
                title = userProfileNode.getElementsByTagName("title").item(0).textContent,
                achievementProgresses = mutableListOf(),
                dailyProgresses = mutableListOf(),
                completedAchievements = mutableListOf(),
                propic = userProfileNode.getElementsByTagName("propic").item(0).textContent,
            )
            loadAchievementProgresses(userProfile, userProfileNode)
            loadDailyProgresses(userProfile, userProfileNode)
            return userProfile
        }
        return null
    }

    private fun findUserProfileNodeByID(xmlFilePath: String, ID: String): Element? {
        val xmlFile = File(xmlFilePath)
        val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = docBuilder.parse(xmlFile)
        val nodeList = doc.getElementsByTagName("userProfile")
        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            if (node.nodeType == org.w3c.dom.Node.ELEMENT_NODE) {
                val element = node as Element
                val profileID = element.getElementsByTagName("id").item(0).textContent
                if (profileID == ID)
                    return element
            }
        }
        return null
    }

    private fun loadAchievementProgresses(userProfile: UserProfile, userProfileNode: Element) {
        val achievementsNode = userProfileNode.getElementsByTagName("achievements").item(0) as Element
        //load completed achievements
        val completedNodes = achievementsNode.getElementsByTagName("completed").item(0) as Element
        val completedAchievementNodes = completedNodes.getElementsByTagName("achievement")
        for (i in 0 until completedAchievementNodes.length) {
            val achievementNode = completedAchievementNodes.item(i) as Element
            val achievementName = achievementNode.getElementsByTagName("name").item(0).textContent
            val achievementDescription = AchievementManager.getDescriptionFromName(achievementName)
            val achievementTarget = AchievementManager.getTargetFromName(achievementName)
            val achievementIcon = AchievementManager.getAchievementIconFromName(achievementName)
            userProfile.completedAchievements.add(Achievement(achievementName, achievementDescription, achievementTarget, achievementIcon))
        }
        //load ongoing achievements
        val ongoingNodes = achievementsNode.getElementsByTagName("ongoing").item(0) as Element
        val ongoingAchievementNodes = ongoingNodes.getElementsByTagName("achievement")
        for (i in 0 until ongoingAchievementNodes.length) {
            val achievementNode = ongoingAchievementNodes.item(i) as Element
            val achievementName = achievementNode.getElementsByTagName("name").item(0).textContent
            val achievementProgress = achievementNode.getElementsByTagName("progress").item(0).textContent.toInt()
            val achievementDescription = AchievementManager.getDescriptionFromName(achievementName)
            val achievementTarget = AchievementManager.getTargetFromName(achievementName)
            val achievementIcon = AchievementManager.getAchievementIconFromName(achievementName)
            userProfile.achievementProgresses.add(AchievementProgress(Achievement(achievementName, achievementDescription, achievementTarget, achievementIcon), achievementProgress))
        }
    }

    private fun loadDailyProgresses(userProfile: UserProfile, userProfileNode: Element) {
        val dailyProgressesNode = userProfileNode.getElementsByTagName("dailies").item(0) as Element
        val dailyNodes = dailyProgressesNode.getElementsByTagName("daily")
        for (i in 0 until dailyNodes.length) {
            val dailyNode = dailyNodes.item(i) as Element
            val dailyName = dailyNode.getElementsByTagName("name").item(0).textContent
            val dailyDescription = DailyManager.getDescriptionFromName(dailyName)
            val dailyXP = DailyManager.getXPFromName(dailyName)
            val dailyTarget = DailyManager.getTargetFromName(dailyName)
            val dailyIcon = DailyManager.getIconFromName(dailyName)
            val dailyProgress = dailyNode.getElementsByTagName("progress").item(0).textContent.toInt()
            val daily = Daily(dailyName, dailyDescription, dailyXP, dailyTarget, dailyIcon)
            val dailyProgressObj = DailyProgress(daily, dailyProgress)
            userProfile.dailyProgresses.add(dailyProgressObj)
        }
    }

}