package app.mobile.consideredcosts.launch

import app.mobile.consideredcosts.functionalTests.BaseOperations

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
        lateinit var baseOperations: BaseOperations

        init {
            baseOperations = BaseOperations()
        }
        fun nextBar() =
            baseOperations.isElementByTextPresent(WelcomeStrings.NEXT.text) && baseOperations.isElementByTextPresent(WelcomeStrings.SKIP.text)

        fun startBar() = baseOperations.isElementByTextPresent(WelcomeStrings.START.text)

}