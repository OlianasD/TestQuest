package gamification

data class DailyProgress(
    val daily: Daily,
    var progress: Int = 0,
    val discarded: Boolean = false,
    var modifiedLocs: List<String> = emptyList(), //used to store unique locators changed (used for some dailies/achs only)
)

data class AchievementProgress(
    val achievement: Achievement,
    var progress: Int = 0
)

class UserProfile(
    var id: String,
    var name: String,
    var level: Int,
    var currentXP: Int,
    var nextXP: Int,
    var title: String,
    val achievementProgresses: MutableList<AchievementProgress>,
    val dailyProgresses: MutableList<DailyProgress>,
    val completedAchievements: MutableList<Achievement>,
    var propic: String,
    var timestamp: Long = System.currentTimeMillis(),//to keep track of expiring dailies
)

{
    fun assignDailies(dailies: List<Daily>) {
        dailies.forEach { d -> dailyProgresses.add(DailyProgress(d)) }    }





}

