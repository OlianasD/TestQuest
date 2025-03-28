package extractor.locator

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.VariableDeclarator
import com.github.javaparser.ast.visitor.VoidVisitorAdapter
import java.nio.file.Path
import com.github.javaparser.ast.comments.LineComment
import com.github.javaparser.ast.comments.BlockComment
import com.github.javaparser.ast.expr.*
import java.io.Serializable


data class Locator(
    val locatorType: String,
    val locatorValue: String,
    val line: Int,
    val methodName: String,
    val className: String,
    val locatorName: String?,
    val locatorPosition: Int,
    val filePath: String,
    val isAnnotation: Boolean = false,
    val countChanges: Int = 0 //to count how many times the locator was changed
): Serializable
{
    //hashcode for uniqueness of locators
    override fun hashCode(): Int {
        if (locatorName != null)
            return 31 * locatorName.hashCode() + methodName.hashCode() + className.hashCode()
        else
           return 31 * locatorPosition + methodName.hashCode() + className.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Locator) return false
        if (locatorName != null)
            return locatorName == other.locatorName &&
                    methodName == other.methodName &&
                    className == other.className
        else
            return locatorPosition == other.locatorPosition &&
                    methodName == other.methodName &&
                    className == other.className
    }

    //this method is used to compare locators that changed class (i.e., from a Test class to a PO class)
    //old locator = the locator in Test class
    //the check is performed on attributes that should not change by changing class (i.e., className, methodName, position) from Test to PO (i.e., name, type, value)
    fun compareThisLocInPOWithOldInTest(old: Locator): Boolean {
        return this.locatorType == old.locatorType &&
                this.locatorValue == old.locatorValue &&
                this.locatorName == old.locatorName &&
                this.className.endsWith("Page") && old.className.endsWith("Test")
    }


    //this method is used to compare locators that changed from noncanonical to canonical (i.e., from annontation/unnamed to named)
    //old locator = the non canonical locator
    //the check is performed on attributes that should not change if only canonical info (i.e., new name, position, method) are changed (i.e., type, value, class)
    fun compareThisLocWithLocInNonCanonicalForm(old: Locator): Boolean {
        return this.locatorType == old.locatorType &&
                this.locatorValue == old.locatorValue &&
                this.className == old.className &&  ((this.locatorName != null && this.locatorName != "" &&
                (old.locatorName == null || old.locatorName == "")) || (!this.isAnnotation && old.isAnnotation)
                || (this.methodName != "" && old.methodName == ""))
    }



}





class LocatorsExtractor : VoidVisitorAdapter<MutableList<Locator>>() {

    private var currentClassName: String = ""
    private var currentMethodName: String = ""
    private var locatorCounter: Int = 0 // it counts the locator position within a method
    private var filePath: String = ""  //the path to the current test file


    //main method called to parse Java file and extract locators via visitors
    fun parseLocators(filePath: Path): List<Locator> {
        this.filePath = filePath.toString() //to keep track of current file
        val parser = JavaParser()
        //clean test files from comments
        val fileContent = filePath.toFile().readText()
        val cu: CompilationUnit? = parser.parse(fileContent).result.orElse(null)
        val locators = mutableListOf<Locator>()
        visit(cu, locators)
        return locators.map { it.copy(filePath = filePath.toString()) }
    }

    //visitor to capture locators from a test/PO class
    override fun visit(cid: ClassOrInterfaceDeclaration, locators: MutableList<Locator>) {
        currentClassName = cid.nameAsString
        locatorCounter = 0 //to reset locator position for each class
        super.visit(cid, locators)
    }

    //visitor to capture locators from a method
    override fun visit(md: MethodDeclaration, locators: MutableList<Locator>) {
        currentMethodName = md.nameAsString
        locatorCounter = 0 //to reset locator position for each method
        super.visit(md, locators)
    }

    //visitor to capture locators from annotations
    override fun visit(fd: FieldDeclaration, locators: MutableList<Locator>) {
        super.visit(fd, locators)
        for (annotation in fd.annotations) {
            if (annotation.name.asString() == "FindBy") {
                val locator = extractFindByAnnotation(annotation) ?: continue
                locatorCounter++
                locators.add(
                    Locator(
                        locatorType = locator.first,
                        locatorValue = locator.second,
                        line = fd.begin.get().line,
                        methodName = "",
                        className = currentClassName,
                        locatorName = fd.variables.firstOrNull()?.nameAsString,
                        locatorPosition = locatorCounter,
                        filePath = filePath,
                        isAnnotation = true
                    )
                )
            }
        }
    }

    //visitor to capture locators from statements
    override fun visit(mce: MethodCallExpr, locators: MutableList<Locator>) {
        super.visit(mce, locators)
        if (mce.nameAsString == "findElement" && mce.scope.isPresent) {
            val scope = mce.scope.get()
            if (scope.toString() == "driver") {
                //extract locator details
                val argument = mce.arguments[0].toString()
                val locator = extractLocator(argument)
                val locatorType = locator.first
                val locatorValue = locator.second
                //find the associated variable name if present
                val locatorName = findVariableName(mce)
                //increment and track the locator position
                locatorCounter++
                locators.add(
                    Locator(locatorType, locatorValue, mce.begin.get().line, currentMethodName,
                        currentClassName, locatorName, locatorCounter, filePath)
                )
            }
        }
    }

    override fun visit(lineComment: LineComment, locators: MutableList<Locator>) {
        //skip in case of comment
    }

    override fun visit(blockComment: BlockComment, locators: MutableList<Locator>) {
        //skip in case of comment
    }


    //helper method to extract locator type and value
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

    //helper method to find the variable name from statements about locators, if available
    private fun findVariableName(mce: MethodCallExpr): String? {
        val parent = mce.parentNode.orElse(null) ?: return null
        return when (parent) {
            is AssignExpr -> {
                //case element = driver.findElement(...)
                if (parent.value == mce) parent.target.toString() else null
            }
            is VariableDeclarator -> {
                //case WebElement element = driver.findElement(...)
                if (parent.initializer.isPresent && parent.initializer.get() == mce) parent.nameAsString else null
            }
            else -> null //case driver.findElement(...)
        }
    }




    //helper method to build annotation as locator
    //two types of annotations: without or with locator type made explicit (e.g., @FindBy("//div[2]") vs @FindBy(xpath="//div[2]"))
    private fun extractFindByAnnotation(annotation: AnnotationExpr): Pair<String, String>? {
        val supportedLocatorTypes = listOf(
            "id", "name", "css", "xpath", "className", "tagName", "linkText", "partialLinkText"
        )
        return when (annotation) {
            is SingleMemberAnnotationExpr -> {
                val value = annotation.memberValue.toString().replace("\"", "")
                Pair("", value)
            }
            is NormalAnnotationExpr -> {
                for (locatorType in supportedLocatorTypes) {
                    val locatorExpr = annotation.pairs.find { it.nameAsString == locatorType }
                    if (locatorExpr != null) {
                        return Pair(locatorType, locatorExpr.value.toString().replace("\"", ""))
                    }
                }
                null
            }
            else -> null
        }
    }

}
