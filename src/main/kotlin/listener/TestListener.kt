package listener

import com.example.demo.TestQuestAction
import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.openapi.project.Project
import gamification.GamificationManager
import locator.Locator
import locator.LocatorsExtractor
import locator.LocatorsFragilityCalculator
import ui.GUIManager
import utils.TestFilesExtractor


data class TestOutcome(
    val testName: String,
    val locatorsOld: List<Locator>,
    val locatorsNew: List<Locator>,
    val isPassed: Boolean,
    val stacktrace: String?
)

class TestListener(private val project: Project) : SMTRunnerEventsListener {

    private lateinit var server: Server
    //private lateinit var locatorsNew: List<Locator>
    //private lateinit var locatorsOld: List<Locator>
    private val testOutcomes = mutableListOf<TestOutcome>() //to collect the outcome of each test

    override fun onTestingStarted(testsRoot: SMTestProxy.SMRootTestProxy) {
        try {
            val testFilePaths = TestFilesExtractor.findTestFilePaths(project)//test files are identified
            //first, locators are extracted during plugin initialization (see TestQuestAction) and saved
            //then, before each run (as we assume locators may have changed), new locators are extracted, saving old ones
            val extractor = LocatorsExtractor()
            if (TestQuestAction.locatorsNew.isNotEmpty()) {//the first run we have to save locators from plugin initialization
                //locatorsOld = TestQuestAction.locatorsNew
                TestQuestAction.locatorsOld = TestQuestAction.locatorsNew
                TestQuestAction.locatorsNew = emptyList()
            } else //after the first run, old locators will be saved considering the previous state
            //locatorsOld = locatorsNew
                TestQuestAction.locatorsOld = TestQuestAction.locatorsNew
            //locatorsNew = testFilePaths.flatMap { extractor.parseLocators(it) }
            TestQuestAction.locatorsNew = testFilePaths.flatMap { extractor.parseLocators(it) }
            server = Server()
            server.start()
            println("Server started!")
        }
        catch (_: RuntimeException) {}//this to handle the case of tests runned even if TestQuest is not opened
    }

    override fun onTestingFinished(testsRoot: SMTestProxy.SMRootTestProxy) {
        try {
            server.stop()
            println("Server stopped!")
            //estimate overall fragility and show it on GUI
            val locEstimator = LocatorsFragilityCalculator()
            val estimation = locEstimator.calculateOverallFragility(TestQuestAction.locatorsNew)
            GUIManager.showOverallLocsFragilityScore(estimation)
            //check how events affected tasks
            GamificationManager.analyzeEvents(testOutcomes)
        }
        catch (_: RuntimeException) {}//this to handle the case of tests runned even if TestQuest is not opened
    }

    override fun onTestFinished(test: SMTestProxy) {
        try {
            println("Test finished!")
            val eventList = Server.events
            println("Events:")
            for (event in eventList) {
                println(event)
            }
            //collect old/new locators + other outcomes related to executed test
            //val oldLocatorsInTest = locatorsOld.filter { it.methodName == test.name }
            val oldLocatorsInTest = TestQuestAction.locatorsOld.filter { it.methodName == test.name }
            //val newLocatorsInTest = locatorsNew.filter { it.methodName == test.name }
            val newLocatorsInTest = TestQuestAction.locatorsNew.filter { it.methodName == test.name }
            val testOutcome =
                TestOutcome(test.name, oldLocatorsInTest, newLocatorsInTest, test.isPassed, test.stacktrace)
            testOutcomes.add(testOutcome)
        }
        catch (_: RuntimeException) {}//this to handle the case of tests runned even if TestQuest is not opened
    }




    override fun onTestsCountInSuite(count: Int) {
    }

    override fun onTestStarted(test: SMTestProxy) {
    }

    override fun onTestFailed(test: SMTestProxy) {
    }

    override fun onTestIgnored(test: SMTestProxy) {
    }

    override fun onSuiteFinished(suite: SMTestProxy) {
    }

    override fun onSuiteStarted(suite: SMTestProxy) {
    }

    override fun onCustomProgressTestsCategory(categoryName: String?, testCount: Int) {
    }

    override fun onCustomProgressTestStarted() {
    }

    override fun onCustomProgressTestFailed() {
    }

    override fun onCustomProgressTestFinished() {
    }

    override fun onSuiteTreeNodeAdded(testProxy: SMTestProxy?) {
    }

    override fun onSuiteTreeStarted(suite: SMTestProxy?) {
    }


















}