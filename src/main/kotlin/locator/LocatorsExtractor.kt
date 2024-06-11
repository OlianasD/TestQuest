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
    val className: String
)

class LocatorsExtractor() : VoidVisitorAdapter<MutableList<Locator>>() {


    private var currentClassName: String = ""
    private var currentMethodName: String = ""

    // Visitor to find all driver.findElement calls
    override fun visit(mce: MethodCallExpr, locators: MutableList<Locator>) {
        super.visit(mce, locators)
        if (mce.nameAsString == "findElement" && mce.scope.isPresent) {
            val scope = mce.scope.get()
            if (scope.toString() == "driver") {
                val argument = mce.arguments[0].toString()
                val locator = extractLocator(argument)
                val locatorType = locator.first
                val locatorValue = locator.second
                locators.add(Locator(locatorType, locatorValue, mce.begin.get().line, currentMethodName, currentClassName))
            }
        }
    }

    override fun visit(md: MethodDeclaration, locators: MutableList<Locator>) {
        currentMethodName = md.nameAsString
        super.visit(md, locators)
    }

    override fun visit(cid: ClassOrInterfaceDeclaration, locators: MutableList<Locator>) {
        currentClassName = cid.nameAsString
        super.visit(cid, locators)
    }

    // Function to parse Java file and extract locators
    fun parseLocators(filePath: Path): List<Locator> {
        val parser = JavaParser()
        val cu: CompilationUnit? = parser.parse(filePath).result.orElse(null)
        val locators = mutableListOf<Locator>()
        visit(cu, locators)
        return locators
    }

    private fun extractLocator(locatorString: String): Pair<String, String> {
        val regex = """By\.(.*?)\("([^"]+)"\)""".toRegex()
        val matchResult = regex.find(locatorString)
        if (matchResult != null && matchResult.groupValues.size == 3) {
            val locatorType = matchResult.groupValues[1]
            val locatorValue = matchResult.groupValues[2]
            return Pair(locatorType, locatorValue)
        } else {
            throw IllegalArgumentException("Invalid locator string: $locatorString")
        }
    }
}
