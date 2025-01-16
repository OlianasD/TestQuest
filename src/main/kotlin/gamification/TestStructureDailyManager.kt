package gamification

import listener.test.TestOutcome

class TestStructureDailyManager {

    companion object {


        private val DAILY_NAMES = listOf(
            "runtc",
            "runts",
            "addAssert2Test",
            "shortenTestWorkFlow",//test workflow must be simple
            "makeTestWorkFlowIndependent", //test workflow must be independent from any other test
            "adaptTestName2Format",//test name must have 3 sections: what is being tested, circumnstances, exp result
            "meaningfulMethodName",//TODO: this may need external resources
            "shortenMethodName",
            "shortenVarName",
            "meaningfulVarName",//TODO: this may need external resources
            "addSetup",
            "addTearDown",
            "removeGlobVar",
            "removeExpSleep",
            "setDriverForEachTest",
            "giveMeaningfulMethodName",//TODO: this may need external resources
            "shortenMethodName",
            "shortenVarName",
            "meaningfulVarName",//TODO: this may need external resources
        )



        //TODO: change icons, xp, target accordingly
        private val ALL_DAILIES = mutableListOf(
            Daily(
                DAILY_NAMES[0],
                "Run ${DailyManager.DAILY_GOAL} test cases successfully",
                DailyManager.RANDOM_DAILY_XP,
                DailyManager.DAILY_GOAL,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                DAILY_NAMES[1],
                "Run a test suite successfully",
                DailyManager.RANDOM_DAILY_XP,
                DailyManager.DAILY_GOAL,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                DAILY_NAMES[3],
                "Add an assert to a test case",
                DailyManager.RANDOM_DAILY_XP,
                DailyManager.DAILY_GOAL,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            //TODO: this checks if test has not too many interactions/locs and too many assertions
            Daily(
                DAILY_NAMES[3],
                "Refactor a long test into two tests",
                DailyManager.RANDOM_DAILY_XP,
                DailyManager.DAILY_GOAL,
                "C:\\Users\\User\\Desktop\\demo\\pics\\daily\\default-daily.png",
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),


        )




        private val DAILY_CHECKS: Map<String, (List<TestOutcome>) -> Int> = mapOf(
            DAILY_NAMES[0] to { testOutcomes -> checkRunTC(testOutcomes) },
            DAILY_NAMES[1] to { testOutcomes -> checkRunTS(testOutcomes) },
        )


        private fun checkRunTC(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                if(!testOutcome.isPassed)
                    continue
                count++
            }
            return count
        }

        private fun checkRunTS(testOutcomes: List<TestOutcome>): Int {
            for(testOutcome in testOutcomes){
                if(!testOutcome.isPassed)
                    return 0
            }
            return 1
        }













    }


}