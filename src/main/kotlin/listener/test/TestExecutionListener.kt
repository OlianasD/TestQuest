package listener.test

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import gamification.GamificationManager
import locator.Locator
import locator.LocatorsAnalyzer
import testquest.TestQuestAction
import java.util.regex.Matcher
import java.util.regex.Pattern

data class TestOutcome(
    val testName: String,
    val className: String,
    val locatorsOld: List<Locator>, //the list of whole locs associated with test BEFORE changes/test execution
    val locatorsNew: List<Locator>, //the list of whole locs associated with test AFTER changes/test execution
    //TODO: locatorsNew is old implementation which may be removed unless all new locs are needed
    // the check for gaming progression is now based on locatorsPassed as only them are counted for progression
    val isPassed: Boolean,
    val stacktrace: String?,
    val errorLine: Int,
    val locatorsPassed: List<Locator>, //the list of locs before a broken loc (if any)
    // TODO: assumption = all locs placed before a error line num are considered 'passed'
    val locatorBroken: Locator? = null, //the locator (if any) that brought to a test failure
    val locatorsNotExecuted: List<Locator> = emptyList() //the list of locs after a broken loc (if any) thus not executed
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
            //old static = previous code change
            //new static = latest code change
            //old dynamic = previous code change BEFORE test execution (used to check dailies)
            //new dynamic = latest code change BEFORE test execution (used to check dailies)
            if(TestQuestAction.locatorsOldStatic.isNotEmpty()) //this to manage the case when tests are run with no locs changed
                TestQuestAction.locatorsOldDynamic = TestQuestAction.locatorsOldStatic
            TestQuestAction.locatorsNewDynamic = TestQuestAction.locatorsNewStatic
            server = Server()
            server.start()
        }
        catch (_: RuntimeException) {}//this to handle the case of tests runned even if TestQuest is not opened
    }

    //this is called at the end of the whole testing process
    override fun onTestingFinished(testsRoot: SMTestProxy.SMRootTestProxy) {
        try {
            server.stop()
            //check how events affected tasks
            GamificationManager.analyzeEvents(testOutcomes)
            testOutcomes.forEach { testOutcome ->
                LocatorsAnalyzer.removeConfirmedFixedLocatorsFromMap(testOutcome.locatorsPassed)
            }
        }
        catch (_: RuntimeException) {}//this to handle the case of tests runned even if TestQuest is not opened
    }

    //this is called at the end of each test execution
    override fun onTestFinished(test: SMTestProxy) {
        try {

            //collect old/new locators + other outcomes related to executed test
            val oldLocatorsInTest = TestQuestAction.locatorsOldDynamic.filter { it.methodName == test.name }
            val newLocatorsInTest = TestQuestAction.locatorsNewDynamic.filter { it.methodName == test.name }

            //find error line (if any), locators successfully passed before it, locator cause of break (if any), and locators not executed as after it
            //notice that:
            // 1. errorLineNum might notify an error that is not related to a locator (e.g., NullPointerExc)
            // 2. brokenLoc refers specifically to a locator that is not found (i.e., NoSuchElementExc)
            val errorLineNum = getLineNumberError(test.stacktrace)
            val passedLocs: List<Locator>
            var brokenLoc: Locator? = null
            var nonExecutedLocs: List<Locator> = emptyList()
            if (errorLineNum != -1) {
                passedLocs = getLocatorsBeforeError(errorLineNum, newLocatorsInTest)
                nonExecutedLocs = getLocatorsAfterError(errorLineNum, newLocatorsInTest)
                //find broken loc from stacktrace
                brokenLoc = newLocatorsInTest.firstOrNull { loc -> test.stacktrace!!.trim().
                    contains("NoSuchElementException") && test.stacktrace!!.contains(loc.locatorValue) }
                if(brokenLoc != null) {
                    val locsAnalizer = LocatorsAnalyzer()
                    locsAnalizer.calculateBrokenLocators(brokenLoc) //update broken locators for next targeted dailies
                }
            }
            else
                passedLocs = newLocatorsInTest
            val testOutcome = TestOutcome(test.name, test.parent.name, oldLocatorsInTest, newLocatorsInTest,
                test.isPassed, test.stacktrace, errorLineNum, passedLocs, brokenLoc, nonExecutedLocs)
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













    private fun getLineNumberError(stackTrace: String?): Int {
        if (stackTrace == null)
            return -1
        val index = stackTrace.indexOf("Test.java:")
        if (index == -1)
            return -1
        //returns line number associated with direct test error, if any
        //TODO: to check when PO are implemented
        val substring = stackTrace.substring(index + "Test.java:".length).trim()
        val lineNumber = substring.takeWhile { it.isDigit() }
        if (lineNumber.isNotEmpty())
            return lineNumber.toInt()
        else
            return -1
    }






    private fun getLocatorsBeforeError(line: Int, locatorsInTest: List<Locator>): List<Locator> {
        return locatorsInTest.filter { locator -> locator.line < line }
    }

    private fun getLocatorsAfterError(line: Int, locatorsInTest: List<Locator>): List<Locator> {
        return locatorsInTest.filter { locator -> locator.line > line }
    }





}