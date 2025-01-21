package extractor.pageobject

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.type.Type
import extractor.locator.Locator
import extractor.locator.LocatorsExtractor
import java.nio.file.Path

data class PageObject(
    val name: String,
    val methods: List<MethodInfo>, // PO methods
    val ancestors: List<String>, // PO ancestors names
    val nonCanonicalLocators: List<Locator> // locators defined with no names or as annotations
)

//method info for the methods within each PO
data class MethodInfo(
    val name: String,
    val returnType: Type,
    val paramTypes: List<Type>,
    val locators: List<Locator>, // list of locators within method
    val assertionLines: List<String>, // list of assertions associated with method info (hopefully, none)
    val seleniumCommands: List<String> // list of selenium commands associated with method info
)

class PageObjectExtractor {

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
            "getText"
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
        var nonCanonicalLocators = mutableListOf<Locator>()

        //for each part of the PageObject
        cu.types[0].members.forEach { member ->
            when (member) {

                //check for method body
                is MethodDeclaration -> {
                    val methodName = member.nameAsString // to retrieve method name
                    val returnType = member.type // to retrieve method return type
                    val parameterTypes = member.parameters.map { it.type }  // to retrieve parameter types

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
        // i.e., having no name (e.g., driver.findElement(...).action)
        // i.e., having no method name being annotations (e.g., @FindBy(...))
        nonCanonicalLocators = methods
            .flatMap { it.locators }
            .filter { it.methodName.isEmpty() || it.locatorName == null }.toMutableList()

        return PageObject(
            name = className,
            methods = methods,
            ancestors = ancestors,
            nonCanonicalLocators = nonCanonicalLocators
        )
    }
}
