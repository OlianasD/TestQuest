package extractor.test

import java.io.File

//e.g., from test lines
// line 15: LoginPage lp = new LoginPage(driver);
// line 16: lp.doLogin("user", "pwd");
// it extracts PageObjectCall: (LoginPage, doLogin, 16, <String, String>)
data class PageObjectCall(val pageObject: String, val method: String, val line: Int, val parameters: List<String> = emptyList())

class TestInfoExtractor {


    //method to extract PO used by tests
    fun parseTestInfo(file: File): Map<String, List<PageObjectCall>> {
        val testMethodRegex = Regex("""public\s+void\s+(\w+)\s*\(.*?\)\s*throws""")
        val pageObjectDeclarationRegex = Regex("""(\w+Page)\s+(\w+)\s*=\s*new\s+\1\(.*?\);""")
        val methodCallRegex = Regex("""(\w+)\.(\w+)\((.*?)\);""")

        val lines = file.readLines()
        val results = mutableMapOf<String, MutableList<PageObjectCall>>()
        var currentTestMethod: String? = null
        val pageObjectVars = mutableMapOf<String, String>()

        for ((index, line) in lines.withIndex()) {

            //detect test methods
            val testMatch = testMethodRegex.find(line)
            if (testMatch != null) {
                currentTestMethod = testMatch.groupValues[1]
                results[currentTestMethod] = mutableListOf()
                pageObjectVars.clear()
            }

            //detect PageObject variable declarations
            val declarationMatch = pageObjectDeclarationRegex.find(line)
            if (declarationMatch != null) {
                val (className, varName) = declarationMatch.destructured
                pageObjectVars[varName] = className
            }

            //detect PageObject method calls
            val callMatch = methodCallRegex.find(line)
            if (callMatch != null) {
                val (varName, method, params) = callMatch.destructured
                val pageObjectClass = pageObjectVars[varName]
                if (pageObjectClass != null && currentTestMethod != null) {
                    //infer the types of the parameters
                    val parameters = inferParameterTypes(params)
                    //save the call with parameters and their inferred types
                    results[currentTestMethod]?.add(PageObjectCall(pageObjectClass, method, index + 1, parameters))
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
        val result = mutableListOf<String>()
        for (param in paramList) {
            when {
                //TODO: more types might be supported
                param.matches(Regex("\".*\"")) -> result.add("String")
                param.matches(Regex("-?\\d+")) -> result.add("int")
                param.matches(Regex("-?\\d+\\.\\d+")) -> result.add("double")
                param.matches(Regex("true|false")) -> result.add("boolean")
                else -> result.add("unknown")
            }
        }
        return result
    }
}
