package gamification

import locator.Locator

class AchievementManager {

    companion object {

        private val allAchievements = mutableListOf(
            Achievement("Is this a locator?", "Just a dummy achievement", 1),
            Achievement("I did it!", "Build your first locator", 1),
            Achievement("Kill them all", "Fix 100 absolute XPath locators", 100),
            Achievement("Keep it short", "Reduce the length of 100 XPath locators", 100),
            Achievement("The higher they are, the louder they fall", "Reduce the height of 100 XPath locators", 100),
            Achievement("Stillness", "Do not change any locator for 48 hours", 999),
            Achievement("Master of Changes", "Change every locator at least once", 999),
            Achievement("I made a mistake", "Change a locator into something worse", 999), //e.g., relative to absolute, longer xpath, id to else, higher xpath
            Achievement("Perfectionism", "Improve every locator at least once", 999), //TODO: wrt which metrics?
            Achievement("Immortality", "Build the perfect locator", 999), //i.e., never changed
            Achievement("An inside job", "Build a locator that fails immediately", 999),
            Achievement("I feel so lonely", "Build a locator whose type is different from any other", 999),
            Achievement("I can read this", "Use a locator name value which is meaningful", 999),
            Achievement("The gift of synthesis", "Build the shortest locator", 999),
            Achievement("The bigger the better", "Build the longest locator", 999),
            Achievement("Strength lies in differences", "Use every locator types", 999),
            Achievement("For we are many", "Use every locator types within the same test suite", 999),
            Achievement("The cake is a lie", "Use a locator named 'cake'", 999),
            Achievement("The clone war", "Use the same locator 50 times", 999),
        )

        fun setupAchievements(userProfile: UserProfile) {
            for (ach in allAchievements) {
                val achProgress = AchievementProgress(ach, 0)
                userProfile.achievementProgresses.add(achProgress)
            }
        }





        private fun isThisALocator(locsOld: List<Locator>, locsNew: List<Locator>): Int {
            return 0
        }

        private fun iDidIt(locsOld: List<Locator>, locsNew: List<Locator>): Int {
            // Implement the actual logic here
            return 0
        }

        private fun killThemAll(locsOld: List<Locator>, locsNew: List<Locator>): Int {
            // Implement the actual logic here
            return 0
        }

        private fun keepItShort(locsOld: List<Locator>, locsNew: List<Locator>): Int {
            // Implement the actual logic here
            return 0
        }

        private fun theHigherTheyAreTheLouderTheyFall(locsOld: List<Locator>, locsNew: List<Locator>): Int {
            // Implement the actual logic here
            return 0
        }

        private fun stillness(locsOld: List<Locator>, locsNew: List<Locator>): Int {
            // Implement the actual logic here
            return 0
        }

        private fun masterOfChanges(locsOld: List<Locator>, locsNew: List<Locator>): Int {
            // Implement the actual logic here
            return 0
        }

        private fun iMadeAMistake(locsOld: List<Locator>, locsNew: List<Locator>): Int {
            // Implement the actual logic here
            return 0
        }

        private fun perfectionism(locsOld: List<Locator>, locsNew: List<Locator>): Int {
            // Implement the actual logic here
            return 0
        }

        private fun immortality(locsOld: List<Locator>, locsNew: List<Locator>): Int {
            // Implement the actual logic here
            return 0
        }

        private fun anInsideJob(locsOld: List<Locator>, locsNew: List<Locator>): Int {
            // Implement the actual logic here
            return 0
        }

        private fun iFeelSoLonely(locsOld: List<Locator>, locsNew: List<Locator>): Int {
            // Implement the actual logic here
            return 0
        }

        private fun iCanReadThis(locsOld: List<Locator>, locsNew: List<Locator>): Int {
            // Implement the actual logic here
            return 0
        }

        private fun theGiftOfSynthesis(locsOld: List<Locator>, locsNew: List<Locator>): Int {
            // Implement the actual logic here
            return 0
        }

        private fun theBiggerTheBetter(locsOld: List<Locator>, locsNew: List<Locator>): Int {
            // Implement the actual logic here
            return 0
        }

        private fun strengthLiesInDifferences(locsOld: List<Locator>, locsNew: List<Locator>): Int {
            // Implement the actual logic here
            return 0
        }

        private fun forWeAreMany(locsOld: List<Locator>, locsNew: List<Locator>): Int {
            // Implement the actual logic here
            return 0
        }

        private fun theCakeIsALie(locsOld: List<Locator>, locsNew: List<Locator>): Int {
            // Implement the actual logic here
            return 0
        }

        private fun theCloneWar(locsOld: List<Locator>, locsNew: List<Locator>): Int {
            // Implement the actual logic here
            return 0
        }


        private val achievementChecks: Map<String, (List<Locator>, List<Locator>) -> Int> = mapOf(
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
            "For we are many" to ::forWeAreMany,
            "The cake is a lie" to ::theCakeIsALie,
            "The clone war" to ::theCloneWar
        )

        fun updateAchievements(userProfile: UserProfile, locatorsOld: List<Locator>, locatorsNew: List<Locator>): Boolean {
            var anyUpdate = false
            val copyOfAchievementProgresses = ArrayList(userProfile.achievementProgresses) //needed since the list is updated during loop
            copyOfAchievementProgresses.forEach { ap ->
                val progress = achievementChecks[ap.achievement.name]?.invoke(locatorsOld, locatorsNew)
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