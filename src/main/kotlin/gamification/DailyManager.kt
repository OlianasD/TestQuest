package gamification

import listener.test.TestOutcome
import extractor.locator.Locator
import locator.LocatorsAnalyzer
import locator.LocatorsFragilityCalculator
import testquest.TestQuestAction
import ui.GUIManager
import utils.FilePathSolver

class DailyManager {

    companion object {

        const val DAILY_GOAL: Int = 3 //TODO: to convert into a map (each daily may have specific requests)
        const val RANDOM_DAILY_XP: Int = 100 //TODO: to convert into a map (each daily may provide specific XP)
        private const val TARGETED_DAILY_XP: Int = 25 //xp for each point fixed from targeted TODO
        private const val DAILIES_PER_USER: Int = 5


        /**********************************RANDOM DAILIES**********************************/

        //TODO: refactor this class by organizing dailies as LocatorsDailyManager, PODailyManager, TestDailyManager
        private val RANDOM_DAILY_NAMES = listOf(
            /************ DAILIES ABOUT LOCATORS ************/
            "xpathAbs",
            "xpathLength",
            "xpathLevel",
            "loc2xpath",
            "loc2id",
            "runLocs20",
            "robustness",
            "lengthShortenMax",
            "addAttrToXPath",
            "remAttrFromXPath",
            "remJSFromXPath",
            "edit5",
            "levelLoweredMax",
            "repair",
            "reducePredicates",
            "newXPath",
            "newID",
            "newSameLoc",
            "newLengthShorterMax",
            "newLevelLowerMax",
            "newRobust",
            "newWantedAttr",
            "newUnwantedAttr",
            "newJS",
            "newLowPredicates",
            /************ DAILIES ABOUT POS ************/
            "addPO", //create a PO within test suite (i.e., basically just a class named _Page is ok)
            "addMethodToPO", //add a method to a PO (i.e., basically just an empty method is ok)
            "addLocsToMethod", //add locs to a method
            "moveLocs2Method", //move locs from test to a method
            "returnPOInMethod", //add a PO as return type for a method
            "moveAssertsToTest", //move asserts from method to test
            "adaptLocs2Format", //adapt locs to have format: WebElement e = driver.findElement(By...)
            "interactWithLocsInMethod", //add Selenium instructions in method to interact with locators
            "addAncestorPO", //add an 'extend' between POs
            "moveCommonMethodToAncestorPO", //move a method shared among POs to an ancestor
            "instantiatePO", //instantiate a PO within a test
            "callMethod", // call a method from a PO within a test
            "callUnusedMethod", //call an unused method from a PO within a test
        )


        //TODO: change icons, xp, target accordingly
        private val ALL_RANDOM_DAILIES = mutableListOf(
            /************ DAILIES ABOUT LOCATORS ************/
            Daily(
                RANDOM_DAILY_NAMES[0],
                "Replace $DAILY_GOAL existing absolute XPath locators with $DAILY_GOAL relative ones",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[1],
                "Reduce the length of $DAILY_GOAL existing XPath locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[2],
                "Reduce the level of $DAILY_GOAL existing XPath locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[3],
                "Convert $DAILY_GOAL existing non-XPath locators to $DAILY_GOAL XPath ones",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[4],
                "Convert $DAILY_GOAL existing non-ID locators to $DAILY_GOAL ID ones",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[5],
                "Run 20 locators successfully",
                RANDOM_DAILY_XP,
                20,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[6],
                "Improve the robustness of $DAILY_GOAL existing locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[7],
                "Shorten the length of $DAILY_GOAL existing XPath locators below " + GamificationManager.MAX_LENGTH + " characters",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[8],
                "Add $DAILY_GOAL references to robust attributes to existing XPaths locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[9],
                "Remove $DAILY_GOAL references to fragile attributes from existing XPaths locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[10],
                "Remove $DAILY_GOAL references to Javascript code from existing XPaths locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[11],
                "Modify 5 different existing locators",
                RANDOM_DAILY_XP,
                5,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[12],
                "Lower the level of $DAILY_GOAL existing locators below " + GamificationManager.MAX_LEVEL + " tags",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[13],
                "Repair $DAILY_GOAL existing broken locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[14],
                "Reduce the number of predicates from $DAILY_GOAL existing locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[15],
                "Implement $DAILY_GOAL new XPath locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[16],
                "Implement $DAILY_GOAL new ID locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[17],
                "Implement a newly locator value and use it more than once in a test suite",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[18],
                "Implement $DAILY_GOAL new XPath locators with length below " + GamificationManager.MAX_LENGTH+ " characters",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[19],
                "Implement $DAILY_GOAL new XPath locators with level below " + GamificationManager.MAX_LEVEL+ " tags",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[20],
                "Implement $DAILY_GOAL new robust locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[21],
                "Implement $DAILY_GOAL new XPath locators with references to robust attributes",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[22],
                "Implement $DAILY_GOAL new XPath locators with no references to fragile attributes",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[23],
                "Implement $DAILY_GOAL new XPath locators with no references to Javascript code",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[24],
                "Implement $DAILY_GOAL new XPath locators with " + GamificationManager.MAX_POS_PRED + " or less positional predicates",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            /************ DAILIES ABOUT POS ************/
            Daily(
                RANDOM_DAILY_NAMES[25],
                "Add a new PageObject to the test suite",
                RANDOM_DAILY_XP,
                1,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[26],
                "Add a new method to a PageObject",
                RANDOM_DAILY_XP,
                1,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[27],
                "Add new $DAILY_GOAL locators to any PageObject method",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[28],
                "Move $DAILY_GOAL locators from tests to any PageObject method",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[29],
                "Define a PageObject as return type for any PageObject method that does not",
                RANDOM_DAILY_XP,
                1,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[30],
                "Move $DAILY_GOAL asserts existing in any PageObject method to a test",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[31],
                "Adapts $DAILY_GOAL locators retrieval from any PageObject method to the canonical form",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[32],
                "Add $DAILY_GOAL interactions with locators in any PageObject method",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[32],
                "Assign an ancestor PageObject to any PageObject",
                RANDOM_DAILY_XP,
                1,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[33],
                "Move a duplicated method from multiple PageObjects to a common ancestor",
                RANDOM_DAILY_XP,
                1,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[34],
                "Instantiate a PageObject within any test",
                RANDOM_DAILY_XP,
                1,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[35],
                "Call a PageObject method from any test",
                RANDOM_DAILY_XP,
                1,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                RANDOM_DAILY_NAMES[36],
                "Call an unused PageObject method from any test",
                RANDOM_DAILY_XP,
                1,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
        )







        private val DAILY_NAME_TO_DESCRIPTION = ALL_RANDOM_DAILIES.associate { it.name to it.description }
        private val DAILY_NAME_TO_TARGET = ALL_RANDOM_DAILIES.associate { it.name to it.target }
        private val DAILY_NAME_TO_ICON = ALL_RANDOM_DAILIES.associate { it.name to it.icon }
        private val DAILY_NAME_TO_XP = ALL_RANDOM_DAILIES.associate { it.name to it.xp }

        private val RANDOM_DAILY_CHECKS: Map<String, (List<TestOutcome>) -> Int> = mapOf(
            RANDOM_DAILY_NAMES[0] to { testOutcomes -> checkAbsXPathRemoved(testOutcomes) },
            RANDOM_DAILY_NAMES[1] to { testOutcomes -> checkXPathLengthReduced(testOutcomes) },
            RANDOM_DAILY_NAMES[2] to { testOutcomes -> checkXPathLevelReduced(testOutcomes) },
            RANDOM_DAILY_NAMES[3] to { testOutcomes -> checkLocs2XPathConverted(testOutcomes) },
            RANDOM_DAILY_NAMES[4] to { testOutcomes -> checkLocs2IDConverted(testOutcomes) },
            RANDOM_DAILY_NAMES[5] to { testOutcomes -> checkRunLoc20(testOutcomes) },
            RANDOM_DAILY_NAMES[6] to { testOutcomes -> checkRobustnessImprovement(testOutcomes) },
            RANDOM_DAILY_NAMES[7] to { testOutcomes -> checkShortenedLengthMax(testOutcomes) },
            RANDOM_DAILY_NAMES[8] to { testOutcomes -> checkWantedAttrsInXPaths(testOutcomes) },
            RANDOM_DAILY_NAMES[9] to { testOutcomes -> checkUnwantedAttrsInXPaths(testOutcomes) },
            RANDOM_DAILY_NAMES[10] to { testOutcomes -> checkJSInXPaths(testOutcomes) },
            RANDOM_DAILY_NAMES[11] to { testOutcomes -> checkChangedLocs5(testOutcomes) },
            RANDOM_DAILY_NAMES[12] to { testOutcomes -> checkLoweredLevelMax(testOutcomes) },
            RANDOM_DAILY_NAMES[13] to { testOutcomes -> checkRepair(testOutcomes) },
            RANDOM_DAILY_NAMES[14] to { testOutcomes -> reducePredicates(testOutcomes) },
            RANDOM_DAILY_NAMES[15] to { testOutcomes -> checkNewXPath(testOutcomes) },
            RANDOM_DAILY_NAMES[16] to { testOutcomes -> checkNewID(testOutcomes) },
            RANDOM_DAILY_NAMES[17] to { testOutcomes -> checkNewMultipleUseLoc(testOutcomes) },
            RANDOM_DAILY_NAMES[18] to { testOutcomes -> checkNewXPathLengthMax(testOutcomes) },
            RANDOM_DAILY_NAMES[19] to { testOutcomes -> checkNewXPathLevelMax(testOutcomes) },
            RANDOM_DAILY_NAMES[20] to { testOutcomes -> checkNewRobust(testOutcomes) },
            RANDOM_DAILY_NAMES[21] to { testOutcomes -> checkNewXPathWithWantedAttrs(testOutcomes) },
            RANDOM_DAILY_NAMES[22] to { testOutcomes -> checkNewXPathWithoutUnwantedAttrs(testOutcomes) },
            RANDOM_DAILY_NAMES[23] to { testOutcomes -> checkNewXPathWithoutJS(testOutcomes) },
            RANDOM_DAILY_NAMES[24] to { testOutcomes -> checkNewXPathWithFewPosPredicates(testOutcomes) },
            RANDOM_DAILY_NAMES[25] to { testOutcomes -> checkNewPO(testOutcomes) },
            RANDOM_DAILY_NAMES[26] to { testOutcomes -> checkNewPOMethod(testOutcomes) },
            RANDOM_DAILY_NAMES[27] to { testOutcomes -> checkAddedLocsToPOMethod(testOutcomes) },
            RANDOM_DAILY_NAMES[28] to { testOutcomes -> checkReturnedPOInPOMethod(testOutcomes) },
            RANDOM_DAILY_NAMES[29] to { testOutcomes -> checkMovedAssertsFromPOMethod(testOutcomes) },
            RANDOM_DAILY_NAMES[30] to { testOutcomes -> checkAdaptedLocsFormatInPOMethod(testOutcomes) },
            RANDOM_DAILY_NAMES[31] to { testOutcomes -> checkInteractionsWithLocsInPOMethod(testOutcomes) },
            RANDOM_DAILY_NAMES[32] to { testOutcomes -> checkNewAncestorPO(testOutcomes) },
            RANDOM_DAILY_NAMES[33] to { testOutcomes -> checkMovedCommonMethodToAncestoPO(testOutcomes) },
            RANDOM_DAILY_NAMES[34] to { testOutcomes -> checkInstantiationPO(testOutcomes) },
            RANDOM_DAILY_NAMES[35] to { testOutcomes -> checkCalledPOMethod(testOutcomes) },
            RANDOM_DAILY_NAMES[36] to { testOutcomes -> checkCalledUnusedPOMethod(testOutcomes) },
            )










        /**********************************TARGETED DAILIES**********************************/

        private val TARGETED_DAILY_NAMES = listOf(
            "absolute", "length", "level", "posPredicate", "badPredicate", "noIDOrXPath", "broken"
        )

        private val ALL_TARGETED_DAILIES = mutableListOf(
            Daily(
                TARGETED_DAILY_NAMES[0],
                "Turns the following absolute XPath locators into relative ones",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
            Daily(
                TARGETED_DAILY_NAMES[1],
                "Reduce the length of the following XPath locators",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
            Daily(
                TARGETED_DAILY_NAMES[2],
                "Reduce the levels of the following XPath locators",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
            Daily(
                TARGETED_DAILY_NAMES[3],
                "Remove/Replace the positional predicates from the following XPath locators",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
            Daily(
                TARGETED_DAILY_NAMES[4],
                "Remove/Replace the weak predicates from the following XPath locators",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
            Daily(
                TARGETED_DAILY_NAMES[5],
                "Replace the following non-ID/XPath locators into ID/XPath ones",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
            Daily(
                TARGETED_DAILY_NAMES[6],
                "Repair the following broken locators",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
        )

        private val TARGETED_DAILY_CHECKS: Map<String, (List<TestOutcome>) -> Int> = mapOf(
            TARGETED_DAILY_NAMES[0] to { testOutcomes -> checkTargetedAbsoluteRemoved(testOutcomes) },
            TARGETED_DAILY_NAMES[1] to { testOutcomes -> checkTargetedLengthReduced(testOutcomes) },
            TARGETED_DAILY_NAMES[2] to { testOutcomes -> checkTargetedLevelReduced(testOutcomes) },
            TARGETED_DAILY_NAMES[3] to { testOutcomes -> checkTargetedPosPredicateRemoved(testOutcomes) },
            TARGETED_DAILY_NAMES[4] to { testOutcomes -> checkTargetedBadPredicateRemoved(testOutcomes) },
            TARGETED_DAILY_NAMES[5] to { testOutcomes -> checkTargetedNoIDNoXpathChanged(testOutcomes) },
            TARGETED_DAILY_NAMES[6] to { testOutcomes -> checkTargetedBrokenLocsRepaired(testOutcomes) }
        )





        //dailies are assigned to user. initially, the usage mode is RANDOM so random dailies are assigned for setup
        fun setupRandomDailies(userProfile: UserProfile) {
            val dailies: List<Daily> = ALL_RANDOM_DAILIES.shuffled().take(DAILIES_PER_USER)
            userProfile.assignDailies(dailies)
        }

        fun assignTargetedDailies(userProfile: UserProfile) {
            val locAnalyzer = LocatorsAnalyzer()
            val analysisMap = locAnalyzer.findTargetedIssuedLocators()
            val currentTargetDailies = ALL_TARGETED_DAILIES.map { daily ->
                val locatorsForDaily = analysisMap[daily.name] ?: emptyList()
                Daily(
                    name = daily.name,
                    description = daily.description,
                    xp = daily.xp,
                    target = daily.target,
                    icon = daily.icon,
                    type = daily.type,
                    targetedLocators = locatorsForDaily
                )
            }
            //remove all old targeted dailies as they will be updated by new check
            userProfile.dailyProgresses.removeAll { dailyProgress ->
                dailyProgress.daily.type.equals("targeted", ignoreCase = true)
            }
            userProfile.assignDailies(currentTargetDailies)
            GUIManager.updateGUI(userProfile, false)
        }

        fun reassignRandomDailiesFromExpire(userProfile: UserProfile){
            userProfile.dailyProgresses.removeAll { dailyProgress ->
                dailyProgress.daily.type.equals("random", ignoreCase = true)
            }
            userProfile.timestamp = System.currentTimeMillis()//this to assign a new expiration time for new dailies
            setupRandomDailies(userProfile)//note that even the same expired dailies could be reassigned
            GUIManager.updateGUI(userProfile, false)
            GamificationManager.updateUserProfile(userProfile)
        }

        fun reassignRandomDailyFromDiscard(userProfile: UserProfile, daily: Daily): DailyProgress {
            //find all dailies but the ones already assigned to user
            val availableDailies = ALL_RANDOM_DAILIES.filter { d ->
                userProfile.dailyProgresses.none { dailyProgress -> dailyProgress.daily.name == d.name }
            }
            //a new daily is randomly selected with discarded set to true (only 1 discard within 24h is possible)
            val newDaily: Daily = availableDailies.shuffled().first()
            val newDailyProgress = DailyProgress(
                newDaily,
                progress = 0,
                discarded = true
            )
            userProfile.dailyProgresses.add(newDailyProgress) //add new daily
            userProfile.dailyProgresses.removeIf { it.daily == daily } //remove discarded daily
            GamificationManager.updateUserProfile(userProfile) //update user profile
            return newDailyProgress
        }

        fun getIconFromName(name: String): String {
            return DAILY_NAME_TO_ICON[name] ?: ""
        }

        fun getDescriptionFromName(name: String): String {
            return DAILY_NAME_TO_DESCRIPTION[name] ?: ""
        }

        fun getTargetFromName(name: String): Int {
            return DAILY_NAME_TO_TARGET[name] ?: 0
        }

        fun getXPFromName(name: String): Int {
            return DAILY_NAME_TO_XP[name] ?: 0
        }

        fun updateDailyProgresses(userProfile: UserProfile, testOutcomes: List<TestOutcome>): List<Pair<Int, Daily?>> {
            val progresses = mutableListOf<Pair<Int, Daily?>>() //to track the progresses (i.e., xp gained and associated daily)

            //find the dailies to update according to gaming mode
            val dailiesToUpdate = when (GamificationManager.mode) {
                GamificationManager.DailyAssignmentMode.RANDOM -> ALL_RANDOM_DAILIES
                GamificationManager.DailyAssignmentMode.TARGETED -> ALL_TARGETED_DAILIES
                else -> emptyList() //TODO
            }
            //check the daily progress according to gaming mode
            val copyOfDailyProgresses = ArrayList(userProfile.dailyProgresses) //needed since the list is updated during loop
            copyOfDailyProgresses.forEach { dp ->
                dailiesToUpdate.find { it.name == dp.daily.name }?.let {
                    val progress = when (GamificationManager.mode) {
                        GamificationManager.DailyAssignmentMode.RANDOM -> RANDOM_DAILY_CHECKS[it.name]?.invoke(testOutcomes)
                        GamificationManager.DailyAssignmentMode.TARGETED -> TARGETED_DAILY_CHECKS[it.name]?.invoke(testOutcomes)
                        GamificationManager.DailyAssignmentMode.INCLUSIVE -> TODO()
                    }
                    if (progress!! > 0) {
                        val dailyProgress = update(userProfile, it, progress)
                        progresses.add(dailyProgress)  //TODO:  add a more sophisticated check/return type
                                                       // if a more sophisticated notification must be provided
                    }
                }
            }
            return progresses //this to keep track of any changes and update the GUI
        }

        private fun update(userProfile: UserProfile, daily: Daily, progress: Int): Pair<Int, Daily?> {
            //TODO: check user profile wrt xml
            var gainedXP = 0 //to track the gained xp
            var involvedDaily: Daily? = null //to track the involved daily

            if(GamificationManager.mode == GamificationManager.DailyAssignmentMode.RANDOM) {
                val dailyProgress = userProfile.dailyProgresses.find { it.daily.name == daily.name }
                dailyProgress?.let { dp ->
                    dp.progress += progress
                    if (dp.progress >= dp.daily.target!!) { // if the daily has been completed, assign xp and remove it from list
                        gainedXP = dp.daily.xp
                        involvedDaily = dp.daily
                        userProfile.currentXP += gainedXP
                        userProfile.dailyProgresses.removeIf { it.daily.name == dp.daily.name }
                    }
                }
            }
            //since targeted dailies might involve a large number of locators, the xp is given for each (counted by progress)
            else if (GamificationManager.mode == GamificationManager.DailyAssignmentMode.TARGETED) {
                gainedXP = daily.xp * progress

                //TODO: remove the following just for testing
                //gainedXP = 50000000


                involvedDaily = daily
                userProfile.currentXP += gainedXP
            }
            //TODO: INCLUSIVE
            return Pair(gainedXP, involvedDaily)
        }















        /******* TARGETED DAILY CHECKS ABOUT LOCATORS *******/

        private fun checkTargetedAbsoluteRemoved(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locatorsNewMap = testOutcome.locatorsPassed.associateBy { it.hashCode() }
                for (oldLocator in testOutcome.locatorsOld) {
                    val newLocator = locatorsNewMap[oldLocator.hashCode()]
                    if (newLocator != null &&
                        newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        !newLocator.locatorValue.startsWith("/html") &&
                        oldLocator.locatorValue.startsWith("/html")) {
                        count++
                    }
                }
            }
            return count
        }

        private fun checkTargetedLengthReduced(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locatorsNewMap = testOutcome.locatorsPassed.associateBy { it.hashCode() }
                for (oldLocator in testOutcome.locatorsOld) {
                    val newLocator = locatorsNewMap[oldLocator.hashCode()]
                    if (newLocator != null &&
                        newLocator.locatorValue.length <= GamificationManager.MAX_LENGTH &&
                        oldLocator.locatorValue.length > GamificationManager.MAX_LENGTH) {
                        count++
                    }
                }
            }
            return count
        }

        private fun checkTargetedLevelReduced(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locatorsNewMap = testOutcome.locatorsPassed.associateBy { it.hashCode() }
                for (oldLocator in testOutcome.locatorsOld) {
                    val newLocator = locatorsNewMap[oldLocator.hashCode()]
                    val oldLevels = oldLocator.locatorValue.split("/").count { it.isNotEmpty() }
                    val newLevels = newLocator?.locatorValue?.split("/")?.count { it.isNotEmpty() } ?: 0
                    if (newLocator != null &&
                        newLevels <= GamificationManager.MAX_LEVEL &&
                        oldLevels > GamificationManager.MAX_LEVEL) {
                        count++
                    }
                }
            }
            return count
        }

        private fun checkTargetedPosPredicateRemoved(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val predicateRegex = Regex("\\[\\d+]")
            for (testOutcome in testOutcomes) {
                val locatorsNewMap = testOutcome.locatorsPassed.associateBy { it.hashCode() }
                for (oldLocator in testOutcome.locatorsOld) {
                    val newLocator = locatorsNewMap[oldLocator.hashCode()]
                    val oldPredicateCount = predicateRegex.findAll(oldLocator.locatorValue).count()
                    val newPredicateCount = newLocator?.locatorValue?.let { predicateRegex.findAll(it).count() } ?: 0
                    if (newLocator != null &&
                        newPredicateCount <= GamificationManager.MAX_POS_PRED &&
                        oldPredicateCount > GamificationManager.MAX_POS_PRED) {
                        count++
                    }
                }
            }
            return count
        }

        private fun checkTargetedBadPredicateRemoved(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val problematicAttributes = GamificationManager.BAD_PREDS + GamificationManager.BAD_JS
            for (testOutcome in testOutcomes) {
                val locatorsNewMap = testOutcome.locatorsPassed.associateBy { it.hashCode() }
                for (oldLocator in testOutcome.locatorsOld) {
                    val newLocator = locatorsNewMap[oldLocator.hashCode()]
                    val hadBadPredicate = problematicAttributes.any { attr ->
                        oldLocator.locatorValue.contains("@$attr", ignoreCase = true)
                    }
                    val isResolved = newLocator != null && problematicAttributes.none { attr ->
                        newLocator.locatorValue.contains("@$attr", ignoreCase = true)
                    }
                    if (hadBadPredicate && isResolved) {
                        count++
                    }
                }
            }
            return count
        }

        private fun checkTargetedNoIDNoXpathChanged(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locatorsNewMap = testOutcome.locatorsPassed.associateBy { it.hashCode() }
                for (oldLocator in testOutcome.locatorsOld) {
                    val newLocator = locatorsNewMap[oldLocator.hashCode()]
                    val wasNonIDOrXpath = !oldLocator.locatorType.equals("id", ignoreCase = true) &&
                            !oldLocator.locatorType.equals("xpath", ignoreCase = true)
                    val isNowIDOrXpath = newLocator != null &&
                            (newLocator.locatorType.equals("id", ignoreCase = true) ||
                                    newLocator.locatorType.equals("xpath", ignoreCase = true))
                    if (wasNonIDOrXpath && isNowIDOrXpath) {
                        count++
                    }
                }
            }
            return count
        }

        private fun checkTargetedBrokenLocsRepaired(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val locsAnalizer = LocatorsAnalyzer()
            val repairedLocators = mutableListOf<Locator>()
            //for each test, count previously broken loc that is no more present in stacktrace of the previously broken test
            for (testOutcome in testOutcomes) {
                val loc = testOutcome.locatorBroken
                if (loc != null) {
                    if(loc.methodName == testOutcome.testName && loc.className == testOutcome.className)
                        if(testOutcome.stacktrace != null){
                            val stackTrace = testOutcome.stacktrace.trim()
                            val locValue = loc.locatorValue.trim()
                            if(!stackTrace.contains(locValue)) {
                                count++
                                repairedLocators.add(loc) //loc repaired as the test did fail but not for that loc
                            }
                        }
                        else
                            repairedLocators.add(loc) //loc repaired as the test did not fail
                }
            }
            locsAnalizer.updateBrokenLocs(repairedLocators)
            return count
        }





        /******* RANDOM DAILY CHECKS ABOUT LOCATORS *******/

        private fun checkAbsXPathRemoved(testOutcomes: List<TestOutcome>): Int {
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
                            !newLocator.locatorValue.startsWith("/html"))
                            count++
                    }
                }
            }
            return count
        }


        private fun checkXPathLengthReduced(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true)){
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorType.equals("xpath", ignoreCase = true)
                            && newLocator.locatorValue.length < oldLocator.locatorValue.length)
                            count++
                    }
                }
            }
            return count
        }

        private fun checkXPathLevelReduced(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true)){
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorType.equals("xpath", ignoreCase = true)
                            && (newLocator.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size <
                            oldLocator.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size))
                            count++
                    }
                }
            }
            return count
        }

        private fun checkLocs2XPathConverted(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (!oldLocator.locatorType.equals("xpath", ignoreCase = true)) {
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorType.equals("xpath", ignoreCase = true))
                            count++
                    }
                }
            }
            return count
        }

        private fun checkLocs2IDConverted(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (!oldLocator.locatorType.equals("id", ignoreCase = true)) {
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorType.equals("id", ignoreCase = true))
                            count++
                    }
                }
            }
            return count
        }

        private fun checkRobustnessImprovement(testOutcomes: List<TestOutcome>): Int{
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    val newLocator = newLocatorsMap[oldLocator.hashCode()]
                    if (newLocator != null && computeFragilityCoefficient(newLocator) <
                        computeFragilityCoefficient(oldLocator))
                        count++
                }
            }
            return count
        }

        private fun checkShortenedLengthMax(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (oldLocator.locatorValue.length > GamificationManager.MAX_LENGTH){
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorValue.length <= GamificationManager.MAX_LENGTH)
                            count++
                    }
                }
            }
            return count
        }

        private fun checkWantedAttrsInXPaths(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true)){
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorType.equals("xpath", ignoreCase = true))
                            count += countWantedAttributes(newLocator.locatorValue) -
                                    countWantedAttributes(oldLocator.locatorValue)
                    }
                }
            }
            return count
        }

        private fun checkUnwantedAttrsInXPaths(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true)){
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorType.equals("xpath", ignoreCase = true))
                            count += countUnwantedAttributes(oldLocator.locatorValue) -
                                    countUnwantedAttributes(newLocator.locatorValue)
                    }
                }
            }
            return count
        }

        private fun checkJSInXPaths(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true)){
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorType.equals("xpath", ignoreCase = true))
                            count += countJavaScriptReferences(oldLocator.locatorValue) -
                                    countJavaScriptReferences(newLocator.locatorValue)
                    }
                }
            }
            return count
        }

        private fun checkChangedLocs5(testOutcomes: List<TestOutcome>): Int {
            val dailyProgress = GamificationManager.userProfile.dailyProgresses
                .find { it.daily.name == "edit5" }
            val existingModifiedLocs = dailyProgress!!.modifiedLocs.toMutableSet()
            val oldSize = existingModifiedLocs.size
            for (testOutcome in testOutcomes) {
                //it checks if locators have changed values and are different from those that were already changed
                val locatorsOldMap = testOutcome.locatorsOld.associateBy { it.locatorName }
                testOutcome.locatorsPassed.forEach { locatorNew ->
                    val locatorOld = locatorsOldMap[locatorNew.locatorName]
                    if (locatorOld != null && locatorNew.locatorValue != locatorOld.locatorValue)
                        existingModifiedLocs.add(locatorNew.locatorName.toString())
                }
            }
            dailyProgress.modifiedLocs = existingModifiedLocs.toList()
            return existingModifiedLocs.size - oldSize //it returns the number of newly changed locs
        }

        private fun checkLoweredLevelMax(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true) &&
                        oldLocator.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size > GamificationManager.MAX_LEVEL){
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorType.equals("xpath", ignoreCase = true)
                            && newLocator.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size <= GamificationManager.MAX_LEVEL)
                            count++
                    }
                }
            }
            return count
        }

        //TODO: this method is old and its implementation must be adapted to testOutcome.locatorsPassed
        private val stackTraceMap = mutableMapOf<String, String>()
        private fun checkRepair(testOutcomes: List<TestOutcome>): Int {
            var repairs = 0
            for (testOutcome in testOutcomes) {
                val testName = testOutcome.testName
                //if no stacktrace exists, it is the first run of this test
                if (!stackTraceMap.containsKey(testName)) {
                    //if test failed, save stacktrace
                    if (!testOutcome.isPassed)
                        stackTraceMap[testName] = testOutcome.stacktrace.orEmpty()
                    //else, skip as test never failed
                    else
                        continue
                }
                //if stacktrace exists, so the test failed in the past
                else {
                    //if stacktrace is the same as old, skip as nothing has changed
                    if (testOutcome.hasSameStacktrace(stackTraceMap[testName]!!))
                        continue
                    //else, retrieve locators that were broken in previous stacktrace
                    else {
                        //find old locators that were broken in previous stacktrace
                        val brokenLocators = testOutcome.locatorsOld.filter { loc ->
                            stackTraceMap[testName]!!.trim().contains(loc.locatorValue.trim())
                        }
                        //if now test has passed, all broken locators count as repaired
                        if(testOutcome.isPassed) {
                            repairs += brokenLocators.size
                            //remove old stacktrace
                            stackTraceMap.remove(testName)
                        }
                        //else, count only repaired ones
                        else {
                            //find new locators same as old but whose values are no more present in current stacktrace
                            val repairedLocators = testOutcome.locatorsNew.filter { loc ->
                                loc.locatorName in brokenLocators.map { it.locatorName } &&
                                        !testOutcome.stacktrace?.trim()?.contains(loc.locatorValue.trim())!!
                            }
                            repairs += repairedLocators.size
                            //update stacktrace
                            stackTraceMap[testName] = testOutcome.stacktrace.toString()
                        }
                    }
                }
            }
            return repairs
        }

        private fun reducePredicates(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val newLocatorsMap = locatorsNew.associateBy { it.hashCode() }
                for (oldLocator in locatorsOld) {
                    if (oldLocator.locatorType.equals("xpath", ignoreCase = true)) {
                        val newLocator = newLocatorsMap[oldLocator.hashCode()]
                        if (newLocator != null && newLocator.locatorType.equals("xpath", ignoreCase = true)) {
                            val oldPredicatesCount = oldLocator.locatorValue.split("[", "]").size / 2 - 1
                            val newPredicatesCount = newLocator.locatorValue.split("[", "]").size / 2 - 1
                            if (newPredicatesCount < oldPredicatesCount)
                                count++
                        }
                    }
                }
            }
            return count
        }

        private fun checkNewXPath(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val oldLocatorsHashes = locatorsOld.map { it.hashCode() }.toSet()
                //new locator hashes only
                val newLocators = locatorsNew.filter { newLocator ->
                    !oldLocatorsHashes.contains(newLocator.hashCode()) &&
                            newLocator.locatorType.equals("xpath", ignoreCase = true)
                }
                count += newLocators.size
            }
            return count
        }


        private fun checkNewID(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val oldLocatorsHashes = locatorsOld.map { it.hashCode() }.toSet()
                //new locator hashes only
                val newLocators = locatorsNew.filter { newLocator ->
                    !oldLocatorsHashes.contains(newLocator.hashCode()) &&
                            newLocator.locatorType.equals("id", ignoreCase = true)
                }
                count += newLocators.size
            }
            return count
        }

        //two cases: new locators using multiple old values or new locators using multiple new values
        private fun checkNewMultipleUseLoc(testOutcomes: List<TestOutcome>): Int {
            val oldLocatorsHashes = mutableSetOf<Int>()
            val newLocators = mutableListOf<Locator>()
            //locators are considered from the whole test suite and not separated by method
            for (testOutcome in testOutcomes) {
                oldLocatorsHashes.addAll(testOutcome.locatorsOld.map { it.hashCode() })
                newLocators.addAll(testOutcome.locatorsPassed)
            }
            //new locator hashes only
            val filteredNewLocators = newLocators.filter { newLocator ->
                !oldLocatorsHashes.contains(newLocator.hashCode())
            }
            //count locator value usages
            val locatorCountMap = mutableMapOf<String, Int>()
            for (locator in filteredNewLocators)
                locatorCountMap[locator.locatorValue] = locatorCountMap.getOrDefault(locator.locatorValue, 0) + 1
            return locatorCountMap.values.count { it > 1 }
        }

        private fun checkNewXPathLengthMax(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val oldLocatorsHashes = locatorsOld.map { it.hashCode() }.toSet()
                //new locator hashes only
                val newLocators = locatorsNew.filter { newLocator ->
                    !oldLocatorsHashes.contains(newLocator.hashCode()) &&
                            (newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                                    newLocator.locatorValue.length <= GamificationManager.MAX_LENGTH)
                }
                count += newLocators.size
            }
            return count
        }

        private fun checkNewXPathLevelMax(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val oldLocatorsHashes = locatorsOld.map { it.hashCode() }.toSet()
                val newLocators = locatorsNew.filter { newLocator ->
                    !oldLocatorsHashes.contains(newLocator.hashCode()) &&
                            newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                            newLocator.locatorValue.split("/")
                                .filter { node -> node.isNotEmpty() }
                                .size <= GamificationManager.MAX_LEVEL
                }
                count += newLocators.size
            }
            return count
        }

        private fun checkNewRobust(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val oldLocatorsHashes = locatorsOld.map { it.hashCode() }.toSet()
                val newLocators = locatorsNew.filter { newLocator ->
                    !oldLocatorsHashes.contains(newLocator.hashCode()) &&
                            newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                            computeFragilityCoefficient(newLocator) <= GamificationManager.ROBUST_THRESHOLD
                }
                count += newLocators.size
            }
            return count
        }

        private fun checkNewXPathWithWantedAttrs(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val oldLocatorsHashes = locatorsOld.map { it.hashCode() }.toSet()
                val newLocators = locatorsNew.filter { newLocator ->
                    !oldLocatorsHashes.contains(newLocator.hashCode()) &&
                            newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                            GamificationManager.GOOD_PREDS.any { attribute ->
                                newLocator.locatorValue.contains("@$attribute", ignoreCase = true)
                            }
                }
                count += newLocators.size
            }
            return count
        }

        private fun checkNewXPathWithoutUnwantedAttrs(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val oldLocatorsHashes = locatorsOld.map { it.hashCode() }.toSet()
                val newLocators = locatorsNew.filter { newLocator ->
                    !oldLocatorsHashes.contains(newLocator.hashCode()) &&
                            newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                            GamificationManager.BAD_PREDS.none { attribute ->
                                newLocator.locatorValue.contains("@$attribute", ignoreCase = true)
                            }
                }
                count += newLocators.size
            }
            return count
        }

        private fun checkNewXPathWithoutJS(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val oldLocatorsHashes = locatorsOld.map { it.hashCode() }.toSet()
                val newLocators = locatorsNew.filter { newLocator ->
                    !oldLocatorsHashes.contains(newLocator.hashCode()) &&
                            newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                            GamificationManager.BAD_JS.none { attribute ->
                                newLocator.locatorValue.contains("@$attribute", ignoreCase = true)
                            }
                }
                count += newLocators.size
            }
            return count
        }

        private fun checkRunLoc20(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for(testOutcome in testOutcomes) {
                count += testOutcome.locatorsPassed.size
            }
            return count
        }

        private fun checkNewXPathWithFewPosPredicates(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            for (testOutcome in testOutcomes) {
                val locatorsOld = testOutcome.locatorsOld
                val locatorsNew = testOutcome.locatorsPassed
                val oldLocatorsHashes = locatorsOld.map { it.hashCode() }.toSet()
                val regex = "\\[.*?]".toRegex() //to count positional predicates
                //find locators that are actually new
                val newLocators = locatorsNew.filter { newLocator ->
                    !oldLocatorsHashes.contains(newLocator.hashCode()) &&
                            newLocator.locatorType.equals("xpath", ignoreCase = true) &&
                            regex.findAll(newLocator.locatorValue).count() <= GamificationManager.MAX_POS_PRED
                }
                count += newLocators.size
            }
            return count
        }





        /******* RANDOM DAILY CHECKS ABOUT PAGE OBJECTS *******/
        private fun checkNewPO(testOutcomes: List<TestOutcome>): Int {
            val oldPONames = TestQuestAction.POsOld.map { it.name }.toSet()
            return if (TestQuestAction.POsNew.any { it.name !in oldPONames }) 1 else 0
        }

        private fun checkNewPOMethod(testOutcomes: List<TestOutcome>): Int {
            val oldPOMethods = TestQuestAction.POsOld.associateBy({ it.name }, { it.methods.map { m -> m.name }.toSet() })
            return if (TestQuestAction.POsNew.any { newPO ->
                    val oldMethods = oldPOMethods[newPO.name] ?: emptySet()
                    newPO.methods.any { it.name !in oldMethods }
                }) 1 else 0
        }

        private fun checkAddedLocsToPOMethod(testOutcomes: List<TestOutcome>): Int {
            val oldPOLocators = TestQuestAction.POsOld.associateBy({ it.name }, { it.methods.flatMap { m -> m.locators }.toSet() })
            return TestQuestAction.POsNew.sumOf { newPO ->
                val oldLocators = oldPOLocators[newPO.name] ?: emptySet()
                newPO.methods.flatMap { it.locators }.count { it !in oldLocators }
            }
        }


        private fun checkReturnedPOInPOMethod(testOutcomes: List<TestOutcome>): Int {
            return 0;
        }

        private fun checkMovedAssertsFromPOMethod(testOutcomes: List<TestOutcome>): Int {
            return 0;
        }

        private fun checkAdaptedLocsFormatInPOMethod(testOutcomes: List<TestOutcome>): Int {
            return 0;
        }

        private fun checkInteractionsWithLocsInPOMethod(testOutcomes: List<TestOutcome>): Int {
            return 0;
        }

        private fun checkNewAncestorPO(testOutcomes: List<TestOutcome>): Int {
            return 0;
        }

        private fun checkMovedCommonMethodToAncestoPO(testOutcomes: List<TestOutcome>): Int {
            return 0;
        }

        private fun checkInstantiationPO(testOutcomes: List<TestOutcome>): Int {
            return 0;
        }

        private fun checkCalledPOMethod(testOutcomes: List<TestOutcome>): Int {
            return 0;
        }

        private fun checkCalledUnusedPOMethod(testOutcomes: List<TestOutcome>): Int {
            return 0;
        }
































        /**AUXILIARY FUNCTIONS**/
        private fun computeFragilityCoefficient(loc: Locator): Double{
            val calc = LocatorsFragilityCalculator()
            return calc.calculateFragility(loc)
        }

        private fun countWantedAttributes(locatorValue: String): Int {
            return GamificationManager.GOOD_PREDS.sumOf { attribute -> Regex(attribute).findAll(locatorValue).count() }
        }

        private fun countUnwantedAttributes(locatorValue: String): Int {
            return GamificationManager.BAD_PREDS.sumOf { attribute -> Regex(attribute).findAll(locatorValue).count() }
        }

        private fun countJavaScriptReferences(locatorValue: String): Int {
            return GamificationManager.BAD_JS.sumOf { attribute -> Regex(attribute).findAll(locatorValue).count() }
        }




















    }
}
