package analyzer.pageobject


import testquest.TestQuestAction
import utils.UserProgressFileHandler


//this class is used to find problematic locators to fix with targeted dailies
class PageObjectsAnalyzer {

    companion object {

        //Global map storing all POs issues detected
        private val targetedIssuedPOs= mutableMapOf<String, List<Any>>()
        //Global map storing all POs issues detected BEFORE any new change (to compare and check what was fixed from last change)
        private var previousTargetedIssuedPOs = emptyMap<String, List<Any>>()
        //Global map storing POs issues detected and fixed, to be verified once tests are executed
        private var targetedFixedAndPendingPOs = mutableMapOf<String, MutableList<Any>>()



        //this method is called to remove the list of fixed POs from pending, once they are exercised in a test
        fun removePendingFixedPOs(passedPOs: List<Any>) {
            //remove fixed and confirmed POs (i.e., PO fixed, following a test execution to confirm)
            targetedFixedAndPendingPOs.keys.forEach { key ->
                val fixedAndPendingPOs = targetedFixedAndPendingPOs[key]?.toMutableList() ?: return@forEach
                val stillFixedAndPendingPOs = fixedAndPendingPOs.filterNot { fixedPendingPO ->
                    passedPOs.any { it.hashCode() == fixedPendingPO.hashCode()
                    }
                }
                if (stillFixedAndPendingPOs.isEmpty()) {
                    targetedFixedAndPendingPOs[key] = emptyList<Any>().toMutableList()
                } else {
                    targetedFixedAndPendingPOs[key] = stillFixedAndPendingPOs.toMutableList()
                }
            }
            //fixed and pending POs are saved for future reuse
            UserProgressFileHandler.saveFixedAndPendingPOsData(targetedFixedAndPendingPOs)
        }


    }

    private var POs = TestQuestAction.POsNew




    //find POs that present issues in the test suite (i.e., targeted as they target the actual issues)
    fun findIssuesInPOs(): MutableMap<String, List<Any>> {
        previousTargetedIssuedPOs = targetedIssuedPOs.toMap() //copy map to keep track of problems fixed BEFORE any new problem found
        calculateMissingCommands()
        calculateMissingReturnedPOs()
        calculateAssertionsInPOs()
        calculateNonCanonicalLocators()
        calculateUnusedPOMethods()
        calculateLocsOutPOs()
        calculateMissingCommonAncestors()

        //load fixedAndPending from file, if exist
        val savedFixedAndPending = UserProgressFileHandler.loadFixedAndPendingPOsData()
        //keep only fixed and pending related to existing POs
        var cleanedSavedFixedAndPending: Map<String, List<Any>>? = null
        if(savedFixedAndPending!=null) {
            cleanedSavedFixedAndPending = savedFixedAndPending.mapValues { (_, savedList) ->
                savedList.filter { po ->
                    POs.any { it.hashCode() == po.hashCode() }
                }
            }.filterValues { it.isNotEmpty() }
        }
        //if some new issue have been observed in the session, retrieve new fixed and pending from analysis and add saved ones
        if(previousTargetedIssuedPOs.isNotEmpty())
            calculateFixedAndPendingPOs(cleanedSavedFixedAndPending)
        //else, populate fixed and pending only with saved data
        else {
            targetedFixedAndPendingPOs =
                cleanedSavedFixedAndPending?.mapValues { it.value.toMutableList() }?.toMutableMap() ?: mutableMapOf()
            targetedIssuedPOs.keys.forEach { key ->
                if (!targetedFixedAndPendingPOs.containsKey(key)) {
                    targetedFixedAndPendingPOs[key] = mutableListOf()
                }
            }
        }
        return targetedIssuedPOs
    }




    private fun calculateMissingCommands() {
        val missingCommandsMethods = POs.flatMap { po ->
            po.methods.filter { it.seleniumCommands.isEmpty() }.map { it }
        }
        if (missingCommandsMethods.isNotEmpty())
            targetedIssuedPOs["missingCommandMethods"] = missingCommandsMethods
    }

    private fun calculateMissingReturnedPOs() {
        val voidReturnMethods = POs.flatMap { po ->
            po.methods.filter { it.returnType.equals("void", ignoreCase = true) }.map { it }
        }
        if (voidReturnMethods.isNotEmpty())
            targetedIssuedPOs["missingRetPOMethods"] = voidReturnMethods
    }

    private fun calculateAssertionsInPOs() {
        val includingAssertionsMethods = POs.flatMap { po ->
            po.methods.filter { it.assertionLines.isNotEmpty() }.map { it }
        }
        if (includingAssertionsMethods.isNotEmpty()) {
            targetedIssuedPOs["assertInPOMethods"] = includingAssertionsMethods
        }
    }

    private fun calculateNonCanonicalLocators() {
        val nonCanonicalLocators = POs.flatMap { it.nonCanonicalLocators }
        if (nonCanonicalLocators.isNotEmpty()) {
            targetedIssuedPOs["nonCanonicalLocs"] = nonCanonicalLocators
        }
    }

    private fun calculateUnusedPOMethods() {
        val usedMethods = TestQuestAction.POCallsNew.values
            .flatten()
            .map { it.pageObject to it.method }
            .toSet()
        val unusedMethods = POs.flatMap { po ->
            po.methods.filter { method -> (po.name to method.name) !in usedMethods }
        }
        if (unusedMethods.isNotEmpty()) {
            targetedIssuedPOs["unusedPOMethods"] = unusedMethods
        }
    }

    private fun calculateLocsOutPOs() {
        val invalidClassLocators = TestQuestAction.locatorsNew.filter { !it.className.endsWith("_Page") }
        if (invalidClassLocators.isNotEmpty())
            targetedIssuedPOs["outPOLocs"] = invalidClassLocators
    }

    //compute POs with same methods but no common ancestors to store the same methods
    private fun calculateMissingCommonAncestors() {
        val POsWithMissingCommonAncestors = POs.filter { po1 ->
            POs.any { po2 ->
                po1 != po2 &&
                        po1.methods.any { method1 ->
                            po2.methods.any { method2 ->
                                method1 == method2
                            }
                        } && po1.ancestors.none { it in po2.ancestors }
            }
        }
        if (POsWithMissingCommonAncestors.isNotEmpty())
            targetedIssuedPOs["missingAncestorPOs"] = POsWithMissingCommonAncestors
    }









    //the logic is that a potentially fixed PO is present in initialAnalysisMap and no more present in analysisMap
    //i.e., the maps before and after analyzing the test suite
    private fun calculateFixedAndPendingPOs(savedFixedAndPending: Map<String, List<Any>>?) {
        previousTargetedIssuedPOs.forEach { (key, initialPOs) ->

            // Get existing issued POs (if any)
            val issuedPOs = targetedIssuedPOs[key] ?: emptyList()

            // Mutable map to track the most recent PO by hash
            val fixedPOsMap = mutableMapOf<Int, Any>()

            // Find issued locators that are now fixed and pending (i.e., no more present in targetedIssuedLocators)
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
            UserProgressFileHandler.saveFixedAndPendingPOsData(targetedFixedAndPendingPOs)
        }

    }


































}
