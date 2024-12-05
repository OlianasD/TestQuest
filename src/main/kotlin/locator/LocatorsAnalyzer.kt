package locator

import gamification.GamificationManager
import testquest.TestQuestAction


//this class is used to find problematic locators to fix with targeted dailies
class LocatorsAnalyzer {

    companion object {
        // Global map shared across all instances
        private val analysisMap = mutableMapOf<String, List<Locator>>()
    }

    private var locators = TestQuestAction.locatorsNewStatic


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

    //save xpath locators with length more than MAX_LENGTH chars
    private fun calculateLongLocators() {
        val longLocators = locators.filter { it.locatorValue.length > GamificationManager.MAX_LENGTH }
        analysisMap["length"] = longLocators
    }

    //save xpath locators with more than MAX_LEVEL levels
    private fun calculateDeepLocators() {
        val deepLocators = locators.filter {
            it.locatorType.equals("xpath", ignoreCase = true) &&
                    it.locatorValue.split("/").count { node -> node.isNotEmpty() } > GamificationManager.MAX_LEVEL
        }
        analysisMap["level"] = deepLocators
    }

    //save xpath locators with more than MAX_POS_PRED positional predicates
    private fun calculatePositionalPredicateLocators() {
        val positionalPredicateLocators = locators.filter {
            it.locatorType.equals("xpath", ignoreCase = true) &&
                    Regex("\\[\\d+]").findAll(it.locatorValue).count() > GamificationManager.MAX_POS_PRED
        }
        analysisMap["posPredicate"] = positionalPredicateLocators
    }

    //save xpath locators with bad predicates
    private fun calculateBadPredicateLocators() {
        val badPredicateLocators = locators.filter { locator ->
            locator.locatorType.equals("xpath", ignoreCase = true) &&
                    (GamificationManager.BAD_PREDS.any { attribute ->
                        locator.locatorValue.contains("@$attribute", ignoreCase = true)
                    } || GamificationManager.BAD_JS.any { jsAttribute ->
                        locator.locatorValue.contains("@$jsAttribute", ignoreCase = true)
                    })
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

    private fun calculateBrokenLocators() {
        analysisMap["broken"] = emptyList()
    }

    //this value is updated once targeted dailies are present and tests are executed
    fun calculateBrokenLocators(brokenLocs: List<Locator>) {
        val currentList = analysisMap["broken"] ?: emptyList()
        val newLocators = brokenLocs.filter { newLoc ->
            currentList.none { existingLoc ->
                existingLoc.locatorValue == newLoc.locatorValue &&
                        existingLoc.methodName == newLoc.methodName &&
                        existingLoc.className == newLoc.className
            }
        }
        val updatedList = currentList + newLocators
        analysisMap["broken"] = updatedList
    }



    fun getBrokenLocs(): List<Locator>? {
        return analysisMap["broken"]
    }

    fun updateBrokenLocs(repairedLocs: List<Locator>) {
        val currentList = analysisMap["broken"] ?: emptyList()
        val updatedList = currentList.filterNot { loc ->
            repairedLocs.any { toRemove ->
                loc.locatorValue == toRemove.locatorValue &&
                        loc.methodName == toRemove.methodName &&
                        loc.className == toRemove.className
            }
        }
        analysisMap["broken"] = updatedList
    }


}
