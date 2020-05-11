package app.mobile.consideredcosts.launch

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.data.SharedPreferencesManager
import app.mobile.consideredcosts.sign.PinActivity
import app.mobile.consideredcosts.sign.SignActivity
import app.mobile.consideredcosts.welcome.WelcomeSliderAdapter
import app.mobile.consideredcosts.welcome.WelcomeSliderFragment
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.fragment_welcome_slider.*


class LaunchActivity : AppCompatActivity(), PagerListener {

    val welcomeSliderAdapter by lazy { WelcomeSliderAdapter(supportFragmentManager) }

    var welcomeScreensSeen: Boolean = false

    private val sharedPreferencesManager by lazy { SharedPreferencesManager(this) }

    private val welcomeSliderFragmentOne = WelcomeSliderFragment.newInstanse(
        R.string.welcome1Title,
        R.drawable.ic_wallet,
        R.string.welcome1Text,
        R.drawable.bg1
    )
    private val welcomeSliderFragmentTwo = WelcomeSliderFragment.newInstanse(
        R.string.welcome2Title,
        R.drawable.ic_save,
        R.string.welcome2Text,
        R.drawable.bg3
    )
    private val welcomeSliderFragmentThree = WelcomeSliderFragment.newInstanse(
        R.string.welcome3Title,
        R.drawable.ic_briefcase,
        R.string.welcome3Text,
        R.drawable.bg5
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCenter.start(
            application, "11f60d15-2cf8-4a18-9465-1924b3e7804f",
            Analytics::class.java, Crashes::class.java
        )
        AppCenter.start(
            application, "11f60d15-2cf8-4a18-9465-1924b3e7804f",
            Analytics::class.java, Crashes::class.java
        )

        if (sharedPreferencesManager.getIsPinSet()!!) openPinActivity()
        else if (sharedPreferencesManager.isFirstOpened()) openSignActivity()

        setContentView(R.layout.activity_welcome)

        welcomeSliderAdapter.list.add(welcomeSliderFragmentOne)
        welcomeSliderAdapter.list.add(welcomeSliderFragmentTwo)
        welcomeSliderAdapter.list.add(welcomeSliderFragmentThree)

        welcomeViewPager.adapter = welcomeSliderAdapter

        welcomeViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {

                when {
                    position == welcomeSliderAdapter.list.size - 1 -> {
                        welcomeScreensSeen = true
                        sharedPreferencesManager.onLaunch(true)

                        welcomeSliderAdapter.getItem(position).btn_next.setOnClickListener()
                        {
                            openSignActivity()
                        }
                    }
                    welcomeScreensSeen -> {
                        welcomeSliderAdapter.getItem(position).btn_next.setOnClickListener()
                        {
                            openSignActivity()
                        }
                    }
                    else -> {
                        welcomeSliderAdapter.getItem(position)
                            .btn_next.setText(R.string.nextLinkText)
                    }
                }

                when (welcomeViewPager.currentItem) {
                    0 -> {
                        welcomeSliderAdapter.getItem(position).indicator1.setTextColor(Color.WHITE)
                        welcomeSliderAdapter.getItem(position).indicator2.setTextColor(Color.GRAY)
                        welcomeSliderAdapter.getItem(position).indicator3.setTextColor(Color.GRAY)
                    }
                    1 -> {
                        welcomeSliderAdapter.getItem(position).indicator1.setTextColor(Color.GRAY)
                        welcomeSliderAdapter.getItem(position).indicator2.setTextColor(Color.WHITE)
                        welcomeSliderAdapter.getItem(position).indicator3.setTextColor(Color.GRAY)
                    }
                    2 -> {
                        welcomeSliderAdapter.getItem(position).indicator1.setTextColor(Color.GRAY)
                        welcomeSliderAdapter.getItem(position).indicator2.setTextColor(Color.GRAY)
                        welcomeSliderAdapter.getItem(position).indicator3.setTextColor(Color.WHITE)
                    }
                }

                //Listener for SKIP button

                if (welcomeScreensSeen) {
                    welcomeSliderAdapter.getItem(position).btn_skip.visibility = View.GONE
                    welcomeSliderAdapter.getItem(position).btn_next.setText(R.string.startLinkText)

                }

            }
        })

    }

    override fun openNextFragment() {
        welcomeViewPager.currentItem++
    }

    override fun openSignActivity() {
        startActivity(Intent(this, SignActivity::class.java))
        finish()
    }

    private fun openPinActivity() {
        startActivity(Intent(this, PinActivity::class.java))
        finish()
    }
}
