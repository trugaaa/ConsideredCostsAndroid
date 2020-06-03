package app.mobile.consideredcosts.functionalTests

import android.app.Activity
import android.app.Instrumentation
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject2

class BaseOperations {
    companion object {
        private val mDevice: UiDevice = UiDevice.getInstance(getInstrumentation())
        const val APP_PACKAGE = "app.mobile.consideredcosts"
        const val TIME_OUT = 15000

        fun isElementByTextPresent(text: String): Boolean {
            mDevice.waitForWindowUpdate(APP_PACKAGE, TIME_OUT.toLong())
            mDevice.findObject(By.text(text))
            return true
        }

        fun findViewByText(text: String): UiObject2 {
            mDevice.waitForWindowUpdate(APP_PACKAGE, TIME_OUT.toLong())
            return mDevice.findObject(By.text(text))
        }

        fun swipeRight() {
            mDevice.swipe(531, 1346, 1033, 1346, 1)
        }

        fun swipeLeft() {
            mDevice.swipe(1033, 1346, 531, 1346, 1)
        }

        fun getCurrentActivity(): Activity? {
            val instrumentation: Instrumentation = getInstrumentation()
            val monitor: Instrumentation.ActivityMonitor =
                instrumentation.addMonitor(
                    "app.mobile.consideredcosts.LaunchActivity",
                    null,
                    false
                )
            return instrumentation.waitForMonitorWithTimeout(monitor, 3000)
        }
    }
}