package gamification

import locator.Locator
import utils.XMLReader
import utils.XMLWriter

class GamificationManager(private val path: String) {



    private val allTitles = mutableListOf(
        Title("Basic Tester", 20),
        Title("Pro Tester", 500),
        Title("Master Tester", 1000),
    )


    private val MAX_LENGTH: Int = 20//TODO: to reason about
    private val MAX_HEIGHT: Int = 5



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
    )


    private fun checkDailies(userProfile: UserProfile, locatorsOld: List<Locator>, locatorsNew: List<Locator>) {
        val allDailies = DailyManager.getDailies()
        val copyOfDailyProgresses = ArrayList(userProfile.dailyProgresses) //needed since the list is updated during loop
        copyOfDailyProgresses.forEach { dailyProgress ->
            allDailies.find { it.name == dailyProgress.daily.name }?.let {
                val progress = dailyChecks[it.name]?.invoke(locatorsOld, locatorsNew)
                if (progress!! > 0)
                    DailyManager.updateDaily(userProfile, it, progress)
            }
        }
    }


    private fun checkAchievement(){

    }


    private fun updateTitleAndLvl(userProfile: UserProfile) {
        val newTitle = allTitles
            .filter { it.xp <= userProfile.currentXP }
            .maxByOrNull { it.xp }
        if (newTitle != null && userProfile.title != newTitle.name) {
            userProfile.title = newTitle.name
            userProfile.level++
        }
    }



    fun updateProgresses(locatorsOld: List<Locator>, locatorsNew: List<Locator>, userProfile: UserProfile) {
        val xmlWriter = XMLWriter()
        checkDailies(userProfile, locatorsOld, locatorsNew)//for each assigned daily, check

        //qui devo ciclare tra achievements ancora attivi
        //userProfile.achievementProgresses.forEach { achievementProgress -> checkAchievement(achievement) }
        //AchievementManager.updateAchievements(userProfile, results)

        updateTitleAndLvl(userProfile)
        xmlWriter.saveUserProfileToXML(path, userProfile)
    }




    //upload user profile data from file if they exist or create a new user profile if they do not
    fun setupUserProfile(name: String): UserProfile {
        val xmlReader = XMLReader()
        val xmlWriter = XMLWriter()
        var userProfile = xmlReader.loadUserProfileFromXML(path, name)
        if(userProfile == null) {
            userProfile = UserProfile(
                name = name,
                level = 1,
                currentXP = 0,
                title = "Newbie Tester",
                achievementProgresses = mutableListOf(),
                dailyProgresses = mutableListOf(),
                completedAchievements = mutableListOf()
            )
            DailyManager.setupDailies(userProfile)
            AchievementManager.setupAchievements(userProfile)
            xmlWriter.addNewUserProfileToXML(path, userProfile)
        }
        return userProfile
    }

}
