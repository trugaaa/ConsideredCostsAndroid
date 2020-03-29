package app.mobile.consideredcosts.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import app.mobile.consideredcosts.*
import app.mobile.consideredcosts.main.navigation.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openFragment(HomeFragment())

        mainNavBar.setOnNavigationItemSelectedListener { item: MenuItem ->
            return@setOnNavigationItemSelectedListener when (item.itemId) {
                R.id.navBarHomeMenuItem -> {
                    openFragment(HomeFragment())
                    true
                }
                R.id.navBarTransactionsMenuItem -> {
                    openFragment(TransactionsFragment())
                    true
                }
                R.id.navBarItemsMenuItem -> {
                    openFragment(ItemsFragment())
                    true
                }
                R.id.navBarGoalsMenuItem -> {
                    openFragment(GoalsFragment())
                    true
                }
                R.id.navBarElseMenuItem -> {
                    openFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }
    private fun openFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.mainContainer,fragment).commit()
    }
}
