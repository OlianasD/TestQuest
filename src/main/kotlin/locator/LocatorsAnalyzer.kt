package locator

import testquest.TestQuestAction

class LocatorsAnalyzer {

    private val analysisMap = mutableMapOf<String, List<Locator>>()
    private var locators = TestQuestAction.locatorsNewStatic //TestQuestAction.locatorsOldDynamic

    fun findTargetedProblems(): MutableMap<String, List<Locator>> {
        analysisMap.clear()
        calculateAbsoluteLocators()
        calculateLongLocators()
        calculateDeepLocators()
        calculatePositionalPredicateLocators()
        calculateBadPredicateLocators()
        calculateNonIDOrXPathLocators()
        calculateBrokenLocators()
        return analysisMap
    }

    //save absolute xpath locators
    private fun calculateAbsoluteLocators() {
        val absoluteLocators = locators.filter {
            it.locatorType.equals("xpath", ignoreCase = true) && it.locatorValue.startsWith("/html")
        }
        analysisMap["absolute"] = absoluteLocators
    }

    //save xpath locators with length more than 50 chars
    private fun calculateLongLocators() {
        val longLocators = locators.filter { it.locatorValue.length > 50 }
        analysisMap["length"] = longLocators
    }

    //save xpath locators with more than 5 levels
    private fun calculateDeepLocators() {
        val deepLocators = locators.filter {
            it.locatorType.equals("xpath", ignoreCase = true) &&
                    it.locatorValue.split("/").count { node -> node.isNotEmpty() } > 5
        }
        analysisMap["level"] = deepLocators
    }

    //save xpath locators with more than 3 positional predicates
    private fun calculatePositionalPredicateLocators() {
        val positionalPredicateLocators = locators.filter {
            it.locatorType.equals("xpath", ignoreCase = true) &&
                    Regex("\\[\\d+]").findAll(it.locatorValue).count() > 3
        }
        analysisMap["posPredicate"] = positionalPredicateLocators
    }

    //save xpath locators with bad predicates
    private fun calculateBadPredicateLocators() {
        val problematicAttributes = setOf(
            "src", "href", "height", "width", "onclick", "onload",
            "onmouseover", "onmouseout", "onchange", "onsubmit",
            "onfocus", "onkeydown"
        )
        val badPredicateLocators = locators.filter {
            it.locatorType.equals("xpath", ignoreCase = true) &&
                    problematicAttributes.any { attribute ->
                        it.locatorValue.contains("@$attribute", ignoreCase = true) }
        }
        analysisMap["badPredicate"] = badPredicateLocators
    }

    //save locators that are neither ID or Xpath
    private fun calculateNonIDOrXPathLocators() {
        val nonIDOrXPathLocators = locators.filter {
            !it.locatorType.equals("id", ignoreCase = true) &&
                    !it.locatorType.equals("xpath", ignoreCase = true)
        }
        analysisMap["noIDOrXPath"] = nonIDOrXPathLocators
    }

    private fun calculateBrokenLocators() {//TODO
        analysisMap["broken"] = emptyList()
    }

}
