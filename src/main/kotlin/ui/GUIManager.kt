package ui

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.Gray
import com.intellij.ui.JBColor
import com.intellij.ui.awt.RelativePoint
import com.intellij.ui.table.JBTable
import com.intellij.util.ui.JBUI
import gamification.DailyManager
import gamification.DailyProgress
import gamification.GamificationManager
import gamification.UserProfile
import extractor.locator.Locator
import listener.changes.CodeChangeListener
import listener.test.TestExecutionListener
import analyzer.locator.LocatorsAnalyzer
import analyzer.pageobject.PageObjectsAnalyzer
import extractor.pageobject.MethodInfo
import extractor.pageobject.PageObject
import testquest.TestQuestAction
import testquest.WindowStateManager
import utils.FilePathSolver
import utils.ProgressFileHandler
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.font.TextAttribute
import java.io.File
import java.io.FileInputStream
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.Timer
import javax.swing.border.TitledBorder
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.DefaultTableModel
import javax.swing.table.TableCellRenderer

object GUIManager {

    private var GamificationWindow: JTextArea? = null // window to show main gamification aspects
    private var FragilityWindow: JFrame? = null // window to show locator scores
    private var currentPopup: JDialog? = null // window to show user changes (e.g., progression, daily mode change)


    //to show immediate user progressions via popup
    private fun showPopup(message: String) {
        SwingUtilities.invokeLater {
            currentPopup?.dispose() //if an active popup is present, hide it
            val pane = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                background = JBColor(Gray._0, Color(0, 0, 0, 180))
            }
            val label = JLabel("<html><div style='padding: 10px; color: white; font-size: 16px;'>$message</div></html>").apply {
                horizontalAlignment = SwingConstants.CENTER
            }
            pane.add(label)
            val dialog = JDialog().apply {
                title = ""
                isUndecorated = true
                isModal = false
                contentPane.add(pane)
                pack()
                val screenSize = Toolkit.getDefaultToolkit().screenSize
                setLocation(screenSize.width - width - 20, screenSize.height - height - 100)
                isVisible = true
            }
            currentPopup = dialog //save current popup reference
            //the popup disposes in 10 secs
            Timer(10000) {
                dialog.dispose()
            }.start()
        }
    }








    //to update the gui based on user progression
    fun updateGUI(userProfile: UserProfile, notifyChange: Boolean, msg: String = "") {
        SwingUtilities.invokeLater {

            // Main panel
            val mainPanel = JPanel(GridBagLayout())
            mainPanel.background = JBColor.LIGHT_GRAY
            val gbc = GridBagConstraints()
            gbc.fill = GridBagConstraints.BOTH
            gbc.insets = JBUI.insets(10)

            // Font settings
            val titleFont = Font("Arial", Font.BOLD, 18)
            val font = Font("Arial", Font.PLAIN, 16)

            // User Info panel
            val userInfoPanel = JPanel()
            userInfoPanel.layout = BoxLayout(userInfoPanel, BoxLayout.Y_AXIS)
            userInfoPanel.border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(JBColor.DARK_GRAY),
                "User Info",
                TitledBorder.CENTER,
                TitledBorder.DEFAULT_POSITION,
                titleFont,
                JBColor.BLACK
            )
            userInfoPanel.background = JBColor.LIGHT_GRAY
            val userInfoInnerPanel = JPanel(GridBagLayout())
            userInfoInnerPanel.background = JBColor.LIGHT_GRAY
            val gbcInner = GridBagConstraints()
            gbcInner.insets = JBUI.insets(5)
            gbcInner.anchor = GridBagConstraints.WEST

            // Name
            val namePanel = JPanel(FlowLayout(FlowLayout.LEFT))
            namePanel.background = JBColor.LIGHT_GRAY
            val nameLabel = JLabel("Name: ${userProfile.name}")
            nameLabel.font = font
            nameLabel.foreground = JBColor.BLACK
            val editNameButton = JButton("Edit")
            editNameButton.addActionListener {
                val newName = JOptionPane.showInputDialog(null, "Enter new name:", userProfile.name)
                if (newName != null && newName.isNotEmpty()) {
                    userProfile.name = newName
                    nameLabel.text = "Name: $newName"
                    GamificationManager.updateUserProfile(userProfile)
                    showPopup("Name updated to $newName")
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
            titleLabel.foreground = JBColor.BLACK
            gbcInner.gridy = 1
            gbcInner.insets = JBUI.insets(15)
            userInfoInnerPanel.add(titleLabel, gbcInner)

            // Level
            val levelLabel = JLabel("Level: ${userProfile.level}")
            levelLabel.font = font
            levelLabel.foreground = JBColor.BLACK
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
            xpLabel.background = JBColor.BLACK
            xpLabel.foreground = JBColor.BLACK
            gbcInner.gridy = 3
            userInfoInnerPanel.add(xpLabel, gbcInner)

            // XP Progress Bar
            val percentComplete = if (nextXP == Int.MAX_VALUE) 100 else (currentXP.toDouble() / nextXP * 100).toInt()
            val xpProgressBar = JProgressBar(0, 100)
            xpProgressBar.value = percentComplete
            xpProgressBar.setStringPainted(true)
            xpProgressBar.string = "$percentComplete%"
            xpProgressBar.font = font.deriveFont(Font.BOLD)
            xpProgressBar.foreground = JBColor.BLACK
            xpProgressBar.background = JBColor.WHITE
            xpProgressBar.preferredSize = Dimension(150, 20)
            gbcInner.gridy = 4
            gbcInner.gridwidth = 2
            userInfoInnerPanel.add(xpProgressBar, gbcInner)

            // Profile picture
            val imageBox = JPanel()
            imageBox.border = BorderFactory.createLineBorder(JBColor.DARK_GRAY)
            imageBox.preferredSize = Dimension(100, 100)
            imageBox.maximumSize = Dimension(100, 100)
            imageBox.background = JBColor.LIGHT_GRAY
            var imageStream = this::class.java.classLoader.getResourceAsStream(userProfile.propic)
            if (imageStream == null) {
                val file = File(userProfile.propic)
                if (file.exists())
                    imageStream = FileInputStream(file)
                else {
                    userProfile.propic = FilePathSolver.USER_PROPIC_PATH
                    imageStream = this::class.java.classLoader.getResourceAsStream(FilePathSolver.USER_PROPIC_PATH)
                }
            }
            var image = ImageIO.read(imageStream)
            var scaledIcon = ImageIcon(image.getScaledInstance(100, 100, Image.SCALE_SMOOTH))
            var imageLabel = JLabel(scaledIcon)
            imageBox.add(imageLabel)

            //in case of propic change
            val editImageButton = JButton("Edit")
            editImageButton.addActionListener {
                val fileChooser = JFileChooser()
                fileChooser.fileFilter = FileNameExtensionFilter("Image files", "jpg", "png", "gif")
                val result = fileChooser.showOpenDialog(null)
                if (result == JFileChooser.APPROVE_OPTION) {
                    val selectedFile: File = fileChooser.selectedFile
                    userProfile.propic = selectedFile.absolutePath //TODO: check if we want the user to select propics from any place
                    GamificationManager.updateUserProfile(userProfile)
                    imageBox.removeAll()
                    imageStream = if (File(userProfile.propic).exists())
                        FileInputStream(userProfile.propic)
                     else
                        this::class.java.classLoader.getResourceAsStream(userProfile.propic)
                    image = ImageIO.read(imageStream)
                    scaledIcon = ImageIcon(image.getScaledInstance(100, 100, Image.SCALE_SMOOTH))
                    imageLabel = JLabel(scaledIcon)
                    imageBox.add(imageLabel)
                    imageBox.add(imageLabel)
                    imageBox.revalidate()
                    imageBox.repaint()
                    showPopup("Profile picture updated")
                }
            }
            gbcInner.gridx = 2
            gbcInner.gridy = 0
            gbcInner.gridheight = 2
            gbcInner.anchor = GridBagConstraints.NORTHEAST
            gbcInner.fill = GridBagConstraints.NONE
            gbcInner.insets = JBUI.insets(0, 15)
            userInfoInnerPanel.add(imageBox, gbcInner)
            gbcInner.gridy = 2
            gbcInner.gridheight = 2
            userInfoInnerPanel.add(editImageButton, gbcInner)
            userInfoPanel.add(userInfoInnerPanel)
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
                BorderFactory.createLineBorder(JBColor.DARK_GRAY),
                "Dailies",
                TitledBorder.CENTER,
                TitledBorder.DEFAULT_POSITION,
                titleFont,
                JBColor.BLACK
            )
            dailiesPanel.background = JBColor.LIGHT_GRAY
            populateDailiesPanel(dailiesPanel, userProfile, font)
            gbc.gridx = 0
            gbc.gridy = 1
            gbc.gridwidth = 2
            gbc.weightx = 1.0
            gbc.weighty = 0.25
            mainPanel.add(dailiesPanel, gbc)

            // Achievements panel
            val achievementsPanel = JPanel()
            achievementsPanel.layout = BorderLayout()
            achievementsPanel.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
            achievementsPanel.background = JBColor.LIGHT_GRAY
            val tabbedPane = JTabbedPane()

            // Unlocked achievements Panel
            val unlockedAchievementsPanel = JPanel()
            unlockedAchievementsPanel.layout = BoxLayout(unlockedAchievementsPanel, BoxLayout.Y_AXIS)
            unlockedAchievementsPanel.background = JBColor.LIGHT_GRAY
            val unlockedScrollPane = JScrollPane(unlockedAchievementsPanel)
            tabbedPane.addTab("Unlocked", unlockedScrollPane)

            // Ongoing achievements Panel
            val ongoingAchievementsPanel = JPanel()
            ongoingAchievementsPanel.layout = BoxLayout(ongoingAchievementsPanel, BoxLayout.Y_AXIS
            )
            ongoingAchievementsPanel.background = JBColor.LIGHT_GRAY
            val ongoingScrollPane = JScrollPane(ongoingAchievementsPanel)
            tabbedPane.addTab("Ongoing", ongoingScrollPane)
            achievementsPanel.add(tabbedPane, BorderLayout.CENTER)

            // Populate Unlocked achievements panel
            for (achievement in userProfile.completedAchievements) {
                val achievementPanel = JPanel()
                achievementPanel.layout = BoxLayout(achievementPanel, BoxLayout.X_AXIS)
                achievementPanel.alignmentX = Component.LEFT_ALIGNMENT
                achievementPanel.background = JBColor.LIGHT_GRAY
                // Icon
                imageStream = this::class.java.classLoader.getResourceAsStream(achievement.icon)
                val icon = ImageIO.read(imageStream)
                val iconLabel = JLabel()
                iconLabel.icon = ImageIcon(icon.getScaledInstance(25, 25, Image.SCALE_SMOOTH))
                achievementPanel.add(iconLabel)
                achievementPanel.background = JBColor.LIGHT_GRAY
                // Achievement Name
                val nameLabel1 = JLabel(achievement.name)
                nameLabel1.font = font
                nameLabel1.foreground = JBColor.BLACK
                achievementPanel.add(nameLabel1)
                // Tooltip for description
                nameLabel1.toolTipText = achievement.description
                nameLabel1.addMouseListener(object : MouseAdapter() {
                    override fun mouseEntered(e: MouseEvent?) {
                        nameLabel1.toolTipText = achievement.description
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
                progressPanel.background = JBColor.LIGHT_GRAY
                // Icon
                imageStream = this::class.java.classLoader.getResourceAsStream(achievement.icon)
                val icon = ImageIO.read(imageStream)
                val iconLabel = JLabel()
                iconLabel.icon = ImageIcon(icon.getScaledInstance(25, 25, Image.SCALE_SMOOTH))
                progressPanel.add(iconLabel)
                progressPanel.background = JBColor.LIGHT_GRAY
                // Achievement Name
                val nameLabel2 = JLabel("${achievement.name} (${progress.progress} / ${achievement.target})")
                nameLabel2.font = font
                nameLabel2.foreground = JBColor.BLACK
                progressPanel.add(nameLabel2)
                // Tooltip for description
                nameLabel2.toolTipText = achievement.description
                nameLabel2.addMouseListener(object : MouseAdapter() {
                    override fun mouseEntered(e: MouseEvent?) {
                        nameLabel2.toolTipText = achievement.description
                    }
                })
                // Add progress panel to ongoingAchievementsPanel
                ongoingAchievementsPanel.add(progressPanel)
            }
            gbc.gridx = 0
            gbc.gridy = 2
            gbc.gridwidth = 2
            gbc.weightx = 1.0
            gbc.weighty = 0.25
            achievementsPanel.border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(JBColor.LIGHT_GRAY),
                "Achievements",
                TitledBorder.CENTER,
                TitledBorder.DEFAULT_POSITION,
                titleFont,
                JBColor.BLACK
            )
            mainPanel.add(achievementsPanel, gbc)

            //Daily Assignment Mode panel
            val dailyAssignmentModePanel = JPanel(FlowLayout(FlowLayout.RIGHT))
            dailyAssignmentModePanel.background = JBColor.LIGHT_GRAY
            val randomMode = JRadioButton("Random")
            randomMode.isSelected = GamificationManager.assignmentMode == GamificationManager.DailyAssignmentMode.RANDOM
            randomMode.addActionListener {
                if (randomMode.isSelected) {
                    GamificationManager.assignmentMode = GamificationManager.DailyAssignmentMode.RANDOM
                    updateGUI(userProfile, false)
                }
            }
            val targetedMode = JRadioButton("Targeted")
            targetedMode.isSelected = GamificationManager.assignmentMode == GamificationManager.DailyAssignmentMode.TARGETED
            targetedMode.addActionListener {
                if (targetedMode.isSelected) {
                    GamificationManager.assignmentMode = GamificationManager.DailyAssignmentMode.TARGETED
                    GamificationManager.assignTargetDailies()//this includes updateGUI() but is preceded by assignment
                }
            }
            val inclusiveMode = JRadioButton("Inclusive")
            inclusiveMode.isSelected = GamificationManager.assignmentMode == GamificationManager.DailyAssignmentMode.INCLUSIVE
            inclusiveMode.addActionListener {
                if (inclusiveMode.isSelected) {
                    GamificationManager.assignmentMode = GamificationManager.DailyAssignmentMode.INCLUSIVE
                    updateGUI(userProfile, false)
                }
            }
            val modeGroup = ButtonGroup()
            modeGroup.add(randomMode)
            modeGroup.add(targetedMode)
            modeGroup.add(inclusiveMode)
            val modeSelectionPanel = JPanel(FlowLayout(FlowLayout.LEFT))
            modeSelectionPanel.add(randomMode)
            modeSelectionPanel.add(targetedMode)
            modeSelectionPanel.add(inclusiveMode)
            val titledBorder = BorderFactory.createTitledBorder("Daily Assignment Mode")
            titledBorder.titleJustification = TitledBorder.CENTER
            modeSelectionPanel.border = titledBorder
            dailyAssignmentModePanel.add(modeSelectionPanel)
            GamificationManager.updateUserProfile(userProfile)
            gbc.gridx = 1
            gbc.gridy = 3
            gbc.gridwidth = 1
            gbc.weightx = 0.0
            gbc.weighty = 0.0
            gbc.anchor = GridBagConstraints.SOUTHEAST
            mainPanel.add(dailyAssignmentModePanel, gbc)

            // Refresh GUI & send notification in case of changes
            GamificationWindow?.removeAll()
            GamificationWindow?.layout = BorderLayout()
            GamificationWindow?.add(mainPanel, BorderLayout.CENTER)
            GamificationWindow?.revalidate()
            GamificationWindow?.repaint()
            mainPanel.revalidate()
            mainPanel.repaint()
            if (notifyChange) {
                val formattedMsg = "<html>${msg.replace("\n", "<br>")}</html>"
                showPopup(formattedMsg)
            }
        }
    }


    //main panel creation and management in case of plugin closed
    fun showGUI() {
        val frame = JFrame("TestQuest").apply {
            defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
            layout = BorderLayout()
            GamificationWindow = JTextArea().apply { isEditable = false }
            add(JScrollPane(GamificationWindow), BorderLayout.CENTER)
            isResizable = false
            setSize(1000, 800)
            setLocationRelativeTo(null)
            isVisible = true
        }
        frame.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                FragilityWindow?.dispose()
                CodeChangeListener.instance.dispose()
                TestExecutionListener.instance.dispose()
                WindowStateManager.isWindowOpen = false //to let the Gamification Window be opened again
                //exitProcess(0)
            }
        })
    }





    private fun populateDailiesPanel(dailiesPanel: JPanel, userProfile: UserProfile, font: Font) {
        // Clear the panel before repopulating
        dailiesPanel.removeAll()
        // Create an inner panel for the scrolling content
        val innerPanel = JPanel()
        innerPanel.layout = BoxLayout(innerPanel, BoxLayout.Y_AXIS)
        for (dailyProgress in userProfile.dailyProgresses) {
            val type = dailyProgress.daily.type
            if (type.equals(GamificationManager.assignmentMode.name, ignoreCase = true)) {  //Show only the dailies of the current mode
                val dailyPanel = JPanel()
                dailyPanel.layout = BoxLayout(dailyPanel, BoxLayout.X_AXIS)
                dailyPanel.alignmentX = Component.LEFT_ALIGNMENT
                dailyPanel.background = JBColor.LIGHT_GRAY
                // Show daily details
                showDailyDetails(dailyPanel, dailyProgress, font)
                // Discard button for random dailies
                if (!dailyProgress.discarded && type.equals("random", ignoreCase = true)) {
                    val removeButton = JButton("Discard")
                    removeButton.addActionListener {
                        val newDailyProgress = DailyManager.reassignRandomDailyFromDiscard(userProfile, dailyProgress.daily)
                        dailyPanel.remove(removeButton)  // Remove discard button as only 1 discard is allowed
                        showDailyDetails(dailyPanel, newDailyProgress, font)  // Update panel with newly assigned daily info
                        innerPanel.revalidate()
                        innerPanel.repaint()
                    }
                    dailyPanel.add(removeButton)
                }
                // Add each daily panel to the innerPanel (the scrollable area)
                innerPanel.add(dailyPanel)
            }
        }
        // Add the scroll pane around the innerPanel that contains all dailies
        val scrollPane = JScrollPane(innerPanel).apply {
            preferredSize = Dimension(500, 500)  //Set the preferred size for the scroll pane
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        }
        innerPanel.minimumSize = Dimension(500, 500)
        // Add the scroll pane to the dailiesPanel
        dailiesPanel.add(scrollPane)
        // Revalidate and repaint the panel to reflect changes
        dailiesPanel.revalidate()
        dailiesPanel.repaint()
    }


    private fun showDailyDetails(dailyPanel: JPanel, dailyProgress: DailyProgress, font: Font) {
        dailyPanel.removeAll() // Remove old elements
        val locatorsToVerifyMap = LocatorsAnalyzer.getFixedAndPendingLocatorsMap() //retrieve map of fixed locators still to verify
        val posToVerifyMap = PageObjectsAnalyzer.getFixedAndPendingLocatorsMap()

        // Create daily label on mouse over for random dailies
        if (dailyProgress.daily.type.equals("random", ignoreCase = true)) {
            val imageStream = this::class.java.classLoader.getResourceAsStream(dailyProgress.daily.icon)
            val icon = ImageIO.read(imageStream)
            val iconLabel = JLabel()
            iconLabel.icon = ImageIcon(icon.getScaledInstance(25, 25, Image.SCALE_SMOOTH))
            dailyPanel.add(iconLabel)
            val additionalDesc = dailyProgress.daily.additionalDescription.let { "$it<br>" }
            val exampleDescription = dailyProgress.daily.exampleDescription.let { "$it<br>" }
            val progressPerc = (dailyProgress.progress * 100 / dailyProgress.daily.target!!)
            val dailyMessage = """
                    <html>
                        <div style= "text-align: justify;" "width: max-content; max-width: 200px; white-space: normal;">
                            <b>Description:</b><br> $additionalDesc<br>
                            <b>Example:</b><br> $exampleDescription<br>
                            <b>Reward:</b> ${dailyProgress.daily.xp} XPs<br><br>
                            <b>Progress:</b> ${progressPerc}%
                        </div>
                    </html>
                """.trimIndent()
            val dailyLabel = JLabel(dailyProgress.daily.description).apply {
                this.font = font
                foreground = JBColor.BLACK
                toolTipText = dailyMessage
            }
            dailyPanel.add(dailyLabel)
        }
        // Create daily label on mouse over and details for targeted dailies
        else if (dailyProgress.daily.type.equals("targeted", ignoreCase = true)) {
            // If no locators or POs (neither to fix or fixed to be verified) are associated with daily, skip
            if (!dailyProgress.daily.isAdvanced && dailyProgress.daily.targetedLocators.isEmpty() &&
                (locatorsToVerifyMap.isEmpty() || (locatorsToVerifyMap[dailyProgress.daily.name]?.isEmpty() == true)))
                return
            if  (dailyProgress.daily.isAdvanced && dailyProgress.daily.issuesInPOs?.isEmpty() == true &&
                (posToVerifyMap.isEmpty() || (posToVerifyMap[dailyProgress.daily.name]?.isEmpty() == true)))
                return
            val imageStream = this::class.java.classLoader.getResourceAsStream(dailyProgress.daily.icon)
            val icon = ImageIO.read(imageStream)
            val iconLabel = JLabel()
            iconLabel.icon = ImageIcon(icon.getScaledInstance(25, 25, Image.SCALE_SMOOTH))
            dailyPanel.add(iconLabel)
            val additionalDesc = dailyProgress.daily.additionalDescription.let { "$it<br>" }
            val exampleDesc = dailyProgress.daily.exampleDescription.let { "$it<br>" }
            val rewardMsg = "${dailyProgress.daily.xp} XP for each listed issue fixed"
            val dailyMessage = """
                    <html>
                        <div style= "text-align: justify;" "width: max-content; max-width: 200px; white-space: normal;">
                            <b>Description:</b> <br> $additionalDesc<br>
                            <b>Example:</b> <br> $exampleDesc<br>
                            <b>Reward:</b> ${rewardMsg}<br>
                        </div>
                    </html>
                """.trimIndent()
            val dailyLabel = JLabel(dailyProgress.daily.description).apply {
                this.font = font
                foreground = JBColor.BLACK
                toolTipText = dailyMessage
            }
            dailyPanel.add(dailyLabel)

            // Show details for targeted dailies with show/hide buttons opening a dedicated window
            var detailsFrame: JFrame? = null
            val toggleButton = JButton("Show")
            toggleButton.addActionListener {
                if (detailsFrame == null || !detailsFrame!!.isVisible) {
                    detailsFrame = JFrame(dailyProgress.daily.description).apply {
                        defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
                        layout = BorderLayout()
                        val targetedIssuesPanel = JPanel().apply {
                            layout = BoxLayout(this, BoxLayout.Y_AXIS)
                        }
                        val smallFont = font.deriveFont((font.size * 0.8f).coerceAtLeast(8f))

                        // show targeted issues for the current daily, whether locators or POs based
                        if  (!dailyProgress.daily.isAdvanced) {

                            //get existing infeasible locators
                            val infeasibleLocators = ProgressFileHandler.loadInfeasibleLocators()
                            val validInfeasibleHashes = infeasibleLocators
                                ?.map { it.hashCode() }
                                ?.intersect(TestQuestAction.locatorsNew.map { it.hashCode() }.toSet())
                                ?: emptySet()

                            dailyProgress.daily.targetedLocators.forEach { locator ->
                                val methodPart = if (locator.methodName.isNotEmpty()) ".${locator.methodName}" else ""
                                val locatorInfo = "${locator.locatorName ?: "N/A"} " +
                                        "[${locator.className}$methodPart, ${locator.line}]"
                                val locatorLabel = JLabel(locatorInfo).apply {
                                    this.font = smallFont
                                    foreground = JBColor.DARK_GRAY
                                }

                                //check if current locator is infeasible for initial state
                                val isInfeasible = validInfeasibleHashes.contains(locator.hashCode())
                                //define a checkbox to set locators to fix as 'infeasible' if they cannot be improved any further
                                val checkBox = JCheckBox("infeasible").apply {
                                    this.font = smallFont.deriveFont(Font.ITALIC)
                                    isSelected = isInfeasible //set by default remembering the locator state
                                    if(isSelected){
                                        locatorLabel.foreground = JBColor.RED
                                        locatorLabel.font = locatorLabel.font.deriveFont(mapOf(TextAttribute.STRIKETHROUGH to TextAttribute.STRIKETHROUGH_ON))

                                    }
                                }
                                //define the action over checkbox
                                checkBox.addActionListener{
                                    //update locator feasibility according to clicks
                                    val target = TestQuestAction.locatorsNew.find { it.hashCode() == locator.hashCode() }
                                    target?.feasible = !checkBox.isSelected
                                    if (checkBox.isSelected) {
                                        locatorLabel.foreground = JBColor.RED
                                        locatorLabel.font = locatorLabel.font.deriveFont(mapOf(TextAttribute.STRIKETHROUGH to TextAttribute.STRIKETHROUGH_ON))
                                    } else {
                                        locatorLabel.foreground = JBColor.DARK_GRAY
                                        locatorLabel.font = smallFont
                                    }
                                    //update infeasible locators
                                    ProgressFileHandler.saveInfeasibleLocators()
                                }
                                val locatorPanel = JPanel().apply {
                                    layout = BoxLayout(this, BoxLayout.X_AXIS)
                                    add(locatorLabel)
                                    add(Box.createRigidArea(Dimension(5, 0)))
                                    add(checkBox)
                                    alignmentX = Component.LEFT_ALIGNMENT
                                    maximumSize = Dimension(Int.MAX_VALUE, locatorLabel.preferredSize.height)
                                }
                                targetedIssuesPanel.add(locatorPanel)
                            }
                        }
                        else if(dailyProgress.daily.isAdvanced) {
                            dailyProgress.daily.issuesInPOs!!.forEach { issue ->
                                when (issue) {
                                    is String -> { //e.g., a PO issue about an assertion line
                                        val label = JLabel(issue).apply {
                                            this.font = smallFont
                                            foreground = JBColor.DARK_GRAY
                                        }
                                        targetedIssuesPanel.add(label)
                                    }
                                    is PageObject -> { //e.g., a PO issue about PO itself
                                        val label = JLabel(issue.name).apply {
                                            this.font = smallFont
                                            foreground = JBColor.DARK_GRAY
                                        }
                                        targetedIssuesPanel.add(label)
                                    }
                                    is Locator -> { //e.g., a PO issue about some of its locators
                                        val methodPart =
                                            if (issue.methodName.isNotEmpty()) ".${issue.methodName}" else ""
                                        val locatorInfo = "${issue.locatorName ?: "N/A"} " +
                                                "[${issue.className}$methodPart, ${issue.line}]"
                                        val label = JLabel(locatorInfo).apply {
                                            this.font = smallFont
                                            foreground = JBColor.DARK_GRAY
                                        }
                                        targetedIssuesPanel.add(label)
                                    }
                                    is MethodInfo -> { //e.g., a PO issue about some of its methods
                                        val label = JLabel(issue.name + " [" + issue.pageObject + "]").apply {
                                            this.font = smallFont
                                            foreground = JBColor.DARK_GRAY
                                        }
                                        targetedIssuesPanel.add(label)
                                    }
                                }
                            }
                        }

                        // separator between targeted issues and potentially fixed issues (to verify via test execution)
                        val separatorLabel = JLabel("---- Fixed and pending (test execution needed) ----").apply {
                            this.font = smallFont
                            foreground = JBColor.GRAY
                        }
                        if(dailyProgress.daily.name != "broken") //broken locs will never be listed as fixed so there's no need to show this label
                            targetedIssuesPanel.add(separatorLabel)

                        // show info of potentially fixed issues to verify, whether locators or POs based
                        if(!dailyProgress.daily.isAdvanced) {
                            val locatorsFromMap = locatorsToVerifyMap[dailyProgress.daily.name]
                            locatorsFromMap?.forEach { locator ->
                                val methodPart = if (locator.methodName.isNotEmpty()) ".${locator.methodName}" else ""
                                val locatorInfo = "${locator.locatorName ?: "N/A"} " +
                                        "[${locator.className}$methodPart, ${locator.line}]"
                                val locatorLabel = JLabel(locatorInfo).apply {
                                    this.font = smallFont
                                    foreground = JBColor.DARK_GRAY
                                }
                                val locatorPanel = JPanel().apply {
                                    layout = BoxLayout(this, BoxLayout.X_AXIS)
                                    add(locatorLabel)
                                    add(Box.createRigidArea(Dimension(5, 0)))
                                    alignmentX = Component.LEFT_ALIGNMENT
                                    maximumSize = Dimension(Int.MAX_VALUE, locatorLabel.preferredSize.height)
                                }
                                targetedIssuesPanel.add(locatorPanel)
                            }
                        }
                        else if(dailyProgress.daily.isAdvanced) {
                            val POsFromMap = posToVerifyMap[dailyProgress.daily.name]
                            POsFromMap?.forEach { issue ->
                                when (issue) {
                                    is String -> { //e.g., a PO issue about an assertion line
                                        val label = JLabel(issue).apply {
                                            this.font = smallFont
                                            foreground = JBColor.DARK_GRAY
                                        }
                                        targetedIssuesPanel.add(label)
                                    }
                                    is PageObject -> { //e.g., a PO issue about PO itself
                                        val label = JLabel(issue.name).apply {
                                            this.font = smallFont
                                            foreground = JBColor.DARK_GRAY
                                        }
                                        targetedIssuesPanel.add(label)
                                    }
                                    is Locator -> { //e.g., a PO issue about some of its locators
                                        val methodPart =
                                            if (issue.methodName.isNotEmpty()) ".${issue.methodName}" else ""
                                        val locatorInfo = "${issue.locatorName ?: "N/A"} " +
                                                "[${issue.className}$methodPart, ${issue.line}]"
                                        val label = JLabel(locatorInfo).apply {
                                            this.font = smallFont
                                            foreground = JBColor.DARK_GRAY
                                        }
                                        targetedIssuesPanel.add(label)
                                    }
                                    is MethodInfo -> { //e.g., a PO issue about some of its methods
                                        val label = JLabel(issue.name + " [" + issue.pageObject + "]").apply {
                                            this.font = smallFont
                                            foreground = JBColor.DARK_GRAY
                                        }
                                        targetedIssuesPanel.add(label)
                                    }
                                }
                            }
                        }

                        val scrollPane = JScrollPane(targetedIssuesPanel).apply {
                            preferredSize = Dimension(400, 150)
                            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                        }
                        add(scrollPane, BorderLayout.CENTER)
                        pack()
                        setLocationRelativeTo(dailyPanel)
                        isVisible = true

                        // Update button text if window is closed
                        addWindowListener(object : WindowAdapter() {
                            override fun windowClosing(e: WindowEvent) {
                                toggleButton.text = "Show"
                                DailyWindowManager.removeWindow(detailsFrame!!)
                                detailsFrame = null
                            }
                        })
                    }
                    DailyWindowManager.addWindow(detailsFrame!!, toggleButton)
                    toggleButton.text = "Hide"
                } else {
                    detailsFrame?.dispose()
                    detailsFrame = null
                    toggleButton.text = "Show"
                    DailyWindowManager.removeWindow(detailsFrame!!)
                }
            }
            dailyPanel.add(toggleButton)
        }
        dailyPanel.revalidate()
        dailyPanel.repaint()
    }







    /** SECTION MANAGING LOCATORS SCORE VISUALIZATION **/


    //to shows the global locators fragility score in a window
    fun showOverallLocsFragilityScore(score: Double) {

        // Set color of estimation from green to red
        val roundedEst = String.format("%.2f", (score * 100))
        val message = "Fragility Score = $roundedEst%"
        val red = (255 * score).toInt()
        val green = (255 * (1 - score)).toInt()
        val color = String.format("#%02x%02x00", red, green)

        // Main label
        val label = JLabel("<html><div style='padding: 8px; font-size:14px; color:$color;'>$message</div></html>")
        label.horizontalAlignment = SwingConstants.CENTER

        // Description label
        val note = JLabel("<html><div style='padding: 5px; font-size:11px; color:#FFFFFF;'>"
                + "The fragility score indicates the likelihood of locators breaking due to software changes. " +
                "Lower scores are preferable.</div></html>")
        note.horizontalAlignment = SwingConstants.CENTER

        // Border
        val window = JWindow()
        val panel = JPanel(BorderLayout())
        panel.border = BorderFactory.createLineBorder(JBColor(Gray._255, Color(255, 215, 0)), 3)
        panel.add(label, BorderLayout.CENTER)
        panel.add(note, BorderLayout.SOUTH)
        window.add(panel)

        // Panel size and position
        window.setSize(370, 120)
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val x = 10
        val y = (screenSize.height - window.height) / 2
        window.setLocation(x, y)

        // Panel not focused
        window.isAlwaysOnTop = true
        window.isVisible = true

        // Panel close in 10 seconds
        Timer(10000) {
            window.dispose()
        }.start()
    }


    // to show balloon on top of each locator once mouse is over
    fun showBalloon(event: MouseEvent, name: String?, score: Double): Balloon {
        var tooltipText = ""
        if (name == null)
            tooltipText = "Fragility Score for this locator: ${String.format("%.2f", score)}"
        else
            tooltipText = "Fragility Score for $name: ${String.format("%.2f", score)}"

        // create new balloon
        val balloon = JBPopupFactory.getInstance()
            .createBalloonBuilder(com.intellij.ui.components.JBLabel(tooltipText))
            .setFillColor(JBColor(Gray._255, Color(60, 63, 65)))
            .setHideOnClickOutside(true)
            .setHideOnKeyOutside(true)
            .setFadeoutTime(3000)
            .createBalloon()

        // place balloon close to mouse position
        balloon.show(RelativePoint(event.component, Point(event.x, event.y)), Balloon.Position.above)
        return balloon
    }




    fun showLocatorScores(project: Project, locatorScores: Map<Locator, Double>) {
        FragilityWindow?.dispose()//to close any already open window
        FragilityWindow = JFrame("Locator Details Panel").apply {
            defaultCloseOperation = JFrame.DO_NOTHING_ON_CLOSE //avoid to close this window
            setSize(800, 400)
            setLocationRelativeTo(null)
            isAlwaysOnTop = false
        }
        //create the table describing locators
        val sortedLocators = locatorScores.entries.sortedByDescending { it.value }
        val columnNames = arrayOf("Score", "Line", "Locator Name", "Locator Type", "Locator Value", "Method Name", "Class Name")
        val tableModel = object : DefaultTableModel(columnNames, 0) {
            override fun isCellEditable(row: Int, column: Int): Boolean { //make table cells non editable
                return false
            }
        }
        for ((locator, score) in sortedLocators) {
            val df = DecimalFormat("#.##", DecimalFormatSymbols(Locale.US))
            df.roundingMode = RoundingMode.CEILING
            val rowData = arrayOf(
                df.format(score),
                "<html><a href=''>${locator.line}</a></html>",
                locator.locatorName,
                locator.locatorType,
                locator.locatorValue,
                locator.methodName,
                locator.className
            )
            tableModel.addRow(rowData)
        }
        //define score colors
        val table = JBTable(tableModel)
        table.columnModel.getColumn(0).cellRenderer =
            TableCellRenderer { tab, value, isSelected, hasFocus, row, column ->
                val component = DefaultTableCellRenderer().getTableCellRendererComponent(
                    tab, value, isSelected, hasFocus, row, column
                )
                val score = (value?.toString()?.toDoubleOrNull()) ?: 0.0
                val color = getScoreColor(score)
                (component as JLabel).foreground = color
                component
            }
        //setup click event on locator line
        table.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                val row = table.rowAtPoint(e.point)
                val col = table.columnAtPoint(e.point)
                if (col == 1) {
                    val locator = sortedLocators[row].key
                    openFileAtLine(project, locator.filePath, locator.line)
                }
            }
        })
        FragilityWindow!!.add(JScrollPane(table), BorderLayout.CENTER)
        FragilityWindow!!.isVisible = true
    }

    private fun getScoreColor(score: Double): Color {
        val red = (score * 255).toInt()
        val green = ((1 - score) * 255).toInt()
        return JBColor(Color(red.coerceIn(0, 255), green.coerceIn(0, 255), 0),
            Color(red.coerceIn(0, 255), green.coerceIn(0, 255), 0))
    }






    //TODO: refactor to move it away
    fun openFileAtLine(project: Project, filePath: String, line: Int) {
        val virtualFile: VirtualFile? = LocalFileSystem.getInstance().findFileByPath(filePath)
        if (virtualFile != null) {
            ApplicationManager.getApplication().invokeLater {
                val editor = FileEditorManager.getInstance(project).openTextEditor(
                    com.intellij.openapi.fileEditor.OpenFileDescriptor(project, virtualFile, line - 1, 0), true
                )
                editor?.caretModel?.moveToOffset(editor.document.getLineStartOffset(line - 1))
            }
        }
    }













    /********** FUNCTIONS TO MANAGE INITIAL USER CHOICES (IF USE OLD DATA, WHICH GAMIFICATION MODE) *****************/

    fun showWindowStoredDataChoice(oldDataTime: String): Boolean {
        if(oldDataTime != "") {
            val panel = JPanel()
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
            panel.add(JLabel("<html>Data previously saved about original test suite found [$oldDataTime]" +
                    "<br>Would you like to set it as original test suite state?</html>"))
            val result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Saved Data Found",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            )
            return result == JOptionPane.YES_OPTION
        }
        return false
    }




    fun showGamificationModeChoice(): Boolean {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.add(JLabel("<html>Please choose a gamification mode for this session:<br></html>"))
        val options = arrayOf("Locator-based", "Advanced")
        val result = JOptionPane.showOptionDialog(
            null,
            panel,
            "Gamification Mode",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        )
        return result == 0 //returns true if the user chose Locator-based, false otherwise
    }







}
