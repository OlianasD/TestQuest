package gamification

import locator.Locator

class Daily(
    val name: String,
    val description: String,
    val xp: Int,
    val target: Int?,
    val icon: String,
    var type: String,//it can either be 'random', 'targeted', or 'inclusive'
    var targetedLocators: List<Locator> = emptyList()//it can be either null or with list or target locators to manage
)
{
}