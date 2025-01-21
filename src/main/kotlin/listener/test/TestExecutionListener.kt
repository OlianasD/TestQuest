package listener.test

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import gamification.GamificationManager
import extractor.locator.Locator
import locator.LocatorsAnalyzer
import testquest.TestQuestAction
import java.util.regex.Matcher
import java.util.regex.Pattern



data class TestOutcome(
    val testName: String,
    val className: String,
    val locatorsOld: List<Locator>, //the list of whole locs associated with test BEFORE changes that brought to test execution
    val locatorsNew: List<Locator>, //the list of whole locs associated with test FINAL changes that brought to test execution
    val isPassed: Boolean,
    val stacktrace: String?,
    val errorLine: Int,
    val locatorsPassed: List<Locator>, //the list of locs before a broken loc (if any)
    // TODO: assumption = all locs placed before a error line num are considered 'passed'
    // TODO: at the moment, we expect test cases to call PO methods, where PO methods should NOT call other PO methods
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
        testOutcomes.clear() //TODO: the testOutcomes is reset at the beginning of each new run. we may want to save it for future achievements
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
        catch (_: RuntimeException) {}//this to handle the case of tests run even if TestQuest is not opened
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
        catch (_: RuntimeException) {}//this to handle the case of tests run even if TestQuest is not opened
    }

    override fun onTestFinished(test: SMTestProxy) {
        try {
            // TEST OUTCOME IS BUILD AS FOLLOWS:
            // 1. COLLECT OLD/NEW LOCATORS FROM TEST AND PO METHODS USED BY TEST
            val (oldLocatorsInTest, newLocatorsInTest) = collectLocators(test)
            // 2. FIND ERROR LINE (IF ANY), LOCATORS PASSED/BROKEN (IF ANY)/UNEXERCISED (IF ANY)
            val analysisResult = analyzeErrorInfo(test, newLocatorsInTest)
            val testOutcome = TestOutcome(
                test.name,
                test.parent.name,
                oldLocatorsInTest,
                newLocatorsInTest,
                test.isPassed,
                test.stacktrace,
                analysisResult.testErrorLineNum,
                analysisResult.passedLocs,
                analysisResult.brokenLoc,
                analysisResult.unexercisedLocs
            )
            testOutcomes.add(testOutcome)
        } catch (_: RuntimeException) {
            // this to handle the case of tests run even if TestQuest is not opened
        }
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



















    // extractFailInfoFromStacktrace() returns a error info,
    // i.e., a structure that captures info of test failed as well as PageObject failed methods
    // errorInfo.first = List(<PO class name, PO method name, error line>), errorInfo.second = <Test class name, Test method name, error line>
    private fun extractFailInfoFromStacktrace(stackTrace: String?): Pair<List<Triple<String, String, Int>>, Triple<String, String, Int>?>? {
        if (stackTrace == null)
            return null
        val pageRegex = Regex("""(\w+Page)\.(\w+)\((\w+Page\.java):(\d+)\)""")
        val testRegex = Regex("""(\w*Test)\.(\w+)\((\w*Test\.java):(\d+)\)""")
        //trace failures about POs
        /*e.g., at com.example.po.AdminPage.doAction(AdminPage.java:23)
                at com.example.po.LoginPage.doLogin(LoginPage.java:55)
                ...
        */
        val pageObjects = pageRegex.findAll(stackTrace).map { match ->
            val (poName, poMethodName, poLineNumber) = match.destructured
            Triple(poName, poMethodName, poLineNumber.toInt())
        }.toList()
        //trace failure about test (always one)
        //e.g., at com.example.tests.LoginTest.testLogin(LoginTest.java:49)
        val failedTest = testRegex.find(stackTrace)?.let { match ->
            val (testClassName, testMethodName, testLineNumber) = match.destructured
            Triple(testClassName, testMethodName, testLineNumber.toInt())
        }
        return Pair(pageObjects, failedTest)
    }

    //collect locators from test
    private fun collectLocators(test: SMTestProxy): Pair<MutableList<Locator>, MutableList<Locator>> {
        // TODO: currently, no PO method calling other PO methods is currently managed
        //first, get locators from test only
        val locationUrl = test.locationUrl
        val testClassName = locationUrl!!.removePrefix("java:test://")
        val oldLocatorsInTest: MutableList<Locator> = TestQuestAction.locatorsOldDynamic
            .filter { it.methodName == test.name && it.className == testClassName }
            .toMutableList()
        val newLocatorsInTest: MutableList<Locator> = TestQuestAction.locatorsNewDynamic
            .filter { it.methodName == test.name && it.className == testClassName }
            .toMutableList()
        //then, retrieve locators from PO method calls
        val oldPageObjectCalls = TestQuestAction.TestInfoOld[test.name] ?: emptyList()
        for (call in oldPageObjectCalls) {
            val oldPageObject = TestQuestAction.POsOld.find { it.name == call.pageObject }
            val oldMethodInfo = oldPageObject?.methods?.find { it.name == call.method }
            if (oldMethodInfo != null) oldLocatorsInTest.addAll(oldMethodInfo.locators)
        }
        val newPageObjectCalls = TestQuestAction.TestInfoNew[test.name] ?: emptyList()
        for (call in newPageObjectCalls) {
            val newPageObject = TestQuestAction.POsNew.find { it.name == call.pageObject }
            val newMethodInfo = newPageObject?.methods?.find { it.name == call.method }
            if (newMethodInfo != null) newLocatorsInTest.addAll(newMethodInfo.locators)
        }
        return Pair(oldLocatorsInTest, newLocatorsInTest)
    }

    data class ErrorAnalysisResult(
        val testErrorLineNum: Int,
        val passedLocs: MutableList<Locator>,
        val brokenLoc: Locator?,
        val unexercisedLocs: MutableList<Locator>
    )

    //find error line (if any), locators passed/broken (if any)/unexercised (if any)
    private fun analyzeErrorInfo(test: SMTestProxy, newLocatorsInTest: List<Locator>): ErrorAnalysisResult {
        val errorInfo = extractFailInfoFromStacktrace(test.stacktrace)
        val testErrorLineNum: Int
        val passedLocs: MutableList<Locator> = mutableListOf()
        var brokenLoc: Locator? = null
        val unexercisedLocs: MutableList<Locator> = mutableListOf()
        //if no errors, all locators are marked as passed
        if (errorInfo == null) {
            testErrorLineNum = -1
            passedLocs.addAll(newLocatorsInTest)
        }
        //else, retrieve locators passed, broken (if any), and unexercised
        else {
            testErrorLineNum = errorInfo.second!!.third
            val poCalls = TestQuestAction.TestInfoNew[test.name] ?: emptyList()
            //get all passed locators from test method preceding test error
            passedLocs.addAll(newLocatorsInTest.filter {
                it.methodName == test.name && it.className == test.parent.name && it.line < testErrorLineNum
            })
            //get all nonexercised locators from test method following test error
            unexercisedLocs.addAll(newLocatorsInTest.filter {
                it.methodName == test.name && it.className == test.parent.name && it.line > testErrorLineNum
            })
            //get all passed and nonexercised locators from PO methods preceding and following test error
            for (call in poCalls) {
                val poCall = TestQuestAction.POsNew.find { it.name == call.pageObject }
                val methodInfo = poCall?.methods?.find { it.name == call.method }
                if (call.line < testErrorLineNum)
                    passedLocs.addAll(methodInfo?.locators ?: emptyList())
                else if (call.line > testErrorLineNum)
                    unexercisedLocs.addAll(methodInfo?.locators ?: emptyList())
            }
            //if the error is not inside a PO method, the broken locator (if any) is searched inside test method
            //notice that we might have test error (e.g., NullPointerExc) and no broken locator (e.g., NoSuchElementException)
            //so we search for broken locator at stacktrace line if NoSuchElementExc string is present
            if (errorInfo.first.isEmpty()) {
                brokenLoc = newLocatorsInTest.firstOrNull { loc ->
                    test.stacktrace!!.contains("NoSuchElementException") && loc.line == testErrorLineNum
                }
            }
            //else, the error is inside a PO method, so additional passed/non exercised locators must be collected
            //from the involved failed PO method
            else {
                val poErrorLineNum = errorInfo.first.last().third //get error line number in PO method (last trace)
                brokenLoc = newLocatorsInTest.firstOrNull { loc ->
                    test.stacktrace!!.contains("NoSuchElementException") && loc.line == poErrorLineNum
                }
                passedLocs.addAll(
                    TestQuestAction.POsNew.filter { it.name == errorInfo.first.last().first }
                        .flatMap { it.methods }
                        .filter { it.name == errorInfo.first.last().second }
                        .flatMap { it.locators }
                        .filter { it.line < poErrorLineNum }
                )
                unexercisedLocs.addAll(
                    TestQuestAction.POsNew.filter { it.name == errorInfo.first.last().first }
                        .flatMap { it.methods }
                        .filter { it.name == errorInfo.first.last().second }
                        .flatMap { it.locators }
                        .filter { it.line > poErrorLineNum }
                )
            }
            if (brokenLoc != null) {
                val locsAnalyzer = LocatorsAnalyzer()
                locsAnalyzer.calculateBrokenLocators(brokenLoc)
            }
        }
        return ErrorAnalysisResult(testErrorLineNum, passedLocs, brokenLoc, unexercisedLocs)
    }








}