package gamification

import listener.test.TestOutcome
import extractor.locator.Locator
import extractor.test.PageObjectCall
import analyzer.locator.LocatorsAnalyzer
import analyzer.locator.LocatorsFragilityCalculator
import analyzer.pageobject.PageObjectsAnalyzer
import testquest.TestQuestAction
import ui.GUIManager
import utils.FilePathSolver

class DailyManager {

    companion object {

        //TODO: we may want to convert goal, xps, and pics into a map specific for each daily
        //TODO: also, we may want to refactor this class by organizing dailies as LocatorsDailyManager, PODailyManager, TestDailyManager
        //TODO: dailies (random and targeted) about tests are still to be implemented and might require additional extractions mechanics (e.g., to extract test structure)
        private const val DAILY_GOAL: Int = 3
        private const val RANDOM_DAILY_XP: Int = 100
        private const val TARGETED_DAILY_XP: Int = 25
        private const val DAILIES_PER_USER: Int = 5


        /**********************************RANDOM DAILIES**********************************/
        private val ALL_RANDOM_DAILIES = mutableListOf(
            /************ DAILIES ABOUT LOCATORS ************/
            Daily(
                "xpathAbs",
                "Replace $DAILY_GOAL existing absolute XPath locators with $DAILY_GOAL relative ones",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "xpathLength",
                "Reduce the length of $DAILY_GOAL existing XPath locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "xpathLevel",
                "Reduce the level of $DAILY_GOAL existing XPath locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "loc2xpath",
                "Convert $DAILY_GOAL existing non-XPath locators to $DAILY_GOAL XPath ones",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "loc2id",
                "Convert $DAILY_GOAL existing non-ID locators to $DAILY_GOAL ID ones",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "runLocs20",
                "Run 20 locators successfully",
                RANDOM_DAILY_XP,
                20,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "robustness",
                "Improve the robustness of $DAILY_GOAL existing locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "lengthShortenMax",
                "Shorten the length of $DAILY_GOAL existing XPath locators below " + GamificationManager.MAX_LENGTH + " characters",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "addAttrToXPath",
                "Add $DAILY_GOAL references to robust attributes to existing XPaths locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "remAttrFromXPath",
                "Remove $DAILY_GOAL references to fragile attributes from existing XPaths locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "remJSFromXPath",
                "Remove $DAILY_GOAL references to Javascript code from existing XPaths locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "edit5",
                "Modify 5 different existing locators",
                RANDOM_DAILY_XP,
                5,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "levelLoweredMax",
                "Lower the level of $DAILY_GOAL existing locators below " + GamificationManager.MAX_LEVEL + " tags",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "repair",
                "Repair $DAILY_GOAL existing broken locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "reducePredicates",
                "Reduce the number of predicates from $DAILY_GOAL existing locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "newXPath",
                "Implement $DAILY_GOAL new XPath locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "newID",
                "Implement $DAILY_GOAL new ID locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "newSameLoc",
                "Implement a newly locator value and use it more than once in a test suite",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "newLengthShorterMax",
                "Implement $DAILY_GOAL new XPath locators with length below " + GamificationManager.MAX_LENGTH+ " characters",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "newLevelLowerMax",
                "Implement $DAILY_GOAL new XPath locators with level below " + GamificationManager.MAX_LEVEL+ " tags",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "newRobust",
                "Implement $DAILY_GOAL new robust locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "newWantedAttr",
                "Implement $DAILY_GOAL new XPath locators with references to robust attributes",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "newUnwantedAttr",
                "Implement $DAILY_GOAL new XPath locators with no references to fragile attributes",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "newJS",
                "Implement $DAILY_GOAL new XPath locators with no references to Javascript code",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            Daily(
                "newLowPredicates",
                "Implement $DAILY_GOAL new XPath locators with " + GamificationManager.MAX_POS_PRED + " or less positional predicates",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name
            ),
            /************ DAILIES ABOUT POS ************/
            Daily(
                "addPO", //create a PO within test suite (i.e., basically just a class named _Page is ok)
                "Add a new PageObject to the test suite and use it in a test",
                RANDOM_DAILY_XP,
                1,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "addMethodToPO", //add a method to a PO (i.e., basically just an empty method is ok)
                "Add a new method of a PageObject and use it in a test",
                RANDOM_DAILY_XP,
                1,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "addLocsToMethod", //add locs to a PO method
                "Add new $DAILY_GOAL locators to any PageObject method",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "moveLocs2Method", //move locs from test to a PO method
                "Move $DAILY_GOAL existing locators from tests to any PageObject method",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "returnPOInMethod", //add a PO as return type for a method
                "Change from void to a PageObject the return type of a PageObject method",
                RANDOM_DAILY_XP,
                1,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "moveAssertsToTest", //move asserts from method to test
                "Remove $DAILY_GOAL existing assertions from any PageObject method",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "adaptLocs2Format", //adapt locs to have format: WebElement e = driver.findElement(By...)
                "Adapts $DAILY_GOAL locators declarations to the canonical form",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "interactWithLocsInMethod", //add Selenium instructions in method to interact with locators
                "Add $DAILY_GOAL Selenium commands in any PageObject method",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "addAncestorPO", //add an ancestor PO
                "Assign an ancestor PageObject to any PageObject",
                RANDOM_DAILY_XP,
                1,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "moveCommonMethodToAncestorPO", //move a method shared among POs to a common ancestor
                "Move a cloned method from multiple PageObjects to a common ancestor",
                RANDOM_DAILY_XP,
                1,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "callMethod", // call a method from a PO within a test
                "Call a PageObject method from any test",
                RANDOM_DAILY_XP,
                1,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "callUnusedMethod", //call an unused method from a PO within a test
                "Call an unused PageObject method from any test",
                RANDOM_DAILY_XP,
                1,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            /************ DAILIES ABOUT TESTS ************/
            Daily(
                "runtc",
                "Run $DAILY_GOAL test cases successfully",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "runts",
                "Run a test suite successfully",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "addAssert2Test",
                "Add an assert to a test case",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "shortenTestWorkFlow",//test workflow must be simple (i.e., checks if test has not too many interactions/locs and too many assertions)
                "Refactor a long test into two tests",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "makeTestWorkFlowIndependent", //test workflow must be independently from any other test
                "Make a test independent from other tests",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "adaptTestName2Format", //test name must have 3 sections: what is being tested, circumnstances, exp result
                "Adapt test name to canonical name",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "shortenMethodName",
                "Shorten a method name",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "shortenVarName",
                "Shorten a variable name",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "addSetup",
                "Add a setup method to a test class",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "addTearDown",
                "Add a tearDown method to a test class",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "removeGlobVar",
                "Remove a global variable from a test class",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
            Daily(
                "removeExpSleep",
                "Remove an explicit time sleep",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                isAdvanced = true
            ),
        )


        /**********************************TARGETED DAILIES**********************************/
        private val ALL_TARGETED_DAILIES = mutableListOf(
            /************ DAILIES ABOUT LOCATORS ************/
            Daily(
                "absolute",
                "Turns the following absolute XPath locators into relative ones",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
            Daily(
                "length",
                "Reduce the length of the following XPath locators",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
            Daily(
                "level",
                "Reduce the levels of the following XPath locators",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
            Daily(
                "posPredicate",
                "Remove/Replace the positional predicates from the following XPath locators",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
            Daily(
                "badPredicate",
                "Remove/Replace the weak predicates from the following XPath locators",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
            Daily(
                "noIDOrXPath",
                "Replace the following non-ID/XPath locators into ID/XPath ones",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
            Daily(
                "broken",
                "Repair the following broken locators",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name
            ),
            /************ DAILIES ABOUT POs ************/
            Daily(
                "emptyPOs",
                "Add methods to the following empty Page Objects",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name,
                isAdvanced = true
            ),
            Daily(
                "missingCommandMethods",
                "Add Selenium commands to the following empty Page Object methods",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name,
                isAdvanced = true
            ),
            Daily(
                "missingRetPOMethods",
                "Change from void to a PageObject the return type of the following Page Object methods",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name,
                isAdvanced = true
            ),
            Daily(
                "assertInPOMethods",
                "Remove from the Page Object methods the following assertions",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name,
                isAdvanced = true
            ),
            Daily(
                "nonCanonicalLocs",
                "Adapt into canonical form the declarations of the following locators",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name,
                isAdvanced = true
            ),
            Daily(
                "unusedPOMethods",
                "Make use of the following unused Page Object methods",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name,
                isAdvanced = true
            ),
            Daily(
                "outPOLocs",
                "Move from tests to Page Object methods the following locators",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name,
                isAdvanced = true
            ),
            Daily(
                "duplicatedMethods",
                "Move to a common PageObject ancestor the following cloned Page Object methods",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name,
                isAdvanced = true
            ),
            /************ DAILIES ABOUT TESTS ************/
        )

        private val DAILY_NAME_TO_DESCRIPTION = ALL_RANDOM_DAILIES.associate { it.name to it.description }
        private val DAILY_NAME_TO_TARGET = ALL_RANDOM_DAILIES.associate { it.name to it.target }
        private val DAILY_NAME_TO_ICON = ALL_RANDOM_DAILIES.associate { it.name to it.icon }
        private val DAILY_NAME_TO_XP = ALL_RANDOM_DAILIES.associate { it.name to it.xp }

        private val RANDOM_DAILY_CHECKS: Map<String, (List<TestOutcome>) -> Int> = mapOf(
            /************ DAILIES ABOUT LOCS ************/
            "xpathAbs" to { testOutcomes -> checkAbsXPathRemoved(testOutcomes) },
            "xpathLength" to { testOutcomes -> checkXPathLengthReduced(testOutcomes) },
            "xpathLevel" to { testOutcomes -> checkXPathLevelReduced(testOutcomes) },
            "loc2xpath" to { testOutcomes -> checkLocs2XPathConverted(testOutcomes) },
            "loc2id" to { testOutcomes -> checkLocs2IDConverted(testOutcomes) },
            "runLocs20" to { testOutcomes -> checkRunLoc20(testOutcomes) },
            "robustness" to { testOutcomes -> checkRobustnessImprovement(testOutcomes) },
            "lengthShortenMax" to { testOutcomes -> checkShortenedLengthMax(testOutcomes) },
            "addAttrToXPath" to { testOutcomes -> checkWantedAttrsInXPaths(testOutcomes) },
            "remAttrFromXPath" to { testOutcomes -> checkUnwantedAttrsInXPaths(testOutcomes) },
            "remJSFromXPath" to { testOutcomes -> checkJSInXPaths(testOutcomes) },
            "edit5" to { testOutcomes -> checkChangedLocs5(testOutcomes) },
            "levelLoweredMax" to { testOutcomes -> checkLoweredLevelMax(testOutcomes) },
            "repair" to { testOutcomes -> checkRepair(testOutcomes) },
            "reducePredicates" to { testOutcomes -> reducePredicates(testOutcomes) },
            "newXPath" to { testOutcomes -> checkNewXPath(testOutcomes) },
            "newID" to { testOutcomes -> checkNewID(testOutcomes) },
            "newSameLoc" to { testOutcomes -> checkNewMultipleUseLoc(testOutcomes) },
            "newLengthShorterMax" to { testOutcomes -> checkNewXPathLengthMax(testOutcomes) },
            "newLevelLowerMax" to { testOutcomes -> checkNewXPathLevelMax(testOutcomes) },
            "newRobust" to { testOutcomes -> checkNewRobust(testOutcomes) },
            "newWantedAttr" to { testOutcomes -> checkNewXPathWithWantedAttrs(testOutcomes) },
            "newUnwantedAttr" to { testOutcomes -> checkNewXPathWithoutUnwantedAttrs(testOutcomes) },
            "newJS" to { testOutcomes -> checkNewXPathWithoutJS(testOutcomes) },
            "newLowPredicates" to { testOutcomes -> checkNewXPathWithFewPosPredicates(testOutcomes) },
            /************ DAILIES ABOUT POS ************/
            "addPO" to { testOutcomes -> checkPOAdded(testOutcomes) },
            "addMethodToPO" to { testOutcomes -> checkPOMethodAdded(testOutcomes) },
            "addLocsToMethod" to { testOutcomes -> checkLocsInPOAdded(testOutcomes) },
            "moveLocs2Method" to { testOutcomes -> checkLocsFromTestsToPOMoved(testOutcomes) },
            "returnPOInMethod" to { testOutcomes -> checkPOReturnTypeAdded(testOutcomes) },
            "moveAssertsToTest" to { testOutcomes -> checkAssertsFromPOToTestsMoved(testOutcomes) },
            "adaptLocs2Format" to { testOutcomes -> checkNonCanonicalLocsChanged(testOutcomes) },
            "interactWithLocsInMethod" to { testOutcomes -> checkSeleniumCommandsAdded(testOutcomes) },
            "addAncestorPO" to { testOutcomes -> checkAncestorPOAdded(testOutcomes) },
            "moveCommonMethodToAncestorPO" to { testOutcomes -> checkCommonMethodMoved(testOutcomes) },
            "callMethod" to { testOutcomes -> checkPOMethodCalled(testOutcomes) },
            "callUnusedMethod" to { testOutcomes -> checkUnusedPOMethodCalled(testOutcomes) },
            /************ DAILIES ABOUT TESTS ************/
            "runtc" to { testOutcomes -> checkRunTC(testOutcomes) },
            "runts" to { testOutcomes -> checkRunTS(testOutcomes) },
            "addAssert2Test" to { testOutcomes -> checkAssertAddedToTest(testOutcomes) },
            "shortenTestWorkFlow" to { testOutcomes -> checkTestShortened(testOutcomes) },
            "makeTestWorkFlowIndependent" to { testOutcomes -> checkTestIndependent(testOutcomes) },
            "adaptTestName2Format" to { testOutcomes -> checkTestNameAdapted(testOutcomes) },
            "shortenMethodName" to { testOutcomes -> checkMethodNameShortened(testOutcomes) },
            "shortenVarName" to { testOutcomes -> checkVarNameShortened(testOutcomes) },
            "addSetup" to { testOutcomes -> checkSetupAdded(testOutcomes) },
            "addTearDown" to { testOutcomes -> checkTearDownAdded(testOutcomes) },
            "removeGlobVar" to { testOutcomes -> checkGlobalVarRemoved(testOutcomes) },
            "removeExpSleep" to { testOutcomes -> checkExpSleepRemoved(testOutcomes) },
        )


        private val TARGETED_DAILY_CHECKS: Map<String, (List<TestOutcome>) -> Int> = mapOf(
            /************ DAILIES ABOUT LOCATORS ************/
            "absolute" to { testOutcomes -> checkTargetedAbsoluteRemoved(testOutcomes) },
            "length" to { testOutcomes -> checkTargetedLengthReduced(testOutcomes) },
            "level" to { testOutcomes -> checkTargetedLevelReduced(testOutcomes) },
            "posPredicate" to { testOutcomes -> checkTargetedPosPredicateRemoved(testOutcomes) },
            "badPredicate" to { testOutcomes -> checkTargetedBadPredicateRemoved(testOutcomes) },
            "noIDOrXPath" to { testOutcomes -> checkTargetedNoIDNoXpathChanged(testOutcomes) },
            "broken" to { testOutcomes -> checkTargetedBrokenLocsRepaired(testOutcomes) },
            /************ DAILIES ABOUT POs ************/
            "emptyPOs" to { testOutcomes -> checkMethodsToEmptyPOAdded(testOutcomes) },
            "missingCommandMethods" to { testOutcomes -> checkCommandsAdded(testOutcomes) },
            "missingRetPOMethods" to { testOutcomes -> checkPOTypeReturned(testOutcomes) },
            "assertInPOMethods" to { testOutcomes -> checkAssertsRemoved(testOutcomes) },
            "nonCanonicalLocs" to { testOutcomes -> checkNonCanonicalLocsNowCanonical(testOutcomes) },
            "unusedPOMethods" to { testOutcomes -> checkUnusedMethodsNowUsed(testOutcomes) },
            "outPOLocs" to { testOutcomes -> checkLocsOutsidePOsRemoved(testOutcomes) },
            "duplicatedMethods" to { testOutcomes -> checkClonedMethodsMoved(testOutcomes) }
            /************ DAILIES ABOUT TESTS ************/
        )


        fun getTargetedLocatorDailyNames(): List<String> {
            return listOf(
                "absolute", "length", "level", "posPredicate", "badPredicate", "noIDOrXPath", "broken"
            )
        }

        fun getTargetedPageObjectDailyNames(): List<String> {
            return listOf(
                "emptyPOs", "missingCommandMethods", "missingRetPOMethods", "assertInPOMethods",
                "nonCanonicalLocs", "unusedPOMethods", "outPOLocs", "duplicatedMethods"
            )
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




        /******* TARGETED DAILY CHECKS ABOUT POs *******/

        //it counts the number of empty PO that have now one or more methods executed correctly
        private fun checkMethodsToEmptyPOAdded(testOutcomes: List<TestOutcome>): Int {
            val count: Int
            val passedMethods = testOutcomes
                .flatMap { it.poMethodCallsPassed }
                .map { it.methodName }
                .toSet()
            count = TestQuestAction.POsOld
                .filter { it.methods.isEmpty() }
                .count { oldPO ->
                    TestQuestAction.POsNew
                        .find { it.name == oldPO.name }
                        ?.methods
                        ?.any { it.name in passedMethods } == true
                }
            return count
        }

        //it counts the number of PO methods called by tests (POCallsNew) and executed correctly (i.e., call line before any error line)
        //that were once (POsOld) without Selenium commands and now have them (POsNew)
        private fun checkCommandsAdded(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            //retrieve passed methods from all tests, keeping no duplicates
            val passedMethods = testOutcomes
                .flatMap { it.poMethodCallsPassed }
                .map { it }
                .toSet()
            for (passedMethod in passedMethods) {
                //get old/new associated PO
                val oldPO = TestQuestAction.POsOld.find { po -> po.name == passedMethod.pageObject }
                val newPO = TestQuestAction.POsNew.find { po -> po.name == passedMethod.pageObject }
                //get old/new method version
                val oldMethod = oldPO?.methods?.find { it.name == passedMethod.methodName }
                val newMethod = newPO?.methods?.find { it.name == passedMethod.methodName }
                //count methods when they previously had no commands and that now have so
                val hadNoSeleniumCommands = oldMethod?.seleniumCommands.isNullOrEmpty()
                val hasNewSeleniumCommands = newMethod?.seleniumCommands?.isNotEmpty() == true
                if (hadNoSeleniumCommands && hasNewSeleniumCommands)
                    count++
            }
            return count
        }

        //it counts the number of PO methods called by tests (POCallsNew) and executed correctly (i.e., call line before any error line)
        //that had once (POsOld) no return type and commands and now have a PageObject as return type (POsNew)
        private fun checkPOTypeReturned(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            //retrieve passed methods from all tests, keeping no duplicates
            val passedMethods = testOutcomes
                .flatMap { it.poMethodCallsPassed }
                .map { it }
                .toSet()
            for (passedMethod in passedMethods) {
                //get old/new associated PO
                val oldPO = TestQuestAction.POsOld.find { po -> po.name == passedMethod.pageObject }
                val newPO = TestQuestAction.POsNew.find { po -> po.name == passedMethod.pageObject }
                //get old/new method version
                val oldMethod = oldPO?.methods?.find { it.name == passedMethod.methodName }
                val newMethod = newPO?.methods?.find { it.name == passedMethod.methodName }
                //count methods that once had void as return type but that now have a PO
                val wasVoid = oldMethod?.returnType?.equals("void", ignoreCase = true) == true
                val isNowPageObject = newMethod?.returnType?.endsWith("Page", ignoreCase = true) == true
                if (wasVoid && isNowPageObject)
                    count++
            }
            return count
        }

        //it counts the number of assertions from PO methods called by tests (POCallsNew) and executed correctly (i.e., call line before any error line)
        //that were once in POs and are now removed
        private fun checkAssertsRemoved(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            //retrieve passed methods from all tests, keeping no duplicates
            val passedMethods = testOutcomes
                .flatMap { it.poMethodCallsPassed }
                .map { it }
                .toSet()
            for (passedMethod in passedMethods) {
                val oldAssertions = TestQuestAction.POsOld
                    .find { it.name == passedMethod.pageObject }
                    ?.methods?.find { it.name == passedMethod.methodName }
                    ?.assertionLines?.size ?: 0
                val newAssertions = TestQuestAction.POsNew
                    .find { it.name == passedMethod.pageObject }
                    ?.methods?.find { it.name == passedMethod.methodName }
                    ?.assertionLines?.size ?: 0
                if (oldAssertions > newAssertions)
                    count += (oldAssertions - newAssertions)
            }
            return count
        }

        //it counts the number of locators from PO methods called by tests and executed correctly
        // that once were non canonical and that are now so
        private fun checkNonCanonicalLocsNowCanonical(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            //retrieve passed locs from all tests, keeping no duplicates
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            //check from passed locs all those that are changed from noncanonical to canonical
            for (passedLoc in passedLocs) {
                //we compare new loc that changed class with previous version
                //as some key attributes changed, basic equal cannot be used
                val oldNonCanonicalLoc = TestQuestAction.locatorsOld.find { oldLocator ->
                    passedLoc.compareThisLocWithLocInNonCanonicalForm(oldLocator) }
                if (oldNonCanonicalLoc != null)
                    count++
            }
            return count
        }

        //it counts the number of PO methods called by tests (POCallsNew) and executed correctly (i.e., call line before any error line)
        //that were not used before in any test
        private fun checkUnusedMethodsNowUsed(testOutcomes: List<TestOutcome>): Int {
            val count: Int
            val oldCalledMethods = mutableSetOf<PageObjectCall>()
            val newCalledMethods = mutableSetOf<PageObjectCall>()
            //collect old and new PO calls from all tests
            for (outcome in testOutcomes) {
                TestQuestAction.POCallsOld[outcome.testName]?.let { oldCalledMethods.addAll(it) }
                TestQuestAction.POCallsNew[outcome.testName]?.let { newCalledMethods.addAll(it) }
            }
            //check if there exist new PO method calls that were not called before and that are passed
            val newlyCalledMethods = newCalledMethods - oldCalledMethods
            count = newlyCalledMethods.count { newMethod ->
                testOutcomes.any { outcome ->
                    outcome.poMethodCallsPassed.any { it.methodName == newMethod.methodName }
                }
            }
            return count
        }

        //it counts the number of locators called by tests and executed correctly
        //that once were in test methods and are now in PO methods
        private fun checkLocsOutsidePOsRemoved(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            //retrieve passed locs from all tests, keeping no duplicates
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            //check from passed locs all those that are moved from tests to PO
            for (passedLoc in passedLocs) {
                //we compare new loc that changed class with previous version
                //as some key attributes changed, basic equal cannot be used
                val matchingOldLocator = TestQuestAction.locatorsOld.find { oldLocator ->
                    passedLoc.compareThisLocInPOWithOldInTest(oldLocator)
                }
                if (matchingOldLocator != null)
                    count++
            }
            return count
        }

        private fun checkClonedMethodsMoved(testOutcomes: List<TestOutcome>): Int {
            val count: Int
            //used a set to count the number of movement actions to move common methods to an ancestor
            // (e.g., if 3 methods have same name and into this set, we will count them as 1 movement)
            val commonMethodsMoved = mutableSetOf<String>()
            for (i in 0 until TestQuestAction.POsOld.size)
                for (j in i + 1 until TestQuestAction.POsOld.size) {
                    val poOld1 = TestQuestAction.POsOld[i]
                    val poOld2 = TestQuestAction.POsOld[j]
                    //find common ancestors OLD
                    val commonAncestorsOldNames = poOld1.ancestors.intersect(poOld2.ancestors.toSet())
                    val commonAncestorsOld = TestQuestAction.POsOld.filter { it.name in commonAncestorsOldNames }
                    //find common methods OLD
                    val commonMethods = poOld1.methods.intersect(poOld2.methods.toSet())
                    for (method in commonMethods)
                        //check no common ancestors OLD had that method before changes
                        if (commonAncestorsOld.none { it.methods.contains(method) }) {
                            //find new versions of the compared POs
                            val poNew1 = TestQuestAction.POsNew.find { it.name == poOld1.name }
                            val poNew2 = TestQuestAction.POsNew.find { it.name == poOld2.name }
                            if (poNew1 != null && poNew2 != null)
                                //check that method is no more present in POs
                                if (!poNew1.methods.contains(method) && !poNew2.methods.contains(method)) {
                                    //find common ancestors NEW
                                    val commonAncestorsNewNames = poNew1.ancestors.intersect(poNew2.ancestors.toSet())
                                    val commonAncestorsNew = TestQuestAction.POsNew.filter { it.name in commonAncestorsNewNames }
                                    //check a common ancestor that now has that method
                                    if (commonAncestorsNew.any { it.methods.contains(method) })
                                        commonMethodsMoved.add(method.name)
                                }

                        }
                }
            //check if any of these moved common methods is passed wrt a test
            val passedMethods = testOutcomes.flatMap { it.poMethodCallsPassed.map { call -> call.methodName } }.toSet()
            count = passedMethods.count { it in commonMethodsMoved }
            return count
        }





        /******* TARGETED DAILY CHECKS ABOUT TESTS *******/

















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

        private fun checkPOAdded(testOutcomes: List<TestOutcome>): Int {
            val oldPONames = TestQuestAction.POsOld.map { it.name }.toSet()
            for (testOutcome in testOutcomes)
                //check from passed PO calls any call that is related to a new PO
                for (poCall in testOutcome.poMethodCallsPassed) {
                    val poName = poCall.pageObject
                    if (poName !in oldPONames)
                        return 1
                }
            return 0
        }

        private fun checkPOMethodAdded(testOutcomes: List<TestOutcome>): Int {
            for (testOutcome in testOutcomes)
                //check from passed PO calls any call that is related to a new method
                for (poCall in testOutcome.poMethodCallsPassed) {
                    if (TestQuestAction.POsOld
                            .find { it.name == poCall.pageObject }
                            ?.methods
                            ?.none { it.name == poCall.methodName } != false) {
                        return 1
                    }
                }
            return 0
        }

        private fun checkLocsInPOAdded(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            //check from passed locs all those that are in PO and new
            for (passedLoc in passedLocs)
                if (passedLoc !in TestQuestAction.locatorsOld && passedLoc.className.endsWith("Page.java"))
                    count++
            return count
        }

        private fun checkLocsFromTestsToPOMoved(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            //check from passed locs all those that are moved from tests to PO
            for (passedLoc in passedLocs) {
                //we compare new loc that changed class with previous version
                //as some key attributes changed, basic equal cannot be used
                val matchingOldLocator = TestQuestAction.locatorsOld.find { oldLocator ->
                        passedLoc.compareThisLocInPOWithOldInTest(oldLocator)  }
                if (matchingOldLocator != null)
                    count++
            }
            return count
        }

        private fun checkPOReturnTypeAdded(testOutcomes: List<TestOutcome>): Int {
            for (testOutcome in testOutcomes) {
                //check passed methods that had void as old return type and PageObject as new
                for (passedMethod in testOutcome.poMethodCallsPassed) {
                    val oldReturnType = TestQuestAction.POsOld
                        .find { it.name == passedMethod.pageObject }
                        ?.methods?.find { it.name == passedMethod.methodName }
                        ?.returnType
                    val newReturnType = TestQuestAction.POsNew
                        .find { it.name == passedMethod.pageObject }
                        ?.methods?.find { it.name == passedMethod.methodName }
                        ?.returnType
                    if (newReturnType!!.endsWith("Page") && oldReturnType.equals("void"))
                        return 1
                }
            }
            return 0
        }

        private fun checkAssertsFromPOToTestsMoved(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            //retrieve passed methods from all tests, keeping no duplicates
            val passedMethods = testOutcomes
                .flatMap { it.poMethodCallsPassed }
                .map { it }
                .toSet()
            //count removed asserts from passed methods in PageObjects
            for (passedMethod in passedMethods) {
                val oldAssertions = TestQuestAction.POsOld
                    .find { it.name == passedMethod.pageObject }
                    ?.methods?.find { it.name == passedMethod.methodName }
                    ?.assertionLines?.size ?: 0
                val newAssertions = TestQuestAction.POsNew
                    .find { it.name == passedMethod.pageObject }
                    ?.methods?.find { it.name == passedMethod.methodName }
                    ?.assertionLines?.size ?: 0
                if (oldAssertions > newAssertions)
                    count += (oldAssertions - newAssertions)
            }
            return count
        }

        private fun checkNonCanonicalLocsChanged(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            //check from passed locs all those that are changed from noncanonical to canonical
            for (passedLoc in passedLocs) {
                //we compare new loc that changed class with previous version
                //as some key attributes changed, basic equal cannot be used
                val oldNonCanonicalLoc = TestQuestAction.locatorsOld.find { oldLocator ->
                    passedLoc.compareThisLocWithLocInNonCanonicalForm(oldLocator) }
                if (oldNonCanonicalLoc != null)
                    count++
            }
            return count
        }

        private fun checkSeleniumCommandsAdded(testOutcomes: List<TestOutcome>): Int {
            var oldInteractions = 0
            var newInteractions = 0
            //retrieve passed methods from all tests, keeping no duplicates
            val passedMethods = testOutcomes
                .flatMap { it.poMethodCallsPassed }
                .map { it }
                .toSet()
            //check diff in interactions from all passed methods
            for (passedMethod in passedMethods) {
                oldInteractions += TestQuestAction.POsOld
                    .filter { it.name == passedMethod.pageObject } //this to count this specific method for this specific page object
                    .flatMap { it.methods }
                    .filter { it.name == passedMethod.methodName}
                    .sumOf { it.seleniumCommands.size }
                newInteractions += TestQuestAction.POsNew
                    .filter { it.name == passedMethod.pageObject } //this to count this specific method for this specific page object
                    .flatMap { it.methods }
                    .filter { it.name == passedMethod.methodName }
                    .sumOf { it.seleniumCommands.size }
            }
            return newInteractions - oldInteractions
        }

        private fun checkAncestorPOAdded(testOutcomes: List<TestOutcome>): Int {
            return if (TestQuestAction.POsNew.any { newPO ->
                    val oldPO = TestQuestAction.POsOld.find { it.name == newPO.name }
                    oldPO != null && oldPO.ancestors.size < newPO.ancestors.size
                }) {
                1
            } else {
                0
            }
        }

        private fun checkCommonMethodMoved(testOutcomes: List<TestOutcome>): Int {
            val commonMethodsMoved = mutableSetOf<String>()
            for (i in 0 until TestQuestAction.POsOld.size) {
                for (j in i + 1 until TestQuestAction.POsOld.size) {
                    val poOld1 = TestQuestAction.POsOld[i]
                    val poOld2 = TestQuestAction.POsOld[j]
                    //find common ancestors OLD
                    val commonAncestorsOldNames = poOld1.ancestors.intersect(poOld2.ancestors.toSet())
                    val commonAncestorsOld = TestQuestAction.POsOld.filter { it.name in commonAncestorsOldNames }
                    //find common methods
                    val commonMethods = poOld1.methods.intersect(poOld2.methods.toSet())
                    for (method in commonMethods) {
                        //check no common ancestors had that method before changes
                        if (commonAncestorsOld.none { it.methods.contains(method) }) {
                            //find new versions of the compared POs
                            val poNew1 = TestQuestAction.POsNew.find { it.name == poOld1.name }
                            val poNew2 = TestQuestAction.POsNew.find { it.name == poOld2.name }
                            if (poNew1 != null && poNew2 != null) {
                                //check that method is no more present in POs
                                if (!poNew1.methods.contains(method) && !poNew2.methods.contains(method)) {
                                    //find common ancestors NEW
                                    val commonAncestorsNewNames = poNew1.ancestors.intersect(poNew2.ancestors.toSet())
                                    val commonAncestorsNew = TestQuestAction.POsNew.filter { it.name in commonAncestorsNewNames }
                                    //check a common ancestor now has that method
                                    if (commonAncestorsNew.any { it.methods.contains(method) }) {
                                        commonMethodsMoved.add(method.name)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //check if any of these moved common methods is passed wrt a test
            for (testOutcome in testOutcomes)
                if (testOutcome.poMethodCallsPassed.any { it.methodName in commonMethodsMoved })
                    return 1
            return 0
        }

        private fun checkPOMethodCalled(testOutcomes: List<TestOutcome>): Int {
            for (outcome in testOutcomes) {
                //find PO method calls for each test. if new calls are more in any test (i.e., a method call was added), 1 is returned
                val oldCalledMethods = TestQuestAction.POCallsOld[outcome.testName]?.toSet() ?: emptySet()
                val newCalledMethods = TestQuestAction.POCallsNew[outcome.testName]?.toSet() ?: emptySet()
                //get diff
                val newlyCalledMethods = newCalledMethods - oldCalledMethods
                //check if new calls were passed
                if (newlyCalledMethods.isNotEmpty() && newlyCalledMethods.any { methodName ->
                        outcome.poMethodCallsPassed.any { it.methodName == methodName.methodName }
                    })
                    return 1
            }
            return 0
        }

        private fun checkUnusedPOMethodCalled(testOutcomes: List<TestOutcome>): Int {
            val oldCalledMethods = mutableSetOf<PageObjectCall>()
            val newCalledMethods = mutableSetOf<PageObjectCall>()
            //collect old and new PO calls from all tests
            for (outcome in testOutcomes) {
                TestQuestAction.POCallsOld[outcome.testName]?.let { oldCalledMethods.addAll(it) }
                TestQuestAction.POCallsNew[outcome.testName]?.let { newCalledMethods.addAll(it) }
            }
            //check if there exist new PO method calls that did not exist before and that are passed
            val newlyCalledMethods = newCalledMethods - oldCalledMethods
            if (newlyCalledMethods.isNotEmpty() && newlyCalledMethods.any { newCall ->
                    testOutcomes.any { outcome ->
                        outcome.poMethodCallsPassed.any { it.methodName == newCall.methodName }
                    }
                })
                return 1
            return 0
        }










        /******* RANDOM DAILY CHECKS ABOUT TESTS *******/

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

        private fun checkAssertAddedToTest(testOutcomes: List<TestOutcome>): Int {
            return 1
        }

        private fun checkTestShortened(testOutcomes: List<TestOutcome>): Int {
            return 1
        }

        private fun checkTestIndependent(testOutcomes: List<TestOutcome>): Int {
            return 1
        }

        private fun checkTestNameAdapted(testOutcomes: List<TestOutcome>): Int {
            return 1
        }

        private fun checkMethodNameShortened(testOutcomes: List<TestOutcome>): Int {
            return 1
        }

        private fun checkVarNameShortened(testOutcomes: List<TestOutcome>): Int {
            return 1
        }

        private fun checkSetupAdded(testOutcomes: List<TestOutcome>): Int {
            return 1
        }

        private fun checkTearDownAdded(testOutcomes: List<TestOutcome>): Int {
            return 1
        }

        private fun checkGlobalVarRemoved(testOutcomes: List<TestOutcome>): Int {
            return 1
        }

        private fun checkExpSleepRemoved(testOutcomes: List<TestOutcome>): Int {
            return 1
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








        //dailies are assigned to new user at the start. initially, the usage mode is RANDOM so random dailies are assigned for setup
        //further, dailies are assigned randomly from not advanced ones (i.e., only those based on locators) if that is the chosen gamification mode
        fun setupRandomDailies(userProfile: UserProfile) {
            val filteredDailies = if (GamificationManager.gamificationMode == GamificationManager.GamificationMode.LOCATOR) {
                ALL_RANDOM_DAILIES.filter { !it.isAdvanced }
            } else
                ALL_RANDOM_DAILIES
            val selectedDailies: List<Daily> = filteredDailies.shuffled().take(DAILIES_PER_USER)
            userProfile.assignDailies(selectedDailies)
        }

        fun assignTargetedDailies(userProfile: UserProfile) {
            val locAnalyzer = LocatorsAnalyzer()
            val issuedLocatorsMap = locAnalyzer.findIssuesInLocators()
            val poAnalyzer = PageObjectsAnalyzer()
            val issuedPOsMap = poAnalyzer.findIssuesInPOs()
            //filter dailies based on gamification mode
            val filteredTargetDailies = if (GamificationManager.gamificationMode == GamificationManager.GamificationMode.LOCATOR) {
                ALL_TARGETED_DAILIES.filterNot { it.isAdvanced }
            } else {
                ALL_TARGETED_DAILIES
            }
            val currentTargetDailies = filteredTargetDailies.map { daily ->
                val locatorsForDaily = issuedLocatorsMap[daily.name] ?: emptyList()
                val posForDaily = issuedPOsMap[daily.name]
                Daily(
                    name = daily.name,
                    description = daily.description,
                    xp = daily.xp,
                    target = daily.target,
                    icon = daily.icon,
                    type = daily.type,
                    targetedLocators = locatorsForDaily,
                    issuesInPOs = posForDaily
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
            //filter dailies according to gamification mode (e.g., if mode is LOCATOR only non advanced dailies are selected)
            val filteredDailies = if (GamificationManager.gamificationMode == GamificationManager.GamificationMode.LOCATOR) {
                availableDailies.filterNot { it.isAdvanced }
            } else {
                availableDailies
            }
            //a new daily is randomly selected with discarded set to true (only 1 discard within 24h is possible)
            val newDaily: Daily = filteredDailies.shuffled().first()
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

        //this is called to update the random dailies and substitute advanced ones with locator-based if locator mode
        //is selected as gamification mode
        fun updateRandomDailies(userProfile: UserProfile) {
            if (GamificationManager.gamificationMode == GamificationManager.GamificationMode.LOCATOR) {
                val removedCount = userProfile.dailyProgresses.count { it.daily.isAdvanced }
                //remove advanced dailies if gamification mode is set to locator
                userProfile.dailyProgresses.removeAll { it.daily.isAdvanced }
                //find locator-based dailies as substitute
                val assignedDailies = userProfile.dailyProgresses.map { it.daily.name }.toSet()
                val availableDailies = ALL_RANDOM_DAILIES
                    .filter { !it.isAdvanced && it.name !in assignedDailies }
                    .shuffled()
                    .take(removedCount)
                userProfile.assignDailies(availableDailies)
            }
            GamificationManager.updateUserProfile(userProfile)
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

        fun getIsAdvancedFromName(name: String): Boolean {
            return (ALL_RANDOM_DAILIES + ALL_TARGETED_DAILIES)
                .find { it.name == name }
                ?.isAdvanced ?: false
        }

        fun updateDailyProgresses(userProfile: UserProfile, testOutcomes: List<TestOutcome>): List<Pair<Int, Daily?>> {
            val progresses = mutableListOf<Pair<Int, Daily?>>() //to track the progresses (i.e., xp gained and associated daily)

            //find the dailies to update according to gaming mode
            val dailiesToUpdate = when (GamificationManager.assignmentMode) {
                GamificationManager.DailyAssignmentMode.RANDOM -> ALL_RANDOM_DAILIES
                GamificationManager.DailyAssignmentMode.TARGETED -> ALL_TARGETED_DAILIES
                else -> emptyList() //TODO
            }
            //check the daily progress according to gaming mode
            val copyOfDailyProgresses = ArrayList(userProfile.dailyProgresses) //needed since the list is updated during loop
            copyOfDailyProgresses.forEach { dp ->
                dailiesToUpdate.find { it.name == dp.daily.name }?.let {
                    val progress = when (GamificationManager.assignmentMode) {
                        GamificationManager.DailyAssignmentMode.RANDOM -> RANDOM_DAILY_CHECKS[it.name]?.invoke(testOutcomes)
                        GamificationManager.DailyAssignmentMode.TARGETED -> TARGETED_DAILY_CHECKS[it.name]?.invoke(testOutcomes)
                        GamificationManager.DailyAssignmentMode.INCLUSIVE -> TODO()
                    }
                    if (progress!! > 0) {
                        val dailyProgress = update(userProfile, it, progress)
                        progresses.add(dailyProgress)  //TODO:  add a more sophisticated check/return type if a more sophisticated notification must be provided
                    }
                }
            }
            return progresses //this to keep track of any changes and update the GUI
        }

        private fun update(userProfile: UserProfile, daily: Daily, progress: Int): Pair<Int, Daily?> {
            var gainedXP = 0 //to track the gained xp
            var involvedDaily: Daily? = null //to track the involved daily

            if(GamificationManager.assignmentMode == GamificationManager.DailyAssignmentMode.RANDOM) {
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
            else if (GamificationManager.assignmentMode == GamificationManager.DailyAssignmentMode.TARGETED) {
                gainedXP = daily.xp * progress

                //TODO: remove the following, used just for testing
                //gainedXP = 50000000


                involvedDaily = daily
                userProfile.currentXP += gainedXP
            }
            else if (GamificationManager.assignmentMode == GamificationManager.DailyAssignmentMode.INCLUSIVE) {
                //TODO
            }
            return Pair(gainedXP, involvedDaily)
        }




















    }
}
