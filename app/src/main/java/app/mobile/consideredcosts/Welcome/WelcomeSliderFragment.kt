package app.mobile.consideredcosts.Welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.mobile.consideredcosts.R
import kotlinx.android.synthetic.main.fragment_welcome_slider.*
import kotlinx.android.synthetic.main.fragment_welcome_slider.view.*

class WelcomeSliderFragment(var resTitle:Int, var resImage:Int, var resText:Int, var resBackground:Int) : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_welcome_slider, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        welcomeFragmentLayout.WelcomeText.setText(this.resText)
        welcomeFragmentLayout.WelcomeImage.setImageResource(this.resImage)
        welcomeFragmentLayout.WelcomeTitle.setText(this.resTitle)
        welcomeFragmentLayout.setBackgroundResource(this.resBackground)
    }

}
