package gamification

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
            "robust"
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
                DAILY_NAMES[16],
                "Write $TARGET_DAILY locators shorter than 10 characters",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Write $TARGET_DAILY locators shorter than 5 characters",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Add @id reference within $TARGET_DAILY Xpaths",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Add @name reference within $TARGET_DAILY Xpaths",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Add @class reference within $TARGET_DAILY Xpaths",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Add @title reference within $TARGET_DAILY Xpaths",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Add @alt reference within $TARGET_DAILY Xpaths",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Add @value reference within $TARGET_DAILY Xpaths",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Remove @src reference within $TARGET_DAILY Xpaths",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Remove @href reference within $TARGET_DAILY Xpaths",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Remove @width reference within $TARGET_DAILY Xpaths",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Remove @onclick reference within $TARGET_DAILY Xpaths",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Implement $TARGET_DAILY XPath locators that do not fail in 3 runs",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Implement $TARGET_DAILY ID locators that do not fail in 3 runs",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Implement $TARGET_DAILY linkText locators that do not fail in 3 runs",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),


            Daily(
                DAILY_NAMES[16],
                "Implement 5 locators of any type",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Implement 5 ID locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),Daily(
                DAILY_NAMES[16],
                "Implement 5 relative XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),Daily(
                DAILY_NAMES[16],
                "Implement 5 linkText locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Implement 5 linkText locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Use the same locator 5 times",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
                "Remove 5 non ID, linkText, or XPath locators",
                XP_DAILY,
                TARGET_DAILY,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png"
            ),
            Daily(
                DAILY_NAMES[16],
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

        private val DAILY_CHECKS: Map<String, (List<Locator>, List<Locator>) -> Int> = mapOf(
            DAILY_NAMES[0] to { locsOld, locsNew -> absoluteXpathCount(locsOld, locsNew) },
            DAILY_NAMES[1] to { locsOld, locsNew -> longXpathCount(locsOld, locsNew) },
            DAILY_NAMES[2] to { locsOld, locsNew -> highXpathCount(locsOld, locsNew) },
            DAILY_NAMES[3] to { locsOld, locsNew -> cssLocatorsCount(locsOld, locsNew) },
            DAILY_NAMES[4] to { locsOld, locsNew -> xpathLocatorsCount(locsOld, locsNew) },
            DAILY_NAMES[5] to { locsOld, locsNew -> idLocatorsCount(locsOld, locsNew) },
            DAILY_NAMES[6] to { locsOld, locsNew -> classLocatorsCount(locsOld, locsNew) },
            DAILY_NAMES[7] to { locsOld, locsNew -> linkTextLocatorsCount(locsOld, locsNew) },
            DAILY_NAMES[8] to { locsOld, locsNew -> nameLocatorsCount(locsOld, locsNew) },
            DAILY_NAMES[9] to { locsOld, locsNew -> xpathAttributeCount(locsOld, locsNew) },
            DAILY_NAMES[10] to { locsOld, locsNew -> xpathTableCount(locsOld, locsNew) },
            DAILY_NAMES[11] to { locsOld, locsNew -> xpathDivCount(locsOld, locsNew) },
            DAILY_NAMES[12] to { locsOld, locsNew -> xpathFormCount(locsOld, locsNew) },
            DAILY_NAMES[13] to { locsOld, locsNew -> xpathButtonCount(locsOld, locsNew) },
            DAILY_NAMES[14] to { locsOld, locsNew -> xpathAnchorCount(locsOld, locsNew) },
            DAILY_NAMES[15] to { locsOld, locsNew -> xpathSpanCount(locsOld, locsNew) },
            DAILY_NAMES[16] to { _, _ -> prova() } //TODO: placeholder to adapt with new method depending on the daily check
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
        private val MAX_HEIGHT: Int = 3



        private fun absoluteXpathCount(locatorsOld: List<Locator>, locatorsNew: List<Locator>): Int {
            return (locatorsOld.count { it.locatorValue.startsWith("/html") } -
                    locatorsNew.count { it.locatorValue.startsWith("/html") })
        }


        private fun cssLocatorsCount(locatorsOld: List<Locator>, locatorsNew: List<Locator>): Int {
            return locatorsNew.count { it.locatorType.equals("css", ignoreCase = true) } -
                    locatorsOld.count { it.locatorType.equals("css", ignoreCase = true) }
        }

        private fun xpathLocatorsCount(locatorsOld: List<Locator>, locatorsNew: List<Locator>): Int {
            return locatorsNew.count { it.locatorType.equals("xpath", ignoreCase = true) } -
                    locatorsOld.count { it.locatorType.equals("xpath", ignoreCase = true) }
        }

        private fun idLocatorsCount(locatorsOld: List<Locator>, locatorsNew: List<Locator>): Int {
            return locatorsNew.count { it.locatorType.equals("id", ignoreCase = true) } -
                    locatorsOld.count { it.locatorType.equals("id", ignoreCase = true) }
        }

        private fun nameLocatorsCount(locatorsOld: List<Locator>, locatorsNew: List<Locator>): Int {
            return locatorsNew.count { it.locatorType.equals("name", ignoreCase = true) } -
                    locatorsOld.count { it.locatorType.equals("name", ignoreCase = true) }
        }

        private fun classLocatorsCount(locatorsOld: List<Locator>, locatorsNew: List<Locator>): Int {
            return locatorsNew.count { it.locatorType.equals("class", ignoreCase = true) } -
                    locatorsOld.count { it.locatorType.equals("class", ignoreCase = true) }
        }

        private fun linkTextLocatorsCount(locatorsOld: List<Locator>, locatorsNew: List<Locator>): Int {
            return (locatorsNew.count {
                it.locatorType.equals("linkText", ignoreCase = true) ||
                        it.locatorType.equals("partialLinkText", ignoreCase = true)
            } -
                    locatorsOld.count {
                        it.locatorType.equals("linkText", ignoreCase = true) ||
                                it.locatorType.equals("partialLinkText", ignoreCase = true)
                    })
        }

        private fun xpathLocators(locatorsOld: List<Locator>, locatorsNew: List<Locator>): Pair<List<Locator>, List<Locator>> {
            val oldXpathLocators = locatorsOld.filter { it.locatorType.equals("xpath", ignoreCase = true) }
            val newXpathLocators = locatorsNew.filter { it.locatorType.equals("xpath", ignoreCase = true) }
            return Pair(oldXpathLocators, newXpathLocators)
        }

        private fun longXpathCount(locatorsOld: List<Locator>, locatorsNew: List<Locator>): Int {
            val (xpathLocatorsOld, xpathLocatorsNew) = xpathLocators(locatorsOld, locatorsNew)
            return (xpathLocatorsOld.count { it.locatorValue.length > MAX_LENGTH } -
                    xpathLocatorsNew.count { it.locatorValue.length > MAX_LENGTH })
        }

        private fun highXpathCount(locatorsOld: List<Locator>, locatorsNew: List<Locator>): Int {
            val (xpathLocatorsOld, xpathLocatorsNew) = xpathLocators(locatorsOld, locatorsNew)
            return (
                    xpathLocatorsOld.count {it.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size > MAX_HEIGHT }
                            -
                            xpathLocatorsNew.count {it.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size > MAX_HEIGHT }
                    )
        }

        private fun xpathAttributeCount(locatorsOld: List<Locator>, locatorsNew: List<Locator>): Int {
            return (locatorsNew.count { it.locatorValue.contains("@") } -
                    locatorsOld.count { it.locatorValue.contains("@") })
        }

        private fun xpathTableCount(locatorsOld: List<Locator>, locatorsNew: List<Locator>): Int {
            return (locatorsNew.count { it.locatorValue.contains("/table") } -
                    locatorsOld.count { it.locatorValue.contains("/table") })
        }

        private fun xpathDivCount(locatorsOld: List<Locator>, locatorsNew: List<Locator>): Int {
            return (locatorsNew.count { it.locatorValue.contains("/div") } -
                    locatorsOld.count { it.locatorValue.contains("/div") })
        }

        private fun xpathSpanCount(locatorsOld: List<Locator>, locatorsNew: List<Locator>): Int {
            return (locatorsNew.count { it.locatorValue.contains("/span") } -
                    locatorsOld.count { it.locatorValue.contains("/span") })
        }

        private fun xpathButtonCount(locatorsOld: List<Locator>, locatorsNew: List<Locator>): Int {
            return (locatorsNew.count { it.locatorValue.contains("/button") } -
                    locatorsOld.count { it.locatorValue.contains("/button") })
        }

        private fun xpathAnchorCount(locatorsOld: List<Locator>, locatorsNew: List<Locator>): Int {
            return (locatorsNew.count { it.locatorValue.contains("/a") } -
                    locatorsOld.count { it.locatorValue.contains("/a") })
        }

        private fun xpathFormCount(locatorsOld: List<Locator>, locatorsNew: List<Locator>): Int {
            return (locatorsNew.count { it.locatorValue.contains("/form") } -
                    locatorsOld.count { it.locatorValue.contains("/form") })
        }

        private fun prova(): Int{
            return 1
        }



        fun updateDailies(userProfile: UserProfile, locatorsOld: List<Locator>, locatorsNew: List<Locator>): Boolean {
            var anyUpdate = false
            val copyOfDailyProgresses = ArrayList(userProfile.dailyProgresses) //needed since the list is updated during loop
            copyOfDailyProgresses.forEach { dp ->
                ALL_DAILIES.find { it.name == dp.daily.name }?.let {
                    val progress = DAILY_CHECKS[it.name]?.invoke(locatorsOld, locatorsNew)
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
