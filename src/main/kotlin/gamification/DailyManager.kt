package gamification

import listener.TestOutcome
import locator.Locator

class DailyManager {


    companion object {

        private val TARGET_DAILY: Int = 1 //TODO: to convert into a map (each daily may have specific requests)

        private val XP_DAILY: Int = 20 //TODO: to convert into a map (each daily may provide specific XP)

        private val DAILIES_PER_USER: Int = 3







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
            "xpathNoBreak",
            "idNoBreak",
            "linkTextNoBreak",
            "repair",
            "runtc",
            "runts",
            "useLoc",
            )

        //TODO: change icons, xp, target accordingly
        private val ALL_DAILIES = mutableListOf(
            Daily(
                DAILY_NAMES[0],
                "Replace $TARGET_DAILY absolute XPath locators with $TARGET_DAILY relative ones",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[1],
                "Reduce the length of $TARGET_DAILY XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[2],
                "Reduce the level of $TARGET_DAILY XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[3],
                "Convert $TARGET_DAILY non-XPath locators to $TARGET_DAILY XPath ones",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[4],
                "Convert $TARGET_DAILY non-ID locators to $TARGET_DAILY ID ones",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[5],
                "Convert $TARGET_DAILY non-linkText locators to $TARGET_DAILY linkText ones",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[6],
                "Improve the robustness of $TARGET_DAILY locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[7],
                "Shorten the length of $TARGET_DAILY locators below 10 characters",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[8],
                "Add $TARGET_DAILY references to @id, @name, @class, @title, @alt" +
                        "or @value attributes to XPaths locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[9],
                "Remove $TARGET_DAILY references to @src, @href, @height, or @width " +
                        "attributes from XPaths locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[10],
                "Remove $TARGET_DAILY references to Javascript code from XPaths locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[11],
                "Modify 5 different locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[12],
                "Lower the level of $TARGET_DAILY locators below 5",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[13],
                "Implement $TARGET_DAILY new XPath locators that do not break",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[14],
                "Implement $TARGET_DAILY new ID locators that do not break",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[15],
                "Implement $TARGET_DAILY new linkText locators that do not break",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Repair $TARGET_DAILY broken locators",
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
                "Implement the same locator more than once in a test suite",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            )
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
            DAILY_NAMES[13] to { testOutcomes -> checkNewXPathNoBreak(testOutcomes) },
            DAILY_NAMES[14] to { testOutcomes -> checkNewIDNoBreak(testOutcomes) },
            DAILY_NAMES[15] to { testOutcomes -> checkNewLinkTextNoBreak(testOutcomes) },
            DAILY_NAMES[16] to { testOutcomes -> checkRepair(testOutcomes) },
            DAILY_NAMES[17] to { testOutcomes -> checkRunTC(testOutcomes) },
            DAILY_NAMES[18] to { testOutcomes -> checkRunTS(testOutcomes) },
            DAILY_NAMES[19] to { testOutcomes -> checkNewMultipleUseLoc(testOutcomes) }
        )





        //dailies are assigned to user
        fun setupDailies(userProfile: UserProfile) {
            val dailies = ALL_DAILIES.shuffled().take(DAILIES_PER_USER)
            userProfile.assignDailies(dailies)
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





        /******* CHECKS *******/
        private fun checkAbsXPathRemoved(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                count += (locatorsOld.count { it.locatorValue.startsWith("/html") } -
                        locatorsNew.count { it.locatorValue.startsWith("/html") })
            }
            return count
        }

        private fun checkXPathLengthReduced(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                for (i in locatorsOld.indices) {
                    val oldLocator = locatorsOld[i]
                    val newLocator = locatorsNew[i]
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        newLocator.locatorType.equals("xpath", ignoreCase = true))
                        if (newLocator.locatorValue.length < oldLocator.locatorValue.length)
                            count++
                }
            }
            return count
        }

        private fun checkXPathLevelReduced(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                for (i in locatorsOld.indices) {
                    val oldLocator = locatorsOld[i]
                    val newLocator = locatorsNew[i]
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        newLocator.locatorType.equals("xpath", ignoreCase = true))
                        if (newLocator.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size
                            < oldLocator.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size)
                            count++
                }
            }
            return count
        }

        private fun checkLocs2XPathConverted(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                for (i in locatorsOld.indices) {
                    val oldLocator = locatorsOld[i]
                    val newLocator = locatorsNew[i]
                    if (!oldLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        newLocator.locatorType.equals("xpath", ignoreCase = true))
                        count++
                }
            }
            return count
        }

        private fun checkLocs2IDConverted(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                for (i in locatorsOld.indices) {
                    val oldLocator = locatorsOld[i]
                    val newLocator = locatorsNew[i]
                    if (!oldLocator.locatorType.equals("id", ignoreCase = true) &&
                        newLocator.locatorType.equals("id", ignoreCase = true))
                        count++
                }
            }
            return count
        }

        private fun checkLocs2LinkTextConverted(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                for (i in locatorsOld.indices) {
                    val oldLocator = locatorsOld[i]
                    val newLocator = locatorsNew[i]
                    if ((!oldLocator.locatorType.equals("linkText", ignoreCase = true) &&
                                !oldLocator.locatorType.equals("partialLinkText", ignoreCase = true)) &&
                        (newLocator.locatorType.equals("linkText", ignoreCase = true) ||
                                newLocator.locatorType.equals("partialLinkText", ignoreCase = true)))
                        count++
                }
            }
            return count
        }

        private fun checkRobustnessImprovement(testOutcomes: List<TestOutcome>): Int{
            var count = 0
            for(testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                for (i in locatorsOld.indices) {
                    val oldLocator = locatorsOld[i]
                    val newLocator = locatorsNew[i]
                    if (computeFragilityCoefficient(newLocator) <
                        computeFragilityCoefficient(oldLocator))
                        count++
                }
            }
            return count
        }

        private fun computeFragilityCoefficient(loc: Locator): Int{
            return 0//TODO:implement coefficient
        }

        private fun checkShortenedLength10(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                for (i in locatorsOld.indices) {
                    val oldLocator = locatorsOld[i]
                    val newLocator = locatorsNew[i]
                    if (newLocator.locatorValue.length < 10 &&
                        newLocator.locatorValue.length < oldLocator.locatorValue.length)
                        count++
                }
            }
            return count
        }

        private fun checkWantedAttrsInXPaths(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                for (i in locatorsOld.indices) {
                    val oldLocator = locatorsOld[i]
                    val newLocator = locatorsNew[i]
                    if (newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        oldLocator.locatorType.equals("xpath", ignoreCase = true))
                        count += countWantedAttributes(newLocator.locatorValue) -
                                countWantedAttributes(oldLocator.locatorValue)                }
            }
            return count
        }

        private fun checkUnwantedAttrsInXPaths(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                for (i in locatorsOld.indices) {
                    val oldLocator = locatorsOld[i]
                    val newLocator = locatorsNew[i]
                    if (newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        oldLocator.locatorType.equals("xpath", ignoreCase = true))
                        count += countUnwantedAttributes(oldLocator.locatorValue) -
                                countUnwantedAttributes(newLocator.locatorValue)
                }
            }
            return count
        }

        private fun checkJSInXPaths(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                for (i in locatorsOld.indices) {
                    val oldLocator = locatorsOld[i]
                    val newLocator = locatorsNew[i]
                    if (newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        oldLocator.locatorType.equals("xpath", ignoreCase = true))
                        count += countJavaScriptReferences(oldLocator.locatorValue) -
                                countJavaScriptReferences(newLocator.locatorValue)
                }
            }
            return count
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


        private val lastChangedLocs: MutableList<Locator> = mutableListOf()

        private fun checkChangedLocs5(testOutcomes: List<TestOutcome>): Int {
            //TODO: bisogna salvarsi da qualche parte i locators modificati
            return 0
        }

        private fun checkLoweredLevel5(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                for (i in locatorsOld.indices) {
                    val oldLocator = locatorsOld[i]
                    val newLocator = locatorsNew[i]
                    if (newLocator.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size < 5 &&
                        newLocator.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size <
                        oldLocator.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size)
                        count++
                }
            }
            return count
        }

        //this is checked for newly added tests or locators
        private fun checkNewXPathNoBreak(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                for (newLocator in locatorsNew) {
                    if (newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        locatorsOld.none { it.locatorType == newLocator.locatorType && it.locatorValue == newLocator.locatorValue }) {
                        count++
                    }
                }
            }
            return count
        }

        //this is checked for newly added tests or locators
        private fun checkNewIDNoBreak(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                for (newLocator in locatorsNew) {
                    if (newLocator.locatorType.equals("id", ignoreCase = true) &&
                        locatorsOld.none { it.locatorType == newLocator.locatorType && it.locatorValue == newLocator.locatorValue }) {
                        count++
                    }
                }
            }
            return count
        }

        //this is checked for newly added tests or locators
        private fun checkNewLinkTextNoBreak(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                for (newLocator in locatorsNew) {
                        if (newLocator.locatorType.equals("linkText", ignoreCase = true) ||
                            newLocator.locatorType.equals("partialLinkText", ignoreCase = true) &&
                            locatorsOld.none { it.locatorType == newLocator.locatorType &&
                                    it.locatorValue == newLocator.locatorValue }) {
                            count++
                    }
                }
            }
            return count
        }

        private fun checkRepair(testOutcomes: List<TestOutcome>): Int {
            return 0 //TODO
        }

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

        //this is checked for newly added tests or locators
        private fun checkNewMultipleUseLoc(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                val locatorCountMap = mutableMapOf<Locator, Int>() //map to count locs occurrences
                //count multiple uses of newly added locators
                for (locator in locatorsNew)
                    if (locatorsOld.none { it.locatorType == locator.locatorType && it.locatorValue == locator.locatorValue })
                        locatorCountMap[locator] = locatorCountMap.getOrDefault(locator, 0) + 1
                count += locatorCountMap.values.count { it > 1 }
            }
            return count
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
            val dailyProgress = userProfile.dailyProgresses.find { it.daily.name == daily.name }
            dailyProgress?.let { dp ->
                dp.progress += progress
                if (dp.progress >= dp.daily.target) { // if the daily has been completed, assign xp and remove it from list
                    userProfile.currentXP += dp.daily.xp
                    userProfile.dailyProgresses.removeIf { it.daily.name == dp.daily.name }
                }
            }
        }


    }
}
