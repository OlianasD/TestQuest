package gamification

import listener.test.TestOutcome

class PageObjectDailyManager {



    companion object {


        private val DAILY_NAMES = listOf(
            "createPO",
            "moveLoc2Method",//add a method to include locs
            "removeUnusedLoc",
            "removeUnusedMethod",
            "removeUnusedPO",
            "removeAssert",//PO should not manage any assertion
            "meaningfulMethodName",//TODO: this may need external resources
            "shortenMethodName",
            "shortenVarName",
            "meaningfulVarName",//TODO: this may need external resources
            "adaptLoc2Format",//loc must have form WebElement e = driver.findElement(By...)
            "addAncestorPO",
            "addPOAsMethodOutput"
        )




        //TODO: change icons, xp, target accordingly
        private val ALL_DAILIES = mutableListOf(
            Daily(
                DAILY_NAMES[0],
                "Create a Page Object",
                DailyManager.RANDOM_DAILY_XP,
                DailyManager.DAILY_GOAL,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            )

        private val DAILY_CHECKS: Map<String, (List<TestOutcome>) -> Int> = mapOf(
            DAILY_NAMES[0] to { testOutcomes -> createPO(testOutcomes) },
        )

        private fun createPO(testOutcomes: List<TestOutcome>): Int {
            return 1
        }


    }


}