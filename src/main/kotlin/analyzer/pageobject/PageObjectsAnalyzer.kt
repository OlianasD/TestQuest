package analyzer.pageobject


import extractor.locator.LocatorKey
import extractor.pageobject.MethodInfo
import extractor.pageobject.MethodInfoKey
import gamification.GamificationManager
import testquest.TestQuestAction
import utils.ProgressFileHandler


//this class is used to find problematic POs to fix with targeted dailies
class PageObjectsAnalyzer {

    companion object {

        //Global map storing all POs issues detected
        private val targetedIssuedPOs= mutableMapOf<String, List<Any>>()
        //Global map storing all POs issues detected BEFORE any new change (to compare and check what was fixed from last change)
        private var previousTargetedIssuedPOs = emptyMap<String, List<Any>>()
        //Global map storing POs issues detected and fixed, to be verified once tests are executed
        private var targetedFixedAndPendingPOs = mutableMapOf<String, MutableList<Any>>()


        fun getFixedAndPendingLocatorsMap(): MutableMap<String, MutableList<Any>>{
            return targetedFixedAndPendingPOs
        }


        //this method is called to remove the list of fixed POs from pending, once they are exercised in a test
        fun removePendingFixedPOs(passedIssue: List<Any>) {
            //remove fixed and confirmed POs (i.e., PO fixed, following a test execution to confirm)
            targetedFixedAndPendingPOs.keys.forEach { key ->
                val fixedAndPendingPOs = targetedFixedAndPendingPOs[key]?.toMutableList() ?: return@forEach
                val stillFixedAndPendingPOs = fixedAndPendingPOs.filterNot { fixedPendingPO ->
                    passedIssue.any { it.hashCode() == fixedPendingPO.hashCode()
                    }
                }
                if (stillFixedAndPendingPOs.isEmpty()) {
                    targetedFixedAndPendingPOs[key] = emptyList<Any>().toMutableList()
                } else {
                    targetedFixedAndPendingPOs[key] = stillFixedAndPendingPOs.toMutableList()
                }
            }
            //fixed and pending POs are saved for future reuse
            ProgressFileHandler.saveFixedAndPendingPOsData(targetedFixedAndPendingPOs)
        }
        fun intersectMethodInfoLists(
            list1: List<MethodInfo>,
            list2: List<MethodInfo>
        ): List<MethodInfo> {

            val comparisonSet = list2.map { method ->
                MethodInfoKey(
                    name = method.name,
                    returnType = method.returnType,
                    paramTypes = method.paramTypes,
                    locators = method.locators.map { locator ->
                        LocatorKey(locator.locatorType, locator.locatorValue)
                    },
                    assertionLines = method.assertionLines,
                    seleniumCommands = method.seleniumCommands
                )
            }.toSet()

            return list1.filter { method ->
                MethodInfoKey(
                    name = method.name,
                    returnType = method.returnType,
                    paramTypes = method.paramTypes,
                    locators = method.locators.map { locator ->
                        LocatorKey(locator.locatorType, locator.locatorValue)
                    },
                    assertionLines = method.assertionLines,
                    seleniumCommands = method.seleniumCommands
                ) in comparisonSet
            }
        }

        fun containsMethod(
            list: List<MethodInfo>,
            target: MethodInfo
        ): Boolean {

            return list.any { candidate ->

                // Confronto campi principali
                candidate.name == target.name &&
                        candidate.returnType == target.returnType &&
                        candidate.paramTypes == target.paramTypes &&
                        candidate.assertionLines == target.assertionLines &&
                        candidate.seleniumCommands == target.seleniumCommands &&

                        // Confronto locators (solo locatorType e locatorValue)
                        candidate.locators.size == target.locators.size &&
                        candidate.locators.zip(target.locators).all { (loc1, loc2) ->
                            loc1.locatorType == loc2.locatorType &&
                                    loc1.locatorValue == loc2.locatorValue
                        }
            }
        }

    }

    private var POs = TestQuestAction.POsNew




    //find POs that present issues in the test suite (i.e., targeted as they target the actual issues)
    fun findIssuesInPOs(): MutableMap<String, List<Any>> {
        previousTargetedIssuedPOs = targetedIssuedPOs.toMap() //copy map to keep track of problems fixed BEFORE any new problem found
        calculateEmptyPageObjects()
        calculateMissingCommandsMethods()
        calculateMissingReturnedPOMethods()
        calculateAssertionsInPOs()
        calculateNonCanonicalLocators()
        calculateUnusedPOMethods()
        calculateLocsOutPOs()
        calculateDuplicatedPOMethods()

        //load fixedAndPending from file, if exist
        val savedFixedAndPending = ProgressFileHandler.loadFixedAndPendingPOsData()
        //keep only fixed and pending related to existing POs
        var cleanedSavedFixedAndPending: Map<String, List<Any>>? = null
        if(savedFixedAndPending!=null) {
            cleanedSavedFixedAndPending = savedFixedAndPending.mapValues { (_, savedList) ->
                savedList.filter { po ->
                    POs.any { it.hashCode() == po.hashCode() }
                }
            }.filterValues { it.isNotEmpty() }
        }
        if(GamificationManager.gamificationMode != GamificationManager.GamificationMode.ADVANCED) {
            //if some new issue have been observed in the session, retrieve new fixed and pending from analysis and add saved ones
            if (previousTargetedIssuedPOs.isNotEmpty())
                calculateFixedAndPendingPOs(cleanedSavedFixedAndPending)
            //else, populate fixed and pending only with saved data
            else {
                targetedFixedAndPendingPOs =
                    cleanedSavedFixedAndPending?.mapValues { it.value.toMutableList() }?.toMutableMap()
                        ?: mutableMapOf()
                targetedIssuedPOs.keys.forEach { key ->
                    if (!targetedFixedAndPendingPOs.containsKey(key)) {
                        targetedFixedAndPendingPOs[key] = mutableListOf()
                    }
                }
            }
        }
        return targetedIssuedPOs
    }





    private fun calculateEmptyPageObjects() {
        val emptyPOs = POs.filter { it.methods.isEmpty() }
        if (emptyPOs.isNotEmpty())
            targetedIssuedPOs["emptyPageObjects"] = emptyPOs
        else
            targetedIssuedPOs["emptyPageObjects"] = emptyList()
    }

    private fun calculateMissingCommandsMethods() {
        val missingCommandsMethods = POs.flatMap { po ->
            po.methods.filter { it.seleniumCommands.isEmpty() }.map { it }
        }
        if (missingCommandsMethods.isNotEmpty())
            targetedIssuedPOs["missingCommandMethods"] = missingCommandsMethods
        else
            targetedIssuedPOs["missingCommandMethods"] = emptyList()
    }

    private fun calculateMissingReturnedPOMethods() {
        val voidReturnMethods = POs.flatMap { po ->
            po.methods.filter { it.returnType.equals("void", ignoreCase = true) }.map { it }
        }
        if (voidReturnMethods.isNotEmpty())
            targetedIssuedPOs["missingRetPOMethods"] = voidReturnMethods
        else
           targetedIssuedPOs["missingRetPOMethods"] = emptyList()

    }

    private fun calculateAssertionsInPOs() {
        val assertionsInPOs = POs.flatMap { po ->
            po.methods.flatMap { it.assertionLines }
        }
        if (assertionsInPOs.isNotEmpty())
            targetedIssuedPOs["assertInPOMethods"] = assertionsInPOs
        else
            targetedIssuedPOs["assertInPOMethods"] = emptyList()
    }

    private fun calculateNonCanonicalLocators() {
        val nonCanonicalLocators = POs.flatMap { it.nonCanonicalLocators }
        if (nonCanonicalLocators.isNotEmpty())
            targetedIssuedPOs["nonCanonicalLocs"] = nonCanonicalLocators
        else
            targetedIssuedPOs["nonCanonicalLocs"] = emptyList()
    }

    private fun calculateUnusedPOMethods() {
        val usedMethods = TestQuestAction.POCallsNew.values
            .flatten()
            .map { it.pageObject to it.methodName }
            .toSet()
        var unusedMethods : ArrayList<MethodInfo> = POs.flatMap { po ->
            po.methods.filter { method -> (po.name to method.name) !in usedMethods }
        } as ArrayList<MethodInfo>
        if (unusedMethods.isNotEmpty()) {
            //check for superclass methods invoked from subclass
           var copyOfUnusedMethods = ArrayList(unusedMethods)
            for(unusedMethod in copyOfUnusedMethods) {
                var currClass = TestQuestAction.POsNew.find { it.name == unusedMethod.pageObject }
                var descendants = TestQuestAction.POsNew.filter { currClass?.name in it.ancestors }
                for(descendant in descendants) {
                   var useInSubclass = Pair(descendant.name, unusedMethod.name)
                    if(useInSubclass in usedMethods) {
                        unusedMethods.remove(unusedMethod)
                        break
                    }
                }
            }
            targetedIssuedPOs["unusedPOMethods"] = unusedMethods
        }
        else
            targetedIssuedPOs["unusedPOMethods"] = emptyList()
    }

    private fun calculateLocsOutPOs() {
        val invalidClassLocators = TestQuestAction.locatorsNew.filter { !it.className.endsWith("_Page") }
        if (invalidClassLocators.isNotEmpty())
            targetedIssuedPOs["outPOLocs"] = invalidClassLocators
        else {
            targetedIssuedPOs["outPOLocs"] = emptyList()
        }
    }

    //compute duplicated methods that should be moved to common ancestor
    private fun calculateDuplicatedPOMethods() {
        val clonedMethods = mutableSetOf<MethodInfo>()
        for (po1 in POs)
            for (po2 in POs)
                if (po1 != po2) {
                    //val commonMethods = po1.methods.toSet().intersect(po2.methods.toSet())
                    val commonMethods = intersectMethodInfoLists(po1.methods, po2.methods)
                    clonedMethods.addAll(commonMethods)
                }
        if (clonedMethods.isNotEmpty())
            targetedIssuedPOs["duplicatedMethods"] = clonedMethods.toList()
        else
            targetedIssuedPOs["duplicatedMethods"] = emptyList()
    }










    //the logic is that a potentially fixed PO is present in initialAnalysisMap and no more present in analysisMap
    //i.e., the maps before and after analyzing the test suite
    private fun calculateFixedAndPendingPOs(savedFixedAndPending: Map<String, List<Any>>?) {
        previousTargetedIssuedPOs.forEach { (key, initialPOs) ->

            // Get existing issued POs (if any)
            val issuedPOs = targetedIssuedPOs[key] ?: emptyList()

            // Mutable map to track the most recent PO by hash
            val fixedPOsMap = mutableMapOf<Int, Any>()

            // Find issued pos that are now fixed and pending (i.e., no more present in targetedIssuedLocators)
            initialPOs.forEach { initialPO ->
                val wasFixed = issuedPOs.none { it.hashCode() == initialPO.hashCode() }
                if (wasFixed)
                    fixedPOsMap[initialPO.hashCode()] = initialPO
            }

            // Get existing fixed and pending POs list (if any)
            val fixedAndPendingPOs = targetedFixedAndPendingPOs[key]?.toMutableList() ?: mutableListOf()

            // Add newly fixed and pending POs and overwrite old ones (e.g., if they change line position or they were fixed multiple times)
            fixedPOsMap.values.forEach { newPO ->
                val existingIndex = fixedAndPendingPOs.indexOfFirst { it.hashCode() == newPO.hashCode() }
                if (existingIndex != -1)
                    fixedAndPendingPOs[existingIndex] = newPO
                else
                    fixedAndPendingPOs.add(newPO)
            }

            // Remove fixed and pending POs that are also issued
            // e.g., they are fixed and made issued again before confirmation
            val filteredFixedAndPendingPOs = fixedAndPendingPOs.filterNot { fixedPO ->
                issuedPOs.any { it.hashCode() == fixedPO.hashCode() }
            }

            // Remove fixed and pending POs that actually do not exist anymore and keep info updated
            // (e.g., in case a fixed PO is moved from a line to another or changed multiple time but never becoming issued)
            val finalFixedAndPendingPOs = POs.filter { po ->
                filteredFixedAndPendingPOs.any { it.hashCode() == po.hashCode() }
            }

            // Add to list the fixed and pending POs, previously saved from file, as long as they are unique
            savedFixedAndPending?.get(key)?.forEach { savedPO ->
                if (fixedAndPendingPOs.none { it.hashCode() == savedPO.hashCode() }) {
                    fixedAndPendingPOs.add(savedPO)
                }
            }

            // Update the map with the filtered POs
            targetedFixedAndPendingPOs[key] = finalFixedAndPendingPOs.toMutableList()

            // Save fixed and pending POs on file for future reuse
           ProgressFileHandler.saveFixedAndPendingPOsData(targetedFixedAndPendingPOs)
        }

    }

}
