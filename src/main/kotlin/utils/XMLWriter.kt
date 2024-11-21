package utils

import gamification.GamificationManager
import gamification.UserProfile
import org.w3c.dom.Document
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

import org.w3c.dom.Element
import java.io.File
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys


class XMLWriter {

    fun saveUserProfileToXML(xmlFilePath: String, userProfile: UserProfile) {
        val xmlFile = File(xmlFilePath)
        val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = docBuilder.parse(xmlFile)
        val userProfilesNode = doc.documentElement
        val userProfileNodes = userProfilesNode.getElementsByTagName("userProfile")
        for (i in 0 until userProfileNodes.length) {
            val node = userProfileNodes.item(i) as Element
            val nodeId = node.getElementsByTagName("id").item(0).textContent
            if (nodeId == userProfile.id) {
                node.getElementsByTagName("name").item(0).textContent = userProfile.name
                node.getElementsByTagName("level").item(0).textContent = userProfile.level.toString()
                node.getElementsByTagName("currentXP").item(0).textContent = userProfile.currentXP.toString()
                node.getElementsByTagName("title").item(0).textContent = userProfile.title
                node.getElementsByTagName("propic").item(0).textContent = userProfile.propic
                node.setAttribute("mode", GamificationManager.mode.name)
                val dailiesNode = node.getElementsByTagName("dailies").item(0) as Element
                //daily expiration time is saved
                dailiesNode.setAttribute("timestamp", userProfile.timestamp.toString())
                //usage mode (random, targeted, inclusive) is saved
                //update dailies
                dailiesNode.textContent = ""
                userProfile.dailyProgresses.forEach { dailyProgress ->
                    val dailyNode = doc.createElement("daily")
                    dailyNode.setAttribute("name", dailyProgress.daily.name)
                    dailyNode.setAttribute("progress", dailyProgress.progress.toString())
                    dailyNode.setAttribute("discarded", dailyProgress.discarded.toString())
                    dailyNode.setAttribute("type", dailyProgress.daily.type)
                    if (dailyProgress.daily.type == "targeted" && dailyProgress.daily.targetedLocators.isNotEmpty()) {
                        val locatorsNode = doc.createElement("locators")
                        dailyProgress.daily.targetedLocators.forEach { locator ->
                            val locatorNode = doc.createElement("locator")
                            locatorNode.setAttribute("locatorName", locator.locatorName ?: "")
                            locatorNode.setAttribute("locatorType", locator.locatorType)
                            locatorNode.setAttribute("locatorValue", locator.locatorValue)
                            locatorNode.setAttribute("methodName", locator.methodName)
                            locatorNode.setAttribute("className", locator.className)
                            locatorNode.setAttribute("line", locator.line.toString())
                            locatorNode.setAttribute("locatorPosition", locator.locatorPosition.toString())
                            locatorNode.setAttribute("filePath", locator.filePath)
                            locatorsNode.appendChild(locatorNode)
                        }
                        dailyNode.appendChild(locatorsNode)
                    }
                    if (dailyProgress.daily.name == "edit5") {
                        val modifiedLocsNode = doc.createElement("modified-locs")
                        dailyProgress.modifiedLocs.forEach { loc ->
                            val locNode = doc.createElement("loc")
                            locNode.textContent = loc
                            modifiedLocsNode.appendChild(locNode)
                        }
                        dailyNode.appendChild(modifiedLocsNode)
                    }
                    dailiesNode.appendChild(dailyNode)
                }
                //update achievements
                val achievementsNode = node.getElementsByTagName("achievements").item(0) as Element
                val completedAchievementsNode = achievementsNode.getElementsByTagName("completed").item(0) as Element
                val ongoingAchievementsNode = achievementsNode.getElementsByTagName("ongoing").item(0) as Element
                completedAchievementsNode.textContent = ""
                ongoingAchievementsNode.textContent = ""
                //update completed achievements
                userProfile.completedAchievements.forEach { achievement ->
                    val achievementNode = doc.createElement("achievement")
                    achievementNode.appendChild(createElementWithText(doc, "name", achievement.name))
                    completedAchievementsNode.appendChild(achievementNode)
                }
                //update ongoing achievements
                userProfile.achievementProgresses.forEach { achievementProgress ->
                    val achievementNode = doc.createElement("achievement")
                    achievementNode.appendChild(createElementWithText(doc, "name", achievementProgress.achievement.name))
                    achievementNode.appendChild(createElementWithText(doc, "progress", achievementProgress.progress.toString()))
                    ongoingAchievementsNode.appendChild(achievementNode)
                }
            }
        }
        val transformer = TransformerFactory.newInstance().newTransformer()
        val source = DOMSource(doc)
        val result = StreamResult(xmlFile)
        transformer.transform(source, result)
    }

    private fun createElementWithText(doc: Document, tagName: String, textContent: String): Element {
        val element = doc.createElement(tagName)
        element.textContent = textContent
        return element
    }

    fun addNewUserProfileToXML(xmlFilePath: String, userProfile: UserProfile) {
        val xmlFile = File(xmlFilePath)
        val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = docBuilder.parse(xmlFile)
        val userProfilesNode = doc.documentElement
        // Create userProfile element
        val userProfileNode = doc.createElement("userProfile")
        userProfileNode.appendChild(createElementWithText(doc, "id", userProfile.id))
        userProfileNode.appendChild(createElementWithText(doc, "name", userProfile.name))
        userProfileNode.appendChild(createElementWithText(doc, "level", userProfile.level.toString()))
        userProfileNode.appendChild(createElementWithText(doc, "currentXP", userProfile.currentXP.toString()))
        userProfileNode.appendChild(createElementWithText(doc, "nextXP", userProfile.nextXP.toString()))
        userProfileNode.appendChild(createElementWithText(doc, "title", userProfile.title))
        userProfileNode.appendChild(createElementWithText(doc, "propic", userProfile.propic))
        userProfileNode.setAttribute("mode", GamificationManager.mode.name)
        // Create achievements element
        val achievementsNode = doc.createElement("achievements")
        val completedNode = doc.createElement("completed")
        userProfile.completedAchievements.forEach { achievement ->
            val achievementNode = doc.createElement("achievement")
            achievementNode.appendChild(createElementWithText(doc, "name", achievement.name))
            achievementNode.appendChild(createElementWithText(doc, "description", achievement.description))
            achievementNode.appendChild(createElementWithText(doc, "target", achievement.target.toString()))
            completedNode.appendChild(achievementNode)
        }
        achievementsNode.appendChild(completedNode)
        val ongoingNode = doc.createElement("ongoing")
        userProfile.achievementProgresses.forEach { achievementProgress ->
            val achievementNode = doc.createElement("achievement")
            achievementNode.appendChild(createElementWithText(doc, "name", achievementProgress.achievement.name))
            achievementNode.appendChild(createElementWithText(doc, "description", achievementProgress.achievement.description))
            achievementNode.appendChild(createElementWithText(doc, "target", achievementProgress.achievement.target.toString()))
            achievementNode.appendChild(createElementWithText(doc, "progress", achievementProgress.progress.toString()))
            ongoingNode.appendChild(achievementNode)
        }
        achievementsNode.appendChild(ongoingNode)
        userProfileNode.appendChild(achievementsNode)
        // Create dailies element
        val dailiesNode = doc.createElement("dailies")
        dailiesNode.setAttribute("timestamp", System.currentTimeMillis().toString())//timestamp needed for expiration in 24h
        userProfile.dailyProgresses.forEach { dailyProgress ->
            val dailyNode = doc.createElement("daily")
            dailyNode.setAttribute("name", dailyProgress.daily.name)
            dailyNode.setAttribute("progress", dailyProgress.progress.toString())
            dailyNode.setAttribute("discarded", dailyProgress.discarded.toString())
            dailyNode.setAttribute("type", dailyProgress.daily.type)
            //handle targeted dailies with associated locators
            if (dailyProgress.daily.type == "targeted" && dailyProgress.daily.targetedLocators.isNotEmpty()) {
                val locatorsNode = doc.createElement("locators")
                dailyProgress.daily.targetedLocators.forEach { locator ->
                    val locatorNode = doc.createElement("locator")
                    locatorNode.setAttribute("locatorName", locator.locatorName ?: "")
                    locatorNode.setAttribute("locatorType", locator.locatorType)
                    locatorNode.setAttribute("locatorValue", locator.locatorValue)
                    locatorNode.setAttribute("methodName", locator.methodName)
                    locatorNode.setAttribute("className", locator.className)
                    locatorNode.setAttribute("line", locator.line.toString())
                    locatorNode.setAttribute("locatorPosition", locator.locatorPosition.toString())
                    locatorNode.setAttribute("filePath", locator.filePath)
                    locatorsNode.appendChild(locatorNode)
                }
                dailyNode.appendChild(locatorsNode)
            }
            if (dailyProgress.daily.name == "edit5") { //TODO: test on new user getting assigned this daily
                val modifiedLocsNode = doc.createElement("modified-locs")
                dailyProgress.modifiedLocs.forEach { loc ->
                    val locNode = doc.createElement("loc")
                    locNode.textContent = loc
                    modifiedLocsNode.appendChild(locNode)
                }
                dailyNode.appendChild(modifiedLocsNode)
            }
            dailiesNode.appendChild(dailyNode)
        }
        userProfileNode.appendChild(dailiesNode)

















        // Append the new userProfile to the root element
        userProfilesNode.appendChild(userProfileNode)
        // Write the changes back to the XML file with formatted output
        val transformer = TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4")
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no")
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no")
        val stringWriter = StringWriter()
        transformer.transform(DOMSource(doc), StreamResult(stringWriter))
        val formattedXml = stringWriter.toString()
        xmlFile.writeText(formattedXml)
        // Remove newlines
        val lines = xmlFile.readLines()
        val nonEmptyLines = lines.filter { it.trim().isNotEmpty() }
        xmlFile.writeText(nonEmptyLines.joinToString("\n"))
    }





}

