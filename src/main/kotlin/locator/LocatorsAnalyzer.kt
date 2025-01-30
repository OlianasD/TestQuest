package locator

import extractor.locator.Locator
import gamification.GamificationManager
import testquest.TestQuestAction


//this class is used to find problematic locators to fix with targeted dailies
class LocatorsAnalyzer {

    companion object {
        //Global map storing all locators issues detected BEFORE any new change (to compare and check what was fixed from last change)
        private var previousTargetedIssuedLocators = emptyMap<String, List<Locator>>()
        //Global map storing all locators issues detected
        private val targetedIssuedLocators = mutableMapOf<String, List<Locator>>()
        //Global map storing locators issues detected and fixed, to be verified once tests are executed
        private val targetedFixedAndPendingLocators = mutableMapOf<String, MutableList<Locator>>()

        fun getFixedAndPendingLocatorsMap(): MutableMap<String, MutableList<Locator>>{
            return targetedFixedAndPendingLocators
        }

        //this method is called to remove the list of fixed locators from pending, once they are exercised in a test
        fun removePendingFixedLocators(locatorsToRemove: List<Locator>) {
            //remove fixed and confirmed locators (i.e., locators fixed, following a test execution to confirm)
            targetedFixedAndPendingLocators.keys.forEach { key ->
                val existingLocators = targetedFixedAndPendingLocators[key]?.toMutableList() ?: return@forEach
                val updatedLocators = existingLocators.filterNot { existingLocator ->
                    locatorsToRemove.any { it.hashCode() == existingLocator.hashCode() }
                }
                if (updatedLocators.isEmpty()) {
                    targetedFixedAndPendingLocators[key] = emptyList<Locator>().toMutableList()
                } else {
                    targetedFixedAndPendingLocators[key] = updatedLocators.toMutableList()
                }
            }
        }

    }





    private var locators = TestQuestAction.locatorsNewStatic

    fun findTargetedIssuedLocators(): MutableMap<String, List<Locator>> {
        previousTargetedIssuedLocators = targetedIssuedLocators.toMap() //copy map to keep track of problems fixed BEFORE any new problem found
        targetedIssuedLocators.keys.retainAll(listOf("broken"))//remove broken locators as they are dynamically computed during test execution
        calculateAbsoluteLocators()
        calculateLongLocators()
        calculateDeepLocators()
        calculatePositionalPredicateLocators()
        calculateBadPredicateLocators()
        calculateNonIDOrXPathLocators()
        if(!targetedIssuedLocators.containsKey("broken"))//if map is new, create an empty list of broken locators
            calculateBrokenLocators()
        //compute differences between initial map and map after evaluation
        //so to retrieve fixed problems to be later confirmed via test execution
        if(previousTargetedIssuedLocators.isNotEmpty())
            calculateFixedAndPendingLocators()
        return targetedIssuedLocators
    }

    //save absolute xpath locators
    private fun calculateAbsoluteLocators() {
        val absoluteLocators = locators.filter {
            it.locatorType.equals("xpath", ignoreCase = true) && it.locatorValue.startsWith("/html")
        }
        targetedIssuedLocators["absolute"] = absoluteLocators
    }

    //save xpath locators with length more than MAX_LENGTH chars
    private fun calculateLongLocators() {
        val longLocators = locators.filter { it.locatorValue.length > GamificationManager.MAX_LENGTH }
        targetedIssuedLocators["length"] = longLocators
    }

    //save xpath locators with more than MAX_LEVEL levels
    private fun calculateDeepLocators() {
        val deepLocators = locators.filter {
            it.locatorType.equals("xpath", ignoreCase = true) &&
                    it.locatorValue.split("/").count { node -> node.isNotEmpty() } > GamificationManager.MAX_LEVEL
        }
        targetedIssuedLocators["level"] = deepLocators
    }

    //save xpath locators with more than MAX_POS_PRED positional predicates
    private fun calculatePositionalPredicateLocators() {
        val positionalPredicateLocators = locators.filter {
            it.locatorType.equals("xpath", ignoreCase = true) &&
                    Regex("\\[\\d+]").findAll(it.locatorValue).count() > GamificationManager.MAX_POS_PRED
        }
        targetedIssuedLocators["posPredicate"] = positionalPredicateLocators
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
        targetedIssuedLocators["badPredicate"] = badPredicateLocators
    }

    //save locators that are neither ID or Xpath
    private fun calculateNonIDOrXPathLocators() {
        val nonIDOrXPathLocators = locators.filter {
            !it.locatorType.equals("id", ignoreCase = true) &&
                    !it.locatorType.equals("xpath", ignoreCase = true)
        }
        targetedIssuedLocators["noIDOrXPath"] = nonIDOrXPathLocators
    }

    private fun calculateBrokenLocators() {
        targetedIssuedLocators["broken"] = emptyList()
    }

    //this value is updated once targeted dailies are present and tests are executed
    fun calculateBrokenLocators(brokenLoc: Locator) {
        val currentList = targetedIssuedLocators["broken"] ?: emptyList()
        val isAlreadyPresent = currentList.any { existingLoc ->
            existingLoc.locatorValue == brokenLoc.locatorValue &&
                    existingLoc.methodName == brokenLoc.methodName &&
                    existingLoc.className == brokenLoc.className
        }
        if (!isAlreadyPresent) {
            val updatedList = currentList + brokenLoc
            targetedIssuedLocators["broken"] = updatedList
        }
    }




    fun getBrokenLocs(): List<Locator>? {
        return targetedIssuedLocators["broken"]
    }

    fun updateBrokenLocs(repairedLocs: List<Locator>) {
        val currentList = targetedIssuedLocators["broken"] ?: emptyList()
        val updatedList = currentList.filterNot { loc ->
            repairedLocs.any { toRemove ->
                loc.locatorValue == toRemove.locatorValue &&
                        loc.methodName == toRemove.methodName &&
                        loc.className == toRemove.className
            }
        }
        targetedIssuedLocators["broken"] = updatedList
    }




    //the logic is that a potentially fixed locator is present in initialAnalysisMap and no more present in analysisMap
    //i.e., the map before and after analyzing the test suite
    private fun calculateFixedAndPendingLocators() {

        previousTargetedIssuedLocators.forEach { (key, initialLocators) ->

            // Get existing issued locators (if any)
            val issuedLocators = targetedIssuedLocators[key] ?: emptyList()

            // Mutable map to track the most recent locator by hash
            val fixedLocatorsMap = mutableMapOf<Int, Locator>()

            // Find issued locators that are now fixed and pending (i.e., no more present in targetedIssuedLocators)
            initialLocators.forEach { initialLocator ->
                val wasFixed = issuedLocators.none { it.hashCode() == initialLocator.hashCode() }
                if (wasFixed)
                    fixedLocatorsMap[initialLocator.hashCode()] = initialLocator
            }

            // Get existing fixed and pending locators list (if any)
            val fixedAndPendingLocators = targetedFixedAndPendingLocators[key]?.toMutableList() ?: mutableListOf()

            // Add newly fixed and pending locators and overwrite old ones (e.g., if they change line position or they were fixed multiple times)
            fixedLocatorsMap.values.forEach { newLocator ->
                val existingIndex = fixedAndPendingLocators.indexOfFirst { it.hashCode() == newLocator.hashCode() }
                if (existingIndex != -1)
                    fixedAndPendingLocators[existingIndex] = newLocator
                else
                    fixedAndPendingLocators.add(newLocator)
            }

            // Remove fixed and pending locators that are also issued
            // e.g., they are fixed and made issued again before confirmation
            val filteredFixedAndPendingLocators = fixedAndPendingLocators.filterNot { fixedLocator ->
                issuedLocators.any { it.hashCode() == fixedLocator.hashCode() }
            }

            // Remove fixed and pending locators that actually do not exist anymore and keep info updated
            // (e.g., in case a fixed locator is moved from a line to another or changed multiple time but never becoming issued)
            val finalFixedAndPendingLocators = locators.filter { locator ->
                filteredFixedAndPendingLocators.any { it.hashCode() == locator.hashCode() }
            }

            // Update the map with the filtered locators
            targetedFixedAndPendingLocators[key] = finalFixedAndPendingLocators.toMutableList()
        }


    }









}
