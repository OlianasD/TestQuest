package gamification

import locator.Locator

class DailyManager {


    companion object {

        private val TARGET_DAILY: Int = 3 //TODO: to convert into a map (each daily may have specific requests)
        private val XP_DAILY: Int = 20 //TODO: to convert into a map (each daily may provide specific XP)
        private val DAILIES_PER_USER: Int = 3

        private val allDailies = mutableListOf(
            Daily("xpathAbs", "Replace $TARGET_DAILY absolute XPath locators with $TARGET_DAILY relative ones", XP_DAILY, TARGET_DAILY),
            Daily("xpathLength", "Reduce the length of $TARGET_DAILY XPath locators", XP_DAILY, TARGET_DAILY),
            Daily("xpathHeight", "Reduce the height of $TARGET_DAILY XPath locators", XP_DAILY, TARGET_DAILY),
            Daily("loc2css", "Convert $TARGET_DAILY non-CSS locators to $TARGET_DAILY CSS ones", XP_DAILY, TARGET_DAILY),
            Daily("loc2xpath", "Convert $TARGET_DAILY non-XPath locators to $TARGET_DAILY XPath one", XP_DAILY, TARGET_DAILY),
            Daily("loc2id", "Convert $TARGET_DAILY non-ID locators to $TARGET_DAILY ID ones", XP_DAILY, TARGET_DAILY),
            Daily("loc2class", "Convert $TARGET_DAILY non-class locators to $TARGET_DAILY class ones", XP_DAILY, TARGET_DAILY),
            Daily("loc2linkText", "Convert $TARGET_DAILY non-linkText locators to $TARGET_DAILY linkText ones", XP_DAILY, TARGET_DAILY),
            Daily("loc2name", "Convert $TARGET_DAILY non-name locators to $TARGET_DAILY name ones", XP_DAILY, TARGET_DAILY),
            Daily("attrRef", "Add a reference to $TARGET_DAILY attributes within $TARGET_DAILY XPath locators", XP_DAILY, TARGET_DAILY),
            Daily("tableRef", "Add a reference to $TARGET_DAILY <table> tags within $TARGET_DAILY XPath locators", XP_DAILY, TARGET_DAILY),
            Daily("divRef", "Add a reference to $TARGET_DAILY <div> tags within $TARGET_DAILY XPath locators", XP_DAILY, TARGET_DAILY),
            Daily("formRef", "Add a reference to $TARGET_DAILY <form> tags within $TARGET_DAILY XPath locators", XP_DAILY, TARGET_DAILY),
            Daily("buttonRef", "Add a reference to $TARGET_DAILY <button> tags within $TARGET_DAILY XPath locators", XP_DAILY, TARGET_DAILY),
            Daily("linkRef", "Add a reference to $TARGET_DAILY <a> tags within $TARGET_DAILY XPath locators", XP_DAILY, TARGET_DAILY),
            Daily("spanRef", "Add a reference to $TARGET_DAILY <span> tags within $TARGET_DAILY XPath locators", XP_DAILY, TARGET_DAILY),
            Daily("robust", "Make $TARGET_DAILY locators more robust", XP_DAILY, TARGET_DAILY), //TODO: which metric?
        )


        fun setupDailies(userProfile: UserProfile) {
            val dailies = allDailies.shuffled().take(DAILIES_PER_USER)
            userProfile.assignDailies(dailies)
        }

        /*TO UPDATE DAILIES*/

        private val MAX_LENGTH: Int = 20//TODO: to reason about
        private val MAX_HEIGHT: Int = 3

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

        private fun absoluteXpathCount(locatorsOld: List<Locator>, locatorsNew: List<Locator>): Int {
            return (locatorsOld.count { it.locatorValue.startsWith("/html") } -
                    locatorsNew.count { it.locatorValue.startsWith("/html") })
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

        private val dailyChecks: Map<String, (List<Locator>, List<Locator>) -> Int> = mapOf(
            "xpathAbs" to { locsOld, locsNew -> absoluteXpathCount(locsOld, locsNew) },
            "xpathLength" to { locsOld, locsNew -> longXpathCount(locsOld, locsNew) },
            "xpathHeight" to { locsOld, locsNew -> highXpathCount(locsOld, locsNew) },
            "loc2css" to { locsOld, locsNew -> cssLocatorsCount(locsOld, locsNew) },
            "loc2xpath" to { locsOld, locsNew -> xpathLocatorsCount(locsOld, locsNew) },
            "loc2id" to { locsOld, locsNew -> idLocatorsCount(locsOld, locsNew) },
            "loc2class" to { locsOld, locsNew -> classLocatorsCount(locsOld, locsNew) },
            "loc2linkText" to { locsOld, locsNew -> linkTextLocatorsCount(locsOld, locsNew) },
            "loc2name" to { locsOld, locsNew -> nameLocatorsCount(locsOld, locsNew) },
            "attrRef" to { locsOld, locsNew -> xpathAttributeCount(locsOld, locsNew) },
            "tableRef" to { locsOld, locsNew -> xpathTableCount(locsOld, locsNew) },
            "divRef" to { locsOld, locsNew -> xpathDivCount(locsOld, locsNew) },
            "formRef" to { locsOld, locsNew -> xpathFormCount(locsOld, locsNew) },
            "buttonRef" to { locsOld, locsNew -> xpathButtonCount(locsOld, locsNew) },
            "linkRef" to { locsOld, locsNew -> xpathAnchorCount(locsOld, locsNew) },
            "spanRef" to { locsOld, locsNew -> xpathSpanCount(locsOld, locsNew) },
            "robust" to { _, _ -> prova() } //TODO: placeholder to adapt with new method depending on the daily check
            //TODO: add more
        )

        fun updateDailies(userProfile: UserProfile, locatorsOld: List<Locator>, locatorsNew: List<Locator>): Boolean {
            var anyUpdate = false
            val allDailies = DailyManager.allDailies
            val copyOfDailyProgresses = ArrayList(userProfile.dailyProgresses) //needed since the list is updated during loop
            copyOfDailyProgresses.forEach { dp ->
                allDailies.find { it.name == dp.daily.name }?.let {
                    val progress = dailyChecks[it.name]?.invoke(locatorsOld, locatorsNew)
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
