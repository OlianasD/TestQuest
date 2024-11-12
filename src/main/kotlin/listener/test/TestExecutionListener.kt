package listener.test

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import gamification.GamificationManager
import locator.Locator
import testquest.TestQuestAction
import java.util.regex.Matcher
import java.util.regex.Pattern

data class TestOutcome(
    val testName: String,
    val className: String,
    val locatorsOld: List<Locator>,
    val locatorsNew: List<Locator>,
    val isPassed: Boolean,
    val stacktrace: String?
) {
    override fun hashCode(): Int {
        return 31 * (testName.hashCode() + className.hashCode())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TestOutcome) return false
        return testName == other.testName && className == other.className
    }

    fun hasSameStacktrace(otherStackTrace: String): Boolean {
        val thisStacktrace = extractNoSuchElementInfo(this.stacktrace)
        val otherStacktrace = extractNoSuchElementInfo(otherStackTrace)
        return thisStacktrace == otherStacktrace
    }

    private fun extractNoSuchElementInfo(stackTrace: String?): String {
        if (stackTrace == "" || stackTrace == null)
            return ""
        val pattern: Pattern = Pattern.compile(
            "org.openqa.selenium.NoSuchElementException:.*?Unable to locate element: \\{.*?\\}",
            Pattern.DOTALL
        )
        val stackTraceAsCharSeq: CharSequence = stackTrace ?: ""
        val matcher: Matcher = pattern.matcher(stackTraceAsCharSeq)
        if (matcher.find())
           return matcher.group()
        return ""
    }
}


class TestExecutionListener : SMTRunnerEventsListener {

    private lateinit var server: Server
    private val testOutcomes = mutableListOf<TestOutcome>() //to collect the outcome of each test

    override fun onTestingStarted(testsRoot: SMTestProxy.SMRootTestProxy) {
        testOutcomes.clear() //TODO: currently, it resets the testOutcomes at the beginning of each new run
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
                TestOutcome(test.name, test.parent.name, oldLocatorsInTest, newLocatorsInTest, test.isPassed, test.stacktrace)
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