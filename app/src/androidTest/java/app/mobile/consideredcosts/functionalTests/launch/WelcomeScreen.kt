package app.mobile.consideredcosts.functionalTests.launch

import app.mobile.consideredcosts.functionalTests.BaseOperations.Companion.isElementByTextPresent
import app.mobile.consideredcosts.functionalTests.BaseOperations.Companion.swipeLeft
import junit.framework.Assert.assertTrue
import org.junit.Assert

class WelcomeScreen {
    enum class WelcomeStrings(val text:String) {
        SKIP("SKIP"),
        NEXT("NEXT"),
        START("START"),
        TITLE_SCREEN_ONE("GET AN OVERVIEW"),
        TEXT_SCREEN_ONE("OF INCOMES AND OUTGOES"),
        TITLE_SCREEN_TWO("SET FINANCIAL GOALS"),
        TEXT_SCREEN_TWO("AND ACHIEVE THEM"),
        TITLE_SCREEN_THREE("TAKE CARE"),
        TEXT_SCREEN_THREE("OF YOUR PROFIT")
    }

    companion object {
        fun nextBar() =
            isElementByTextPresent(WelcomeStrings.NEXT.text) && isElementByTextPresent(WelcomeStrings.SKIP.text)

        fun startBar() = isElementByTextPresent(WelcomeStrings.START.text)

        fun firstScreenOpened() {
            assertTrue(isElementByTextPresent(WelcomeStrings.TITLE_SCREEN_ONE.text))
            assertTrue(isElementByTextPresent(WelcomeStrings.TEXT_SCREEN_ONE.text))
            assertTrue(nextBar())
        }

        fun secondScreenOpened() {
            swipeLeft()
            assertTrue(isElementByTextPresent(WelcomeStrings.TITLE_SCREEN_TWO.text))
            assertTrue(isElementByTextPresent(WelcomeStrings.TEXT_SCREEN_TWO.text))
            assertTrue(nextBar())
        }

        fun thirdScreenOpened() {
            swipeLeft()
            swipeLeft()
            assertTrue(isElementByTextPresent(WelcomeStrings.TITLE_SCREEN_THREE.text))
            assertTrue(isElementByTextPresent(WelcomeStrings.TEXT_SCREEN_THREE.text))
            assertTrue(startBar())
        }
    }
}