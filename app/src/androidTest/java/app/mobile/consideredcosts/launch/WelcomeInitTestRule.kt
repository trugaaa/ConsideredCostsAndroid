package app.mobile.consideredcosts.launch

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class WelcomeInitTestRule : TestRule {
    override fun apply(base: Statement?, description: Description?): Statement = WelcomeStatement(base)

    class WelcomeStatement(private val base: Statement?) : Statement() {
        @Throws(Throwable::class)
        override fun evaluate() {
            val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
            targetContext.getSharedPreferences("", Context.MODE_PRIVATE).edit().clear().apply()
            try {
                base?.evaluate() // This executes your tests
            } finally {
                // Add something you do after test
            }
        }

    }
}