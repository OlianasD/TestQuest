package ui

import com.intellij.util.ui.JBUI
import gamification.UserProfile
import java.awt.*
import javax.swing.*
import javax.swing.border.Border
import javax.swing.border.TitledBorder

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

    fun updateGUI(userProfile: UserProfile, notifyChange: Boolean) {
        SwingUtilities.invokeLater {
            // Main panel
            val mainPanel = JPanel(GridBagLayout())
            mainPanel.border = BorderFactory.createEmptyBorder(0, 0, 50, 50)
            mainPanel.background = Color.LIGHT_GRAY
            val gbc = GridBagConstraints()
            gbc.fill = GridBagConstraints.BOTH
            gbc.insets = JBUI.insets(10)

            // Font settings
            val titleFont = Font("Arial", Font.BOLD, 18) // title size
            val font = Font("Arial", Font.PLAIN, 16) // content size

            // User data panel
            val userInfoPanel = JPanel()
            userInfoPanel.layout = BoxLayout(userInfoPanel, BoxLayout.Y_AXIS)
            userInfoPanel.border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "User Info",
                TitledBorder.CENTER, // Center the title
                TitledBorder.DEFAULT_POSITION,
                titleFont,
                Color.BLACK
            )
            userInfoPanel.background = Color.LIGHT_GRAY

            val userInfoInnerPanel = JPanel(GridBagLayout())
            userInfoInnerPanel.background = Color.LIGHT_GRAY
            val gbcInner = GridBagConstraints()
            gbcInner.insets = JBUI.insets(5)
            gbcInner.anchor = GridBagConstraints.WEST

            val nameLabel = JLabel("Name: ${userProfile.name}")
            nameLabel.font = font
            nameLabel.foreground = Color.BLACK
            gbcInner.gridx = 0
            gbcInner.gridy = 0
            gbcInner.weightx = 1.0
            gbcInner.fill = GridBagConstraints.HORIZONTAL
            userInfoInnerPanel.add(nameLabel, gbcInner)

            val titleLabel = JLabel("Title: ${userProfile.title}")
            titleLabel.font = font
            titleLabel.foreground = Color.BLACK
            gbcInner.gridy = 1
            userInfoInnerPanel.add(titleLabel, gbcInner)

            val levelLabel = JLabel("Level: ${userProfile.level}")
            levelLabel.font = font
            levelLabel.foreground = Color.BLACK
            gbcInner.gridy = 2
            userInfoInnerPanel.add(levelLabel, gbcInner)

            val currentXP = userProfile.currentXP
            val nextXP = userProfile.nextXP
            val xpLabel: JLabel
            if (nextXP == Int.MAX_VALUE)
                xpLabel = JLabel("Current XP: $currentXP/-")
            else
                xpLabel = JLabel("Current XP: $currentXP/$nextXP")
            xpLabel.font = font
            xpLabel.background = Color.BLACK
            xpLabel.foreground = Color.BLACK
            gbcInner.gridy = 3
            userInfoInnerPanel.add(xpLabel, gbcInner)
            val percentComplete = if (nextXP == Int.MAX_VALUE) 100 else (currentXP.toDouble() / nextXP * 100).toInt()
            val xpProgressBar = JProgressBar(0, 100)
            xpProgressBar.value = percentComplete
            xpProgressBar.setStringPainted(true)
            xpProgressBar.string = "$percentComplete%"
            xpProgressBar.font = font.deriveFont(Font.BOLD)
            xpProgressBar.foreground = Color.BLACK
            xpProgressBar.background = Color.WHITE
            xpProgressBar.preferredSize = Dimension(150, 20)
            gbcInner.gridy = 4
            gbcInner.gridwidth = 2
            userInfoInnerPanel.add(xpProgressBar, gbcInner)

            val imageBox = JPanel()
            imageBox.border = BorderFactory.createLineBorder(Color.DARK_GRAY)
            imageBox.preferredSize = Dimension(150, 150)
            imageBox.maximumSize = Dimension(150, 150)
            imageBox.background = Color.LIGHT_GRAY
            gbcInner.gridx = 2
            gbcInner.gridy = 0
            gbcInner.gridheight = 5
            gbcInner.anchor = GridBagConstraints.NORTHEAST
            gbcInner.fill = GridBagConstraints.NONE
            userInfoInnerPanel.add(imageBox, gbcInner)

            userInfoPanel.add(userInfoInnerPanel)
            userInfoPanel.preferredSize = Dimension(800, 300)
            gbc.gridx = 0
            gbc.gridy = 0
            gbc.gridwidth = 2
            gbc.weightx = 1.0
            gbc.weighty = 0.33
            mainPanel.add(userInfoPanel, gbc)

            // Dailies panel
            val dailiesPanel = JPanel()
            dailiesPanel.layout = BoxLayout(dailiesPanel, BoxLayout.Y_AXIS)
            dailiesPanel.border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Dailies",
                TitledBorder.CENTER, // Center the title
                TitledBorder.DEFAULT_POSITION,
                titleFont,
                Color.BLACK
            )
            dailiesPanel.background = Color.LIGHT_GRAY
            for (dailyProgress in userProfile.dailyProgresses) {
                val dailyLabel = JLabel(dailyProgress.daily.description)
                dailyLabel.font = font
                dailyLabel.foreground = Color.BLACK
                dailiesPanel.add(dailyLabel)
            }
            dailiesPanel.preferredSize = Dimension(800, 300)
            gbc.gridx = 0
            gbc.gridy = 1
            gbc.gridwidth = 1
            gbc.weightx = 1.0
            gbc.weighty = 0.33
            mainPanel.add(dailiesPanel, gbc)

            // Achievements panel
            val achievementsPanel = JPanel()
            achievementsPanel.layout = BoxLayout(achievementsPanel, BoxLayout.Y_AXIS)
            achievementsPanel.border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Achievements Unlocked",
                TitledBorder.CENTER, // Center the title
                TitledBorder.DEFAULT_POSITION,
                titleFont,
                Color.BLACK
            )
            achievementsPanel.background = Color.LIGHT_GRAY
            for (achievement in userProfile.completedAchievements) {
                val achievementLabel = JLabel(achievement.name)
                achievementLabel.font = font
                achievementLabel.foreground = Color.BLACK
                achievementsPanel.add(achievementLabel)
            }
            achievementsPanel.preferredSize = Dimension(800, 300)
            gbc.gridx = 0
            gbc.gridy = 2
            gbc.gridwidth = 1
            gbc.weightx = 1.0
            gbc.weighty = 0.33
            mainPanel.add(achievementsPanel, gbc)

            // Update GUI
            textArea?.removeAll()
            textArea?.layout = BorderLayout()
            textArea?.add(mainPanel, BorderLayout.CENTER)
            textArea?.revalidate()
            textArea?.repaint()

            // Send notification popup in case of changes
            if (notifyChange)
                showPopup("New Level: ${userProfile.level}\nNew Title: ${userProfile.title}\nNew XP: ${userProfile.currentXP}")
        }
    }

    fun showGUI() {
        val frame = JFrame("Test Quest - A quest to improve locators robustness")
        frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        frame.layout = BorderLayout()
        textArea = JTextArea()
        textArea!!.isEditable = false
        val scrollPane = JScrollPane(textArea)
        frame.add(scrollPane, BorderLayout.CENTER)
        frame.isResizable = false
        frame.setSize(1000, 800)  // Impostiamo una dimensione coerente con i pannelli
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
    }




}