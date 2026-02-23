package listener.test

import com.intellij.execution.testframework.sm.runner.SMTRunnerEventsListener
import com.intellij.execution.testframework.sm.runner.SMTestProxy
import com.intellij.openapi.project.Project
import com.intellij.util.messages.MessageBusConnection
import gamification.GamificationManager
import extractor.locator.Locator
import analyzer.locator.LocatorsAnalyzer
import analyzer.pageobject.PageObjectsAnalyzer
import extractor.pageobject.PageObject
import extractor.test.PageObjectCall
import testquest.TestQuestAction
import ui.DailyWindowManager
import utils.ProgressFileHandler
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
    val locatorsUnexercised: List<Locator> = emptyList(), //the list of locs after a broken loc (if any) thus not executed
    val poMethodCallsExercised: List<PageObjectCall> = emptyList(), //the list of PO method calls exercised (if any)
    val poMethodCallsUnexercised: List<PageObjectCall> = emptyList() //the list of PO method calls not exercised due to error (if any)
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






class TestExecutionListener private constructor() : SMTRunnerEventsListener {

    private lateinit var server: Server
    private val testOutcomes = mutableListOf<TestOutcome>() //to collect the outcome of each test

    companion object {


        val instance = TestExecutionListener()
        private var connection: MessageBusConnection? = null


        fun registerListener(project: Project) {
            connection = project.messageBus.connect()
            connection?.subscribe(SMTRunnerEventsListener.TEST_STATUS, instance)
        }

    }





    override fun onTestingStarted(testsRoot: SMTestProxy.SMRootTestProxy) {
        testOutcomes.clear()
        try {
            //server = Server()
            //server.start()
            DailyWindowManager.closeAllWindows() //close all opened frames about targeted dailies
        }
        catch (_: RuntimeException) {}//this to handle the case of tests run even if TestQuest is not opened
    }



    //this is called at the end of the whole testing process
    override fun onTestingFinished(testsRoot: SMTestProxy.SMRootTestProxy) {
        try {
            //server.stop()

            //1. CHECK EVENTS FOR PROGRESSION
            GamificationManager.analyzeEvents(testOutcomes)
            testOutcomes.forEach { testOutcome ->
                LocatorsAnalyzer.removePendingFixedLocators(testOutcome.locatorsPassed)
                PageObjectsAnalyzer.removePendingFixedPOs(testOutcome.poMethodCallsExercised) //TODO: check this
            }

            //old data (locators, PO calls, POs) is updated with:
            // - new data that passed the tests
            // - old data that passed the tests
            // - old data that did not pass the test but still exists

            //2. LOCATORS UPDATE
            val locatorsPassed = testOutcomes.flatMap { it.locatorsPassed }.distinct()
            TestQuestAction.locatorsOld = TestQuestAction.locatorsOld
                .filter { it in TestQuestAction.locatorsNew } //keep old locators that still exist only
                .map { oldLocator -> locatorsPassed.find { it == oldLocator } ?: oldLocator } //overwrite old locators with passed version and keep old locators for not passed ones
                .toMutableList()
            //add newly added passed locs
            (TestQuestAction.locatorsOld as MutableList<Locator>).addAll(locatorsPassed.filterNot { it in TestQuestAction.locatorsOld })

            //3. PO CALLS UPDATE
            val callsExercised = testOutcomes.flatMap { it.poMethodCallsExercised }.distinct()
            TestQuestAction.POCallsOld = TestQuestAction.POCallsOld.mapValues { (test, oldCalls) ->
                val updatedCalls = oldCalls
                    .filter { it in TestQuestAction.POCallsNew[test].orEmpty() } //keep old calls that still exist only
                    .map { oldCall -> callsExercised.find { it == oldCall } ?: oldCall } //overwrite old calls with exercised version and keep old calls for not exercised ones
                //add newly added exercised calls from existing tests
                (updatedCalls + callsExercised.filterNot { it in updatedCalls }).distinct()
            }.toMutableMap()
            //add newly added exercised calls from new tests
            TestQuestAction.POCallsOld = TestQuestAction.POCallsOld.toMutableMap().apply {
                TestQuestAction.POCallsNew.forEach { (test, newCalls) ->
                    if (!this.containsKey(test))
                        this[test] = newCalls.filter { it in callsExercised }.distinct()
                }
            }

            //4. POs UPDATE
            val exercisedPONames = callsExercised.map { it.pageObject }.toSet()
            val allNewPONames = TestQuestAction.POsNew.map { it.name }.toSet()
            TestQuestAction.POsOld = TestQuestAction.POsOld.mapNotNull  { oldPO ->
                //if oldPO still exists
                if (oldPO.name in allNewPONames) {
                    //if was not exercised, keep old version
                    if (oldPO.name !in exercisedPONames)
                        oldPO
                    //else, update it
                    else {
                        //get new PO version
                        val newPO = TestQuestAction.POsNew.find { it.name == oldPO.name }!!
                        //update old methods
                        val filteredUpdatedMethods = oldPO.methods
                            //keep only methods that still exist
                            .filter { oldMethod -> newPO.methods.any { it.name == oldMethod.name } }
                            .map { oldMethod ->
                                val newMethod = newPO.methods.find { it.name == oldMethod.name }!!
                                //update method with newest version of locators that passed and old version of locators not passed but still existing
                                val updatedLocators = (
                                        newMethod.locators.filter { it in locatorsPassed } +
                                                oldMethod.locators.filter { it !in locatorsPassed && it in newMethod.locators }
                                        ).distinct()
                                //TODO: assertions + selenium commands are now all copied but a more complex structure with error line to determine those passed to copy might be needed
                                oldMethod.copy(locators = updatedLocators)
                            }
                        //update with newly added methods that had some locators passed
                        val additionalMethods = newPO.methods
                            .filter { newMethod -> oldPO.methods.none { it.name == newMethod.name } } //only new methods
                            .mapNotNull { newMethod ->
                                val updatedLocators = newMethod.locators.filter { it in locatorsPassed }.distinct()
                                if(updatedLocators.isNotEmpty())
                                    //TODO: assertions + selenium commands are now all copied but a more complex structure with error line to determine those passed to copy might be needed
                                    newMethod.copy(locators = updatedLocators)
                                else
                                    null
                            }
                        val updatedMethods = (filteredUpdatedMethods + additionalMethods).distinctBy { it.name }

                        //update ancestors
                        val updatedAncestors = newPO.ancestors

                        //update noncanonical locs that passed, keeping those still existing but not passed
                        val updatedNonCanonicalLocators = oldPO.nonCanonicalLocators
                            //keep only still existing non canonical locators
                            .filter { locator -> locator in newPO.nonCanonicalLocators }
                            //overwrite old passed
                            .map { locator ->
                            locatorsPassed.find { it == locator } ?: locator
                        }.toMutableSet().apply {
                            //add newly added passed non canonical
                            addAll(
                                newPO.nonCanonicalLocators.filter { locator ->
                                    locator in locatorsPassed && locator !in oldPO.nonCanonicalLocators
                                }
                            )
                        }.toList()
                        //returned updated copy of PO with most components as 'passed' or not passed but still existing
                        oldPO.copy(
                            methods = updatedMethods,
                            ancestors = updatedAncestors,
                            nonCanonicalLocators = updatedNonCanonicalLocators
                        )
                    }
                }
                //else, if does not exist remove it
                else
                    null
            }.toMutableList()
            //add new POs that are exercised, but only with updated methods/locators
            val newExercisedPOs = TestQuestAction.POsNew
                .filter { newPO ->
                    newPO.name in exercisedPONames && TestQuestAction.POsOld.none { it.name == newPO.name }
                }
                .map { newPO ->
                    //update new methods with only passed locators
                    //TODO: assertions + selenium commands are now all copied but a more complex structure with error line to determine those passed to copy might be needed
                    val newMethods = newPO.methods.map { method ->
                        val updatedLocators = method.locators.filter { it in locatorsPassed }
                        method.copy(locators = updatedLocators)
                    }
                    //update nonCanonicalLocators keeping only those that are passed
                    val updatedNonCanonicalLocators = newPO.nonCanonicalLocators
                        .filter { it in locatorsPassed }
                        .distinct()
                    //create new PO with updated methods and locators
                    newPO.copy(
                        methods = newMethods,
                        nonCanonicalLocators = updatedNonCanonicalLocators
                    )
                }
            //add the new PageObjects to the old list
            (TestQuestAction.POsOld as MutableList<PageObject>).addAll(newExercisedPOs)


            //5. SAVE CHANGES ON FILE
            ProgressFileHandler.saveOldData()//to store user progress that needs to be tested next time
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
            //3. FIND PO METHOD CALLS THAT ARE EXERCISED/UNEXERCISED DUE TO FAILURE
            val (exercised, unexercisedCalls) = TestQuestAction.POCallsNew[test.name]?.let { calls ->
                if (analysisResult.testErrorLineNum == -1) //if testErrorLineNum è -1, all calls are passed
                    calls to emptyList()
                else
                    calls.filter { it.line <= analysisResult.testErrorLineNum } to
                            calls.filter { it.line > analysisResult.testErrorLineNum }

            } ?: (emptyList<PageObjectCall>() to emptyList())
            //4. TEST OUTCOME IS CREATED
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
                analysisResult.unexercisedLocs,
                exercised,
                unexercisedCalls
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




    fun dispose() {
        if (::server.isInitialized)
            instance.server.stop()
        connection?.disconnect()
    }














    // extractFailInfoFromStacktrace() returns a error info,
    // i.e., a structure that captures info of test failed as well as PageObject failed methods
    // errorInfo.first = List(<PO class name, PO method name, error line>), errorInfo.second = <Test class name, Test method name, error line>
    private fun extractErrorInfoFromStacktrace(stackTrace: String?): Pair<List<Triple<String, String, Int>>, Triple<String, String, Int>?>? {
        if (stackTrace == null)
            return null
        val pageRegex = Regex("""(\w+Page)\.(\w+)\((\w+Page\.java):(\d+)\)""")
        val testRegex = Regex("""(\w*Test)\.(\w+)\((\w*Test\.java):(\d+)\)""")
        //trace failures about POs
        /*e.g., at com.example.po.AdminPage.doAction(AdminPage.java:23)
                at com.example.po.LoginPage.doLogin(LoginPage.java:55)
                ...
        */
        val failedPageObjects = pageRegex.findAll(stackTrace).map { match ->
            val (poName, poMethodName, _, poLineNumber) = match.destructured
            Triple(poName, poMethodName, poLineNumber.toInt())
        }.toList()
        //trace failure about test (always one)
        //e.g., at com.example.tests.LoginTest.testLogin(LoginTest.java:49)
        val failedTest = testRegex.find(stackTrace)?.let { match ->
            val (testClassName, testMethodName, _, testLineNumber) = match.destructured
            Triple(testClassName, testMethodName, testLineNumber.toInt())
        }
        return Pair(failedPageObjects, failedTest)
    }

    //collect locators from test
    private fun collectLocators(test: SMTestProxy): Pair<MutableList<Locator>, MutableList<Locator>> {
        // TODO: currently, no PO method calling other PO methods is managed
        //first, get locators from test only
        val locationUrl = test.locationUrl
        val testClassName = locationUrl!!.removePrefix("java:test://").substringAfterLast('.').substringBefore('/')
        val oldLocatorsInTest: MutableList<Locator> = TestQuestAction.locatorsOld
            .filter { it.methodName == test.name && it.className == testClassName }
            .toMutableList()
        val newLocatorsInTest: MutableList<Locator> = TestQuestAction.locatorsNew
            .filter { it.methodName == test.name && it.className == testClassName }
            .toMutableList()
        //then, retrieve locators from PO method calls
        val oldPageObjectCalls = TestQuestAction.POCallsOld[test.name] ?: emptyList()
        for (call in oldPageObjectCalls) {
            val oldPageObject = TestQuestAction.POsOld.find { it.name == call.pageObject }
            val oldMethodInfo = oldPageObject?.methods?.find { it.name == call.methodName }
            if (oldMethodInfo != null) oldLocatorsInTest.addAll(oldMethodInfo.locators)
        }
        val newPageObjectCalls = TestQuestAction.POCallsNew[test.name] ?: emptyList()
        for (call in newPageObjectCalls) {
            val newPageObject = TestQuestAction.POsNew.find { it.name == call.pageObject }
            val newMethodInfo = newPageObject?.methods?.find { it.name == call.methodName }
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
        val errorInfo = extractErrorInfoFromStacktrace(test.stacktrace)  //errorInfo is a pair: first = error info related to POs (if any)
                                                                        // second = error info related to test
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
            val poCalls = TestQuestAction.POCallsNew[test.name] ?: emptyList()
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
                val po = TestQuestAction.POsNew.find { it.name == call.pageObject }
                val methodInfo = po?.methods?.find { it.name == call.methodName }
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
                val poErrorLineNum = errorInfo.first.last().third //get error line number (third value) from error in PO method (last PO is the one)
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