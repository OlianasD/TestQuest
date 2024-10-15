package listener.test

import testquest.TestQuestAction
import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.openapi.project.Project
import gamification.GamificationManager
import locator.Locator

data class TestOutcome(
    val testName: String,
    val locatorsOld: List<Locator>,
    val locatorsNew: List<Locator>,
    val isPassed: Boolean,
    val stacktrace: String?
)

class TestListener(private val project: Project) : SMTRunnerEventsListener {

    private lateinit var server: Server
    private val testOutcomes = mutableListOf<TestOutcome>() //to collect the outcome of each test

    override fun onTestingStarted(testsRoot: SMTestProxy.SMRootTestProxy) {
        try {
            //locs static i.e., any change to code before test execution
            //locs dynamic i.e., those used to check progression following actual execution
            //old = those retrieved at plugin start and just after each test run (any change following is new)
            //new = those just before each test run (to collect any possible change on code)
            if (TestQuestAction.locatorsNewDynamic.isNotEmpty())
                TestQuestAction.locatorsOldDynamic = TestQuestAction.locatorsNewDynamic
            TestQuestAction.locatorsNewDynamic = TestQuestAction.locatorsNewStatic //retrieve locators at the start of testing
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
            val oldLocatorsInTest = TestQuestAction.locatorsOldDynamic.filter { it.methodName == test.name }
            val newLocatorsInTest = TestQuestAction.locatorsNewDynamic.filter { it.methodName == test.name }
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