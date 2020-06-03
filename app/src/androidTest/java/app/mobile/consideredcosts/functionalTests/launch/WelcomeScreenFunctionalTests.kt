package app.mobile.consideredcosts.functionalTests.launch

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import app.mobile.consideredcosts.functionalTests.launch.WelcomeScreen.Companion.firstScreenOpened
import app.mobile.consideredcosts.functionalTests.launch.WelcomeScreen.Companion.secondScreenOpened
import app.mobile.consideredcosts.functionalTests.launch.WelcomeScreen.Companion.thirdScreenOpened

import org.junit.Before

@RunWith(AndroidJUnit4::class)
class WelcomeScreenFunctionalTests {
    @get:Rule
    val activityRule: ActivityTestRule<LaunchActivity> =
        ActivityTestRule(LaunchActivity::class.java, true, true)

    @Before
    fun initialization(){
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        targetContext.getSharedPreferences("", Context.MODE_PRIVATE).edit().clear().apply()
    }

    @Test
    fun firstWelcomeScreenValidation() {
        firstScreenOpened()
    }

    @Test
    fun secondWelcomeScreenValidation() {
        secondScreenOpened()
    }

    @Test
    fun thirdWelcomeScreenValidation() {
        thirdScreenOpened()
    }
}
