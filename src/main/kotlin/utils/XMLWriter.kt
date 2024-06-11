package utils

import gamification.UserProfile
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory


class XMLWriter {

    fun updateXmlWithUserProfile(xmlFilePath: String, userProfile: UserProfile) {
        val xmlFile = File(xmlFilePath)
        val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = docBuilder.parse(xmlFile)

        val userProfilesNode = doc.documentElement
        val userProfileNodes = userProfilesNode.getElementsByTagName("userProfile")

        for (i in 0 until userProfileNodes.length) {
            val node = userProfileNodes.item(i) as Element
            val nodeName = node.getElementsByTagName("name").item(0).textContent
            if (nodeName == userProfile.name) {
                node.getElementsByTagName("level").item(0).textContent = userProfile.level.toString()
                node.getElementsByTagName("currentXP").item(0).textContent = userProfile.currentXP.toString()
                node.getElementsByTagName("title").item(0).textContent = userProfile.title
                val dailiesNode = node.getElementsByTagName("dailies").item(0) as Element
                dailiesNode.textContent = ""
                userProfile.dailyProgresses.forEach { dailyProgress ->
                    val dailyNode = doc.createElement("daily")
                    dailyNode.appendChild(createElementWithText(doc, "name", dailyProgress.daily.name))
                    dailyNode.appendChild(createElementWithText(doc, "progress", dailyProgress.progress.toString()))
                    dailiesNode.appendChild(dailyNode)
                }
            }
        }
        val transformer = TransformerFactory.newInstance().newTransformer()
        val source = DOMSource(doc)
        val result = StreamResult(xmlFile)
        transformer.transform(source, result)
    }

    private fun createElementWithText(doc: org.w3c.dom.Document, tagName: String, textContent: String): Element {
        val element = doc.createElement(tagName)
        element.textContent = textContent
        return element
    }



}