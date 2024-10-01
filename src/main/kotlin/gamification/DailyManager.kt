package gamification

import com.example.demo.PluginData
import listener.TestOutcome
import locator.Locator
import ui.GUIManager

class DailyManager {


    companion object {

        private val TARGET_DAILY: Int = 3 //TODO: to convert into a map (each daily may have specific requests)

        private val XP_DAILY: Int = 100 //TODO: to convert into a map (each daily may provide specific XP)

        private val DAILIES_PER_USER: Int = 5







        private val DAILY_NAMES = listOf(
            "xpathAbs",
            "xpathLength",
            "xpathLevel",
            "loc2xpath",
            "loc2id",
            "loc2linkText",
            "robustness",
            "lengthShorten10",
            "addAttrToXPath",
            "remAttrFromXPath",
            "remJSFromXPath",
            "edit5",
            "levelLowered5",
            "newXPath",
            "newID",
            "newLinkText",
            "repair",
            "runtc",
            "runts",
            "useLoc",
            "newLengthShorter10",
            "newLevelLower5",
            "newRobust",
            "newWantedAttr",
            "newUnwantedAttr",
            "newJS",
            "runLocs20"
            )

        //TODO: change icons, xp, target accordingly
        private val ALL_DAILIES = mutableListOf(
            Daily(
                DAILY_NAMES[0],
                "Replace $TARGET_DAILY existing absolute XPath locators with $TARGET_DAILY relative ones",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[1],
                "Reduce the length of $TARGET_DAILY existing XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[2],
                "Reduce the level of $TARGET_DAILY existing XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[3],
                "Convert $TARGET_DAILY existing non-XPath locators to $TARGET_DAILY XPath ones",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[4],
                "Convert $TARGET_DAILY existing non-ID locators to $TARGET_DAILY ID ones",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[5],
                "Convert $TARGET_DAILY existing non-linkText locators to $TARGET_DAILY linkText ones",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[6],
                "Improve the robustness of $TARGET_DAILY existing locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[7],
                "Shorten the length of $TARGET_DAILY existing locators below 10 characters",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[8],
                "Add $TARGET_DAILY references to @id, @name, @class, @title, @alt " +
                        "or @value attributes to existing XPaths locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[9],
                "Remove $TARGET_DAILY references to @src, @href, @height, or @width " +
                        "attributes from existing XPaths locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[10],
                "Remove $TARGET_DAILY references to Javascript code from existing XPaths locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[11],
                "Modify 5 different existing locators",
                XP_DAILY,
                5,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[12],
                "Lower the level of $TARGET_DAILY existing locators below 5 tags",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[13],
                "Implement $TARGET_DAILY new XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[14],
                "Implement $TARGET_DAILY new ID locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[15],
                "Implement $TARGET_DAILY new linkText locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Repair $TARGET_DAILY existing broken locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[17],
                "Run $TARGET_DAILY test cases successfully",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[18],
                "Run a test suite successfully",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[19],
                "Implement the same locator value more than once in a test suite",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[20],
                "Implement $TARGET_DAILY new XPath locators with length below 10 characters",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[21],
                "Implement $TARGET_DAILY new XPath locators with level below 5 tags",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[22],
                "Implement $TARGET_DAILY new robust locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[23],
                "Implement $TARGET_DAILY new XPath locators with references to @id, @name, @class, @title, @alt" +
                        "or @value attributes",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[24],
                "Implement $TARGET_DAILY new XPath locators with no references to @src, @href, @height, or @width" +
                        "attributes",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[25],
                "Implement $TARGET_DAILY new XPath locators with no references to Javascript code",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[26],
                "Run 20 locators successfully",
                XP_DAILY,
                20,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),






            /*Daily(
                DAILY_NAMES[27],
                "Edit the same locator value more than once in a test suite",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[28],
                "Remove $TARGET_DAILY broken locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),*/




        )

        private val DAILY_NAME_TO_DESCRIPTION = ALL_DAILIES.associate { it.name to it.description }
        private val DAILY_NAME_TO_TARGET = ALL_DAILIES.associate { it.name to it.target }
        private val DAILY_NAME_TO_ICON = ALL_DAILIES.associate { it.name to it.icon }
        private val DAILY_NAME_TO_XP = ALL_DAILIES.associate { it.name to it.xp }

        private val DAILY_CHECKS: Map<String, (List<TestOutcome>) -> Int> = mapOf(
            DAILY_NAMES[0] to { testOutcomes -> checkAbsXPathRemoved(testOutcomes) },
            DAILY_NAMES[1] to { testOutcomes -> checkXPathLengthReduced(testOutcomes) },
            DAILY_NAMES[2] to { testOutcomes -> checkXPathLevelReduced(testOutcomes) },
            DAILY_NAMES[3] to { testOutcomes -> checkLocs2XPathConverted(testOutcomes) },
            DAILY_NAMES[4] to { testOutcomes -> checkLocs2IDConverted(testOutcomes) },
            DAILY_NAMES[5] to { testOutcomes -> checkLocs2LinkTextConverted(testOutcomes) },
            DAILY_NAMES[6] to { testOutcomes -> checkRobustnessImprovement(testOutcomes) },
            DAILY_NAMES[7] to { testOutcomes -> checkShortenedLength10(testOutcomes) },
            DAILY_NAMES[8] to { testOutcomes -> checkWantedAttrsInXPaths(testOutcomes) },
            DAILY_NAMES[9] to { testOutcomes -> checkUnwantedAttrsInXPaths(testOutcomes) },
            DAILY_NAMES[10] to { testOutcomes -> checkJSInXPaths(testOutcomes) },
            DAILY_NAMES[11] to { testOutcomes -> checkChangedLocs5(testOutcomes) },
            DAILY_NAMES[12] to { testOutcomes -> checkLoweredLevel5(testOutcomes) },
            DAILY_NAMES[13] to { testOutcomes -> checkNewXPath(testOutcomes) },
            DAILY_NAMES[14] to { testOutcomes -> checkNewID(testOutcomes) },
            DAILY_NAMES[15] to { testOutcomes -> checkNewLinkText(testOutcomes) },
            DAILY_NAMES[16] to { testOutcomes -> checkRepair(testOutcomes) },
            DAILY_NAMES[17] to { testOutcomes -> checkRunTC(testOutcomes) },
            DAILY_NAMES[18] to { testOutcomes -> checkRunTS(testOutcomes) },
            DAILY_NAMES[19] to { testOutcomes -> checkNewMultipleUseLoc(testOutcomes) },
            DAILY_NAMES[20] to { testOutcomes -> checkNewXPathLength10(testOutcomes) },
            DAILY_NAMES[21] to { testOutcomes -> checkNewXPathLevel5(testOutcomes) },
            DAILY_NAMES[22] to { testOutcomes -> checkNewRobust(testOutcomes) },
            DAILY_NAMES[23] to { testOutcomes -> checkNewXPathWithWantedAttrs(testOutcomes) },
            DAILY_NAMES[24] to { testOutcomes -> checkNewXPathWithoutUnwantedAttrs(testOutcomes) },
            DAILY_NAMES[25] to { testOutcomes -> checkNewXPathWithoutJS(testOutcomes) },
            DAILY_NAMES[26] to { testOutcomes -> checkRunLoc20(testOutcomes) },
        )





        //dailies are assigned to user
        fun setupDailies(userProfile: UserProfile) {
            val dailies: List<Daily>
            if(GamificationManager.mode == GamificationManager.DailyAssignmentMode.random)
                dailies = ALL_DAILIES.shuffled().take(DAILIES_PER_USER)
            else if(GamificationManager.mode == GamificationManager.DailyAssignmentMode.targeted)
                dailies = emptyList() //TODO: implement targeted assignment (i.e., assign daily based on observed issues)
            else
                dailies = emptyList() //TODO: implement inclusive assignment (i.e., assign daily based on uncovered functionalities)
            userProfile.assignDailies(dailies)
        }

        fun reassignDailiesFromExpire(userProfile: UserProfile){
            userProfile.dailyProgresses.clear()
            userProfile.timestamp = System.currentTimeMillis()
            setupDailies(userProfile)//note that even the same expired dailies could be reassigned
            GUIManager.updateGUI(userProfile, notifyChange = false)
            GamificationManager.updateUserProfile(userProfile)
        }

        fun reassignDailyFromDiscard(userProfile: UserProfile, daily: Daily): DailyProgress? {
            //find all dailies but the one that is going to be discarded
            val availableDailies = ALL_DAILIES.filter { d ->
                userProfile.dailyProgresses.none { dailyProgress -> dailyProgress.daily.name == d.name }
            }
            //timestamp of discarded daily is retrieved
            //val discardedDailyProgress = userProfile.dailyProgresses.find { it.daily == daily } ?: return null
            //val discardedTimestamp = userProfile.timestamp
            //new daily is selected
            //with discarded set to true (only 1 discard within 24h is possible) and timestamp set to oldTimestamp
            val newDaily: Daily
            if(GamificationManager.mode == GamificationManager.DailyAssignmentMode.random)
                newDaily = availableDailies.shuffled().first()
            else if(GamificationManager.mode == GamificationManager.DailyAssignmentMode.targeted)
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
                ALL_DAILIES.find { it.name == dp.daily.name }?.let {
                    val progress = DAILY_CHECKS[it.name]?.invoke(testOutcomes)
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

        private fun checkLocs2LinkTextConverted(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                if (!testOutcome.isPassed)
                    continue
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (!oldLocator.locatorType.equals("linkText", ignoreCase = true) &&
                        !oldLocator.locatorType.equals("partialLinkText", ignoreCase = true)) {
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && (newLocator.locatorType.equals("linkText", ignoreCase = true)
                                    || newLocator.locatorType.equals("partialLinkText", ignoreCase = true)))
                            count++
                    }
                }
            }
            return count
        }

        //TODO: coefficient function must be implemented
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

        //TODO: check
        private fun checkRepair(testOutcomes: List<TestOutcome>): Int {
            /*val repairedLocators = mutableSetOf<String>()
            for (testOutcome in testOutcomes) {
                if (testOutcome.isPassed) {
                    val newLocators = testOutcome.locatorsNew.associateBy { it.locatorName }
                    // Iterate over old locators to find the ones that were broken
                    val brokenLocators = testOutcome.locatorsOld.filter { oldLocator ->
                        oldLocator.locatorValue in testOutcome.stacktrace // Check if it was broken and mentioned in the stack trace
                    }

                    // Check if these locators are now fixed
                    for (brokenLocator in brokenLocators) {
                        val newLocator = newLocators[brokenLocator.locatorName]
                        if (newLocator != null && newLocator.locatorValue != brokenLocator.locatorValue) {
                            repairedLocators.add(brokenLocator.locatorValue)
                        }
                    }
                }
            }
            return repairedLocators.size*/
            return 0
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

        private fun checkNewLinkText(testOutcomes: List<TestOutcome>): Int {
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
                            (newLocator.locatorType.equals("linkText", ignoreCase = true) ||
                                    newLocator.locatorType.equals("partialLinkText", ignoreCase = true))
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
                            computeFragilityCoefficient(newLocator) < 0.5 //TODO: 0.5 enough?
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



        /******* RUN CHECKS *******/

        private fun checkRunTC(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                count++
            }
            return count
        }

        private fun checkRunTS(testOutcomes: List<TestOutcome>): Int {
            for(testOutcome in testOutcomes){
                if(!testOutcome.isPassed)
                    return 0
            }
            return 1
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










        /**AUXILIARY FUNCTIONS**/
        fun computeFragilityCoefficient(loc: Locator): Int{
            return 0//TODO:implement coefficient
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
