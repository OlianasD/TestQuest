package gamification

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

        fun getDailies(): MutableList<Daily> {
            return allDailies
        }


        fun updateDaily(userProfile: UserProfile, daily: Daily, progress: Int) {
            if (progress > 0) {
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
}
