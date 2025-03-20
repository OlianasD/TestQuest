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
                "Convert $DAILY_GOAL existing absolute XPath locators to relative XPath locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription = "Absolute XPath locators are characterized by the tag 'html' at the beginning (e.g., /html/body/div/form/input[2]). " +
                        "You might want to avoid absolute XPaths, as they expose the full page structure, and make them relative to improve flexibility and robustness, " +
                        "ensuring they remain valid if the document structure changes slightly.",
                exampleDescription = "/html/body/div/form/input[2] --> //input[2]"
            ),
            Daily(
                "xpathLength",
                "Reduce the length of $DAILY_GOAL existing XPath locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription =  "The length of an XPath is the number of characters composing it (e.g., /div/form/input[2] has 18 characters). " +
                        "The longer an XPath is, the more prone it is to fragility. " +
                        "You might want to reduce the length of an XPath locator to strengthen it, by finding, if possible, " +
                        "a reference to more internal elements without exposing an overly complex structure.",
                exampleDescription =  "table/tr/div/form/input[2] --> //input[2]"
            ),
            Daily(
                "xpathLevel",
                "Reduce the levels of $DAILY_GOAL existing XPath locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription =
                        "The levels in XPaths are the tags composing them (e.g., /div/form/input[2] has 3 tags).  " +
                        "The more levels an XPath has, the more prone it is to fragility. " +
                        "You might want to reduce the levels of an XPath to strengthen it, by finding, if possible, " +
                        "a reference to more internal elements without exposing an overly complex structure.",
                exampleDescription =  "/div/form/input[2] --> //input[2]"
            ),
            Daily(
                "loc2xpath",
                "Convert $DAILY_GOAL existing non-XPath locators to XPath",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription = "As XPath locators are known as a reliable locator strategy, you might want to have " +
                        "them implemented in the test suite, by converting non-XPath locators to XPath.",
                exampleDescription = "'Click here to proceed' (link-based locator) --> //div/a (XPath-based locator)"
            ),
            Daily(
                "loc2id",
                "Convert $DAILY_GOAL existing non-ID locators to ID",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription = "As ID locators are known as the most reliable locator strategy, you might want to have " +
                        "them implemented in the test suite, if possible, by converting non-ID locators to ID.",
                exampleDescription = "'Click here to proceed' (link-based locator) --> proceedLink (ID-based locator)"
            ),
            Daily(
                "runLocs20",
                "Run 20 locators successfully",
                RANDOM_DAILY_XP,
                20,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription = "Once implemented, locators should be tested in order to validate them and check they do not break. " +
                        "You might want to run the test scripts with edited or newly added locators as soon as possible.",
                exampleDescription = "-"
            ),
            Daily(
                "robustness",
                "Improve the robustness of $DAILY_GOAL existing locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription = "The robustness of a locator defines its ability to withstand future evolutions " +
                        "of the app without breaking, as it does not rely on fragile aspects that change over time. " +
                        "There are specific characteristics that make a locator robust. Usually, ID and relative XPath locators are the best and" +
                        " in most cases, exposing the less is better (e.g., shorter XPaths). " +
                        "You might want to improve the robustness of existing locators to make them less fragile to future changes.",
                exampleDescription = "/div/form/input[2] --> //input[2] OR 'Click here to proceed' (link-based locator) --> proceedLink (ID-based locator)"
            ),
            Daily(
                "lengthShortenMax",
                "Shorten the length of $DAILY_GOAL existing XPath locators below " + GamificationManager.MAX_LENGTH + " characters",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription =  "The length of an XPath is the number of characters composing it (e.g., /div/form/input[2] has 18 characters). " +
                        "The longer an XPath is, the more prone it is to fragility. " +
                        "You might want to reduce the length of an XPath locator below a tolerance value of ${GamificationManager.MAX_LENGTH} characters.",
                exampleDescription = "/div/div/span/form[3]/input[@id='...'] --> //form[3]/input[@id='...'] "
            ),
            Daily(
                "addAttrToXPath",
                "Add $DAILY_GOAL references to robust attributes to existing XPath locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription = "XPaths can refer to predicates, that are expressions enclosed in square brackets that filter" +
                        " based on a condition (e.g., //input[@class='userData']). Some predicates, particularly @id, @name, @class, @title, @alt, and @value," +
                        " are notoriously robust as they refer to conditions that change rarely over time. " +
                        "You might want to add to existing XPath locators references " +
                        "to these robust predicates.",
                exampleDescription = "//div/input --> //div/input[@class='userData']"
            ),
            Daily(
                "remAttrFromXPath",
                "Remove $DAILY_GOAL references to fragile attributes from existing XPath locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription = "XPaths can refer to predicates, that are expressions enclosed in square brackets that filter" +
                        " based on a condition (e.g., //input[@class='userData']). Some predicates, particularly @src, @href, @height, and @width," +
                        " are notoriously fragile as they refer to structural properties that change frequently over time. " +
                        "You might want to remove from existing XPath locators references " +
                        "to these fragile predicates, even by replacing them with other properties.",
                exampleDescription = "//img[@width='358'] --> //img[@class='propic']"
            ),
            Daily(
                "remJSFromXPath",
                "Remove $DAILY_GOAL references to Javascript code from existing XPath locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription = "XPaths can refer to predicates, that are expressions enclosed in square brackets that filter" +
                        " based on a condition (e.g., //input[@class='userData']). Some predicates, particularly @onclick, @onload, @onmouseover, @onmouseout, @onchange, @onsubmit, @onfocus, and @onkeydow," +
                        " are notoriously fragile as they expose Javascript code that may change frequently over time. " +
                        "You might want to remove from existing XPath locators references " +
                        "to these fragile predicates, even by replacing them with other properties.",
                exampleDescription = "//img[@onload=\"showPropic()\"] --> //img[@class='propic']"
            ),
            Daily(
                "levelLoweredMax",
                "Lower the levels of $DAILY_GOAL existing XPath locators below " + GamificationManager.MAX_LEVEL,
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription =
                        "The levels in an XPath is the number of nodes composing it (e.g., /div/form/input[2] has 3 levels).  " +
                        "The more levels an XPath has, the more prone it is to fragility. " +
                        "You might want to reduce the length of an XPath locator below a tolerance value of ${GamificationManager.MAX_LEVEL}.",
                exampleDescription =  "/div/form/input[2] --> //input[2]"
            ),
            Daily(
                "repair",
                "Repair $DAILY_GOAL existing broken locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription = "As locators break sooner or later due to the software evolution, you might want to have them repaired by " +
                        "finding a new value for the locator and, if needed, a new locator strategy."
            ),
            Daily(
                "reducePredicates",
                "Remove $DAILY_GOAL positional predicates from existing XPath locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription ="Since the exposure of positional predicates in XPaths can be a source of locator fragility as they reveal structural information, " +
                        "you might want to reduce their number from XPath locators when possible, even by replacing them with other properties.",
                exampleDescription = "//div[3]/form[@class='userData']/input[2] --> //form[@class='userData']/input[@class='...']"
            ),
            Daily(
                "newXPath",
                "Implement $DAILY_GOAL new XPath locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription = "As XPath locators are known as a reliable locator strategy, you might want to " +
                        "add new XPath locators in the test suite, when needed, potentially avoiding absolute XPaths (those starting from the root element '/html').",
                exampleDescription = "-"
            ),
            Daily(
                "newID",
                "Implement $DAILY_GOAL new ID locators",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription = "As ID locators are known as the most reliable locator strategy, you might want to " +
                        "add new ID locators in the test suite, when needed.",
                exampleDescription = "-"
            ),
            Daily(
                "newLengthShorterMax",
                "Implement $DAILY_GOAL new XPath locators with length below or equal to " + GamificationManager.MAX_LENGTH,
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription =  "The length of an XPath is the number of characters composing it (e.g., /div/form/input[2] has 18 characters). " +
                        "The longer an XPath is, the more prone it is to fragility. " +
                        "You might want to add new XPath locators with the length below or equal a tolerance value of ${GamificationManager.MAX_LENGTH} characters.",
                exampleDescription = "/div/div/span/form[3]/input[@id='...'] --> //form[3]/input[@id='...'] "
            ),
            Daily(
                "newLevelLowerMax",
                "Implement $DAILY_GOAL new XPath locators with levels below or equal to " + GamificationManager.MAX_LEVEL,
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription =  "The levels of an XPath is the number of nodes composing it (e.g., /div/form/input[2] has 3 levels). " +
                        "The more levels an XPath has, the more prone it is to fragility. " +
                        "You might want to add new XPath locators having levels below or equal to a tolerance value of ${GamificationManager.MAX_LEVEL}.",
                exampleDescription =  "/div/form/input[2] --> //input[2]"
            ),
            Daily(
                "newRobust",
                "Implement $DAILY_GOAL new locators with fragility score below ${GamificationManager.FRAGILITY_THRESHOLD}",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription = "The robustness of a locator defines its ability to withstand future evolutions " +
                        "of the app without breaking, as it does not rely on fragile aspects that change over time. " +
                        "There are specific characteristics that make a locator robust rather than fragile. Usually, ID and relative XPath locators are the best and" +
                        " in most cases, exposing the less is better (e.g., shorter XPaths). " +
                        "You might want to add new locators keeping their fragility score below ${GamificationManager.FRAGILITY_THRESHOLD}.",
                exampleDescription = "-"
            ),
            Daily(
                "newWantedAttr",
                "Implement $DAILY_GOAL new XPath locators with references to robust attributes",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription = "XPaths can refer to predicates, that are expressions enclosed in square brackets that filter" +
                        " based on a condition (e.g., //input[@class='userData']). Some predicates, particularly @id, @name, @class, @title, @alt, and @value," +
                        " are notoriously robust as they refer to conditions that change rarely over time. " +
                        "You might want to add new XPath locators with references " +
                        "to these robust predicates.",
                exampleDescription = "-"
            ),
            Daily(
                "newUnwantedAttr",
                "Implement $DAILY_GOAL new XPath locators with no references to fragile attributes",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription = "XPaths can refer to predicates, that are expressions enclosed in square brackets that filter" +
                        " based on a condition (e.g., //input[@class='userData']). Some predicates, particularly @src, @href, @height, and @width," +
                        " are notoriously fragile as they refer to structural properties that change frequently over time. " +
                        "You might want to add new XPath locators avoiding references " +
                        "to these fragile predicates.",
                exampleDescription = "-"
            ),
            Daily(
                "newJS",
                "Implement $DAILY_GOAL new XPath locators with no references to Javascript code",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription = "XPaths can refer to predicates, that are expressions enclosed in square brackets that filter" +
                        " based on a condition (e.g., //input[@class='userData']). Some predicates, particularly @onclick, @onload, @onmouseover, @onmouseout, @onchange, @onsubmit, @onfocus, and @onkeydow," +
                        " are notoriously fragile as they expose Javascript code that may change frequently over time. " +
                        "You might want to add new XPath locators avoiding references " +
                        "to these fragile predicates.",
                exampleDescription = "-"
            ),
            Daily(
                "newLowPredicates",
                "Implement $DAILY_GOAL new XPath locators with up to " + GamificationManager.MAX_POS_PRED + " positional predicates",
                RANDOM_DAILY_XP,
                DAILY_GOAL,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.RANDOM.name,
                additionalDescription ="Since the exposure of positional predicates in XPaths can be a source of locator fragility as they reveal structural information, " +
                        "you might want to add new XPath locators with a number of predicates equal or below the threshold of ${GamificationManager.MAX_POS_PRED} predicates, when possible.",
                exampleDescription = "-"
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
                "Convert the following absolute XPath locators to relative",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name,
                additionalDescription = "Absolute XPath locators are characterized by the tag 'html' at the beginning (e.g., /html/body/div/form/input[2]). " +
                        "You might want to avoid absolute XPaths, as they expose the full page structure, and make them relative to improve flexibility and robustness, " +
                        "ensuring they remain valid if the document structure changes slightly.",
                exampleDescription = "/html/body/div/form/input[2] --> //input[2]"
            ),
            Daily(
                "length",
                "Reduce the length of the following XPath locators below or equal to ${GamificationManager.MAX_LENGTH}",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name,
                additionalDescription =  "The length of an XPath is the number of characters composing it (e.g., /div/form/input[2] has 18 characters). " +
                        "The longer an XPath is, the more prone it is to fragility. " +
                        "You might want to reduce the length of an XPath locator below or equal to a tolerance value of ${GamificationManager.MAX_LENGTH} characters, to strengthen it, by finding, if possible, " +
                        "a reference to more internal elements without exposing an overly complex structure.",
                exampleDescription =  "table/tr/div/form/input[2] --> //input[2]"
            ),
            Daily(
                "level",
                "Reduce the levels of the following XPath locators below or equal to ${GamificationManager.MAX_LEVEL}",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name,
                additionalDescription =
                "The levels in XPaths are the tags composing them (e.g., /div/form/input[2] has 3 tags).  " +
                        "The more levels an XPath has, the more prone it is to fragility. " +
                        "You might want to reduce the levels of an XPath below or equal to a tolerance value of ${GamificationManager.MAX_LEVEL}, to strengthen it, by finding, if possible, " +
                        "a reference to more internal elements without exposing an overly complex structure.",
                exampleDescription =  "/div/form/input[2] --> //input[2]"
            ),
            Daily(
                "posPredicate",
                "Remove positional predicates and keep them up to ${GamificationManager.MAX_POS_PRED} from the following XPath locators",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name,
                additionalDescription ="Since the exposure of positional predicates in XPaths can be a source of locator fragility as they reveal structural information, " +
                        "you might want to reduce their number from XPath locators when possible, even by replacing them with other properties.",
                exampleDescription = "//div[3]/form[@class='userData']/input[2] --> //form[@class='userData']/input[@class='...']"
            ),
            Daily(
                "badPredicate",
                "Remove all the fragile predicates from the following XPath locators",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name,
                additionalDescription = "XPaths can refer to predicates, that are expressions enclosed in square brackets that filter" +
                        " based on a condition (e.g., //input[@class='userData']). Some predicates, particularly @src, @href, @height, @width, @onclick, @onload, @onmouseover, @onmouseout, @onchange, @onsubmit, @onfocus, and @onkeydown," +
                        " are notoriously fragile as they expose to structural properties or Javascript code that can change frequently over time. " +
                        "You might want to remove from existing XPath locators references " +
                        "to these fragile predicates, even by replacing them with other properties.",
                exampleDescription = "//img[@width='358'] --> //img[@class='propic']"
            ),
            Daily(
                "noIDOrXPath",
                "Convert the following non-ID/XPath locators into ID/XPath",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name,
                additionalDescription = "As ID and relative XPath locators are known as the most reliable locator strategies, you might want to have " +
                        "them implemented in the test suite, if possible, by converting non-ID or non-XPath locators to ID and XPath locators.",
                exampleDescription = "'Click here to proceed' (link-based locator) --> //a[2] (XPath-based locator)"
            ),
            Daily(
                "broken",
                "Repair the following broken locators",
                TARGETED_DAILY_XP,
                null,
                FilePathSolver.DAILY_PICS_PATH,
                GamificationManager.DailyAssignmentMode.TARGETED.name,
                additionalDescription = "As locators break sooner or later due to the software evolution, you might want to have them repaired by " +
                        "finding a new value for the locator and, if needed, a new locator strategy."
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
            "levelLoweredMax" to { testOutcomes -> checkLoweredLevelMax(testOutcomes) },
            "repair" to { testOutcomes -> checkRepair(testOutcomes) },
            "reducePredicates" to { testOutcomes -> reducePredicates(testOutcomes) },
            "newXPath" to { testOutcomes -> checkNewXPath(testOutcomes) },
            "newID" to { testOutcomes -> checkNewID(testOutcomes) },
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

        /******* TARGETED DAILY CHECKS ABOUT LOCATORS *******/

        private fun checkTargetedAbsoluteRemoved(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            for (passedLoc in passedLocs) {
                for (oldLoc in oldLocs) {
                    if(passedLoc.hashCode() != oldLoc.hashCode())
                        continue
                    if (passedLoc.locatorType.equals("xpath", ignoreCase = true) &&
                        !passedLoc.locatorValue.startsWith("/html") &&
                        oldLoc.locatorValue.startsWith("/html"))
                        count++
                    break
                }
            }
            return count
        }

        private fun checkTargetedLengthReduced(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            for (passedLoc in passedLocs) {
                for (oldLoc in oldLocs) {
                    if(passedLoc.hashCode() != oldLoc.hashCode())
                        continue
                    if (passedLoc.locatorValue.length <= GamificationManager.MAX_LENGTH &&
                        oldLoc.locatorValue.length > GamificationManager.MAX_LENGTH)
                        count++
                    break
                }
            }
            return count
        }

        private fun checkTargetedLevelReduced(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            for (passedLoc in passedLocs) {
                for (oldLoc in oldLocs) {
                    if(passedLoc.hashCode() != oldLoc.hashCode())
                        continue
                    val oldLevels = oldLoc.locatorValue.split("/").count { it.isNotEmpty() }
                    val newLevels = passedLoc.locatorValue.split("/").count { it.isNotEmpty() }
                    if (GamificationManager.MAX_LEVEL in newLevels..<oldLevels)
                        count++
                    break
                }
            }
            return count
        }

        private fun checkTargetedPosPredicateRemoved(testOutcomes: List<TestOutcome>): Int {
            val predicateRegex = Regex("\\[\\d+]")
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            for (passedLoc in passedLocs) {
                for (oldLoc in oldLocs) {
                    if(passedLoc.hashCode() != oldLoc.hashCode())
                        continue
                    val oldPredicateCount = oldLoc.locatorValue.let { predicateRegex.findAll(it).count() }
                    val newPredicateCount = passedLoc.locatorValue.let { predicateRegex.findAll(it).count() }
                    if (GamificationManager.MAX_POS_PRED in newPredicateCount..<oldPredicateCount)
                        count++
                    break
                }
            }
            return count
        }

        private fun checkTargetedBadPredicateRemoved(testOutcomes: List<TestOutcome>): Int {
            val problematicAttributes = GamificationManager.BAD_PREDS + GamificationManager.BAD_JS
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            for (passedLoc in passedLocs) {
                for (oldLoc in oldLocs) {
                    if(passedLoc.hashCode() != oldLoc.hashCode())
                        continue
                    val hadBadPredicate = problematicAttributes.any { attr ->
                        oldLoc.locatorValue.contains(attr, ignoreCase = true)
                    }
                    val isResolved = problematicAttributes.none { attr ->
                        passedLoc.locatorValue.contains(attr, ignoreCase = true)
                    }
                    if (hadBadPredicate && isResolved)
                        count++
                    break
                }
            }
            return count
        }

        private fun checkTargetedNoIDNoXpathChanged(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            for (passedLoc in passedLocs) {
                for (oldLoc in oldLocs) {
                    if(passedLoc.hashCode() != oldLoc.hashCode())
                        continue
                    val wasNonIDOrXpath = !oldLoc.locatorType.equals("id", ignoreCase = true) &&
                            !oldLoc.locatorType.equals("xpath", ignoreCase = true)
                    val isNowIDOrXpath = passedLoc.locatorType.equals("id", ignoreCase = true) ||
                            passedLoc.locatorType.equals("xpath", ignoreCase = true)
                    if (wasNonIDOrXpath && isNowIDOrXpath)
                        count++
                    break
                }
            }
            return count
        }

        private fun checkTargetedBrokenLocsRepaired(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val locsAnalizer = LocatorsAnalyzer()
            val brokenLocs = GamificationManager.userProfile.dailyProgresses
                .find { it.daily.name == "broken" }
                ?.daily?.targetedLocators
            val repairedLocators = mutableListOf<Locator>()
            if(brokenLocs.isNullOrEmpty())
                return 0
            //for each test, count previously broken loc that is no more present in stacktrace (if any) of the test
            for(brokenLoc in brokenLocs)
                for (testOutcome in testOutcomes) {
                    if (testOutcome.locatorsPassed.contains(brokenLoc)) {
                        val stackTrace = testOutcome.stacktrace?.trim()
                        //if there is no stacktrace or the stacktrace does not include the now repaired loc
                        if(stackTrace == null || !stackTrace.contains(brokenLoc.locatorValue)) {
                            count++
                            repairedLocators.add(brokenLoc)
                            break
                        }
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
                .flatMap { it.poMethodCallsExercised }
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
                .flatMap { it.poMethodCallsExercised }
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
                .flatMap { it.poMethodCallsExercised }
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
                .flatMap { it.poMethodCallsExercised }
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
                    outcome.poMethodCallsExercised.any { it.methodName == newMethod.methodName }
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
            val passedMethods = testOutcomes.flatMap { it.poMethodCallsExercised.map { call -> call.methodName } }.toSet()
            count = passedMethods.count { it in commonMethodsMoved }
            return count
        }





        /******* TARGETED DAILY CHECKS ABOUT TESTS *******/

















        /******* RANDOM DAILY CHECKS ABOUT LOCATORS *******/

        private fun checkAbsXPathRemoved(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            for (passedLoc in passedLocs) {
                for (oldLoc in oldLocs) {
                    if(passedLoc.hashCode() != oldLoc.hashCode())
                        continue
                    if (oldLoc.locatorType.equals("xpath", ignoreCase = true) &&
                        oldLoc.locatorValue.startsWith("/html") &&
                        passedLoc.locatorType.equals("xpath", ignoreCase = true) &&
                        !passedLoc.locatorValue.startsWith("/html"))
                            count++
                    break
                }
            }
            return count
        }


        private fun checkXPathLengthReduced(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            for (passedLoc in passedLocs) {
                for (oldLoc in oldLocs) {
                    if(passedLoc.hashCode() != oldLoc.hashCode())
                        continue
                    if (oldLoc.locatorType.equals("xpath", ignoreCase = true) &&
                        passedLoc.locatorType.equals("xpath", ignoreCase = true)
                        && passedLoc.locatorValue.length < oldLoc.locatorValue.length)
                        count++
                    break
                }
            }
            return count
        }

        private fun checkXPathLevelReduced(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            for (passedLoc in passedLocs) {
                for (oldLoc in oldLocs) {
                    if(passedLoc.hashCode() != oldLoc.hashCode())
                        continue
                    if (oldLoc.locatorType.equals("xpath", ignoreCase = true) &&
                        passedLoc.locatorType.equals("xpath", ignoreCase = true) &&
                        passedLoc.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size <
                                oldLoc.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size)
                        count++
                    break
                }
            }
            return count
        }

        private fun checkLocs2XPathConverted(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            for (passedLoc in passedLocs) {
                for (oldLoc in oldLocs) {
                    if(passedLoc.hashCode() != oldLoc.hashCode())
                        continue
                    if (!oldLoc.locatorType.equals("xpath", ignoreCase = true) &&
                     passedLoc.locatorType.equals("xpath", ignoreCase = true))
                        count++
                    break
                }
            }
            return count
        }

        private fun checkLocs2IDConverted(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            for (passedLoc in passedLocs) {
                for (oldLoc in oldLocs) {
                    if(passedLoc.hashCode() != oldLoc.hashCode())
                        continue
                    if (!oldLoc.locatorType.equals("id", ignoreCase = true) &&
                        passedLoc.locatorType.equals("id", ignoreCase = true))
                        count++
                    break
                }
            }
            return count
        }

        private fun checkRobustnessImprovement(testOutcomes: List<TestOutcome>): Int{
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            for (passedLoc in passedLocs) {
                for (oldLoc in oldLocs) {
                    if(passedLoc.hashCode() != oldLoc.hashCode())
                        continue
                    if (computeFragilityCoefficient(passedLoc) <  computeFragilityCoefficient(oldLoc))
                        count++
                    break
                }
            }
            return count
        }

        private fun checkShortenedLengthMax(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            for (passedLoc in passedLocs) {
                for (oldLoc in oldLocs) {
                    if(passedLoc.hashCode() != oldLoc.hashCode())
                        continue
                    if (oldLoc.locatorType.equals("xpath", ignoreCase = true) &&
                        oldLoc.locatorValue.length > GamificationManager.MAX_LENGTH &&
                        passedLoc.locatorType.equals("xpath", ignoreCase = true) &&
                        passedLoc.locatorValue.length <= GamificationManager.MAX_LENGTH)
                        count++
                    break
                }
            }
            return count
        }

        private fun checkWantedAttrsInXPaths(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            for (passedLoc in passedLocs) {
                for (oldLoc in oldLocs) {
                    if(passedLoc.hashCode() != oldLoc.hashCode())
                        continue
                    if (oldLoc.locatorType.equals("xpath", ignoreCase = true) &&
                        passedLoc.locatorType.equals("xpath", ignoreCase = true))
                            count += countWantedAttributes(passedLoc.locatorValue) -
                                    countWantedAttributes(oldLoc.locatorValue)
                    break
                }
            }
            return count
        }

        private fun checkUnwantedAttrsInXPaths(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            for (passedLoc in passedLocs) {
                for (oldLoc in oldLocs) {
                    if(passedLoc.hashCode() != oldLoc.hashCode())
                        continue
                    if (oldLoc.locatorType.equals("xpath", ignoreCase = true) &&
                        passedLoc.locatorType.equals("xpath", ignoreCase = true))
                        count += countUnwantedAttributes(oldLoc.locatorValue) -
                                countUnwantedAttributes(passedLoc.locatorValue)
                    break
                }
            }
            return count
        }

        private fun checkJSInXPaths(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            for (passedLoc in passedLocs) {
                for (oldLoc in oldLocs) {
                    if(passedLoc.hashCode() != oldLoc.hashCode())
                        continue
                    if (oldLoc.locatorType.equals("xpath", ignoreCase = true)&&
                        passedLoc.locatorType.equals("xpath", ignoreCase = true))
                        count += countJavaScriptReferences(oldLoc.locatorValue) -
                                    countJavaScriptReferences(passedLoc.locatorValue)
                    break
                }
            }
            return count
        }

        private fun checkLoweredLevelMax(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            for (passedLoc in passedLocs) {
                for (oldLoc in oldLocs) {
                    if(passedLoc.hashCode() != oldLoc.hashCode())
                        continue
                    if (oldLoc.locatorType.equals("xpath", ignoreCase = true) &&
                        oldLoc.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size > GamificationManager.MAX_LEVEL &&
                        passedLoc.locatorType.equals("xpath", ignoreCase = true) &&
                        passedLoc.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size <= GamificationManager.MAX_LEVEL)
                        count++
                    break
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
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            for (passedLoc in passedLocs) {
                for (oldLoc in oldLocs) {
                    if(passedLoc.hashCode() != oldLoc.hashCode())
                        continue
                    if (oldLoc.locatorType.equals("xpath", ignoreCase = true) && passedLoc.locatorType.equals("xpath", ignoreCase = true)) {
                        val oldPredicatesCount = "\\[\\d+\\]".toRegex().findAll(oldLoc.locatorValue).count()
                        val newPredicatesCount = "\\[\\d+\\]".toRegex().findAll(passedLoc.locatorValue).count()
                            count += oldPredicatesCount - newPredicatesCount
                    }
                    break
                }
            }
            return count
        }

        private fun checkNewXPath(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            val newPassedLocs = passedLocs.filter { passedLoc ->
                passedLoc.locatorType.equals("xpath", ignoreCase = true) &&
                oldLocs.all { oldLoc -> oldLoc.hashCode() != passedLoc.hashCode() }
            }
            count += newPassedLocs.size
            return count
        }


        private fun checkNewID(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            val newPassedLocs = passedLocs.filter { passedLoc ->
                passedLoc.locatorType.equals("id", ignoreCase = true) &&
                oldLocs.all { oldLoc -> oldLoc.hashCode() != passedLoc.hashCode() }
            }
            count += newPassedLocs.size
            return count
        }

        private fun checkNewXPathLengthMax(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            val newPassedLocs = passedLocs.filter { passedLoc ->
                passedLoc.locatorType.equals("xpath", ignoreCase = true) &&
                passedLoc.locatorValue.length <= GamificationManager.MAX_LENGTH &&
                oldLocs.none { oldLoc -> oldLoc.hashCode() == passedLoc.hashCode() }
            }
            count += newPassedLocs.size
            return count
        }

        private fun checkNewXPathLevelMax(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            val newPassedLocs = passedLocs.filter { passedLoc ->
                passedLoc.locatorType.equals("xpath", ignoreCase = true) &&
                passedLoc.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size <= GamificationManager.MAX_LEVEL &&
                oldLocs.none { oldLoc -> oldLoc.hashCode() == passedLoc.hashCode() }
            }
            count += newPassedLocs.size
            return count
        }

        private fun checkNewRobust(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            val newPassedLocs = passedLocs.filter { passedLoc ->
                computeFragilityCoefficient(passedLoc) <= GamificationManager.FRAGILITY_THRESHOLD &&
                oldLocs.none { oldLoc -> oldLoc.hashCode() == passedLoc.hashCode() }
            }
            count += newPassedLocs.size
            return count
        }

        private fun checkNewXPathWithWantedAttrs(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            val newPassedLocs = passedLocs.filter { passedLoc ->
                    passedLoc.locatorType.equals("xpath", ignoreCase = true) &&
                    GamificationManager.GOOD_PREDS.any { attribute ->
                        passedLoc.locatorValue.contains(attribute, ignoreCase = true) }
                            &&
                    oldLocs.none { oldLoc -> oldLoc.hashCode() == passedLoc.hashCode()
                }
            }
            count += newPassedLocs.size
            return count
        }

        private fun checkNewXPathWithoutUnwantedAttrs(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            val newPassedLocs = passedLocs.filter { passedLoc ->
                passedLoc.locatorType.equals("xpath", ignoreCase = true) &&
                GamificationManager.BAD_PREDS.none { attribute -> passedLoc.locatorValue.contains(attribute, ignoreCase = true) } &&
                oldLocs.none { oldLoc -> oldLoc.hashCode() == passedLoc.hashCode() }
            }
            count += newPassedLocs.size
            return count
        }

        private fun checkNewXPathWithoutJS(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            val newPassedLocs = passedLocs.filter { passedLoc ->
                passedLoc.locatorType.equals("xpath", ignoreCase = true) &&
                GamificationManager.BAD_JS.none { attribute -> passedLoc.locatorValue.contains(attribute, ignoreCase = true) } &&
                oldLocs.none { oldLoc -> oldLoc.hashCode() == passedLoc.hashCode() }
            }
            count += newPassedLocs.size
            return count
        }

        private fun checkRunLoc20(testOutcomes: List<TestOutcome>): Int {
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            return passedLocs.size
        }

        private fun checkNewXPathWithFewPosPredicates(testOutcomes: List<TestOutcome>): Int {
            var count = 0
            val regex = "\\[\\d+\\]".toRegex() //to count positional predicates
            val passedLocs = testOutcomes
                .flatMap { it.locatorsPassed }
                .toSet()
            val oldLocs = testOutcomes
                .flatMap { it.locatorsOld }
                .toSet()
            val newPassedLocs = passedLocs.filter { passedLoc ->
                passedLoc.locatorType.equals("xpath", ignoreCase = true) &&
                regex.findAll(passedLoc.locatorValue).count() <= GamificationManager.MAX_POS_PRED &&
                oldLocs.none { oldLoc -> oldLoc.hashCode() == passedLoc.hashCode() }
            }
            count += newPassedLocs.size
            return count
        }





        /******* RANDOM DAILY CHECKS ABOUT PAGE OBJECTS *******/

        private fun checkPOAdded(testOutcomes: List<TestOutcome>): Int {
            val oldPONames = TestQuestAction.POsOld.map { it.name }.toSet()
            for (testOutcome in testOutcomes)
                //check from passed PO calls any call that is related to a new PO
                for (poCall in testOutcome.poMethodCallsExercised) {
                    val poName = poCall.pageObject
                    if (poName !in oldPONames)
                        return 1
                }
            return 0
        }

        private fun checkPOMethodAdded(testOutcomes: List<TestOutcome>): Int {
            for (testOutcome in testOutcomes)
                //check from passed PO calls any call that is related to a new method
                for (poCall in testOutcome.poMethodCallsExercised) {
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
                for (passedMethod in testOutcome.poMethodCallsExercised) {
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
                .flatMap { it.poMethodCallsExercised }
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
                .flatMap { it.poMethodCallsExercised }
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
                if (testOutcome.poMethodCallsExercised.any { it.methodName in commonMethodsMoved })
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
                        outcome.poMethodCallsExercised.any { it.methodName == methodName.methodName }
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
                        outcome.poMethodCallsExercised.any { it.methodName == newCall.methodName }
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
                    issuesInPOs = posForDaily,
                    isAdvanced = daily.isAdvanced,
                    additionalDescription = daily.additionalDescription,
                    exampleDescription = daily.exampleDescription
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

        fun getAdditionalDescriptionFromName(name: String): String {
            return (ALL_RANDOM_DAILIES + ALL_TARGETED_DAILIES)
                .find { it.name == name }
                ?.additionalDescription!!
        }

        fun getExampleDescriptionFromName(name: String): String {
            return (ALL_RANDOM_DAILIES + ALL_TARGETED_DAILIES)
                .find { it.name == name }
                ?.exampleDescription!!
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
