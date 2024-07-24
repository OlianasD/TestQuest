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
            "xpathHeight",
            "loc2css",
            "loc2xpath",
            "loc2id",
            "loc2class",
            "loc2linkText",
            "loc2name",
            "attrRef",
            "tableRef",
            "divRef",
            "formRef",
            "buttonRef",
            "linkRef",
            "spanRef",
            "robust",
            "stringa1",
            "stringa2",
            "stringa3",
            "stringa4",
            "stringa5",
            "stringa6",
            "stringa7",
            "stringa8",
            "stringa9",
            "stringa10",
            "stringa11",
            "stringa12",
            "stringa13",
            "stringa14",
            "stringa15",
            "stringa16",
            "stringa17",
            "stringa18",
            "stringa19",
            "stringa20",
            "stringa21",
            "stringa22",
            "stringa23",
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
                "Convert $TARGET_DAILY non-CSS locators to $TARGET_DAILY CSS ones",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[4],
                "Convert $TARGET_DAILY non-XPath locators to $TARGET_DAILY XPath one",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[5],
                "Convert $TARGET_DAILY non-ID locators to $TARGET_DAILY ID ones",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[6],
                "Convert $TARGET_DAILY non-class locators to $TARGET_DAILY class ones",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[7],
                "Convert $TARGET_DAILY non-linkText locators to $TARGET_DAILY linkText ones",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[8],
                "Convert $TARGET_DAILY non-name locators to $TARGET_DAILY name ones",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[9],
                "Add a reference to $TARGET_DAILY attributes within $TARGET_DAILY XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[10],
                "Add a reference to $TARGET_DAILY <table> tags within $TARGET_DAILY XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[11],
                "Add a reference to $TARGET_DAILY <div> tags within $TARGET_DAILY XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[12],
                "Add a reference to $TARGET_DAILY <form> tags within $TARGET_DAILY XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[13],
                "Add a reference to $TARGET_DAILY <button> tags within $TARGET_DAILY XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[14],
                "Add a reference to $TARGET_DAILY <a> tags within $TARGET_DAILY XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[15],
                "Add a reference to $TARGET_DAILY <span> tags within $TARGET_DAILY XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Improve the robustness of $TARGET_DAILY locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ), //TODO: which metric?
            Daily(
                DAILY_NAMES[17],
                "Write $TARGET_DAILY locators shorter than 10 characters",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[18],
                "Write $TARGET_DAILY locators shorter than 5 characters",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[19],
                "Add @id reference within $TARGET_DAILY Xpaths",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[20],
                "Add @name reference within $TARGET_DAILY Xpaths",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[21],
                "Add @class reference within $TARGET_DAILY Xpaths",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[22],
                "Add @title reference within $TARGET_DAILY Xpaths",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[23],
                "Add @alt reference within $TARGET_DAILY Xpaths",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[24],
                "Add @value reference within $TARGET_DAILY Xpaths",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[25],
                "Remove @src reference within $TARGET_DAILY Xpaths",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[26],
                "Remove @href reference within $TARGET_DAILY Xpaths",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[27],
                "Remove @width reference within $TARGET_DAILY Xpaths",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[28],
                "Remove @onclick reference within $TARGET_DAILY Xpaths",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[29],
                "Implement $TARGET_DAILY XPath locators that do not fail in 3 runs",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[30],
                "Implement $TARGET_DAILY ID locators that do not fail in 3 runs",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[31],
                "Implement $TARGET_DAILY linkText locators that do not fail in 3 runs",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[32],
                "Implement 5 locators of any type",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[33],
                "Implement 5 ID locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),Daily(
                DAILY_NAMES[34],
                "Implement 5 relative XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),Daily(
                DAILY_NAMES[35],
                "Implement 5 linkText locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[36],
                "Implement 5 linkText locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[37],
                "Use the same locator 5 times",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[38],
                "Remove 5 non ID, linkText, or XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[39],
                "Modify a locator 3 times in 24 hours",
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
            DAILY_NAMES[0] to { testOutcomes -> absoluteXpathCount(testOutcomes) },
            DAILY_NAMES[1] to { testOutcomes -> longXpathCount(testOutcomes) },
            DAILY_NAMES[2] to { testOutcomes -> highXpathCount(testOutcomes) },
            DAILY_NAMES[3] to { testOutcomes -> cssLocatorsCount(testOutcomes) },
            DAILY_NAMES[4] to { testOutcomes -> xpathLocatorsCount(testOutcomes) },
            DAILY_NAMES[5] to { testOutcomes -> idLocatorsCount(testOutcomes) },
            DAILY_NAMES[6] to { testOutcomes -> classLocatorsCount(testOutcomes) },
            DAILY_NAMES[7] to { testOutcomes -> linkTextLocatorsCount(testOutcomes) },
            DAILY_NAMES[8] to { testOutcomes -> nameLocatorsCount(testOutcomes) },
            DAILY_NAMES[9] to { testOutcomes -> xpathAttributeCount(testOutcomes) },
            DAILY_NAMES[10] to { testOutcomes -> xpathTableCount(testOutcomes) },
            DAILY_NAMES[11] to { testOutcomes -> xpathDivCount(testOutcomes) },
            DAILY_NAMES[12] to { testOutcomes -> xpathFormCount(testOutcomes) },
            DAILY_NAMES[13] to { testOutcomes -> xpathButtonCount(testOutcomes) },
            DAILY_NAMES[14] to { testOutcomes -> xpathAnchorCount(testOutcomes) },
            DAILY_NAMES[15] to { testOutcomes -> xpathSpanCount(testOutcomes) },
            DAILY_NAMES[16] to { testOutcomes -> robustnessImprovementCount(testOutcomes) }
            //TODO: add more
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






        /*TO UPDATE DAILIES*/

        private val MAX_LENGTH: Int = 20//TODO: to reason about
        private val MAX_LEVEL: Int = 3




        private fun cssLocatorsCount(testOutcomes: List<TestOutcome>): Int {
            /*return locatorsNew.count { it.locatorType.equals("css", ignoreCase = true) } -
                    locatorsOld.count { it.locatorType.equals("css", ignoreCase = true) }*/
            return 0
        }

        private fun xpathLocatorsCount(testOutcomes: List<TestOutcome>): Int {
            /*return locatorsNew.count { it.locatorType.equals("xpath", ignoreCase = true) } -
                    locatorsOld.count { it.locatorType.equals("xpath", ignoreCase = true) }*/
            return 0
        }

        private fun idLocatorsCount(testOutcomes: List<TestOutcome>): Int {
            /*return locatorsNew.count { it.locatorType.equals("id", ignoreCase = true) } -
                    locatorsOld.count { it.locatorType.equals("id", ignoreCase = true) }*/
            return 0
        }

        private fun nameLocatorsCount(testOutcomes: List<TestOutcome>): Int {
            /*return locatorsNew.count { it.locatorType.equals("name", ignoreCase = true) } -
                    locatorsOld.count { it.locatorType.equals("name", ignoreCase = true) }*/
            return 0
        }

        private fun classLocatorsCount(testOutcomes: List<TestOutcome>): Int {
            /*return locatorsNew.count { it.locatorType.equals("class", ignoreCase = true) } -
                    locatorsOld.count { it.locatorType.equals("class", ignoreCase = true) }*/
            return 0
        }

        private fun linkTextLocatorsCount(testOutcomes: List<TestOutcome>): Int {
            /*return (locatorsNew.count {
                it.locatorType.equals("linkText", ignoreCase = true) ||
                        it.locatorType.equals("partialLinkText", ignoreCase = true)
            } -
                    locatorsOld.count {
                        it.locatorType.equals("linkText", ignoreCase = true) ||
                                it.locatorType.equals("partialLinkText", ignoreCase = true)
                    })*/
            return 0
        }

        private fun xpathLocators(locatorsOld: List<Locator>, locatorsNew: List<Locator>): Pair<List<Locator>, List<Locator>> {
            val oldXpathLocators = locatorsOld.filter { it.locatorType.equals("xpath", ignoreCase = true) }
            val newXpathLocators = locatorsNew.filter { it.locatorType.equals("xpath", ignoreCase = true) }
            return Pair(oldXpathLocators, newXpathLocators)
        }

        private fun xpathAttributeCount(testOutcomes: List<TestOutcome>): Int {
            /*return (locatorsNew.count { it.locatorValue.contains("@") } -
                    locatorsOld.count { it.locatorValue.contains("@") })*/
            return 0
        }

        private fun xpathTableCount(testOutcomes: List<TestOutcome>): Int {
            /*return (locatorsNew.count { it.locatorValue.contains("/table") } -
                    locatorsOld.count { it.locatorValue.contains("/table") })*/
            return 0
        }

        private fun xpathDivCount(testOutcomes: List<TestOutcome>): Int {
            /*return (locatorsNew.count { it.locatorValue.contains("/div") } -
                    locatorsOld.count { it.locatorValue.contains("/div") })*/
            return 0
        }

        private fun xpathSpanCount(testOutcomes: List<TestOutcome>): Int {
            /*return (locatorsNew.count { it.locatorValue.contains("/span") } -
                    locatorsOld.count { it.locatorValue.contains("/span") })*/
            return 0
        }

        private fun xpathButtonCount(testOutcomes: List<TestOutcome>): Int {
            /*return (locatorsNew.count { it.locatorValue.contains("/button") } -
                    locatorsOld.count { it.locatorValue.contains("/button") })*/
            return 0
        }

        private fun xpathAnchorCount(testOutcomes: List<TestOutcome>): Int {
            /*return (locatorsNew.count { it.locatorValue.contains("/a") } -
                    locatorsOld.count { it.locatorValue.contains("/a") })*/
            return 0
        }

        private fun xpathFormCount(testOutcomes: List<TestOutcome>): Int {
            /*return (locatorsNew.count { it.locatorValue.contains("/form") } -
                    locatorsOld.count { it.locatorValue.contains("/form") })*/
            return 0
        }









        private fun absoluteXpathCount(testOutcomes: List<TestOutcome>): Int {
            /*return (locatorsOld.count { it.locatorValue.startsWith("/html") } -
                    locatorsNew.count { it.locatorValue.startsWith("/html") })*/
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                count += (locatorsOld.count { it.locatorValue.startsWith("/html") } -
                        locatorsNew.count { it.locatorValue.startsWith("/html") })
            }
            return count
        }


        private fun computeFragilityCoefficient(loc: Locator): Int{
            return 1
        }

        private fun longXpathCount(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                for (i in locatorsOld.indices) {
                    val oldLocator = locatorsOld[i]
                    val newLocator = locatorsNew[i]
                    if (oldLocator.locatorType == "xpath" && newLocator.locatorType == "xpath")
                        if (newLocator.locatorValue.length < oldLocator.locatorValue.length)
                            count++
                }
            }
            return count
        }

        private fun robustnessImprovementCount(testOutcomes: List<TestOutcome>): Int{
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                for (i in locatorsOld.indices) {
                    val oldLocator = locatorsOld[i]
                    val newLocator = locatorsNew[i]
                    if (computeFragilityCoefficient(oldLocator) <
                        computeFragilityCoefficient(newLocator))
                        count++
                }
            }
            return count
        }


        private fun highXpathCount(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                for (i in locatorsOld.indices) {
                    val oldLocator = locatorsOld[i]
                    val newLocator = locatorsNew[i]
                    if (oldLocator.locatorType == "xpath" && newLocator.locatorType == "xpath")
                        if (newLocator.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size
                            < oldLocator.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size
                        )
                            count++
                }
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
