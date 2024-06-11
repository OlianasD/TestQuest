package ui

import com.intellij.util.ui.JBUI
import gamification.UserProfile
import gamification.LocatorsAnalyzer
import java.awt.*
import javax.swing.*

class GUIManager {

    private var textArea: JTextArea? = null

    private fun showPopup(message: String) {
        val pane = JPanel()
        pane.layout = BorderLayout()
        val label = JLabel("<html><div style='padding: 10px;'>$message</div></html>")
        label.horizontalAlignment = SwingConstants.CENTER
        pane.add(label, BorderLayout.CENTER)
        val dialog = JDialog()
        dialog.title = "Profile Update"
        dialog.contentPane.add(pane)
        dialog.isModal = false
        dialog.setSize(300, 150)
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val x = screenSize.width - dialog.width
        val y = screenSize.height - dialog.height
        dialog.setLocation(x - 20, y - 20)
        dialog.add(pane, BorderLayout.PAGE_END)
        dialog.isVisible = true
        Timer(5000) {
            dialog.dispose()
        }.start()
    }


    fun updateGUI(metricsResults: Map<String, Int>, userProfile: UserProfile, notifyChange: Boolean) {
        if (metricsResults != null) {
            SwingUtilities.invokeLater {
                //main panel
                val mainPanel = JPanel(GridBagLayout())
                mainPanel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
                val gbc = GridBagConstraints()
                gbc.fill = GridBagConstraints.HORIZONTAL
                gbc.insets = JBUI.insets(10)

                //user data panel
                val userInfoPanel = JPanel()
                userInfoPanel.layout = BoxLayout(userInfoPanel, BoxLayout.Y_AXIS)
                userInfoPanel.border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "User Info")
                val nameLabel = JLabel("Name: ${userProfile.name}")
                val titleLabel = JLabel("Title: ${userProfile.title}")
                val levelLabel = JLabel("Level: ${userProfile.level}")
                val xpLabel = JLabel("Current XP: ${userProfile.currentXP}")
                val imageBox = JPanel()
                imageBox.border = BorderFactory.createLineBorder(Color.LIGHT_GRAY)
                imageBox.preferredSize = Dimension(100, 100)
                userInfoPanel.add(nameLabel)
                userInfoPanel.add(titleLabel)
                userInfoPanel.add(levelLabel)
                userInfoPanel.add(xpLabel)
                userInfoPanel.add(imageBox)
                gbc.gridx = 0
                gbc.gridy = 0
                gbc.gridwidth = 2
                mainPanel.add(userInfoPanel, gbc)

                //dailies panel
                val dailiesPanel = JPanel()
                dailiesPanel.layout = BoxLayout(dailiesPanel, BoxLayout.Y_AXIS)
                dailiesPanel.border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Dailies")
                for (dailyProgress in userProfile.dailyProgresses) {
                    dailiesPanel.add(JLabel(dailyProgress.daily.description))
                }
                gbc.gridx = 0
                gbc.gridy = 1
                gbc.gridwidth = 1
                mainPanel.add(dailiesPanel, gbc)

                //quests panel
                val questsPanel = JPanel()
                questsPanel.layout = BoxLayout(questsPanel, BoxLayout.Y_AXIS)
                questsPanel.border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Quests")
                for (questProgress in userProfile.questProgresses) {
                    questsPanel.add(JLabel(questProgress.quest.description))
                }
                gbc.gridx = 0
                gbc.gridy = 2
                mainPanel.add(questsPanel, gbc)

                //achievements panel
                val achievementsPanel = JPanel()
                achievementsPanel.layout = BoxLayout(achievementsPanel, BoxLayout.Y_AXIS)
                achievementsPanel.border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Achievements")
                for (achievement in userProfile.achievements) {
                    achievementsPanel.add(JLabel(achievement.name))
                }
                gbc.gridx = 0
                gbc.gridy = 3
                mainPanel.add(achievementsPanel, gbc)

                //update GUI
                textArea?.removeAll()
                textArea?.layout = BorderLayout()
                textArea?.add(mainPanel, BorderLayout.CENTER)
                textArea?.revalidate()
                textArea?.repaint()

                //TODO: here the notification should also address achieved dailies/quests/achievements and target only actual changes
                //e.g., if I change a locator but this does not affect the profile, no popup should be shown
                if(notifyChange)
                    showPopup("New Level: ${userProfile.level}\nNew Title: ${userProfile.title}\nNew XP: ${userProfile.currentXP}")
            }
        }
    }


    fun showGUI() {
        val frame = JFrame("Test Quest - A quest to improve locators robustness")
        frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        frame.layout = BorderLayout()
        textArea = JTextArea()
        textArea!!.isEditable = false
        val scrollPane = JScrollPane(textArea)
        scrollPane.preferredSize = Dimension(800, 600)
        frame.add(scrollPane, BorderLayout.CENTER)
        frame.pack()
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
    }




}