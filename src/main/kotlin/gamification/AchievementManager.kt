package gamification

import listener.test.TestOutcome
import extractor.locator.Locator
import analyzer.locator.LocatorsFragilityCalculator
import utils.FilePathSolver
import java.io.File

class AchievementManager {

    companion object {

        //TODO: change icon accordingly
        private val ALL_ACHIEVEMENTS = mutableListOf(
            Achievement(
                "I did it!",
                "Implement your first locator",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
                ),
            Achievement(
                "Kill them all",
                "Transform 100 absolute XPath locators into relative ones",
                100,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "Keep it short",
                "Reduce the length of 100 XPath locators",
                100,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "The higher they are, the louder they fall",
                "Reduce the level of 100 XPath locators",
                100,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "Stillness",
                "Do not change any locator for 24 hours",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "Ops... I made a mistake!",
                "Worsen the quality of a locator",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "I'm starting to learn things",
                "Improve the robustness of 1 locator",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "Perfectionism",
                "Improve locators robustness 100 times",
                100,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "Immortality",
                "Build a locator that survives 100 runs",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "An inside job",
                "Build a locator that fails immediately",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "I feel so lonely",
                "Use a locator type different from any other type currently used",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "The gift of synthesis",
                "Build the shortest locator",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "The bigger the better",
                "Build the longest locator",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "Strength lies in differences",
                "Use every locator types",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "The cake is a lie",
                "Rename a locator as 'cake'",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "The clone war",
                "Use the same locator in 10 different test cases",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "Expand the colony",
                "Add 100 new locators from any test suite",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "You are not welcome anymore",
                "Remove 100 locators from any test suite",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "What's the difference?",
                "Add a locator that has same name, type and value of another one",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "Is this a test?",
                "Execute your first test successfully",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "I joined the Test Marathon",
                "Execute 1000 tests successfully",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "The Beginner's Luck",
                "Run your first working locator",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "That's no luck anymore",
                "Run 1000 working locators",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "First aid",
                "Repair your first locator",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "ER Medical Drama",
            "Repair 1000 locators",
                 1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "I didn't touch it!",
                "Break a locator",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "Flawless Victory",
                "Implement the perfect locator", //FC<=0.1
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "Frankenstein",
                "Implement the worst locator", //FC>=1
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),
            Achievement(
                "Locatorwarts School of Robustness and Testing",
                "Improve the robustness of 1000 different locators",
                1,
                FilePathSolver.ACHIEVEMENT_PICS_PATH,
            ),




            /*Achievement(
                "Kill them all",
                "Implement 100 robust locators",
                100,
                PathConfig.getResourcePath(FilePathSolver.ACHIEVEMENT_PICS_PATH),
            ),
            Achievement(
                "Kill them all",
                "Implement 100 ID locators",
                100,
                PathConfig.getResourcePath(FilePathSolver.ACHIEVEMENT_PICS_PATH),
            ),
            Achievement(
                "Kill them all",
                "Implement 100 relative XPath locators",
                100,
                PathConfig.getResourcePath(FilePathSolver.ACHIEVEMENT_PICS_PATH),
            ),
            Achievement(
                "Is this a locator?",
                "Open the plugin for the first time",
                1,
                PathConfig.getResourcePath(FilePathSolver.ACHIEVEMENT_PICS_PATH),
            ),
            Achievement(
                "I am a bit confused",
                "Change a locator 3 times in 24 hours",
                1,
                PathConfig.getResourcePath(FilePathSolver.ACHIEVEMENT_PICS_PATH),
            ),
            Achievement(
                "What am I doing?",
                "Change a test case 3 times in 24 hours",
                1,
                PathConfig.getResourcePath(FilePathSolver.ACHIEVEMENT_PICS_PATH),
            ),
            Achievement(
                "Domino Effect",
                "Add a locator which makes another locator to break", //non x forza nosuch
                1,
                PathConfig.getResourcePath(FilePathSolver.ACHIEVEMENT_PICS_PATH),
            ),
            Achievement(
                "You are a wizard, Harry",
                "Reach level 5",
                1,
                PathConfig.getResourcePath(FilePathSolver.ACHIEVEMENT_PICS_PATH),
            ),
            Achievement(
                "Look To My Coming, At First Light On The Fifth Day",
                "Reach level 10",
                1,
                PathConfig.getResourcePath(FilePathSolver.ACHIEVEMENT_PICS_PATH),
            ),
            Achievement(
                "Ho there wanderer, stay thy course a while and indulge an old man",
                "Reach level 15",
                1,
                PathConfig.getResourcePath(FilePathSolver.ACHIEVEMENT_PICS_PATH),
            ),
            Achievement(
                "A wizard does not make mistakes",
                "Reach level 20",
                1,
                PathConfig.getResourcePath(FilePathSolver.ACHIEVEMENT_PICS_PATH),
            ),*/

        )







        fun setupAchievements(userProfile: UserProfile) {
            for (ach in ALL_ACHIEVEMENTS) {
                val achProgress = AchievementProgress(ach, 0)
                userProfile.achievementProgresses.add(achProgress)
                currentUser = userProfile
            }
        }

        fun updateUserProfile(userProfile: UserProfile){
            currentUser = userProfile
        }

        private lateinit var currentUser: UserProfile

        private val achievementNameToDescriptionMap = ALL_ACHIEVEMENTS.associate { it.name to it.description }
        private val achievementNameToTargetMap = ALL_ACHIEVEMENTS.associate { it.name to it.target }
        private val achievementNameToIconMap = ALL_ACHIEVEMENTS.associate { it.name to it.icon }


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
            "Ops... I made a mistake!" to ::iMadeAMistake,
            "I'm starting to learn things" to ::learningThings,
            "Perfectionism" to ::perfectionism,
            "Immortality" to ::immortality,
            "An inside job" to ::anInsideJob,
            "I feel so lonely" to ::iFeelSoLonely,
            "The gift of synthesis" to ::theGiftOfSynthesis,
            "The bigger the better" to ::theBiggerTheBetter,
            "Strength lies in differences" to ::strengthLiesInDifferences,
            "The cake is a lie" to ::theCakeIsALie,
            "The clone war" to ::theCloneWar,
            "Expand the colony" to ::expandTheColony,
            "You are not welcome anymore" to ::youAreNotWelcomeAnymore,
            "What's the difference?" to ::whatTheDifference,
            "Is this a test?" to ::isThisATest,
            "I joined the Test Marathon" to ::testMarathon,
            "The Beginner's Luck" to ::beginnerLuck,
            "That's no luck anymore" to ::noMoreLuck,
            "First aid" to ::firstAid,
            "ER Medical Drama" to ::ERMedicalDrama,
            "I didn't touch it!" to ::iDidNotTouchIt,
            "Flawless Victory" to ::flawlessVictory,
            "Frankenstein" to ::frankenstein,
            "Locatorwarts School of Robustness and Testing" to ::locatorwarts
             )









        private fun isThisALocator(testOutcomes: List<TestOutcome>): Int {
            return testOutcomes.size//TODO: only a dummy achievement, to remove
        }

        private fun iDidIt(testOutcomes: List<TestOutcome>): Int {
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val hasNewLocator = locatorsNew.any { newLocator ->
                    locatorsOld.none { oldLocator ->
                        oldLocator.hashCode() == newLocator.hashCode()
                    }
                }
                if (hasNewLocator)
                    return 1
            }
            return 0
        }

        private fun killThemAll(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        oldLocator.locatorValue.startsWith("/html")) {
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                            !newLocator.locatorValue.startsWith("/html")) {
                            count++
                        }
                    }
                }
            }
            return count
        }
        private fun keepItShort(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    val newLocator = newLocatorsMap[oldLocator.hashCode()]
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        newLocator != null &&
                        newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        newLocator.locatorValue.length < oldLocator.locatorValue.length) {
                        count++
                    }
                }
            }
            return count
        }

        private fun theHigherTheyAreTheLouderTheyFall(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    val newLocator = newLocatorsMap[oldLocator.hashCode()]
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        newLocator != null &&
                        newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        newLocator.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size <
                        oldLocator.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size) {
                        count++
                    }
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

        // i.e., relative to absolute xpath, shorter to longer xpath, id to else, lower to higher xpath,
        //       lower to higher fragility score, test from passed to failed because of that new locator
        private fun iMadeAMistake(testOutcomes: List<TestOutcome>): Int {
            val calc = LocatorsFragilityCalculator()
            for (testOutcome in testOutcomes) {
                val locsOld = testOutcome.locatorsOld
                val locsNew = testOutcome.locatorsPassed
                val newLocatorsMap = locsNew.associateBy { it.hashCode() }
                for (oldLocator in locsOld) {
                    val newLocator = newLocatorsMap[oldLocator.hashCode()] ?: continue
                    if (!oldLocator.locatorValue.startsWith("/html") &&
                        newLocator.locatorValue.startsWith("/html")) {
                        return 1
                    }
                    if (oldLocator.locatorType.equals("id", ignoreCase = true) &&
                        !newLocator.locatorType.equals(oldLocator.locatorType, ignoreCase = true)) {
                        return 1
                    }
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        oldLocator.locatorValue.length < newLocator.locatorValue.length) {
                        return 1
                    }
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        oldLocator.locatorValue.split("/").size < newLocator.locatorValue.split("/").size) {
                        return 1
                    }
                    // check if locatorNew has worse fragility coefficient
                    if (calc.calculateFragility(oldLocator) < calc.calculateFragility(newLocator)) {
                        return 1
                    }
                    // check if the test breaks because of that new locator
                    if (!testOutcome.isPassed && testOutcome.locatorBroken != null &&
                        testOutcome.locatorBroken.locatorValue == newLocator.locatorValue
                    )
                        return 1
                }
            }
            return 0
        }

        private fun learningThings(testOutcomes: List<TestOutcome>): Int {
            val calc = LocatorsFragilityCalculator()
            for (testOutcome in testOutcomes) {
                val locsOld = testOutcome.locatorsOld
                val locsNew = testOutcome.locatorsPassed
                val newLocatorsMap = locsNew.associateBy { it.hashCode() }
                for (oldLocator in locsOld) {
                    val newLocator = newLocatorsMap[oldLocator.hashCode()] ?: continue
                    if (calc.calculateFragility(newLocator) < calc.calculateFragility(oldLocator))
                        return 1
                }
            }
            return 0
        }

        private fun perfectionism(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val calc = LocatorsFragilityCalculator()
            for (testOutcome in testOutcomes) {
                val locsOld = testOutcome.locatorsOld
                val locsNew = testOutcome.locatorsPassed
                val newLocatorsMap = locsNew.associateBy { it.hashCode() }
                for (oldLocator in locsOld) {
                    val newLocator = newLocatorsMap[oldLocator.hashCode()] ?: continue
                    if (calc.calculateFragility(newLocator) < calc.calculateFragility(oldLocator))
                        count++
                }
            }
            return count
        }

        private fun immortality(testOutcomes: List<TestOutcome>): Int {
            val counterFileName = "locator_counts${currentUser.id}.txt"//create a counter file for locators if not exists
            val survivingCounterLocatorFile = File(counterFileName)
            if (!survivingCounterLocatorFile.exists())
                survivingCounterLocatorFile.createNewFile()
            val counters = mutableMapOf<Int, Int>()
            survivingCounterLocatorFile.readLines().forEach { line ->
                val parts = line.split(',')
                if (parts.size == 2) {
                    val key = parts[0].toIntOrNull()
                    val count = parts[1].toIntOrNull()
                    if (key != null && count != null) {
                        counters[key] = count
                    }
                }
            }
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val oldLocatorsMap = locatorsOld.associateBy { it.hashCode() }
                for (newLocator in locatorsNew) {
                    val newKey = newLocator.hashCode()
                    if (oldLocatorsMap.containsKey(newKey)) {
                        counters[newKey] = (counters[newKey] ?: 0) + 1
                        if (counters[newKey] == 100) {
                            survivingCounterLocatorFile.delete()
                            return 1
                        }
                    } else {
                        counters[newKey] = 0
                    }
                }
                //remove entries of old locators no more present in new locators
                testOutcome.locatorsOld.forEach { oldLocator ->
                    val oldKey = oldLocator.hashCode()
                    if (!testOutcome.locatorsNew.any { it.hashCode() == oldKey }) {
                        counters.remove(oldKey)
                    }
                }
            }
            survivingCounterLocatorFile.printWriter().use { out ->
                counters.forEach { (key, count) ->
                    out.println("$key,$count")
                }
            }
            return 0
        }

        private fun anInsideJob(testOutcomes: List<TestOutcome>): Int {
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsNew
                if (testOutcome.isPassed)
                    continue
                //if locator is newly added and it is the reason of a test failure
                val oldLocatorsMap = locatorsOld.associateBy { it.hashCode() }
                locatorsNew.forEach { newLocator ->
                    val newLocatorHash = newLocator.hashCode()
                    if (!oldLocatorsMap.containsKey(newLocatorHash)) {
                        if (testOutcome.stacktrace!!.contains("NoSuchElementException") &&
                            testOutcome.stacktrace.contains(newLocator.locatorValue)) {
                            return 1
                        }
                    }
                }
            }
            return 0
        }

        private fun iFeelSoLonely(testOutcomes: List<TestOutcome>): Int {
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsPassed = testOutcome.locatorsPassed
                locatorsPassed.forEach { passedLocator ->
                    val isUniqueType = locatorsOld.none { it.locatorType == passedLocator.locatorType }
                    if (isUniqueType) {
                        return 1
                    }
                }
            }
            return 0
        }

        private fun theGiftOfSynthesis(testOutcomes: List<TestOutcome>): Int {
            for (testOutcome in testOutcomes) {
                val locatorsNew = testOutcome.locatorsPassed
                locatorsNew.forEach { newLocator ->
                    if (newLocator.locatorValue.length == 1) {//TODO: assumption is shortest locator when length == 1
                        return 1
                    }
                }
            }
            return 0
        }

        private fun theBiggerTheBetter(testOutcomes: List<TestOutcome>): Int {
            for (testOutcome in testOutcomes) {
                val locatorsNew = testOutcome.locatorsPassed
                locatorsNew.forEach { newLocator ->
                    if (newLocator.locatorValue.length > 50) {//TODO: assumption is longest locator when length > 50
                        return 1
                    }
                }
            }
            return 0
        }

        private fun strengthLiesInDifferences(testOutcomes: List<TestOutcome>): Int {
            val locTypes = setOf("id", "name", "xpath", "linkText", "css", "className", "tagName")
            val locatorTypesFileName = "locator_types${currentUser.id}.txt"
            val locatorTypesFile = File(locatorTypesFileName)
            val foundTypes = mutableSetOf<String>()
            if (!locatorTypesFile.exists()) {
                locatorTypesFile.createNewFile()
            }
            locatorTypesFile.readLines().forEach { line -> foundTypes.add(line.trim()) }
            for (testOutcome in testOutcomes) {
                val locatorsNew = testOutcome.locatorsPassed
                locatorsNew.forEach { locator -> foundTypes.add(locator.locatorType) }
                locatorTypesFile.printWriter().use { writer ->
                    foundTypes.forEach { type ->
                        writer.println(type)
                    }
                }
                if (locTypes.all { it in foundTypes }) {
                    locatorTypesFile.delete()
                    return 1
                }
            }
            return 0
        }

        private fun theCakeIsALie(testOutcomes: List<TestOutcome>): Int {
            for (testOutcome in testOutcomes) {
                val locatorsNew = testOutcome.locatorsPassed
                locatorsNew.forEach { newLocator ->
                    if (newLocator.locatorName == "cake") {
                        return 1
                    }
                }
            }
            return 0
        }

        private fun theCloneWar(testOutcomes: List<TestOutcome>): Int {
            val locatorCounts = mutableMapOf<Locator, Int>()
            for (testOutcome in testOutcomes) {
                val locatorsNew = testOutcome.locatorsPassed
                locatorsNew.forEach { newLocator ->
                    locatorCounts[newLocator] = locatorCounts.getOrDefault(newLocator, 0) + 1
                    if (locatorCounts[newLocator] == 10)
                        return 1
                }
            }
            return 0
        }

        private fun expandTheColony(testOutcomes: List<TestOutcome>): Int {
            val locatorFileName = "locator_new${currentUser.id}.txt"
            val newLocatorsFile = File(locatorFileName)
            val storedHashes = mutableSetOf<Int>()
            if (!newLocatorsFile.exists()) {
                newLocatorsFile.createNewFile()
            }
            newLocatorsFile.readLines().forEach { line ->
                val hash = line.trim().toIntOrNull()
                if (hash != null) {
                    storedHashes.add(hash)
                }
            }
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val oldLocatorsMap = locatorsOld.associateBy { it.hashCode() }
                //if locator is new, add it to file
                locatorsNew.forEach { newLocator ->
                    val newLocatorHash = newLocator.hashCode()
                    if (!oldLocatorsMap.containsKey(newLocatorHash)) {
                        storedHashes.add(newLocatorHash)
                    }
                }
                //discard locators that were new but are no more present (e.g., added and then removed)
                storedHashes.removeIf { storedHash ->
                    locatorsNew.none { it.hashCode() == storedHash }
                }
                //if 100 new locators are hit, then achievement is done (added as new in even previous step and still present)
                if (storedHashes.size >= 100) {
                    newLocatorsFile.delete()
                    return 1
                }
            }
            //update file with updated list of new locators
            newLocatorsFile.printWriter().use { writer ->
                storedHashes.forEach { hash ->
                    writer.println(hash)
                }
            }
            return 0
        }

        private fun youAreNotWelcomeAnymore(testOutcomes: List<TestOutcome>): Int {
            val removedLocatorsFileName = "removed_locators${currentUser.id}.txt"
            val removedLocatorsFile = File(removedLocatorsFileName)
            val removedHashes = mutableSetOf<Int>()
            if (!removedLocatorsFile.exists()) {
                removedLocatorsFile.createNewFile()
            }
            removedLocatorsFile.readLines().forEach { line ->
                val hash = line.trim().toIntOrNull()
                if (hash != null) {
                    removedHashes.add(hash)
                }
            }
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                locatorsOld.forEach { oldLocator ->
                    if (!locatorsNew.contains(oldLocator)) {
                        val oldLocatorHash = oldLocator.hashCode()
                        removedHashes.add(oldLocatorHash)
                    }
                }
                if (removedHashes.size >= 100) {
                    removedLocatorsFile.delete()
                    return 1
                }
            }
            removedLocatorsFile.printWriter().use { writer ->
                removedHashes.forEach { hash ->
                    writer.println(hash)
                }
            }
            return 0
        }

        private fun whatTheDifference(testOutcomes: List<TestOutcome>): Int {
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val oldLocatorsHashes = locatorsOld.map { it.hashCode() }.toSet()
                for (newLocator in locatorsNew) {
                    val newLocatorHash = newLocator.hashCode()
                    //new locator must not be present in old locators
                    if (!oldLocatorsHashes.contains(newLocatorHash)) {
                        //new locator must have same name, value, and type of another existing locator but different hash
                        if (locatorsNew.any {
                                it.hashCode() != newLocatorHash && it.locatorType == newLocator.locatorType &&
                                it.locatorValue == newLocator.locatorValue && it.locatorName == newLocator.locatorName
                            })
                            return 1
                    }
                }
            }
            return 0
        }

        private fun isThisATest(testOutcomes: List<TestOutcome>): Int {
            return if (testOutcomes.any { it.isPassed }) 1 else 0
        }

        private fun testMarathon(testOutcomes: List<TestOutcome>): Int {
            val passedTestsFileName = "passed_tests${currentUser.id}.txt"
            val passedTestsFile = File(passedTestsFileName)
            if (!passedTestsFile.exists()) {
                passedTestsFile.createNewFile()
            }
            val storedHashes = mutableListOf<Int>()
            passedTestsFile.readLines().forEach { line ->
                val hash = line.trim().toIntOrNull()
                if (hash != null) {
                    storedHashes.add(hash)
                }
            }
            testOutcomes.filter { it.isPassed }.forEach { passedTest ->
                storedHashes.add(passedTest.hashCode())
            }
            if (storedHashes.size >= 1000) {
                passedTestsFile.delete()
                return 1
            }
            passedTestsFile.printWriter().use { writer ->
                storedHashes.forEach { hash ->
                    writer.println(hash)
                }
            }
            return 0
        }

        private fun beginnerLuck(testOutcomes: List<TestOutcome>): Int {
            testOutcomes.firstOrNull { it.isPassed && it.locatorsNew.isNotEmpty()}?.let {
                return 1
            }
            return 0
        }

        private fun noMoreLuck(testOutcomes: List<TestOutcome>): Int {
            val executedLocs = "executed_locs${currentUser.id}.txt"
            val executedLocsFile = File(executedLocs)
            if (!executedLocsFile.exists())
                executedLocsFile.createNewFile()
            val storedHashes = mutableListOf<Int>()
            executedLocsFile.readLines().forEach { line ->
                val hash = line.trim().toIntOrNull()
                if (hash != null) {
                    storedHashes.add(hash)
                }
            }
            //count number of executed locs included in passed tests
            testOutcomes.filter { it.isPassed }.forEach { passedTest ->
                passedTest.locatorsNew.forEach { locator ->
                    storedHashes.add(locator.hashCode())
                }
            }
            if (storedHashes.size >= 1000) {
                executedLocsFile.delete()
                return 1
            }
            executedLocsFile.printWriter().use { writer ->
                storedHashes.forEach { hash ->
                    writer.println(hash)
                }
            }
            return 0
        }

        private fun firstAid(testOutcomes: List<TestOutcome>): Int {
            val brokenLocsFileName = "broken_locs${currentUser.id}.txt"
            val brokenLocsFile = File(brokenLocsFileName)
            if (!brokenLocsFile.exists())
                brokenLocsFile.createNewFile()
            val storedHashes = mutableListOf<Int>()
            brokenLocsFile.readLines().forEach { line ->
                val hash = line.trim().toIntOrNull()
                if (hash != null) {
                    storedHashes.add(hash)
                }
            }
            for (testOutcome in testOutcomes) {
                val locatorsNewHashes = testOutcome.locatorsNew.map { it.hashCode() }
                //add broken locators to file (i.e., locs contained in stacktrace NoSuchElementException)
                if (!testOutcome.isPassed && testOutcome.stacktrace?.trim()?.
                                            contains("NoSuchElementException") == true)
                    testOutcome.locatorsNew.filter {testOutcome.stacktrace.trim().
                        contains(it.locatorValue.trim()) }.forEach { storedHashes.add(it.hashCode()) }
                else {
                    //retrieve locs that are now passed and remove file of previously saved ones
                    val repairedLocators = locatorsNewHashes.filter { storedHashes.contains(it) }
                    if (repairedLocators.isNotEmpty()) {
                        brokenLocsFile.delete()
                        return 1
                    }
                }
            }
            //update files with broken locs
            brokenLocsFile.printWriter().use { writer ->
                storedHashes.forEach { hash ->
                    writer.println(hash)
                }
            }
            return 0
        }

        private fun ERMedicalDrama(testOutcomes: List<TestOutcome>): Int {
            val brokenLocsFileName = "broken_locs_${currentUser.id}.txt"
            val repairedLocsFileName = "repaired_locs_${currentUser.id}.txt"
            val brokenLocsFile = File(brokenLocsFileName)
            val repairedLocsFile = File(repairedLocsFileName)
            if (!brokenLocsFile.exists()) brokenLocsFile.createNewFile()
            if (!repairedLocsFile.exists()) repairedLocsFile.createNewFile()
            val brokenLocators = mutableSetOf<Int>()
            val repairedLocators = mutableSetOf<Int>()
            brokenLocsFile.readLines().forEach { line ->
                val hash = line.trim().toIntOrNull()
                if (hash != null) {
                    brokenLocators.add(hash)
                }
            }
            repairedLocsFile.readLines().forEach { line ->
                val hash = line.trim().toIntOrNull()
                if (hash != null) {
                    repairedLocators.add(hash)
                }
            }
            for (testOutcome in testOutcomes) {
                if (!testOutcome.isPassed && testOutcome.stacktrace?.trim()?.contains("NoSuchElementException") == true) {
                    testOutcome.locatorsNew.filter { testOutcome.stacktrace.trim().contains(it.locatorValue.trim()) }
                        .forEach {
                            brokenLocators.add(it.hashCode())
                        }
                }
                if (testOutcome.isPassed) {
                    testOutcome.locatorsNew.filter { brokenLocators.contains(it.hashCode()) }
                        .forEach {
                            repairedLocators.add(it.hashCode())
                            brokenLocators.remove(it.hashCode())
                        }
                }
            }
            if (repairedLocators.size >= 1000) {
                brokenLocsFile.delete()
                repairedLocsFile.delete()
                return 1
            }
            brokenLocsFile.printWriter().use { writer ->
                brokenLocators.forEach { hash ->
                    writer.println(hash)
                }
            }
            repairedLocsFile.printWriter().use { writer ->
                repairedLocators.forEach { hash ->
                    writer.println(hash)
                }
            }
            return 0
        }

        private fun iDidNotTouchIt(testOutcomes: List<TestOutcome>): Int {
            val passedLocsFileName = "passed_locs${currentUser.id}.txt"
            val passedLocsFile = File(passedLocsFileName)
            if (!passedLocsFile.exists()) passedLocsFile.createNewFile()
            val storedHashes = mutableListOf<Int>()
            passedLocsFile.readLines().forEach { line ->
                val hash = line.trim().toIntOrNull()
                if (hash != null) {
                    storedHashes.add(hash)
                }
            }
            for (testOutcome in testOutcomes) {
                if (!testOutcome.isPassed && testOutcome.stacktrace?.trim()?.contains("NoSuchElementException") == true) {
                    val failedLocators = testOutcome.locatorsNew.filter { testOutcome.stacktrace.trim().contains(it.locatorValue.trim()) }
                    //if a failed locator originally was valid, return 1
                    if (failedLocators.any { storedHashes.contains(it.hashCode()) }) {
                        passedLocsFile.delete()
                        return 1
                    }
                }
            }
            return 0
        }

        private fun flawlessVictory(testOutcomes: List<TestOutcome>): Int {
            val calc = LocatorsFragilityCalculator()
            for (testOutcome in testOutcomes) {
                for (locator in testOutcome.locatorsPassed) {
                    val fragilityScore = calc.calculateFragility(locator)
                    if (fragilityScore <= 0.1)
                        return 1
                }
            }
            return 0
        }

        private fun frankenstein(testOutcomes: List<TestOutcome>): Int {
            val calc = LocatorsFragilityCalculator()
            for (testOutcome in testOutcomes) {
                for (locator in testOutcome.locatorsPassed) {
                    val fragilityScore = calc.calculateFragility(locator)
                    if (fragilityScore >= 1)
                        return 1
                }
            }
            return 0
        }

        private fun locatorwarts(testOutcomes: List<TestOutcome>): Int {
            val calc = LocatorsFragilityCalculator()
            val improvedLocatorsFileName = "improved_locators${currentUser.id}.txt"
            val improvedLocatorsFile = File(improvedLocatorsFileName)
            if (!improvedLocatorsFile.exists()) {
                improvedLocatorsFile.createNewFile()
            }
            val savedHashes = mutableSetOf<Int>()
            improvedLocatorsFile.readLines().forEach { line ->
                val hash = line.trim().toIntOrNull()
                if (hash != null) {
                    savedHashes.add(hash)
                }
            }
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    val newLocator = newLocatorsMap[oldLocator.hashCode()]
                    if(newLocator!=null && calc.calculateFragility(newLocator) < calc.calculateFragility(oldLocator))
                        savedHashes.add(newLocator.hashCode())
                }
            }
            if (savedHashes.size >= 1000) {
                improvedLocatorsFile.delete()
                improvedLocatorsFile.delete()
                return 1
            }
            improvedLocatorsFile.printWriter().use { writer ->
                savedHashes.forEach { hash ->
                    writer.println(hash)
                }
            }
            return 0
        }









        fun updateAchievementProgresses(userProfile: UserProfile, testOutcomes: List<TestOutcome>): List<Achievement?> {
            val progresses = mutableListOf<Achievement?>() //to track the progresses (i.e., achievement completed)
            val copyOfAchievementProgresses = ArrayList(userProfile.achievementProgresses) //needed since the list is updated during loop
            copyOfAchievementProgresses.forEach { ap ->
                try {
                    val progress = achievementChecks[ap.achievement.name]?.invoke(testOutcomes)
                    if (progress != null && progress > 0) {
                        val achievementProgress = update(userProfile, ap.achievement, progress)
                        achievementProgress?.let { progresses.add(it) } //TODO:  add a more sophisticated check/return type
                                                                        // if a more sophisticated notification must be provided
                    }
                } catch (e: Exception) {
                    println("Exception occurred while processing achievement: ${ap.achievement.name}")
                    e.printStackTrace()
                }


            }
            return progresses //this to keep track of any changes and update the GUI
        }

        private fun update(userProfile: UserProfile, achievement: Achievement, progress: Int): Achievement? {
            var involvedAchievement: Achievement? = null //to track the involved achievement

            val achievementProgress = userProfile.achievementProgresses.find { it.achievement.name == achievement.name }
            achievementProgress?.let { ap ->
                ap.progress += progress
                if (ap.progress >= ap.achievement.target) { // if the achievement has been completed, remove it from list
                    userProfile.completedAchievements.add(ap.achievement)
                    userProfile.achievementProgresses.removeIf { it.achievement.name == achievement.name }
                    involvedAchievement = ap.achievement
                }
            }
            return involvedAchievement
        }









    }


}