package gamification

import listener.TestOutcome
import locator.Locator
import java.io.File

class AchievementManager {

    companion object {

        //TODO: change icon accordingly
        private val allAchievements = mutableListOf(
            Achievement(
                "Is this a locator?",
                "Just a dummy achievement",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "I did it!",
                "Implement your first locator",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "Kill them all",
                "Transform 100 absolute XPath locators into relative ones",
                100,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "Keep it short",
                "Reduce the length of 100 XPath locators",
                100,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "The higher they are, the louder they fall",
                "Reduce the level of 100 XPath locators",
                100,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "Stillness",
                "Do not change any locator for 24 hours",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "Master of Changes",
                "Change every locator at least once",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "Ops... I made a mistake!",
                "Change a locator into something worse",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "Perfectionism",
                "Improve the robustness of every locator at least once",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "Immortality",
                "Build the immortal locator (a locator that survives 100 runs)",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "An inside job",
                "Build a locator that fails immediately",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "I feel so lonely",
                "Build a locator whose type is different from any other",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "I can read this",
                "Use a locator name value which is meaningful",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "The gift of synthesis",
                "Build the shortest locator",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),

            Achievement(
                "The bigger the better",
                "Build the longest locator",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "Strength lies in differences",
                "Use every locator types",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "The cake is a lie",
                "Use a locator named 'cake'",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "The clone war",
                "Use the same locator in 10 different test cases",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "Expand the colony",
                "Add 100 new locators",
                100,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "You are not welcome anymore",
                "Remove 100 locators",
                100,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "What's the difference?",
                "Add a locator that is a copy of another one",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            )







            ,
            Achievement(
                "Is this a test?",
                "Execute your first test",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "I joined the Test Marathon",
                "Execute 1000 tests",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "The Beginner's Luck",
                "Run your first working locator",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "There's no luck anymore",
                "Run 1000 working locators",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "First aid",
                "Repair your first locator",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "ER Medical Drama",
            "Repair 1000 locators",
                 1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "I didn't touch it!",
                "Break a locator",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "My dog ate it",
                "Break a test",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "Flawless Victory",
                "Implement the perfect locator", //FC=0
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "Locatorwarts School of Robustness and Testing",
                "Improve the robustness of 1000 locators",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                "I am a bit confused",
                "Change a locator 3 times in 24 hours",
                1,
                "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            ),
            Achievement(
                    "What am I doing?",
                    "Change a test case 3 times in 24 hours",
                    1,
                    "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"
            )
        )





        fun setupAchievements(userProfile: UserProfile) {
            for (ach in allAchievements) {
                val achProgress = AchievementProgress(ach, 0)
                userProfile.achievementProgresses.add(achProgress)
            }
        }


        private val achievementNameToDescriptionMap = allAchievements.associate { it.name to it.description }
        private val achievementNameToTargetMap = allAchievements.associate { it.name to it.target }
        private val achievementNameToIconMap = allAchievements.associate { it.name to it.icon }


        fun getAchievementIconFromName(name: String): String {
            return achievementNameToIconMap[name] ?: ""
        }

        fun getDescriptionFromName(name: String): String {
            return achievementNameToDescriptionMap[name] ?: ""
        }

        fun getTargetFromName(name: String): Int {
            return achievementNameToTargetMap[name] ?: 0
        }


        private val achievementChecks: Map<String, (List<TestOutcome>) -> Int> = mapOf(
            "Is this a locator?" to ::isThisALocator,
            "I did it!" to ::iDidIt,
            "Kill them all" to ::killThemAll,
            "Keep it short" to ::keepItShort,
            "The higher they are, the louder they fall" to ::theHigherTheyAreTheLouderTheyFall,
            "Stillness" to ::stillness,
            "Master of Changes" to ::masterOfChanges,
            "I made a mistake" to ::iMadeAMistake,
            "Perfectionism" to ::perfectionism,
            "Immortality" to ::immortality,
            "An inside job" to ::anInsideJob,
            "I feel so lonely" to ::iFeelSoLonely,
            "I can read this" to ::iCanReadThis,
            "The gift of synthesis" to ::theGiftOfSynthesis,
            "The bigger the better" to ::theBiggerTheBetter,
            "Strength lies in differences" to ::strengthLiesInDifferences,
            "The cake is a lie" to ::theCakeIsALie,
            "The clone war" to ::theCloneWar,
            "Expand the colony" to ::expandTheColony,
            "You are not welcome anymore" to ::youAreNotWelcomeAnymore,
            "What's the difference?" to ::whatTheDifference,
        )







        private fun isThisALocator(testOutcomes: List<TestOutcome>): Int {
            return testOutcomes.size//TODO: only a dummy achievement, to remove
        }

        private fun iDidIt(testOutcomes: List<TestOutcome>): Int {
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                if(!testOutcome.isPassed || locatorsOld.size == locatorsNew.size || locatorsOld.isNotEmpty())
                    continue
                return 1
            }
            return 0
        }

        private fun killThemAll(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                if(!testOutcome.isPassed || locatorsOld.size != locatorsNew.size)
                    continue
                count += (locatorsOld.count { it.locatorValue.startsWith("/html") } -
                        locatorsNew.count { it.locatorValue.startsWith("/html") })
            }
            return count
        }

        private fun keepItShort(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                if(!testOutcome.isPassed || locatorsOld.size != locatorsNew.size)
                    continue
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

        private fun theHigherTheyAreTheLouderTheyFall(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                if(!testOutcome.isPassed || locatorsOld.size != locatorsNew.size)
                    continue
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

        private fun stillness(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locsOld = testOutcome.locatorsOld
                val locsNew = testOutcome.locatorsNew
                // TODO
            }
            return count
        }

        private fun masterOfChanges(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locsOld = testOutcome.locatorsOld
                val locsNew = testOutcome.locatorsNew
                // TODO
            }
            return count
        }


        // e.g., relative to absolute, longer xpath, id to else, higher xpath
        private fun iMadeAMistake(testOutcomes: List<TestOutcome>): Int {
            for (testOutcome in testOutcomes) {
                val locsOld = testOutcome.locatorsOld
                val locsNew = testOutcome.locatorsNew
                for (i in locsOld.indices) {
                    val oldLocator = locsOld[i]
                    val newLocator = locsNew[i]
                    // check if locatorNew is absolute
                    if (!oldLocator.locatorValue.startsWith("/html") && newLocator.locatorValue.startsWith("/html")) {
                        return 1
                    }
                    // check if locatorNew is no more id, xpath, or linktext
                    if ((oldLocator.locatorType.equals("xpath", ignoreCase = true) ||
                                oldLocator.locatorType.equals("id", ignoreCase = true) ||
                                oldLocator.locatorType.equals("linktext", ignoreCase = true)) &&
                        !newLocator.locatorType.equals(oldLocator.locatorType, ignoreCase = true)) {
                        return 1
                    }
                    // check if locatorNew is longer
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        oldLocator.locatorValue.length < newLocator.locatorValue.length) {
                        return 1
                    }
                    // check if locatorNew has more levels
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        oldLocator.locatorValue.split("/").size < newLocator.locatorValue.split("/").size) {
                        return 1
                    }
                    // check if locatorNew has worse fragility coefficient
                    if (computeFragilityCoefficient(oldLocator) <
                        computeFragilityCoefficient(newLocator)) {
                        return 1
                    }
                    // check if the test breaks because of that locator
                    if (!testOutcome.isPassed && testOutcome.stacktrace!!.contains("NoSuchElementException")) {
                        val regex = """"selector":"(.*?)"""".toRegex()
                        val matchResult = regex.find(testOutcome.stacktrace)
                        if (matchResult != null) {
                            if(matchResult.groupValues[1].contains(newLocator.locatorValue))
                                return 1
                        }
                    }
                }
            }
            return 0
        }

        private fun perfectionism(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locsOld = testOutcome.locatorsOld
                val locsNew = testOutcome.locatorsNew
                // TODO
            }
            return count
        }

        private fun immortality(testOutcomes: List<TestOutcome>): Int {
            val counterFile = "locator_counts.txt"
            val file = File(counterFile)
            val counters = mutableMapOf<String, Int>()
            if (file.exists()) {
                file.readLines().forEach { line ->
                    val parts = line.split(',')
                    if (parts.size == 2) {
                        counters[parts[0]] = parts[1].toIntOrNull() ?: 0
                    }
                }
            }
            var updated = false
            for (testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                //check locators new that are unchanged and whose counter of unchange hits 100, updating counter file
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                locatorsNew.forEach { newLocator ->
                    val newKey = "${newLocator.locatorType}:${newLocator.locatorValue}"
                    if (locatorsOld.any { oldLocator ->
                            oldLocator.locatorType == newLocator.locatorType &&
                                    oldLocator.locatorValue == newLocator.locatorValue
                        }) {
                        counters[newKey] = (counters[newKey] ?: 0) + 1
                        if (counters[newKey] == 100) {
                            updated = true
                        }
                    } else {
                        counters[newKey] = 0
                    }
                }
                locatorsOld.forEach { oldLocator ->
                    val oldKey = "${oldLocator.locatorType}:${oldLocator.locatorValue}"
                    if (locatorsNew.none { newLocator ->
                            newLocator.locatorType == oldLocator.locatorType &&
                                    newLocator.locatorValue == oldLocator.locatorValue
                        }) {
                        counters.remove(oldKey)
                    }
                }
            }
            file.printWriter().use { out ->
                counters.forEach { (key, count) ->
                    out.println("$key,$count")
                }
            }
            return if (updated) 1 else 0
        }

        private fun anInsideJob(testOutcomes: List<TestOutcome>): Int {
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                //if locator is newly added and it is the reason of a test failure
                if(testOutcome.isPassed)
                    continue
                locatorsNew.forEach { newLocator ->
                    if (locatorsOld.none { oldLocator ->
                            oldLocator.locatorType == newLocator.locatorType &&
                                    oldLocator.locatorValue == newLocator.locatorValue
                        }) {
                        if (testOutcome.stacktrace!!.contains("NoSuchElementException")) {
                            val regex = """"selector":"(.*?)"""".toRegex()
                            val matchResult = regex.find(testOutcome.stacktrace)
                            if (matchResult != null) {
                                if(matchResult.groupValues[1].contains(newLocator.locatorValue))
                                    return 1
                            }
                        }
                    }
                }
            }
            return 0
        }

        private fun iFeelSoLonely(testOutcomes: List<TestOutcome>): Int {
            for (testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                val locatorsNew = testOutcome.locatorsNew
                locatorsNew.forEach { newLocator ->
                    if (locatorsNew.count { it.locatorType == newLocator.locatorType } == 1) {
                        return 1
                    }
                }
            }
            return 0
        }

        private fun iCanReadThis(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locsOld = testOutcome.locatorsOld
                val locsNew = testOutcome.locatorsNew
                // TODO
            }
            return count
        }

        private fun theGiftOfSynthesis(testOutcomes: List<TestOutcome>): Int {
            for (testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                val locatorsNew = testOutcome.locatorsNew
                locatorsNew.forEach { newLocator ->
                    if (newLocator.locatorValue.length == 1) {//assumption: shortest locator when length == 1
                        return 1
                    }
                }
            }
            return 0
        }

        private fun theBiggerTheBetter(testOutcomes: List<TestOutcome>): Int {
            for (testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                val locatorsNew = testOutcome.locatorsNew
                locatorsNew.forEach { newLocator ->
                    if (newLocator.locatorValue.length > 50) {//assumption: longest locator when length > 50
                        return 1
                    }
                }
            }
            return 0
        }

        private fun strengthLiesInDifferences(testOutcomes: List<TestOutcome>): Int {
            val locTypes = setOf("id", "name", "xpath", "linkText", "css", "className", "tagName")
            for (testOutcome in testOutcomes) {
                if (!testOutcome.isPassed)
                    continue
                val locatorsNew = testOutcome.locatorsNew
                val foundTypes = locatorsNew.map { it.locatorType }.toSet()
                if (locTypes.all { it in foundTypes }) {
                    return 1
                }
            }
            return 0
        }

        private fun theCakeIsALie(testOutcomes: List<TestOutcome>): Int {
            for (testOutcome in testOutcomes) {
                if (!testOutcome.isPassed)
                    continue
                val locatorsNew = testOutcome.locatorsNew
                locatorsNew.forEach { newLocator ->
                    if (newLocator.locatorValue == "cake") {
                        return 1
                    }
                }
            }
            return 0
        }

        private fun theCloneWar(testOutcomes: List<TestOutcome>): Int {
            val locatorCounts = mutableMapOf<Locator, Int>()
            for (testOutcome in testOutcomes) {
                if (!testOutcome.isPassed)
                    continue
                val locatorsNew = testOutcome.locatorsNew
                locatorsNew.forEach { newLocator ->
                    locatorCounts[newLocator] = locatorCounts.getOrDefault(newLocator, 0) + 1
                    if (locatorCounts[newLocator] == 10)
                        return 1
                }
            }
            return 0
        }

        private fun expandTheColony(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                if (!testOutcome.isPassed)
                    continue
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                locatorsNew.forEach { newLocator ->
                    if (!locatorsOld.contains(newLocator))
                        count++
                }
            }
            return count
        }

        private fun youAreNotWelcomeAnymore(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                if (!testOutcome.isPassed)
                    continue
                val locatorsNew = testOutcome.locatorsNew
                val locatorsOld = testOutcome.locatorsOld
                locatorsOld.forEach { oldLocator ->
                    if (!locatorsNew.contains(oldLocator))
                        count++
                }
            }
            return count
        }

        private fun whatTheDifference(testOutcomes: List<TestOutcome>): Int {
            for (testOutcome in testOutcomes) {
                if (!testOutcome.isPassed)
                    continue
                val locatorsNew = testOutcome.locatorsNew
                val locatorsOld = testOutcome.locatorsOld
                locatorsNew.forEach { newLocator ->
                    if (!locatorsOld.contains(newLocator) && locatorsNew.contains(newLocator))
                        return 1
                }
            }
            return 0
        }







        fun computeFragilityCoefficient(loc: Locator): Int{
            return 0//TODO:implement coefficient
        }





















        fun updateAchievements(userProfile: UserProfile, testOutcomes: List<TestOutcome>): Boolean {
            var anyUpdate = false
            val copyOfAchievementProgresses = ArrayList(userProfile.achievementProgresses) //needed since the list is updated during loop
            copyOfAchievementProgresses.forEach { ap ->
                val progress = achievementChecks[ap.achievement.name]?.invoke(testOutcomes)
                if (progress!! > 0) {
                    update(userProfile, ap.achievement, progress)
                    anyUpdate = true
                }
            }
            return anyUpdate
        }

        private fun update(userProfile: UserProfile, achievement: Achievement, progress: Int) {
            val achievementProgress = userProfile.achievementProgresses.find { it.achievement.name == achievement.name }
            achievementProgress?.let { ap ->
                ap.progress += progress
                if (ap.progress >= ap.achievement.target) { // if the achievement has been completed, remove it from list
                    userProfile.completedAchievements.add(ap.achievement)
                    userProfile.achievementProgresses.removeIf { it.achievement.name == achievement.name }
                }
            }
        }









    }


}