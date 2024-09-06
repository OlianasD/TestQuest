package com.example.demo

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import gamification.DailyManager
import gamification.GamificationManager
import org.w3c.dom.Element
import ui.GUIManager
import utils.XMLReader
import java.io.File
import java.io.StringWriter
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

@Service(Service.Level.PROJECT)
class PeriodicTaskProjectService {

    private val scheduler: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    private var waitTime: Long = 1

    init {//TODO: the following code do not work. comment it or fix it
        scheduler.scheduleAtFixedRate({
            if (PluginData.userProfileId.isNotEmpty()) {
                removeExpiredDailiesFromXML()
            }
        }, 0, waitTime, TimeUnit.MILLISECONDS)
    }

    fun dispose() {
        scheduler.shutdown()
    }

    private fun removeExpiredDailiesFromXML() {
        val xmlFile = File("C:\\Users\\User\\Desktop\\demo\\users.xml") // TODO: path
        if (!xmlFile.exists()) {
            println("File not found: ${xmlFile.absolutePath}")
            return
        }
        val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val doc = docBuilder.parse(xmlFile)
        val userProfilesNode = doc.documentElement
        val currentTime = System.currentTimeMillis()
        val twentyFourHoursInMillis = 24 * 60 * 60 * 1000
        var diffTime: Long = 1
        val userProfileNodes = userProfilesNode.getElementsByTagName("userProfile")
        for (i in 0 until userProfileNodes.length) {
            val userProfileNode = userProfileNodes.item(i) as Element
            val dailiesNode = userProfileNode.getElementsByTagName("dailies").item(0) as Element
            val dailyNodes = dailiesNode.getElementsByTagName("daily")
            val toRemove = mutableListOf<Element>()
            for (j in 0 until dailyNodes.length) {
                val dailyNode = dailyNodes.item(j) as Element
                val timestampStr = dailyNode.getElementsByTagName("timestamp").item(0).textContent
                val timestamp = timestampStr.toLong()
                diffTime = currentTime - timestamp //TODO: the whole check here must be made only once
                if (currentTime - timestamp > twentyFourHoursInMillis) {
                    toRemove.add(dailyNode)
                }
            }
            toRemove.forEach { dailyNode ->
                dailiesNode.removeChild(dailyNode)
            }
        }
        waitTime = diffTime //the idea is to set next time check to when the dailies will expire

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
        val xmlReader = XMLReader()
        val tempUserProfile =
            xmlReader.loadUserProfileFromXML(GamificationManager.usersDataFile, PluginData.userProfileId)
        //TODO: verificare che userProfile venga settato prima da GamificationManager via TestQuest
        DailyManager.setupDailies(tempUserProfile!!)//new dailies are assigned
        val guiManager = GUIManager()
        guiManager.updateGUI(tempUserProfile, notifyChange = true) //update GUI after expiration and reassignment
        GamificationManager.updateUserProfileAfterGUIChanges(tempUserProfile)//update user profile
    }
}
