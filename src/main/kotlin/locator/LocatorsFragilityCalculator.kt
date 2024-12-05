package locator

import gamification.GamificationManager
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.exp

class LocatorsFragilityCalculator {


    fun calculateOverallFragility(locators: List<Locator>): Double{
        var overallFragility = 0.0
        for (loc in locators)
            overallFragility += calculateFragility(loc)
        return overallFragility/locators.size
    }


    fun calculateFragility(locator: Locator): Double {
        return when (locator.locatorType) {
            "id" -> 0.1
            "name"-> calculateNameRobustness(locator.locatorValue)
            "linkText"-> calculateLinkTextRobustness(locator.locatorValue)
            "cssSelector" -> calculateCssFragility(locator.locatorValue)
            "className" -> calculateClassNameRobustness(locator.locatorValue)
            "xpath"-> calculateXpathFragility(locator.locatorValue)
            else -> {0.5}//e.g., tagName
        }
    }

    //weights and factors affecting the robustness are: long names, names with many words,
    // names with keywords which are likely unstable (e.g., input), and names with numbers
    private fun calculateNameRobustness(value: String): Double {
        val lengthWeight = 1.0
        val wordsWeight = 1.0
        val meaningWeight = 1.0
        val numberWeight = 1.0
        val maxLength = 30
        val lengthFactor = value.length.coerceAtMost(maxLength) / maxLength.toDouble()
        val maxWords = 5
        val wordFactor = value.split(Regex("\\W+")).size / maxWords.toDouble()
        val genericTerms = listOf(
            "button", "header", "container", "input", "text", "name", "class", "submit",
            "link", "item", "element", "field", "section", "footer", "nav", "menu", "value",
            "content", "list", "widget", "form", "label", "option", "row", "column",
            "tile", "modal", "card", "panel", "data", "image", "icon", "table", "grid", "title",
            "block", "sidebar", "tab", "input-group", "checkbox", "radio", "dropdown",
            "tooltip", "message", "alert", "status", "card-header", "card-body", "form-control"
        )
        val meaningCount = value.split(Regex("\\W+")).count { term -> genericTerms.contains(term.lowercase()) }
        val meaningFactor = meaningCount * 0.5
        val numbersCount = value.count { it.isDigit() }
        val numberFactor = numbersCount * 0.5
        val totalScore = (lengthWeight * lengthFactor) +
                (wordsWeight * wordFactor) +
                (meaningWeight * meaningFactor) +
                (numberWeight * numberFactor)
        return totalScore / (lengthWeight + wordsWeight + meaningWeight + numberWeight)
    }

    private fun calculateClassNameRobustness(value: String): Double{
        return calculateNameRobustness(value)
    }

    //weights and factors affecting the robustness are: length, presence of numbers which may change
    private fun calculateLinkTextRobustness(value: String): Double {
        val lengthWeight = 1.0
        val lengthFactor = value.length.toDouble() / GamificationManager.MAX_LENGTH
        val dateNumWeight = 1.0
        val numbersInText = value.count { it.isDigit() }
        val numberFactor = numbersInText * 0.2
        val totalScore = (lengthWeight * lengthFactor) + (dateNumWeight * numberFactor)
        return totalScore / (lengthWeight + dateNumWeight)
    }

    private fun calculateCssFragility(cssSelector: String): Double {
        val isSpecific = cssSelector.contains("#")
        val classCount = cssSelector.count { it == '.' }
        val combinatorCount = cssSelector.count { it in listOf(' ', '>', '+', '~') }
        val length = cssSelector.length
        val strongSelectors = listOf("#", ".")
        val fragileSelectors = listOf("a", "img", "div", "span", "p", "input", "button")
        val strongSelectorsCount = strongSelectors.sumOf { cssSelector.split(it).size - 1 }
        val fragileSelectorsCount = fragileSelectors.sumOf { cssSelector.split(it).size - 1 }
        val specificityWeight = 1.0
        val specificityFactor = if (isSpecific) 0.0 else 1.0
        val classWeight = 1.0
        val maxClassCount = 3
        val classFactor = (classCount / maxClassCount.toDouble())
        val combinatorWeight = 1.0
        val maxCombinatorCount = 3
        val combinatorFactor = (combinatorCount / maxCombinatorCount.toDouble())
        val strongSelectorWeight = 1.0
        val strongSelectorFactor = strongSelectorsCount * -1
        val fragileSelectorWeight = 1.0
        val maxFragileSelectors = 3
        val fragileSelectorFactor = (fragileSelectorsCount / maxFragileSelectors.toDouble())
        val lengthWeight = 1.0
        val maxLength = 20
        val lengthFactor = length / maxLength.toDouble()
        val totalScore = (specificityWeight * specificityFactor) +
                (classWeight * classFactor) +
                (combinatorWeight * combinatorFactor) +
                (strongSelectorWeight * strongSelectorFactor) +
                (fragileSelectorWeight * fragileSelectorFactor) +
                (lengthWeight * lengthFactor)
        val totalWeights = specificityWeight + classWeight + combinatorWeight +
                strongSelectorWeight + fragileSelectorWeight + lengthWeight
        return (totalScore / totalWeights).coerceIn(0.0, 1.0)
    }

    //weights and factors affecting it are: absolute, num levels, num predicates, length, presence of good/bad preds
    private fun calculateXpathFragility(xpath: String): Double {
        val isAbsolute = xpath.startsWith("/html")
        val levels = xpath.count { it == '/' }
        val predicates = xpath.count { it == '[' }
        val length = xpath.length
        val goodPredicatesCount = GamificationManager.GOOD_PREDS.sumOf { xpath.split(it).size - 1 }
        val badPredicatesCount = (GamificationManager.BAD_PREDS + GamificationManager.BAD_JS)
            .sumOf { attribute -> xpath.split("@$attribute").size - 1 }
        val absoluteWeight = 1.0
        val absoluteFactor = if (isAbsolute) 5.0 else 0.1
        val levelsWeight = 2.0
        val maxLevels = GamificationManager.MAX_LEVEL
        val levelsFactor = (levels / maxLevels.toDouble())
        val predicatesWeight = 2.5
        val maxPredicates = GamificationManager.MAX_POS_PRED
        val predicatesFactor = (predicates / maxPredicates.toDouble())
        val goodPredicatesWeight = 1.0
        val goodPredicatesFactor = goodPredicatesCount * -0.2
        val badPredicatesWeight = 4.0
        val maxBadPredicates = 1
        val badPredicatesFactor = (badPredicatesCount / maxBadPredicates.toDouble())
        val lengthWeight = 2.0
        val maxLength = GamificationManager.MAX_LENGTH
        val lengthFactor = (length / maxLength.toDouble())
        val totalScore = (absoluteWeight * absoluteFactor) +
                (levelsWeight * levelsFactor) +
                (predicatesWeight * predicatesFactor) +
                (goodPredicatesWeight * goodPredicatesFactor) +
                (badPredicatesWeight * badPredicatesFactor) +
                (lengthWeight * lengthFactor)
        val totalWeights = listOf(absoluteWeight, levelsWeight, predicatesWeight, badPredicatesWeight, lengthWeight)
            .filter { it > 0 }
            .sum()
        return (totalScore / totalWeights).coerceIn(0.0, 1.0)
    }




}