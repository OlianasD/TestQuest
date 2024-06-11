package utils

import gamification.*
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory


class XMLReader {

    fun createUserProfileFromXML(xmlFilePath: String, name: String): UserProfile? {
        val userProfileNode = findUserProfileNodeByName(xmlFilePath, name)
        if (userProfileNode != null) {
            val userProfile = UserProfile(
                name = userProfileNode.getElementsByTagName("name").item(0).textContent,
                level = userProfileNode.getElementsByTagName("level").item(0).textContent.toInt(),
                currentXP = userProfileNode.getElementsByTagName("currentXP").item(0).textContent.toInt(),
                title = userProfileNode.getElementsByTagName("title").item(0).textContent,
                achievementProgresses = mutableListOf(),
                dailyProgresses = mutableListOf(),
                questProgresses = mutableListOf(),
                achievements = mutableListOf()
            )
            loadAchievementProgresses(userProfile, userProfileNode)
            loadDailyProgresses(userProfile, userProfileNode)
            loadQuestProgresses(userProfile, userProfileNode)
            return userProfile
        }
        else {
            return null
        }
    }

    private fun findUserProfileNodeByName(xmlFilePath: String, name: String): Element? {
        val xmlFile = File(xmlFilePath)
        val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = docBuilder.parse(xmlFile)
        val nodeList = doc.getElementsByTagName("userProfile")
        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            if (node.nodeType == org.w3c.dom.Node.ELEMENT_NODE) {
                val element = node as Element
                val profileName = element.getElementsByTagName("name").item(0).textContent
                if (profileName == name) {
                    return element
                }
            }
        }
        return null
    }

    private fun loadAchievementProgresses(userProfile: UserProfile, userProfileNode: Element) {
        val achievementProgressesNode = userProfileNode.getElementsByTagName("achievements").item(0) as Element
        val achievementNodes = achievementProgressesNode.getElementsByTagName("achievement")
        for (i in 0 until achievementNodes.length) {
            val achievementNode = achievementNodes.item(i) as Element
            val achievementName = achievementNode.getElementsByTagName("name").item(0).textContent
            val achievementDescription = achievementNode.getElementsByTagName("description").item(0).textContent
            val achievementTarget = achievementNode.getElementsByTagName("target").item(0).textContent.toInt()
            userProfile.achievements.add(Achievement(achievementName, achievementDescription, achievementTarget))
        }
    }

    private fun loadDailyProgresses(userProfile: UserProfile, userProfileNode: Element) {
        val dailyProgressesNode = userProfileNode.getElementsByTagName("dailies").item(0) as Element
        val dailyNodes = dailyProgressesNode.getElementsByTagName("daily")
        for (i in 0 until dailyNodes.length) {
            val dailyNode = dailyNodes.item(i) as Element
            val dailyName = dailyNode.getElementsByTagName("name").item(0).textContent
            val dailyDescription = dailyNode.getElementsByTagName("description").item(0).textContent
            val dailyXP = dailyNode.getElementsByTagName("xp").item(0).textContent.toInt()
            val dailyTarget = dailyNode.getElementsByTagName("target").item(0).textContent.toInt()
            val dailyProgress = dailyNode.getElementsByTagName("progress").item(0).textContent.toInt()
            val daily = Daily(dailyName, dailyDescription, dailyXP, dailyTarget)
            val dailyProgressObj = DailyProgress(daily, dailyProgress)
            userProfile.dailyProgresses.add(dailyProgressObj)
        }
    }

    private fun loadQuestProgresses(userProfile: UserProfile, userProfileNode: Element) {
        val questProgressesNode = userProfileNode.getElementsByTagName("quests").item(0) as Element
        val questNodes = questProgressesNode.getElementsByTagName("quest")
        for (i in 0 until questNodes.length) {
            val questNode = questNodes.item(i) as Element
            val questName = questNode.getElementsByTagName("name").item(0).textContent
            val questDescription = questNode.getElementsByTagName("description").item(0).textContent
            val questXP = questNode.getElementsByTagName("xp").item(0).textContent.toInt()
            val questTarget = questNode.getElementsByTagName("target").item(0).textContent.toInt()
            val questProgress = questNode.getElementsByTagName("progress").item(0).textContent.toInt()
            val quest = Quest(questName, questDescription, questXP, questTarget)
            val questProgressObj = QuestProgress(quest, questProgress)
            userProfile.questProgresses.add(questProgressObj)
        }
    }



}