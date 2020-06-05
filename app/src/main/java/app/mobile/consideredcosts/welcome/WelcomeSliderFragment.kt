package app.mobile.consideredcosts.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.launch.PagerListener
import kotlinx.android.synthetic.main.fragment_welcome_slider.*
import kotlinx.android.synthetic.main.fragment_welcome_slider.view.*

class WelcomeSliderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome_slider, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        welcomeFragmentLayout.WelcomeText.setText(arguments!!.getInt(RES_TEXT))
        welcomeFragmentLayout.WelcomeImage.setImageResource(arguments!!.getInt(RES_IMAGE))
        welcomeFragmentLayout.WelcomeTitle.setText(arguments!!.getInt(RES_TITLE))
        welcomeFragmentLayout.setBackgroundResource(arguments!!.getInt(RES_BACKGROUND))

        btn_skip.setOnClickListener()
        {
            (activity as? PagerListener)?.openSignActivity()
        }

        btn_next.setOnClickListener()
        {
            (activity as? PagerListener)?.openNextFragment()
        }
    }

    companion object {
        fun newInstanse(
            resTitle: Int,
            resImage: Int,
            resText: Int,
            resBackground: Int
        ): WelcomeSliderFragment {
            return WelcomeSliderFragment().apply {
                arguments = Bundle().apply {
                    putInt(RES_TITLE, resTitle)
                    putInt(RES_IMAGE, resImage)
                    putInt(RES_TEXT, resText)
                    putInt(RES_BACKGROUND, resBackground)
                }
            }
        }

        private const val RES_TITLE = "RES_TITLE"
        private const val RES_IMAGE = "RES_IMAGE"
        private const val RES_TEXT = "RES_TEXT"
        private const val RES_BACKGROUND = "RES_BACKGROUND"
    }

}
