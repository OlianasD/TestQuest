package ui

import com.intellij.util.ui.JBUI
import gamification.DailyManager
import gamification.DailyProgress
import gamification.GamificationManager
import gamification.UserProfile
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.io.File
import javax.swing.*
import javax.swing.border.TitledBorder
import javax.swing.filechooser.FileNameExtensionFilter

class GUIManager {

    private var textArea: JTextArea? = null
    private var changed: Boolean = false
    private var dailyRemoved: Boolean = false


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
            val titleFont = Font("Arial", Font.BOLD, 18)
            var font = Font("Arial", Font.PLAIN, 16)

            // User Info panel
            val userInfoPanel = JPanel()
            userInfoPanel.layout = BoxLayout(userInfoPanel, BoxLayout.Y_AXIS)
            userInfoPanel.border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "User Info",
                TitledBorder.CENTER,
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

            // Name
            val namePanel = JPanel(FlowLayout(FlowLayout.LEFT))
            namePanel.background = Color.LIGHT_GRAY
            val nameLabel = JLabel("Name: ${userProfile.name}")
            nameLabel.font = font
            nameLabel.foreground = Color.BLACK
            val editNameButton = JButton("Edit")
            editNameButton.addActionListener {
                val newName = JOptionPane.showInputDialog(null, "Enter new name:", userProfile.name)
                if (newName != null && newName.isNotEmpty()) {
                    userProfile.name = newName
                    nameLabel.text = "Name: $newName"
                    GamificationManager.updateUserProfileAfterGUIChanges(userProfile)
                    changed = true
                    if (notifyChange) {
                        showPopup("Name updated to $newName")
                    }
                }
            }
            namePanel.add(nameLabel)
            namePanel.add(editNameButton)
            gbcInner.gridx = 0
            gbcInner.gridy = 0
            gbcInner.weightx = 0.5
            gbcInner.fill = GridBagConstraints.HORIZONTAL
            gbcInner.insets = JBUI.insetsLeft(10)
            userInfoInnerPanel.add(namePanel, gbcInner)

            // Title
            val titleLabel = JLabel("Title: ${userProfile.title}")
            titleLabel.font = font
            titleLabel.foreground = Color.BLACK
            gbcInner.gridy = 1
            gbcInner.insets = JBUI.insets(15)
            userInfoInnerPanel.add(titleLabel, gbcInner)

            // Level
            val levelLabel = JLabel("Level: ${userProfile.level}")
            levelLabel.font = font
            levelLabel.foreground = Color.BLACK
            gbcInner.gridy = 2
            userInfoInnerPanel.add(levelLabel, gbcInner)

            // XP
            val currentXP = userProfile.currentXP
            val nextXP = userProfile.nextXP
            val xpLabel = if (nextXP == Int.MAX_VALUE)
                JLabel("Current XP: $currentXP/-")
            else
                JLabel("Current XP: $currentXP/$nextXP")
            xpLabel.font = font
            xpLabel.background = Color.BLACK
            xpLabel.foreground = Color.BLACK
            gbcInner.gridy = 3
            userInfoInnerPanel.add(xpLabel, gbcInner)

            // XP Progress Bar
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

            // Profile picture
            val imageBox = JPanel()
            imageBox.border = BorderFactory.createLineBorder(Color.DARK_GRAY)
            imageBox.preferredSize = Dimension(100, 100)
            imageBox.maximumSize = Dimension(100, 100)
            imageBox.background = Color.LIGHT_GRAY
            var imageIcon = ImageIcon(userProfile.propic)
            var scaledIcon = ImageIcon(imageIcon.image.getScaledInstance(100, 100, Image.SCALE_SMOOTH))
            var imageLabel = JLabel(scaledIcon)
            imageBox.add(imageLabel)
            val editImageButton = JButton("Edit")
            editImageButton.addActionListener {
                val fileChooser = JFileChooser()
                fileChooser.fileFilter = FileNameExtensionFilter("Image files", "jpg", "png", "gif")
                val result = fileChooser.showOpenDialog(null)
                if (result == JFileChooser.APPROVE_OPTION) {
                    val selectedFile: File = fileChooser.selectedFile
                    userProfile.propic = selectedFile.absolutePath
                    GamificationManager.updateUserProfileAfterGUIChanges(userProfile)
                    changed = true
                    imageBox.removeAll()
                    imageIcon = ImageIcon(selectedFile.absolutePath)
                    scaledIcon = ImageIcon(imageIcon.image.getScaledInstance(100, 100, Image.SCALE_SMOOTH))
                    imageLabel = JLabel(scaledIcon)
                    imageBox.add(imageLabel)
                    imageBox.revalidate()
                    imageBox.repaint()
                    if (notifyChange) {
                        showPopup("Image updated")
                    }
                }
            }
            gbcInner.gridx = 2
            gbcInner.gridy = 0
            gbcInner.gridheight = 3
            gbcInner.anchor = GridBagConstraints.NORTHEAST
            gbcInner.fill = GridBagConstraints.NONE
            gbcInner.insets = JBUI.insets(0, 15)
            userInfoInnerPanel.add(imageBox, gbcInner)
            gbcInner.gridy = 5
            gbcInner.gridheight = 1
            userInfoInnerPanel.add(editImageButton, gbcInner)
            userInfoPanel.add(userInfoInnerPanel)
            userInfoPanel.preferredSize = Dimension(800, 220)
            gbc.gridx = 0
            gbc.gridy = 0
            gbc.gridwidth = 2
            gbc.weightx = 1.0
            gbc.weighty = 0.25
            mainPanel.add(userInfoPanel, gbc)

            // Dailies panel
            val dailiesPanel = JPanel()
            dailiesPanel.layout = BoxLayout(dailiesPanel, BoxLayout.Y_AXIS)
            dailiesPanel.border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Dailies",
                TitledBorder.CENTER,
                TitledBorder.DEFAULT_POSITION,
                titleFont,
                Color.BLACK
            )
            dailiesPanel.background = Color.LIGHT_GRAY
            for (dailyProgress in userProfile.dailyProgresses) {
                val dailyPanel = JPanel()
                dailyPanel.layout = BoxLayout(dailyPanel, BoxLayout.X_AXIS)
                dailyPanel.alignmentX = Component.LEFT_ALIGNMENT
                dailyPanel.background = Color.LIGHT_GRAY
                // Daily Description
                showDailyDetails(dailyPanel, dailyProgress, font) //show daily info
                // Discard button
                var discarded = false
                val removeButton = JButton("Discard")
                removeButton.addActionListener {
                    val newDailyProgress = DailyManager.reassignDailyFromDiscard(userProfile, dailyProgress.daily)
                    dailyPanel.remove(removeButton)//remove discard button as only 1 discard is allowed
                    showDailyDetails(dailyPanel, newDailyProgress, font)//update panel with newly assigned daily info
                    discarded = true //TODO: use a file to memorize that the discard occurred for that daily
                    dailiesPanel.revalidate()
                    dailiesPanel.repaint()
                }
                if(!discarded)
                    dailyPanel.add(removeButton)
                dailiesPanel.add(dailyPanel)
            }
            dailiesPanel.preferredSize = Dimension(800, 270)
            gbc.gridx = 0
            gbc.gridy = 1
            gbc.gridwidth = 1
            gbc.weightx = 1.0
            gbc.weighty = 0.375
            mainPanel.add(dailiesPanel, gbc)

            // Achievements panel
            val achievementsPanel = JPanel()
            achievementsPanel.layout = BorderLayout()
            achievementsPanel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
            achievementsPanel.background = Color.LIGHT_GRAY
            val tabbedPane = JTabbedPane()
            // Unlocked achievements Panel
            val unlockedAchievementsPanel = JPanel()
            unlockedAchievementsPanel.layout = BoxLayout(unlockedAchievementsPanel, BoxLayout.Y_AXIS)
            unlockedAchievementsPanel.background = Color.LIGHT_GRAY
            val unlockedScrollPane = JScrollPane(unlockedAchievementsPanel)
            tabbedPane.addTab("Unlocked", unlockedScrollPane)
            // Ongoing achievements Panel
            val ongoingAchievementsPanel = JPanel()
            ongoingAchievementsPanel.layout = BoxLayout(ongoingAchievementsPanel, BoxLayout.Y_AXIS
            )
            ongoingAchievementsPanel.background = Color.LIGHT_GRAY
            val ongoingScrollPane = JScrollPane(ongoingAchievementsPanel)
            tabbedPane.addTab("Ongoing", ongoingScrollPane)
            achievementsPanel.add(tabbedPane, BorderLayout.CENTER)
            // Populate Unlocked achievements panel
            for (achievement in userProfile.completedAchievements) {
                val achievementPanel = JPanel()
                achievementPanel.layout = BoxLayout(achievementPanel, BoxLayout.X_AXIS)
                achievementPanel.alignmentX = Component.LEFT_ALIGNMENT
                achievementPanel.background = Color.LIGHT_GRAY
                // Icon
                val iconLabel = JLabel()
                val icon = ImageIcon(achievement.icon)
                iconLabel.icon = ImageIcon(icon.image.getScaledInstance(50, 50, Image.SCALE_SMOOTH))
                achievementPanel.add(iconLabel)
                achievementPanel.background = Color.LIGHT_GRAY
                // Achievement Name
                val nameLabel = JLabel(achievement.name)
                nameLabel.font = font
                nameLabel.foreground = Color.BLACK
                achievementPanel.add(nameLabel)
                // Tooltip for description
                nameLabel.toolTipText = achievement.description
                nameLabel.addMouseListener(object : MouseAdapter() {
                    override fun mouseEntered(e: MouseEvent?) {
                        nameLabel.toolTipText = achievement.description
                    }
                })
                // Add achievement panel to unlockedAchievementsPanel
                unlockedAchievementsPanel.add(achievementPanel)
            }
            // Populate Ongoing achievements panel
            for (progress in userProfile.achievementProgresses) {
                val achievement = progress.achievement
                val progressPanel = JPanel()
                progressPanel.layout = BoxLayout(progressPanel, BoxLayout.X_AXIS)
                progressPanel.alignmentX = Component.LEFT_ALIGNMENT
                progressPanel.background = Color.LIGHT_GRAY
                // Icon
                val iconLabel = JLabel()
                val icon = ImageIcon(achievement.icon)
                iconLabel.icon = ImageIcon(icon.image.getScaledInstance(50, 50, Image.SCALE_SMOOTH))
                progressPanel.add(iconLabel)
                progressPanel.background = Color.LIGHT_GRAY
                // Achievement Name
                val nameLabel = JLabel("${achievement.name} (${progress.progress} / ${achievement.target})")
                nameLabel.font = font
                nameLabel.foreground = Color.BLACK
                progressPanel.add(nameLabel)
                // Tooltip for description
                nameLabel.toolTipText = achievement.description
                nameLabel.addMouseListener(object : MouseAdapter() {
                    override fun mouseEntered(e: MouseEvent?) {
                        nameLabel.toolTipText = achievement.description
                    }
                })
                // Add progress panel to ongoingAchievementsPanel
                ongoingAchievementsPanel.add(progressPanel)
            }
            achievementsPanel.preferredSize = Dimension(800, 270)
            gbc.gridx = 0
            gbc.gridy = 2
            gbc.gridwidth = 1
            gbc.weightx = 1.0
            gbc.weighty = 0.375
            mainPanel.add(achievementsPanel, gbc)

            // Refresh GUI & send notification in case of changes
            textArea?.removeAll()
            textArea?.layout = BorderLayout()
            textArea?.add(mainPanel, BorderLayout.CENTER)
            textArea?.revalidate()
            textArea?.repaint()

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
        frame.setSize(1000, 800)
        frame.setLocationRelativeTo(null)
        frame.isVisible = true
    }


    private fun showDailyDetails(dailyPanel: JPanel, dailyProgress: DailyProgress, font: Font){
        val existingLabel = dailyPanel.components.find { it is JLabel } as? JLabel
        existingLabel?.let {
            dailyPanel.remove(it)
        }
        val dailyLabel = JLabel(dailyProgress.daily.description)
        dailyLabel.font = font
        dailyLabel.foreground = Color.BLACK
        dailyPanel.add(dailyLabel)
        // Tooltip for progression
        dailyLabel.toolTipText = "Progress: ${dailyProgress.progress}"
        dailyLabel.addMouseListener(object : MouseAdapter() {
            override fun mouseEntered(e: MouseEvent?) {
                dailyLabel.toolTipText = "Progress: ${dailyProgress.progress}"
            }
        })
    }








}
