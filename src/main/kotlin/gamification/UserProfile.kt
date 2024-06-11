package gamification

data class DailyProgress(
    val daily: Daily,
    var progress: Int = 0
)

data class QuestProgress(
    val quest: Quest,
    var progress: Int = 0
)

data class AchievementProgress(
    val achievement: Achievement,
    var progress: Int = 0
)

//TODO: da rendere persistente
class UserProfile(
    var name: String,
    var level: Int,
    var currentXP: Int,
    var title: String,
    val achievementProgresses: MutableList<AchievementProgress>,
    val dailyProgresses: MutableList<DailyProgress>,
    val questProgresses: MutableList<QuestProgress>,
    val achievements: MutableList<Achievement> //achieved achievements
)

{

    fun assignDailies(dailies: List<Daily>) {
        dailies.forEach { d -> dailyProgresses.add(DailyProgress(d)) }    }

    fun assignQuests(quests: List<Quest>) {
        quests.forEach { q -> questProgresses.add(QuestProgress(q)) }
    }

}

