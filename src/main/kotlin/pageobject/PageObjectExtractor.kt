package pageobject

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.expr.AnnotationExpr
import com.github.javaparser.ast.expr.MethodCallExpr
import locator.Locator
import java.nio.file.Path

data class PageObject(
    val name: String,
    val methods: List<MethodInfo>, // PO methods
    val ancestors: List<String>, // PO ancestors
    val nonCanonicalLocators: List<String> // locators defined differently from WebElement e = driver.findBy...
)

data class MethodInfo(
    val name: String,
    val returnType: String,
    val locators: List<Locator>, // list of locators within method
    val assertionLines: List<String>, // list of assertions associated with method info (hopefully, none)
    val seleniumCommands: List<String> // list of selenium commands associated with method info
)

class PageObjectExtractor {

    companion object {
        // TODO: extend the list with further commands if needed
        val SELENIUM_COMMANDS = listOf(
            "click",
            "sendKeys",
            "getText",
            "submit",
            "clear",
            "getAttribute",
            "getCssValue",
            "isDisplayed",
            "isEnabled",
            "isSelected",
            "getTagName",
            "doubleClick",
            "contextClick",
            "dragAndDrop",
            "moveToElement",
            "scrollIntoView"
        )
    }

    fun parsePageObject(filePath: Path, locators: List<Locator>): PageObject {
        val parser = JavaParser()
        val cu: CompilationUnit = parser.parse(filePath).result.orElse(null)
            ?: throw IllegalArgumentException("Unable to parse the file at: $filePath")

        val className = cu.types[0].nameAsString // to retrieve PO name

        // to retrieve PO ancestors
        val ancestors = cu.types[0]
            .asClassOrInterfaceDeclaration()
            .extendedTypes.map { it.nameAsString }

        val methods = mutableListOf<MethodInfo>()
        val nonCanonicalLocators = mutableListOf<String>()

        //for each part of the PageObject
        cu.types[0].members.forEach { member ->
            when (member) {

                //check for @FindBy annotations (non canonical way to declare locators)
                is FieldDeclaration -> {
                    member.annotations.forEach { annotation ->
                        if (annotation.nameAsString == "FindBy") {
                            nonCanonicalLocators.add(annotation.toString()) // Always add to non-canonical locators
                        }
                    }
                }

                //check for method body
                is MethodDeclaration -> {
                    val methodName = member.nameAsString // to retrieve method name
                    val returnType = member.type.toString() // to retrieve method return type
                    val methodLocators = locators.filter { // to retrieve method locators from input locators
                        it.className == className && it.methodName == methodName
                    }
                    val assertionLines = mutableListOf<String>()
                    val seleniumCommands = mutableListOf<String>()
                    val methodBody = member.body.orElse(null)
                    methodBody?.statements?.forEach { statement ->
                        val statementString = statement.toString()
                        // to retrieve method assertions (hopefully none)
                        if (statementString.contains("assert")) {
                            assertionLines.add(statementString)
                        }
                        // to retrieve method commands on locators
                        if (SELENIUM_COMMANDS.any { statementString.contains(it) }) {
                            seleniumCommands.add(statementString)
                        }
                        // to retrieve locators with no names assigned
                        if (statementString.contains("findElement") && !statementString.contains("=")) {
                            nonCanonicalLocators.add(statementString)
                        }
                    }

                    methods.add(
                        MethodInfo(
                            name = methodName,
                            returnType = returnType,
                            locators = methodLocators,
                            assertionLines = assertionLines,
                            seleniumCommands = seleniumCommands
                        )
                    )
                }
            }
        }

        return PageObject(
            name = className,
            methods = methods,
            ancestors = ancestors,
            nonCanonicalLocators = nonCanonicalLocators
        )
    }
}
