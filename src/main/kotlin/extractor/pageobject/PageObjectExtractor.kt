package extractor.pageobject

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.stmt.Statement
import com.github.javaparser.ast.stmt.TryStmt
import extractor.locator.Locator
import java.io.Serializable
import java.nio.file.Path

data class PageObject(
    val name: String,
    val methods: List<MethodInfo>, // PO methods
    val ancestors: List<String>, // PO ancestors names
    val nonCanonicalLocators: List<Locator> // locators defined with no names or as annotations
): Serializable{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PageObject
        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}

//method info for the methods within each PO
data class MethodInfo(
    val name: String,
    val returnType: String,
    val paramTypes: List<String>,
    val locators: List<Locator>, // list of locators within method
    val assertionLines: List<String>, // list of assertions associated with method info (hopefully, none)
    val seleniumCommands: List<String> // list of selenium commands associated with method info
): Serializable{

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as MethodInfo
        return name == other.name &&
                returnType == other.returnType &&
                paramTypes == other.paramTypes &&
                locators == other.locators &&
                assertionLines == other.assertionLines &&
                seleniumCommands == other.seleniumCommands
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + returnType.hashCode()
        result = 31 * result + paramTypes.hashCode()
        result = 31 * result + locators.hashCode()
        result = 31 * result + assertionLines.hashCode()
        result = 31 * result + seleniumCommands.hashCode()
        return result
    }
}



class PageObjectExtractor {

    //list of actions over webelements used to determine which statements within PO methods must be saved
    companion object {
        // TODO: extend the list with further commands, if needed (e.g., via Action or JavascriptExecutor)
        val SELENIUM_COMMANDS = listOf(
            "click",
            "sendKeys",
            "getText",
            "submit",
            "clear",
            "getAttribute",
            "isDisplayed",
            "isEnabled",
            "isSelected",
            "getTagName",
            "doubleClick",
            "getText",
            "selectByVisibleText",
            "selectByValue",
            "selectByIndex"
        )
    }

    fun parsePageObject(filePath: Path, locators: List<Locator>): PageObject {
        val parser = JavaParser()
        val fileContent = filePath.toFile().readText()
        val cu: CompilationUnit = parser.parse(fileContent).result.orElse(null)
            ?: throw IllegalArgumentException("Unable to parse the file at: $filePath")
        val className = cu.types[0].nameAsString // to retrieve PO name
        // to retrieve PO ancestors
        val ancestors = cu.types[0]
            .asClassOrInterfaceDeclaration()
            .extendedTypes.map { it.nameAsString }
        //for each part of the PageObject
        val methods = mutableListOf<MethodInfo>()
        cu.types[0].members.forEach { member ->
            when (member) {
                //check for method body
                is MethodDeclaration -> {
                    val methodName = member.nameAsString // to retrieve method name
                    val returnType = member.type.asString() // to retrieve method return type
                    val parameterTypes = member.parameters.map { it.type.asString() }  // to retrieve parameter types
                    val methodLocators = locators.filter { // to retrieve canonical PO method locators
                        it.className == className && it.methodName == methodName
                    }.toMutableList()
                    //to retrieve locators associated with annotations within PO methods (e.g., element.click() when element is the name of an annotation)
                    //TODO: assumption is that no @CacheLookup is used so any reference must be saved as it is like a new findElement search
                    val localVarMap = mutableMapOf<String?, Boolean>()
                    locators.forEach { locator ->
                        if (locator.methodName == "" && locator.className == className) {
                            member.body.ifPresent { methodBody ->
                                //remove comments
                                methodBody.allContainedComments.forEach { it.remove() }
                                //collect locators that refer to annotations via processStatement()
                                methodBody.statements.forEach { statement ->
                                    extractAnnotationLocators(statement, methodLocators, methodName, localVarMap, locator)
                                }
                            }
                        }
                    }
                    //locator position must be adapted in order to track both classic declarations and annotations refs
                    methodLocators.sortBy { it.line }
                    methodLocators.forEachIndexed { index, locator ->
                        methodLocators[index] = locator.copy(locatorPosition = index + 1)
                    }
                    //asserts and selenium commands are detected, skipping commented statements
                    //TODO: at the moment, the whole line is extracted. in the future, we may want to implement a more precise extraction
                    val assertionLines = mutableListOf<String>()
                    val seleniumCommands = mutableListOf<String>()
                    val methodBody = member.body.orElse(null)
                    if (methodBody != null) {
                        val comments = methodBody.getAllContainedComments().map { it.toString().trim() }
                        methodBody.statements.forEach { statement ->
                            val statementString = statement.toString().trim()
                            //to skip commented statements
                            if (comments.any { statementString.startsWith(it) })
                                return@forEach
                            //to retrieve method assertions (hopefully none)
                            if (statementString.contains("assert"))
                                assertionLines.add(statementString)
                            //to retrieve method commands on locators
                            if (SELENIUM_COMMANDS.any { statementString.contains(it) })
                                seleniumCommands.add(statementString)
                        }
                    }
                    methods.add(
                        MethodInfo(
                            name = methodName,
                            returnType = returnType,
                            paramTypes = parameterTypes,
                            locators = methodLocators,
                            assertionLines = assertionLines,
                            seleniumCommands = seleniumCommands
                        )
                    )
                }
            }
        }
        // to retrieve non canonical locators from all locators associated with pageobject
        // i.e., having empty method name as they are annotations (e.g., @FindBy(...))
        // i.e., having null name within methods (e.g., driver.findElement(...).action)
        val nonCanonicalLocators: MutableList<Locator> = locators
            .filter { it.className == className && (it.methodName.isEmpty() || it.locatorName == null) }.toMutableList()
        return PageObject(
            name = className,
            methods = methods,
            ancestors = ancestors,
            nonCanonicalLocators = nonCanonicalLocators
        )
    }



    private fun extractAnnotationLocators(statement: Statement, methodLocators: MutableList<Locator>, methodName: String, localVarMap: MutableMap<String?, Boolean>, locator: Locator) {
        val lineNumber = statement.range.orElse(null)?.begin?.line ?: -1
        val statementString = statement.toString().trim()
        //check try/catch statement recursively
        if (statement is TryStmt) {
            statement.tryBlock.statements.forEach { innerStatement ->
                extractAnnotationLocators(innerStatement, methodLocators, methodName, localVarMap, locator)
            }
            return
        }
        //skip locators declarations as not annotations and save them to detect local use
        if (locator.locatorName != null &&
            statementString.contains("WebElement ${locator.locatorName} =")) {
            localVarMap[locator.locatorName] = true
            return
        }
        //skip unnamed locators as not annotations
        if (statementString.contains("driver.findElement"))
            return
        //save locator referencing annotation
        if (locator.locatorName != null &&
            statementString.contains(locator.locatorName) &&
            !localVarMap.containsKey(locator.locatorName)) {
            methodLocators.add(locator.copy(methodName = methodName, line = lineNumber, isAnnotation = true))
        }
    }



}
