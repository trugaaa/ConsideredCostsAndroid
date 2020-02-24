package app.mobile.consideredcosts.welcome

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class WelcomeSliderAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val list: MutableList<Fragment> = ArrayList()

    override fun getItem(position: Int): Fragment = list[position]

    override fun getCount(): Int = list.size

}