package extractor.test

import java.io.File

data class PageObjectCall(val pageObject: String, val method: String, val line: Int, val parameters: List<String> = emptyList())

class PageObjectCallExtractor {


    fun parsePOCalls(file: File): Map<String, List<PageObjectCall>> {
        val testMethodRegex = Regex("""public\s+void\s+(\w+)\s*\(.*?\)\s*throws""")
        val pageObjectDeclarationRegex = Regex("""(\w+Page)\s+(\w+)\s*=\s*new\s+\1\(.*?\);""")

        val lines = file.readLines()
        val results = mutableMapOf<String, MutableList<PageObjectCall>>() //a map that associates each test method with calls
        var currentTestMethod: String? = null
        val pageObjectVars = mutableMapOf<String, String>() //map to save PO types associated with variables
        var insideCommentBlock = false //to manage commented code

        for ((index, line) in lines.withIndex()) {
            val trimmedLine = line.trim()

            //to manage and ignore commented code
            if (trimmedLine.startsWith("/*")) insideCommentBlock = true
            if (insideCommentBlock) {
                if (trimmedLine.endsWith("*/")) insideCommentBlock = false
                continue
            }
            if (trimmedLine.startsWith("//")) continue

            //to detect PO methods
            val testMatch = testMethodRegex.find(trimmedLine)
            if (testMatch != null) {
                currentTestMethod = testMatch.groupValues[1]
                results[currentTestMethod] = mutableListOf()
                pageObjectVars.clear()
            }

            //to detect PO instance declarations
            val declarationMatch = pageObjectDeclarationRegex.find(trimmedLine)
            if (declarationMatch != null) {
                val (className, varName) = declarationMatch.destructured
                pageObjectVars[varName] = className //it associates variable with PO type
            }

            //to detect PO method calls
            if (pageObjectVars.isNotEmpty()) {
                //the regex dynamically manage PO method calls based on declared variables (e.g., it detects 'po' following a ClassPage po = ...)
                //that could be inside other methods (e.g., println(po.getValue))
                val methodCallRegex = Regex("""\b(${pageObjectVars.keys.joinToString("|")})\.(\w+)\(([^)]*)\)""")
                val callMatches = methodCallRegex.findAll(trimmedLine)
                for (callMatch in callMatches) {
                    val (varName, method, params) = callMatch.destructured
                    val pageObjectClass = pageObjectVars[varName] //it retrieves PO type from current declaration
                    if (pageObjectClass != null && currentTestMethod != null) {
                        val parameters = inferParameterTypes(params)  //retrieves parameters (if any) associated with call
                        results[currentTestMethod]?.add(PageObjectCall(pageObjectClass, method, index + 1, parameters))
                    }
                }
            }
        }

        return results
    }


    private fun inferParameterTypes(params: String): List<String> {
        if (params.isBlank()) {
            return emptyList() //void
        }
        val paramList = params.split(",").map { it.trim() }
        return paramList.map {
            when { //TODO: extend types if more are needed
                it.matches(Regex("\".*\"")) -> "String"
                it.matches(Regex("-?\\d+")) -> "int"
                it.matches(Regex("-?\\d+\\.\\d+")) -> "double"
                it.matches(Regex("true|false")) -> "boolean"
                else -> "unknown"
            }
        }
    }
}
