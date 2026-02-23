package extractor.test

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.body.VariableDeclarator
import testquest.TestQuestAction
import java.io.File
import java.io.Serializable

data class PageObjectCall(
    val pageObject: String,
    val methodName: String,
    val line: Int,
    val parameters: List<String> = emptyList(),
    val ancestors: List<String>? = emptyList()
): Serializable
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PageObjectCall
        if (pageObject != other.pageObject) return false
        if (methodName != other.methodName) return false
        if (parameters != other.parameters) return false
        return true
    }
    override fun hashCode(): Int {
        var result = pageObject.hashCode()
        result = 31 * result + methodName.hashCode()
        result = 31 * result + parameters.hashCode()
        return result
    }
}

class PageObjectCallExtractor {

    fun parsePOCalls(file: File): Map<String, List<PageObjectCall>> {
        val parser = JavaParser()
        val parseResult = parser.parse(file)
        val cu: CompilationUnit = parseResult.result.get()
        val results = mutableMapOf<String, MutableList<PageObjectCall>>()

        //analyze test methods
        cu.findAll(MethodDeclaration::class.java).forEach { method ->
            if (method.annotations.any { it.nameAsString == "Test" }) {
                val currentTestMethod = method.nameAsString
                results[currentTestMethod] = mutableListOf()
                val pageObjectVars = mutableMapOf<String, String>()

                //find PO declarations (e.g., LoginPage lp = new LoginPage()...) used to store variable names
                //in order to detect next PO method calls (e.g., lp.doLogin())
                method.findAll(VariableDeclarator::class.java).forEach { varDecl ->
                    val typeName = varDecl.type.asString()
                    val varName = varDecl.nameAsString
                    if (typeName.endsWith("Page")) {
                        pageObjectVars[varName] = typeName
                    }
                }

                //find PO method calls
                method.findAll(MethodCallExpr::class.java).forEach { methodCall ->
                    val scope = methodCall.scope.orElse(null)
                    if (scope != null && scope.isNameExpr) {
                        val varName = scope.asNameExpr().nameAsString
                        val methodName = methodCall.nameAsString
                        val parameters = inferParameterTypes(methodCall.arguments.map { it.toString() })
                        val ancestors = TestQuestAction.POsNew.find {pageObjectVars[varName] == it.name}?.ancestors
                        if (varName in pageObjectVars) {
                            results[currentTestMethod]?.add(
                                PageObjectCall(pageObjectVars[varName]!!, methodName, methodCall.range.get().begin.line, parameters, ancestors)
                            )
                        }
                    }
                }
            }
        }
        return results
    }

    private fun inferParameterTypes(params: List<String>): List<String> {
        //TODO: more parameter types for PO method calls can be considered in the future
        return params.map {
            when {
                it.matches(Regex("\".*\"")) -> "String"
                it.matches(Regex("-?\\d+")) -> "int"
                it.matches(Regex("-?\\d+\\.\\d+")) -> "double"
                it.matches(Regex("true|false")) -> "boolean"
                else -> "unknown"
            }
        }
    }
}
