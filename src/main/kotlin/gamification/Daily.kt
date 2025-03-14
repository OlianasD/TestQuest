package gamification

import extractor.locator.Locator

class Daily(
    val name: String,
    val description: String,
    val xp: Int,
    val target: Int?,
    val icon: String,
    var type: String,//it can either be 'random', 'targeted', or 'inclusive'
    var targetedLocators: List<Locator> = emptyList(),//it can be either null or with list or target locators to manage
    var issuesInPOs: List<Any>? = emptyList(),//it can be either null or with list or target issues in POs (e.g., empty PO)
    val isAdvanced: Boolean = false, //if false the daily is about locators mode only. if yes, it is about the
                                        //advanced mode (i.e., PO, test, ...)
    val additionalDescription: String = "" //it is used to provide additional details for the daily (what is the daily purpose, what is the reward, ...)
)
