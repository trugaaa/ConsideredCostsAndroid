package app.mobile.consideredcosts.main.navigation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.data.SharedPreferencesManager
import app.mobile.consideredcosts.main.navigation.profile.UserActivity
import app.mobile.consideredcosts.sign.PinActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import java.lang.Exception

class ProfileFragment : Fragment() {
    private val sharedPreferences by lazy {
        SharedPreferencesManager(context!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUserInfo()

        edit_info_profile.setOnClickListener {
            userActivityInvoke()
        }

        logout_profile.setOnClickListener {
            logout()
        }
    }

    private fun updateUserInfo() {
        try {
            user_firstName_profile.text = sharedPreferences.getUsername()
            user_secondName_profile.text = sharedPreferences.getUsername()
        } catch (ex: Exception) {
            ex.message.let {
                Log.e("Crash", ex.message!!)
            }
        }
    }

    private fun userActivityInvoke() {
        try {
            startActivity(Intent(context, UserActivity::class.java))
        } catch (ex: Exception) {
            ex.message.let {
                Log.e("Crash", ex.message!!)
            }
        }
    }

    private fun logout() {
        try {
            startActivity(Intent(context, PinActivity::class.java))
            activity!!.finish()
        } catch (ex: Exception) {
            ex.message.let {
                Log.e("Crash", ex.message!!)
            }
        }
    }
}
