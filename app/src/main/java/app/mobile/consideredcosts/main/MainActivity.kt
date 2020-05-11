package app.mobile.consideredcosts.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.mobile.consideredcosts.*
import app.mobile.consideredcosts.data.DataHolder
import app.mobile.consideredcosts.data.SharedPreferencesManager
import app.mobile.consideredcosts.http.RetrofitClient
import app.mobile.consideredcosts.main.navigation.*
import app.mobile.consideredcosts.sign.PinActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val sharedPreferences by lazy {
        SharedPreferencesManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        refreshLists()

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

    override fun onResume() {
        if (DataHolder.isSentToItemsAdd) {
            mainNavBar.selectedItemId = R.id.navBarItemsMenuItem
            openFragment(ItemsFragment())
            DataHolder.isSentToItemsAdd = false
        }

        refreshLists()
        super.onResume()
    }

    private fun refreshLists() {
        refreshCurrencyList()
        refreshItemsList()
    }

    private fun refreshCurrencyList() {
        try {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    launch {
                        val response = RetrofitClient.getCurrencyList()
                        when (response.code()) {
                            200 -> {
                                DataHolder.currencyList = response.body()!!.data!!.list
                            }
                            504, 503, 502, 501, 500 -> {
                                invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                            }
                            401 -> {
                                openPinActivity()
                            }
                            else -> {
                                invokeGeneralErrorActivity(
                                    response.body()?.firstMessage
                                        ?: resources.getString(R.string.unknownError))
                            }
                        }
                    }
                }
            }
        } catch (e: KotlinNullPointerException) {
            e.message.let {
                Log.e("Crash caught:", e.message!!)
            }
        }
    }

    private fun refreshItemsList() {
        try {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    launch {
                        val response = RetrofitClient.getItems(sharedPreferences.getToken()!!)
                        when (response.code()) {
                            200 -> {
                                DataHolder.itemsList = response.body()!!.data!!.list!!
                            }
                            504, 503, 502, 501, 500 -> {
                                invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                            }
                            401 -> {
                                openPinActivity()
                            }
                            else -> {

                                invokeGeneralErrorActivity(
                                    response.body()?.firstMessage
                                        ?: resources.getString(R.string.unknownError))
                            }
                        }

                    }
                }
            }
        } catch (e: KotlinNullPointerException) {
            e.message.let {
                Log.e("Crash caught:", e.message!!)
            }
        }
    }

    fun invokeGeneralErrorActivity(errorText: String) {
        val snackBar = Snackbar.make(
            mainActivityLayout,
            errorText,
            Snackbar.LENGTH_LONG
        )

        snackBar.view.setBackgroundColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorError
            )
        )
        snackBar.setActionTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.colorPrimaryText
            )
        )
        snackBar.show()
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.mainContainer, fragment).commit()
    }

    private fun openPinActivity() {
        startActivity(Intent(this, PinActivity::class.java))
        finish()
    }
}
