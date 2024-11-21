package gamification

import listener.test.TestOutcome
import locator.Locator
import locator.LocatorsAnalyzer
import locator.LocatorsFragilityCalculator
import ui.GUIManager

class DailyManager {

    companion object {

        const val TARGET_DAILY: Int = 3 //TODO: to convert into a map (each daily may have specific requests)
        const val XP_DAILY: Int = 100 //TODO: to convert into a map (each daily may provide specific XP)
        private const val DAILIES_PER_USER: Int = 5
        private const val ROBUST_THRESHOLD = 0.5 //threshold used to determine whether a locator is robust

        private val RANDOM_DAILY_NAMES = listOf(
            "xpathAbs",
            "xpathLength",
            "xpathLevel",
            "loc2xpath",
            "loc2id",
            "runLocs20",
            "robustness",
            "lengthShorten10",
            "addAttrToXPath",
            "remAttrFromXPath",
            "remJSFromXPath",
            "edit5",
            "levelLowered5",
            "repair",
            "reducePredicates",
            "newXPath",
            "newID",
            "newSameLoc",
            "newLengthShorter10",
            "newLevelLower5",
            "newRobust",
            "newWantedAttr",
            "newUnwantedAttr",
            "newJS",
            "newLowPredicates"
        )

        //TODO: change icons, xp, target accordingly
        private val ALL_RANDOM_DAILIES = mutableListOf(
            Daily(
                RANDOM_DAILY_NAMES[0],
                "Replace $TARGET_DAILY existing absolute XPath locators with $TARGET_DAILY relative ones",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[1],
                "Reduce the length of $TARGET_DAILY existing XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[2],
                "Reduce the level of $TARGET_DAILY existing XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[3],
                "Convert $TARGET_DAILY existing non-XPath locators to $TARGET_DAILY XPath ones",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[4],
                "Convert $TARGET_DAILY existing non-ID locators to $TARGET_DAILY ID ones",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[5],
                "Run 20 locators successfully",
                XP_DAILY,
                20,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[6],
                "Improve the robustness of $TARGET_DAILY existing locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[7],
                "Shorten the length of $TARGET_DAILY existing locators below 10 characters",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[8],
                "Add $TARGET_DAILY references to @id, @name, @class, @title, @alt " +
                        "or @value attributes to existing XPaths locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[9],
                "Remove $TARGET_DAILY references to @src, @href, @height, or @width " +
                        "attributes from existing XPaths locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[10],
                "Remove $TARGET_DAILY references to Javascript code from existing XPaths locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[11],
                "Modify 5 different existing locators",
                XP_DAILY,
                5,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[12],
                "Lower the level of $TARGET_DAILY existing locators below 5 tags",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[13],
                "Repair $TARGET_DAILY existing broken locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[14],
                "Reduce the number of predicates from $TARGET_DAILY existing locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[15],
                "Implement $TARGET_DAILY new XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[16],
                "Implement $TARGET_DAILY new ID locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[17],
                "Implement a newly locator value and use it more than once in a test suite",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[18],
                "Implement $TARGET_DAILY new XPath locators with length below 10 characters",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[19],
                "Implement $TARGET_DAILY new XPath locators with level below 5 tags",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[20],
                "Implement $TARGET_DAILY new robust locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[21],
                "Implement $TARGET_DAILY new XPath locators with references to @id, @name, @class, @title, @alt" +
                        "or @value attributes",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[22],
                "Implement $TARGET_DAILY new XPath locators with no references to @src, @href, @height, or @width" +
                        "attributes",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[23],
                "Implement $TARGET_DAILY new XPath locators with no references to Javascript code",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[24],
                "Implement $TARGET_DAILY new XPath locators with 3 or less predicates",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
        )

        private val DAILY_NAME_TO_DESCRIPTION = ALL_RANDOM_DAILIES.associate { it.name to it.description }
        private val DAILY_NAME_TO_TARGET = ALL_RANDOM_DAILIES.associate { it.name to it.target }
        private val DAILY_NAME_TO_ICON = ALL_RANDOM_DAILIES.associate { it.name to it.icon }
        private val DAILY_NAME_TO_XP = ALL_RANDOM_DAILIES.associate { it.name to it.xp }

        private val RANDOM_DAILY_CHECKS: Map<String, (List<TestOutcome>) -> Int> = mapOf(
            RANDOM_DAILY_NAMES[0] to { testOutcomes -> checkAbsXPathRemoved(testOutcomes) },
            RANDOM_DAILY_NAMES[1] to { testOutcomes -> checkXPathLengthReduced(testOutcomes) },
            RANDOM_DAILY_NAMES[2] to { testOutcomes -> checkXPathLevelReduced(testOutcomes) },
            RANDOM_DAILY_NAMES[3] to { testOutcomes -> checkLocs2XPathConverted(testOutcomes) },
            RANDOM_DAILY_NAMES[4] to { testOutcomes -> checkLocs2IDConverted(testOutcomes) },
            RANDOM_DAILY_NAMES[5] to { testOutcomes -> checkRunLoc20(testOutcomes) },
            RANDOM_DAILY_NAMES[6] to { testOutcomes -> checkRobustnessImprovement(testOutcomes) },
            RANDOM_DAILY_NAMES[7] to { testOutcomes -> checkShortenedLength10(testOutcomes) },
            RANDOM_DAILY_NAMES[8] to { testOutcomes -> checkWantedAttrsInXPaths(testOutcomes) },
            RANDOM_DAILY_NAMES[9] to { testOutcomes -> checkUnwantedAttrsInXPaths(testOutcomes) },
            RANDOM_DAILY_NAMES[10] to { testOutcomes -> checkJSInXPaths(testOutcomes) },
            RANDOM_DAILY_NAMES[11] to { testOutcomes -> checkChangedLocs5(testOutcomes) },
            RANDOM_DAILY_NAMES[12] to { testOutcomes -> checkLoweredLevel5(testOutcomes) },
            RANDOM_DAILY_NAMES[13] to { testOutcomes -> checkRepair(testOutcomes) },
            RANDOM_DAILY_NAMES[14] to { testOutcomes -> reducePredicates(testOutcomes) },
            RANDOM_DAILY_NAMES[15] to { testOutcomes -> checkNewXPath(testOutcomes) },
            RANDOM_DAILY_NAMES[16] to { testOutcomes -> checkNewID(testOutcomes) },
            RANDOM_DAILY_NAMES[17] to { testOutcomes -> checkNewMultipleUseLoc(testOutcomes) },
            RANDOM_DAILY_NAMES[18] to { testOutcomes -> checkNewXPathLength10(testOutcomes) },
            RANDOM_DAILY_NAMES[19] to { testOutcomes -> checkNewXPathLevel5(testOutcomes) },
            RANDOM_DAILY_NAMES[20] to { testOutcomes -> checkNewRobust(testOutcomes) },
            RANDOM_DAILY_NAMES[21] to { testOutcomes -> checkNewXPathWithWantedAttrs(testOutcomes) },
            RANDOM_DAILY_NAMES[22] to { testOutcomes -> checkNewXPathWithoutUnwantedAttrs(testOutcomes) },
            RANDOM_DAILY_NAMES[23] to { testOutcomes -> checkNewXPathWithoutJS(testOutcomes) },
            RANDOM_DAILY_NAMES[24] to { testOutcomes -> checkNewXPathWithLessThan3Predicates(testOutcomes) },
            )




        /***********************************************************************************/

        private val TARGETED_DAILY_NAMES = listOf(
            "absolute", "length", "level", "posPredicate", "badPredicate", "noIDOrXPath", "broken"
        )

        private val ALL_TARGETED_DAILIES = mutableListOf(
            Daily(
                TARGETED_DAILY_NAMES[0],
                "Turns the following absolute XPath locators into relative ones",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
            Daily(
                TARGETED_DAILY_NAMES[1],
                "Reduce the length of the following XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
            Daily(
                TARGETED_DAILY_NAMES[2],
                "Reduce the levels of the following XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
            Daily(
                TARGETED_DAILY_NAMES[3],
                "Remove/Replace the positional predicates from the following XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
            Daily(
                TARGETED_DAILY_NAMES[4],
                "Remove/Replace the weak predicates from the following XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
            Daily(
                TARGETED_DAILY_NAMES[5],
                "Replace the following non-ID/XPath locators into ID/XPath ones",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
            Daily(
                TARGETED_DAILY_NAMES[6],
                "Repair the following broken locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
        )



        private val TARGETED_DAILY_CHECKS: Map<String, (List<TestOutcome>) -> Int> = mapOf(
            TARGETED_DAILY_NAMES[0] to { testOutcomes -> checkTargetedAbsolute(testOutcomes) },
            TARGETED_DAILY_NAMES[1] to { testOutcomes -> checkTargetedLength(testOutcomes) },
            TARGETED_DAILY_NAMES[2] to { testOutcomes -> checkTargetedLevel(testOutcomes) },
            TARGETED_DAILY_NAMES[3] to { testOutcomes -> checkTargetedPosPredicate(testOutcomes) },
            TARGETED_DAILY_NAMES[4] to { testOutcomes -> checkTargetedBadPredicate(testOutcomes) },
            TARGETED_DAILY_NAMES[5] to { testOutcomes -> checkTargetedNoIDNoXpath(testOutcomes) },
            TARGETED_DAILY_NAMES[6] to { testOutcomes -> checkTargetedBroken(testOutcomes) }
        )

        private fun checkTargetedAbsolute(testOutcomes: List<TestOutcome>): Int {
            return 0
        }
        private fun checkTargetedLength(testOutcomes: List<TestOutcome>): Int {
            return 0
        }
        private fun checkTargetedLevel(testOutcomes: List<TestOutcome>): Int {
            return 0
        }
        private fun checkTargetedPosPredicate(testOutcomes: List<TestOutcome>): Int {
            return 0
        }
        private fun checkTargetedBadPredicate(testOutcomes: List<TestOutcome>): Int {
            return 0
        }
        private fun checkTargetedNoIDNoXpath(testOutcomes: List<TestOutcome>): Int {
            return 0
        }
        private fun checkTargetedBroken(testOutcomes: List<TestOutcome>): Int {
            return 0
        }







        //dailies are assigned to user. initially, the usage mode is RANDOM so random dailies are assigned for setup
        fun setupDailies(userProfile: UserProfile) {
            val dailies: List<Daily>
            dailies = ALL_RANDOM_DAILIES.shuffled().take(DAILIES_PER_USER)
            userProfile.assignDailies(dailies)
        }



        fun assignTargetDailies(userProfile: UserProfile) {
            val locAnalyzer = LocatorsAnalyzer()
            val analysisMap = locAnalyzer.findTargetedProblems()
            val currentTargetDailies = ALL_TARGETED_DAILIES.mapNotNull { daily ->
                val locatorsForDaily = analysisMap[daily.name] ?: emptyList()
                if (locatorsForDaily.isNotEmpty()) {
                    Daily(
                        name = daily.name,
                        description = daily.description,
                        xp = daily.xp,
                        target = daily.target,
                        icon = daily.icon,
                        type = daily.type,
                        targetedLocators = locatorsForDaily
                    )
                } else {
                    null
                }
            }
            //remove all old targeted dailies as they will be updated by new check
            userProfile.dailyProgresses.removeAll { dailyProgress ->
                dailyProgress.daily.type.equals("targeted", ignoreCase = true)
            }
            userProfile.assignDailies(currentTargetDailies)
            GUIManager.updateGUI(userProfile, false)
        }






        fun reassignDailiesFromExpire(userProfile: UserProfile){
            userProfile.dailyProgresses.clear()
            userProfile.timestamp = System.currentTimeMillis()//this to assign a new expiration time for new dailies
            setupDailies(userProfile)//note that even the same expired dailies could be reassigned
            GUIManager.updateGUI(userProfile, notifyChange = false)
            GamificationManager.updateUserProfile(userProfile)
        }

        fun reassignDailyFromDiscard(userProfile: UserProfile, daily: Daily): DailyProgress {
            //find all dailies but the one that is going to be discarded
            val availableDailies = ALL_RANDOM_DAILIES.filter { d ->
                userProfile.dailyProgresses.none { dailyProgress -> dailyProgress.daily.name == d.name }
            }
            //new daily is selected with discarded set to true (only 1 discard within 24h is possible)
            val newDaily: Daily
            if(GamificationManager.mode == GamificationManager.DailyAssignmentMode.RANDOM)
                newDaily = availableDailies.shuffled().first()
            else if(GamificationManager.mode == GamificationManager.DailyAssignmentMode.TARGETED)
                newDaily = availableDailies.last() //TODO: implement targeted assignment (i.e., assign daily based on observed issues)
            else
                newDaily = availableDailies.last()//TODO: implement inclusive assignment (i.e., assign daily based on uncovered functionalities)
            val newDailyProgress = DailyProgress(
                newDaily,
                progress = 0,
                discarded = true
            )
            userProfile.dailyProgresses.add(newDailyProgress) //add new daily
            userProfile.dailyProgresses.removeIf { it.daily == daily } //remove discarded daily
            GamificationManager.updateUserProfile(userProfile) //update user profile
            return newDailyProgress
        }

        fun getIconFromName(name: String): String {
            return DAILY_NAME_TO_ICON[name] ?: ""
        }

        fun getDescriptionFromName(name: String): String {
            return DAILY_NAME_TO_DESCRIPTION[name] ?: ""
        }

        fun getTargetFromName(name: String): Int {
            return DAILY_NAME_TO_TARGET[name] ?: 0
        }

        fun getXPFromName(name: String): Int {
            return DAILY_NAME_TO_XP[name] ?: 0
        }

        fun updateDailies(userProfile: UserProfile, testOutcomes: List<TestOutcome>): Boolean {
            var anyUpdate = false
            val copyOfDailyProgresses = ArrayList(userProfile.dailyProgresses) //needed since the list is updated during loop
            copyOfDailyProgresses.forEach { dp ->
                ALL_RANDOM_DAILIES.find { it.name == dp.daily.name }?.let {
                    val progress = RANDOM_DAILY_CHECKS[it.name]?.invoke(testOutcomes)
                    if (progress!! > 0) {
                        update(userProfile, it, progress)
                        anyUpdate = true
                    }
                }
            }
            return anyUpdate //this to keep track of any changes and update the GUI
        }

        private fun update(userProfile: UserProfile, daily: Daily, progress: Int) {
            //TODO: check user profile wrt xml
            val dailyProgress = userProfile.dailyProgresses.find { it.daily.name == daily.name }
            dailyProgress?.let { dp ->
                dp.progress += progress
                if (dp.progress >= dp.daily.target) { // if the daily has been completed, assign xp and remove it from list
                    userProfile.currentXP += dp.daily.xp
                    userProfile.dailyProgresses.removeIf { it.daily.name == dp.daily.name }
                }
            }
        }
























        /******* CHECKS ON EDITED LOCATORS (OLDLOCS SIZE MUST BE LIKE NEWLOCS SIZE) *******/

        private fun checkAbsXPathRemoved(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                if (!testOutcome.isPassed)
                    continue
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        oldLocator.locatorValue.startsWith("/html")) {
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                            !newLocator.locatorValue.startsWith("/html"))
                            count++
                    }
                }
            }
            return count
        }


        private fun checkXPathLengthReduced(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                if(!testOutcome.isPassed)
                    continue
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true)){
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorType.equals("xpath", ignoreCase = true)
                            && newLocator.locatorValue.length < oldLocator.locatorValue.length)
                            count++
                    }
                }
            }
            return count
        }

        private fun checkXPathLevelReduced(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                if(!testOutcome.isPassed)
                    continue
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true)){
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorType.equals("xpath", ignoreCase = true)
                            && (newLocator.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size <
                            oldLocator.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size))
                            count++
                    }
                }
            }
            return count
        }

        private fun checkLocs2XPathConverted(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                if (!testOutcome.isPassed)
                    continue
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (!oldLocator.locatorType.equals("xpath", ignoreCase = true)) {
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorType.equals("xpath", ignoreCase = true))
                            count++
                    }
                }
            }
            return count
        }

        private fun checkLocs2IDConverted(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                if (!testOutcome.isPassed)
                    continue
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (!oldLocator.locatorType.equals("id", ignoreCase = true)) {
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorType.equals("id", ignoreCase = true))
                            count++
                    }
                }
            }
            return count
        }

        private fun checkRobustnessImprovement(testOutcomes: List<TestOutcome>): Int{
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                if (!testOutcome.isPassed)
                    continue
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    val newLocator = newLocatorsMap[oldLocator.hashCode()]
                    if (newLocator != null && computeFragilityCoefficient(newLocator) <
                        computeFragilityCoefficient(oldLocator))
                        count++
                }
            }
            return count
        }

        private fun checkShortenedLength10(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                if (!testOutcome.isPassed)
                    continue
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (oldLocator.locatorValue.length >= 10){
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorValue.length < 10)
                            count++
                    }
                }
            }
            return count
        }

        private fun checkWantedAttrsInXPaths(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                if (!testOutcome.isPassed)
                    continue
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true)){
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorType.equals("xpath", ignoreCase = true))
                            count += countWantedAttributes(newLocator.locatorValue) -
                                    countWantedAttributes(oldLocator.locatorValue)
                    }
                }
            }
            return count
        }

        private fun checkUnwantedAttrsInXPaths(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                if (!testOutcome.isPassed)
                    continue
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true)){
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorType.equals("xpath", ignoreCase = true))
                            count += countUnwantedAttributes(oldLocator.locatorValue) -
                                    countUnwantedAttributes(newLocator.locatorValue)
                    }
                }
            }
            return count
        }

        private fun checkJSInXPaths(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                if (!testOutcome.isPassed)
                    continue
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true)){
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorType.equals("xpath", ignoreCase = true))
                            count += countJavaScriptReferences(oldLocator.locatorValue) -
                                    countJavaScriptReferences(newLocator.locatorValue)
                    }
                }
            }
            return count
        }

        private fun checkChangedLocs5(testOutcomes: List<TestOutcome>): Int {
            val dailyProgress = GamificationManager.userProfile.dailyProgresses
                .find { it.daily.name == "edit5" }
            val existingModifiedLocs = dailyProgress!!.modifiedLocs.toMutableSet()
            val oldSize = existingModifiedLocs.size
            for (testOutcome in testOutcomes)
                if (testOutcome.isPassed) {
                    //it checks if locators have changed values and are different from those that were already changed
                    val locatorsOldMap = testOutcome.locatorsOld.associateBy { it.locatorName }
                    testOutcome.locatorsNew.forEach { locatorNew ->
                        val locatorOld = locatorsOldMap[locatorNew.locatorName]
                        if (locatorOld != null && locatorNew.locatorValue != locatorOld.locatorValue)
                            existingModifiedLocs.add(locatorNew.locatorName.toString())
                    }
                }
            dailyProgress.modifiedLocs = existingModifiedLocs.toList()
            return existingModifiedLocs.size - oldSize //it returns the number of newly changed locs
        }

        private fun checkLoweredLevel5(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                if (!testOutcome.isPassed)
                    continue
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        oldLocator.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size >= 5){
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorType.equals("xpath", ignoreCase = true)
                            && newLocator.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size < 5)
                            count++
                    }
                }
            }
            return count
        }

        private val stackTraceMap = mutableMapOf<String, String>()
        private fun checkRepair(testOutcomes: List<TestOutcome>): Int {
            var repairs = 0
            for (testOutcome in testOutcomes) {
                val testName = testOutcome.testName
                //if no stacktrace exists, it is the first run of this test
                if (!stackTraceMap.containsKey(testName)) {
                    //if test failed, save stacktrace
                    if (!testOutcome.isPassed)
                        stackTraceMap[testName] = testOutcome.stacktrace.orEmpty()
                    //else, skip as test never failed
                    else
                        continue
                }
                //if stacktrace exists, so the test failed in the past
                else {
                    //if stacktrace is the same as old, skip as nothing has changed
                    if (testOutcome.hasSameStacktrace(stackTraceMap[testName]!!))
                        continue
                    //else, retrieve locators that were broken in previous stacktrace
                    else {
                        //find old locators that were broken in previous stacktrace
                        val brokenLocators = testOutcome.locatorsOld.filter { loc ->
                            stackTraceMap[testName]!!.trim().contains(loc.locatorValue.trim())
                        }
                        //if now test has passed, all broken locators count as repaired
                        if(testOutcome.isPassed) {
                            repairs += brokenLocators.size
                            //remove old stacktrace
                            stackTraceMap.remove(testName)
                        }
                        //else, count only repaired ones
                        else {
                            //find new locators same as old but whose values are no more present in current stacktrace
                            val repairedLocators = testOutcome.locatorsNew.filter { loc ->
                                loc.locatorName in brokenLocators.map { it.locatorName } &&
                                        !testOutcome.stacktrace?.trim()?.contains(loc.locatorValue.trim())!!
                            }
                            repairs += repairedLocators.size
                            //update stacktrace
                            stackTraceMap[testName] = testOutcome.stacktrace.toString()
                        }
                    }
                }
            }
            return repairs
        }

        private fun reducePredicates(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                if (!testOutcome.isPassed)
                    continue
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true)) {
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorType.equals("xpath", ignoreCase = true)) {
                            val oldPredicatesCount = oldLocator.locatorValue.split("[", "]").size / 2 - 1
                            val newPredicatesCount = newLocator.locatorValue.split("[", "]").size / 2 - 1
                            if (newPredicatesCount < oldPredicatesCount)
                                count++
                        }
                    }
                }
            }
            return count
        }







        /******* CHECKS ON NEW LOCATORS *******/

        private fun checkNewXPath(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                if (!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                val oldLocatorsHashes = locatorsOld.map { it.hashCode() }.toSet()
                //new locator hashes only
                val newLocators = locatorsNew.filter { newLocator ->
                    !oldLocatorsHashes.contains(newLocator.hashCode()) &&
                            newLocator.locatorType.equals("xpath", ignoreCase = true)
                }
                count += newLocators.size
            }
            return count
        }


        private fun checkNewID(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                if (!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                val oldLocatorsHashes = locatorsOld.map { it.hashCode() }.toSet()
                //new locator hashes only
                val newLocators = locatorsNew.filter { newLocator ->
                    !oldLocatorsHashes.contains(newLocator.hashCode()) &&
                            newLocator.locatorType.equals("id", ignoreCase = true)
                }
                count += newLocators.size
            }
            return count
        }

        //two cases: new locators using multiple old values or new locators using multiple new values
        private fun checkNewMultipleUseLoc(testOutcomes: List<TestOutcome>): Int {
            val oldLocatorsHashes = mutableSetOf<Int>()
            val newLocators = mutableListOf<Locator>()
            //locators are considered from the whole test suite and not separated by method
            for (testOutcome in testOutcomes) {
                if (!testOutcome.isPassed)
                    continue
                oldLocatorsHashes.addAll(testOutcome.locatorsOld.map { it.hashCode() })
                newLocators.addAll(testOutcome.locatorsNew)
            }
            //new locator hashes only
            val filteredNewLocators = newLocators.filter { newLocator ->
                !oldLocatorsHashes.contains(newLocator.hashCode())
            }
            //count locator value usages
            val locatorCountMap = mutableMapOf<String, Int>()
            for (locator in filteredNewLocators)
                locatorCountMap[locator.locatorValue] = locatorCountMap.getOrDefault(locator.locatorValue, 0) + 1
            return locatorCountMap.values.count { it > 1 }
        }

        private fun checkNewXPathLength10(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                if (!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                val oldLocatorsHashes = locatorsOld.map { it.hashCode() }.toSet()
                //new locator hashes only
                val newLocators = locatorsNew.filter { newLocator ->
                    !oldLocatorsHashes.contains(newLocator.hashCode()) &&
                            (newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                                    newLocator.locatorValue.length < 10)
                }
                count += newLocators.size
            }
            return count
        }

        private fun checkNewXPathLevel5(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                if (!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                val oldLocatorsHashes = locatorsOld.map { it.hashCode() }.toSet()
                val newLocators = locatorsNew.filter { newLocator ->
                    !oldLocatorsHashes.contains(newLocator.hashCode()) &&
                            newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                            newLocator.locatorValue.split("/")
                                .filter { node -> node.isNotEmpty() }
                                .size < 5
                }
                count += newLocators.size
            }
            return count
        }

        private fun checkNewRobust(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                if (!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                val oldLocatorsHashes = locatorsOld.map { it.hashCode() }.toSet()
                val newLocators = locatorsNew.filter { newLocator ->
                    !oldLocatorsHashes.contains(newLocator.hashCode()) &&
                            newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                            computeFragilityCoefficient(newLocator) < ROBUST_THRESHOLD
                }
                count += newLocators.size
            }
            return count
        }

        private fun checkNewXPathWithWantedAttrs(testOutcomes: List<TestOutcome>): Int {
            val wantedAttrs = listOf("id", "name", "class", "title", "alt", "value")
            var count = 0
            for (testOutcome in testOutcomes) {
                if (!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                val oldLocatorsHashes = locatorsOld.map { it.hashCode() }.toSet()
                val newLocators = locatorsNew.filter { newLocator ->
                    !oldLocatorsHashes.contains(newLocator.hashCode()) &&
                            newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                            wantedAttrs.any { attribute ->
                                newLocator.locatorValue.contains("@$attribute", ignoreCase = true)
                            }
                }
                count += newLocators.size
            }
            return count
        }

        private fun checkNewXPathWithoutUnwantedAttrs(testOutcomes: List<TestOutcome>): Int {
            val unwantedAttrs = listOf("src", "href", "height", "width")
            var count = 0
            for (testOutcome in testOutcomes) {
                if (!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                val oldLocatorsHashes = locatorsOld.map { it.hashCode() }.toSet()
                val newLocators = locatorsNew.filter { newLocator ->
                    !oldLocatorsHashes.contains(newLocator.hashCode()) &&
                            newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                            unwantedAttrs.none { attribute ->
                                newLocator.locatorValue.contains("@$attribute", ignoreCase = true)
                            }
                }
                count += newLocators.size
            }
            return count
        }

        private fun checkNewXPathWithoutJS(testOutcomes: List<TestOutcome>): Int {
            val jsAttrs = listOf("onclick", "onload", "onmouseover", "onmouseout", "onchange", "onsubmit",
                "onfocus", "onblur", "onkeydown", "onkeyup", "onkeypress")
            var count = 0
            for (testOutcome in testOutcomes) {
                if (!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                val oldLocatorsHashes = locatorsOld.map { it.hashCode() }.toSet()
                val newLocators = locatorsNew.filter { newLocator ->
                    !oldLocatorsHashes.contains(newLocator.hashCode()) &&
                            newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                            jsAttrs.none { attribute ->
                                newLocator.locatorValue.contains("@$attribute", ignoreCase = true)
                            }
                }
                count += newLocators.size
            }
            return count
        }

        private fun checkRunLoc20(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                count += testOutcome.locatorsNew.size
            }
            return count
        }

        private fun checkNewXPathWithLessThan3Predicates(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                if (!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                val oldLocatorsHashes = locatorsOld.map { it.hashCode() }.toSet()
                val regex = "\\[.*?\\]".toRegex() //to count predicates
                //find locators that are actually new
                val newLocators = locatorsNew.filter { newLocator ->
                    !oldLocatorsHashes.contains(newLocator.hashCode()) &&
                            newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                            regex.findAll(newLocator.locatorValue).count() <= 3
                }
                count += newLocators.size
            }
            return count
        }










        /**AUXILIARY FUNCTIONS**/
        private fun computeFragilityCoefficient(loc: Locator): Double{
            val calc = LocatorsFragilityCalculator()
            return calc.calculateFragility(loc)
        }

        private fun countWantedAttributes(locatorValue: String): Int {
            val wantedAttributes = listOf("@id", "@name", "@class", "@title", "@alt", "@value")
            return wantedAttributes.sumOf { attribute -> Regex(attribute).findAll(locatorValue).count() }
        }

        private fun countUnwantedAttributes(locatorValue: String): Int {
            val unwantedAttributes = listOf("@src", "@href", "@width", "@height")
            return unwantedAttributes.sumOf { attribute -> Regex(attribute).findAll(locatorValue).count() }
        }

        private fun countJavaScriptReferences(locatorValue: String): Int {
            val jsAttributes = listOf("onclick", "onload", "onmouseover", "onmouseout", "onchange",
                "onsubmit", "onfocus", "onblur", "onkeydown", "onkeyup", "onkeypress")
            return jsAttributes.sumOf { attribute -> Regex(attribute).findAll(locatorValue).count() }
        }




















    }
}
