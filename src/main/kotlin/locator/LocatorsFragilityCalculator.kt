package locator

import kotlin.math.exp

class LocatorsFragilityCalculator {


    fun calculateOverallFragility(locators: List<Locator>): Double{
        var overallFragility: Double = 0.0
        for (loc in locators)
            overallFragility += calculateFragility(loc)
        return overallFragility/locators.size
    }


    fun calculateFragility(locator: Locator): Double {
        return when (locator.locatorType) {
            "id" -> 0.1
            "name"-> 0.4
            "linkText"-> 0.4
            "cssSelector" -> 0.4
            "className" -> 0.4
            "xpath"-> calculateXpathFragility(locator.locatorValue)
            else -> {0.0}
        }
    }

    private fun calculateXpathFragility(xpath: String): Double {
        val isAbsolute = xpath.startsWith("/html")
        val levels = xpath.count { it == '/' }
        val predicates = xpath.count { it == '[' }
        val length = xpath.length

        val strongPredicates = listOf("@id", "@name", "@class", "@title", "@alt", "@value")
        val fragilePredicates = listOf(
            "@src", "@href", "@height", "@width",
            "onclick", "onload", "onmouseover", "onmouseout", "onchange", "onsubmit", "onfocus", "onkeydown"
        )

        val goodPredicatesCount = strongPredicates.sumOf { xpath.split(it).size - 1 }
        val badPredicatesCount = fragilePredicates.sumOf { xpath.split(it).size - 1 }

        val weightAbsolute = if (isAbsolute) 2.0 else -0.5
        val weightLevels = levels * 0.3
        val weightPredicates = predicates * 0.2
        val weightGoodPredicates = -goodPredicatesCount * 0.6
        val weightBadPredicates = badPredicatesCount * 0.8
        val weightLength = if (length > 30) 0.4 else 0.1

        val rawFragilityScore = (weightAbsolute + weightLevels + weightPredicates +
                weightGoodPredicates + weightBadPredicates + weightLength)

        return 1 / (1 + exp(-rawFragilityScore))
    }




}