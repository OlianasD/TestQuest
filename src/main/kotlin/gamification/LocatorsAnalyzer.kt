package gamification

import locator.Locator

class LocatorsAnalyzer {

    private val MAX_LENGTH: Int = 20//TODO: to reason about
    private val MAX_HEIGHT: Int = 5

    fun analyzeLocators(locators: Collection<Locator>): Map<String, Int> {

        val cssLocatorsCount = locators.count { it.locatorType.equals("css", ignoreCase = true) }
        val xpathLocatorsCount = locators.count { it.locatorType.equals("xpath", ignoreCase = true) }
        val idLocatorsCount = locators.count { it.locatorType.equals("id", ignoreCase = true) }
        val nameLocatorsCount = locators.count { it.locatorType.equals("name", ignoreCase = true) }
        val classLocatorsCount = locators.count { it.locatorType.equals("class", ignoreCase = true) }
        val linkTextLocatorsCount = locators.count {
            it.locatorType.equals("linkText", ignoreCase = true) ||
                    it.locatorType.equals("partialLinkText", ignoreCase = true)
        }

        val xpathLocators = locators.filter { it.locatorType.equals("xpath", ignoreCase = true) }
        val longXpathCount = xpathLocators.count { it.locatorValue.length > MAX_LENGTH }
        val highXpathCount = xpathLocators.count { it.locatorValue.split("/").filter { node -> node.isNotEmpty() }.size > MAX_HEIGHT }
        val absoluteXpathCount = xpathLocators.count { it.locatorValue.startsWith("/html") }

        val xpathAttributeCount = xpathLocators.count { it.locatorValue.contains("@") }
        val xpathTableCount = xpathLocators.count { it.locatorValue.contains("/table") }
        val xpathDivCount = xpathLocators.count { it.locatorValue.contains("/div") }
        val xpathSpanCount = xpathLocators.count { it.locatorValue.contains("/span") }
        val xpathButtonCount = xpathLocators.count { it.locatorValue.contains("/button") }
        val xpathAnchorCount = xpathLocators.count { it.locatorValue.contains("/a") }
        val xpathFormCount = xpathLocators.count { it.locatorValue.contains("/form") }

        //TODO: up to now, the map is strongly related to the dailies
        return mapOf(
            "xpathLength" to longXpathCount,
            "xpathHeight" to highXpathCount,
            "xpathAbs" to absoluteXpathCount,
            "loc2css" to cssLocatorsCount,
            "loc2xpath" to xpathLocatorsCount,
            "loc2id" to idLocatorsCount,
            "loc2name" to nameLocatorsCount,
            "loc2class" to classLocatorsCount,
            "loc2linkText" to linkTextLocatorsCount,
            "attrRef" to xpathAttributeCount,
            "tableRef" to xpathTableCount,
            "divRef" to xpathDivCount,
            "spanRef" to xpathSpanCount,
            "buttonRef" to xpathButtonCount,
            "linkRef" to xpathAnchorCount,
            "formRef" to xpathFormCount
        )
    }

    fun compareMetrics(old: Map<String, Int>, new: Map<String, Int>): Map<String, Int> {
        return mapOf(
            //good result: positive value (target is to reduce)
            "xpathLength" to ((old["xpathLength"] ?: 0) - (new["xpathLength"] ?: 0)),
            "xpathHeight" to ((old["xpathHeight"] ?: 0) - (new["xpathHeight"] ?: 0)),
            "xpathAbs" to ((old["xpathAbs"] ?: 0) - (new["xpathAbs"] ?: 0)),
            //good result: negative value (target is to increase)
            "loc2css" to ((new["loc2css"] ?: 0) - (old["loc2css"] ?: 0)),
            "loc2xpath" to ((new["loc2xpath"] ?: 0) - (old["loc2xpath"] ?: 0)),
            "loc2id" to ((new["loc2id"] ?: 0) - (old["loc2id"] ?: 0)),
            "loc2name" to ((new["loc2name"] ?: 0) - (old["loc2name"] ?: 0)),
            "loc2class" to ((new["loc2class"] ?: 0) - (old["loc2class"] ?: 0)),
            "loc2linkText" to ((new["loc2linkText"] ?: 0) - (old["loc2linkText"] ?: 0)),
            "attrRef" to ((new["attrRef"] ?: 0) - (old["attrRef"] ?: 0)),
            "tableRef" to ((new["tableRef"] ?: 0) - (old["tableRef"] ?: 0)),
            "divRef" to ((new["divRef"] ?: 0) - (old["divRef"] ?: 0)),
            "spanRef" to ((new["spanRef"] ?: 0) - (old["spanRef"] ?: 0)),
            "buttonRef" to ((new["buttonRef"] ?: 0) - (old["buttonRef"] ?: 0)),
            "linkRef" to ((new["linkRef"] ?: 0) - (old["linkRef"] ?: 0)),
            "formRef" to ((new["formRef"] ?: 0) - (old["formRef"] ?: 0))
        )
    }



}
