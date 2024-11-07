package locator

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import java.nio.file.Path

data class Locator(
    val locatorType: String,
    val locatorValue: String,
    val line: Int,
    val methodName: String,
    val className: String,
    val locatorName: String?,
    val locatorPosition: Int,
    val filePath: String
)
{
    //hashcode for uniqueness of locators
    override fun hashCode(): Int {
        if (locatorName != null)
            return 31 * locatorName.hashCode() + methodName.hashCode() + className.hashCode()//TODO: risky if many same name locators
        else
           return 31 * locatorPosition + methodName.hashCode() + className.hashCode()//TODO: risky if many non named locators
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Locator) return false
        if (locatorName != null)
            return locatorName == other.locatorName &&
                    methodName == other.methodName &&
                    className == other.className &&
                    locatorPosition == other.locatorPosition
        else
            return locatorPosition == other.locatorPosition &&
                    methodName == other.methodName &&
                    className == other.className
    }
}





class LocatorsExtractor : VoidVisitorAdapter<MutableList<Locator>>() {

    private var currentClassName: String = ""
    private var currentMethodName: String = ""
    private var locatorCounter: Int = 0 // it counts the locator position within a method
    private var filePath: String = ""  //the path to the current test file


    // Visitor to find all variable assignments and driver.findElement calls
    override fun visit(mce: MethodCallExpr, locators: MutableList<Locator>) {
        super.visit(mce, locators)

        if (mce.nameAsString == "findElement" && mce.scope.isPresent) {
            val scope = mce.scope.get()
            if (scope.toString() == "driver") {
                //Extract locator details
                val argument = mce.arguments[0].toString()
                val locator = extractLocator(argument)
                val locatorType = locator.first
                val locatorValue = locator.second
                //Find the associated variable name if present
                val locatorName = findVariableName(mce)
                //Increment and track the locator position
                locatorCounter++
                locators.add(Locator(locatorType, locatorValue, mce.begin.get().line, currentMethodName,
                    currentClassName, locatorName, locatorCounter, filePath))
            }
        }
    }

    override fun visit(md: MethodDeclaration, locators: MutableList<Locator>) {
        currentMethodName = md.nameAsString
        locatorCounter = 0 //Reset locator position counter for each new method
        super.visit(md, locators)
    }

    override fun visit(cid: ClassOrInterfaceDeclaration, locators: MutableList<Locator>) {
        currentClassName = cid.nameAsString
        super.visit(cid, locators)
    }

    // Function to parse Java file and extract locators
    fun parseLocators(filePath: Path): List<Locator> {
        this.filePath = filePath.toString() //to keep track of current file
        val parser = JavaParser()
        val cu: CompilationUnit? = parser.parse(filePath).result.orElse(null)
        val locators = mutableListOf<Locator>()
        visit(cu, locators)
        //return locators
        return locators.map { it.copy(filePath = filePath.toString()) }
    }

    // Helper method to extract locator type and value
    private fun extractLocator(locatorString: String): Pair<String, String> {
        if (locatorString.contains("By.linkText(")) {
            val variableName = locatorString.substringAfter("By.linkText(").substringBefore(")").trim()
            if (variableName.isNotEmpty()) {
                return Pair("linkText", variableName)
            }
        }
        //val regex = """By\.(.*?)\("([^"]+)"\)""".toRegex()
        val regex = """By\.(\w+)\(\s*["'](.+?)["']\s*\)""".toRegex()
        val matchResult = regex.find(locatorString)
        if (matchResult != null && matchResult.groupValues.size == 3) {
            val locatorType = matchResult.groupValues[1]
            val locatorValue = matchResult.groupValues[2]
            return Pair(locatorType, locatorValue)
        } else {
            throw IllegalArgumentException("Invalid locator string: $locatorString")
        }
    }

    // Helper method to extract locator type and value
    /*private fun extractLocator1(locatorString: String): Pair<String, String> {
        val regex = """By\.(\w+)\(\s*(?:"([^"]+)"|(\w+)|(".*?"))(\s*\+\s*(".*?"))?\s*\)""".toRegex()
        val matchResult = regex.find(locatorString)
        if (matchResult != null) {
            val locatorType = matchResult.groupValues[1]
            val locatorValue = StringBuilder()
            if (matchResult.groupValues[2].isNotEmpty()) {
                locatorValue.append(matchResult.groupValues[2])
            } else if (matchResult.groupValues[3].isNotEmpty()) {
                locatorValue.append(matchResult.groupValues[3])
            }
            matchResult.groupValues[5]?.let {
                locatorValue.append(it.trim())
            }
            return Pair(locatorType, locatorValue.toString())
        }
        //TODO: check whether there are locator values stored in variables
        if (locatorString.contains("By.linkText(")) {
            val variableName = locatorString.substringAfter("By.linkText(").substringBefore(")").trim()
            if (variableName.isNotEmpty()) {
                return Pair("linkText", variableName)
            }
        }
        throw IllegalArgumentException("Invalid locator string: $locatorString")
    }*/














    // Helper method to find the variable name if available
    private fun findVariableName(mce: MethodCallExpr): String? {
        val parentNode = mce.parentNode.orElse(null)
        // Check if the parent is an assignment (VariableDeclarationExpr)
        if (parentNode != null && parentNode.toString().contains("= driver.findElement")) {
            val parentText = parentNode.toString()
            val variableRegex = """(\w+)\s*=\s*driver\.findElement""".toRegex()
            val matchResult = variableRegex.find(parentText)
            if (matchResult != null) {
                return matchResult.groupValues[1]  // Return the variable name
            }
        }
        return null  // Return null if no variable assignment found
    }
}
