package gamification

import locator.Locator

class AchievementManager {

    companion object {

        //TODO: change icon accordingly
        private val allAchievements = mutableListOf(
            Achievement("Is this a locator?", "Just a dummy achievement", 1, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png" ),
            Achievement("I did it!", "Build your first locator", 1, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"),
            Achievement("Kill them all", "Transform 100 absolute XPath locators into relative ones", 100, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"),
            Achievement("Keep it short", "Reduce the length of 100 XPath locators", 100, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"),
            Achievement("The higher they are, the louder they fall", "Reduce the height of 100 XPath locators", 100, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"),
            Achievement("Stillness", "Do not change any locator for 48 hours", 999, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"),
            Achievement("Master of Changes", "Change every locator at least once", 999, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"),
            Achievement("Ops... I made a mistake!", "Change a locator into something worse", 999, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"), //e.g., relative to absolute, longer xpath, id to else, higher xpath
            Achievement("Perfectionism", "Improve the robustness of every locator at least once", 999, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"),
            Achievement("Immortality", "Build the perfect locator", 999, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"), //i.e., never changed
            Achievement("An inside job", "Build a locator that fails immediately", 999, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"),
            Achievement("I feel so lonely", "Build a locator whose type is different from any other", 999, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"),
            Achievement("I can read this", "Use a locator name value which is meaningful", 999, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"),
            Achievement("The gift of synthesis", "Build the shortest locator", 999, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"),
            Achievement("The bigger the better", "Build the longest locator", 999, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"),
            Achievement("Strength lies in differences", "Use every locator types", 999, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"),
            Achievement("For we are many", "Use every locator types within the same test suite", 999, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"),
            Achievement("The cake is a lie", "Use a locator named 'cake'", 999, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"),
            Achievement("The clone war", "Use the same locator 50 times", 999, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"),
            Achievement("Expand the colony", "Add 100 new locators", 999, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"),
            Achievement("You are not welcome anymore", "Remove 100 locators", 999, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"),
            Achievement("I've seen you before", "Execute a locator without breakages 100 times", 999, "C:\\Users\\User\\Desktop\\demo\\pics\\achievement\\default-achievement.png"),
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