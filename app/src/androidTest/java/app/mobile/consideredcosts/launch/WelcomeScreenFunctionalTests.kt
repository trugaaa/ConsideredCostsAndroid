package app.mobile.consideredcosts.launch

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import app.mobile.consideredcosts.functionalTests.BaseOperations
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertTrue
import org.junit.BeforeClass

@RunWith(AndroidJUnit4::class)
class WelcomeScreenFunctionalTests {

    @get:Rule
        val myTestRule = WelcomeInitTestRule()

    @get:Rule
    val activityRule: ActivityTestRule<LaunchActivity> =
        ActivityTestRule(LaunchActivity::class.java, true, true)

    companion object {
        lateinit var welcomeScreen: WelcomeScreen
        lateinit var baseOperations: BaseOperations

        @BeforeClass
        fun init() {
            baseOperations = BaseOperations()
            welcomeScreen = WelcomeScreen()
        }
    }

    @Test
    fun firstWelcomeScreenValidation() {
        assertTrue(baseOperations.isElementByTextPresent(WelcomeScreen.WelcomeStrings.TITLE_SCREEN_ONE.text))
        assertTrue(baseOperations.isElementByTextPresent(WelcomeScreen.WelcomeStrings.TEXT_SCREEN_ONE.text))
        assertTrue(welcomeScreen.nextBar())
    }

//    @Test
//    fun secondWelcomeScreenValidation() {
//        swipeLeft()
//        assertTrue(isElementByTextPresent(WelcomeScreen.WelcomeStrings.TITLE_SCREEN_TWO.text))
//        assertTrue(isElementByTextPresent(WelcomeScreen.WelcomeStrings.TEXT_SCREEN_TWO.text))
//        assertTrue(WelcomeScreen.nextBar())
//    }
//
//    @Test
//    fun thirdWelcomeScreenValidation() {
//        swipeLeft()
//        swipeLeft()
//        assertTrue(isElementByTextPresent(WelcomeScreen.WelcomeStrings.TITLE_SCREEN_THREE.text))
//        assertTrue(isElementByTextPresent(WelcomeScreen.WelcomeStrings.TEXT_SCREEN_THREE.text))
//        assertTrue(WelcomeScreen.startBar())
//    }
}
