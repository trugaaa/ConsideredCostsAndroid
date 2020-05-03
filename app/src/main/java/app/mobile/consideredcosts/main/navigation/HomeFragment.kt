package app.mobile.consideredcosts.main.navigation


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.sign.PinActivity

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun openPinActivity()
    {
        startActivity(Intent(context, PinActivity::class.java))
        activity!!.finish()
    }
}
