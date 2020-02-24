package app.mobile.consideredcosts

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import app.mobile.consideredcosts.SignInSignUpActivity.SignInSignUpActivity
import app.mobile.consideredcosts.Welcome.WelcomeSliderAdapter
import app.mobile.consideredcosts.Welcome.WelcomeSliderFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_welcome_slider.*

class MainActivity : AppCompatActivity() {

    lateinit var welcomeSliderAdapter : WelcomeSliderAdapter
    lateinit var activity: Activity

    var welcomeScreensSeen:Boolean = false

    private val welcomeSliderFragmentOne = WelcomeSliderFragment(R.string.welcome1Title, R.drawable.ic_wallet, R.string.welcome1Text, R.drawable.bg1 )
    private val welcomeSliderFragmentTwo = WelcomeSliderFragment(R.string.welcome2Title, R.drawable.ic_save, R.string.welcome2Text, R.drawable.bg3 )
    private val welcomeSliderFragmentThree = WelcomeSliderFragment(R.string.welcome3Title, R.drawable.ic_briefcase, R.string.welcome3Text, R.drawable.bg5 )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Strange
        activity = this

        welcomeSliderAdapter = WelcomeSliderAdapter(supportFragmentManager)
        welcomeSliderAdapter.list.add(welcomeSliderFragmentOne)
        welcomeSliderAdapter.list.add(welcomeSliderFragmentTwo)
        welcomeSliderAdapter.list.add(welcomeSliderFragmentThree)

        welcomeViewPager.adapter = welcomeSliderAdapter
        welcomeViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener
        {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {

                if(position==welcomeSliderAdapter.list.size-1)
                {   welcomeScreensSeen = true

                    welcomeSliderAdapter.getItem(position).btn_next.setOnClickListener()
                    {
                        startActivity(Intent(activity,SignInSignUpActivity::class.java))
                        finish()
                    }
                }
                else
                {

                    if(welcomeScreensSeen) {
                        welcomeSliderAdapter.getItem(position).btn_next.setOnClickListener()
                        {
                            startActivity(Intent(activity,SignInSignUpActivity::class.java))
                            finish()
                        }
                    }else
                    {
                        welcomeSliderAdapter.getItem(position).btn_next.setText(R.string.nextLinkText)
                        welcomeSliderAdapter.getItem(position).btn_next.setOnClickListener()
                        {
                            welcomeViewPager.currentItem++
                        }
                    }


                    welcomeSliderAdapter.getItem(position).btn_skip.setOnClickListener()
                    {
                        startActivity(Intent(activity,SignInSignUpActivity::class.java))
                        finish()
                    }

                }

                //All fragments
                when(welcomeViewPager.currentItem){
                    0->{
                        welcomeSliderAdapter.getItem(position).indicator1.setTextColor(Color.WHITE)
                        welcomeSliderAdapter.getItem(position).indicator2.setTextColor(Color.GRAY)
                        welcomeSliderAdapter.getItem(position).indicator3.setTextColor(Color.GRAY)
                    }
                    1->{
                        welcomeSliderAdapter.getItem(position).indicator1.setTextColor(Color.GRAY)
                        welcomeSliderAdapter.getItem(position).indicator2.setTextColor(Color.WHITE)
                        welcomeSliderAdapter.getItem(position).indicator3.setTextColor(Color.GRAY)
                    }
                    2->{
                        welcomeSliderAdapter.getItem(position).indicator1.setTextColor(Color.GRAY)
                        welcomeSliderAdapter.getItem(position).indicator2.setTextColor(Color.GRAY)
                        welcomeSliderAdapter.getItem(position).indicator3.setTextColor(Color.WHITE)
                    }
                }

                //Listener for SKIP button
                welcomeSliderAdapter.getItem(position).btn_skip.setOnClickListener()
                {
                    startActivity(Intent(activity,SignInSignUpActivity::class.java))
                    finish()
                }

                if(welcomeScreensSeen)
                {
                    welcomeSliderAdapter.getItem(position).btn_skip.visibility = View.GONE
                    welcomeSliderAdapter.getItem(position).btn_next.setText(R.string.startLinkText)

                }

            }


        })

    }
}
