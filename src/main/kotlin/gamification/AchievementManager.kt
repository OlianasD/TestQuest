package gamification

class AchievementManager {

    companion object {

        private val allAchievements = mutableListOf(
            Achievement("Is this a locator?", "Just a dummy achievement", 1),
            Achievement("Absolute XPath Destroyer", "Fix 100 absolute XPath locators", 100),
            Achievement("Length Reducer", "Reduce the length of 100 XPath locators", 100),
            Achievement("Height Reducer", "Reduce the height of 100 XPath locators", 100),
            Achievement("Stillness", "Do not change any locator for 48 hours", 999),
            Achievement("Master of Changes", "Change every locator at least once", 999),
            Achievement("I made a mistake", "Change a locator into something worse", 999), //e.g., relative to absolute, longer xpath, id to else, higher xpath
            Achievement("The perfectionist", "Improve every locator at least once", 999), //TODO: wrt which metrics?
            Achievement("Immortality", "Build the perfect locator", 999), //i.e., never changed
            Achievement("An inside job", "Build a locator that fails immediately", 999),
            Achievement("The lonely", "Build a locator whose type is different from any other", 999),
            Achievement("I can read this", "Use a locator name value which is meaningful", 999),
            Achievement("The gift of synthesis", "Build the shortest locator", 999),
            Achievement("The bigger the better", "Build the longest locator", 999),
            Achievement("Strength lies in differences", "Use every locator types", 999),
            Achievement("The cake is a lie", "Use a locator named 'cake'", 999),
            Achievement("The clone war", "Use the same locator 10 times", 999),
            )

        fun setupAchievements(userProfile: UserProfile) {
            for (ach in allAchievements) {
                val achProgress = AchievementProgress(ach, 0)
                userProfile.achievementProgresses.add(achProgress)
            }
        }

        fun getAchievements(): MutableList<Achievement> {
            return allAchievements
        }

        fun updateAchievements(userProfile: UserProfile, results: Map<String, Int>){
            /*val achProgress = userProfile.achievementProgresses.find { it.achievement.name == ach.name }
            achProgress?.let {
                achProgress.progress += progress
                if (achProgress.progress >= ach.target){
                    addAchievement(userProfile, ach)
                }
            }*/
            results.forEach { (achName, progress) ->
                println("Updating achievement: $achName with progress: $progress")
                if(progress > 0) {
                    val achProgress = userProfile.achievementProgresses.find { it.achievement.name == achName }
                    achProgress?.let { ap ->
                        ap.progress += progress
                        if (ap.progress >= ap.achievement.target) {
                            userProfile.completedAchievements.add(ap.achievement)                        }
                    }
                }
            }
        }




    }


}