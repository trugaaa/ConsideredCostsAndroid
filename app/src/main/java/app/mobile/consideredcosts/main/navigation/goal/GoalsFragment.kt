package app.mobile.consideredcosts.main.navigation.goal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.adapters.GoalAdapter
import app.mobile.consideredcosts.data.DataHolder
import app.mobile.consideredcosts.data.SharedPreferencesManager
import app.mobile.consideredcosts.http.RetrofitClient
import app.mobile.consideredcosts.http.models.GoalElement
import app.mobile.consideredcosts.sign.PinActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_goals.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GoalsFragment : Fragment() {
    private val sharedPreferences by lazy {
        SharedPreferencesManager(context!!)
    }

    private val adapter by lazy {
        GoalAdapter(mutableListOf()) { position, list ->
            deletingGoal(position, list)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_goals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goalsRecyclerView.layoutManager = LinearLayoutManager(context!!)
        goalsRecyclerView.adapter = adapter
        updateLayout(DataHolder.goalsList)

        goalsAddButton.setOnClickListener {
            startActivity(Intent(context, GoalActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        gettingList()
    }

    private fun updateLayout(list: MutableList<GoalElement>) {
        try {
            if (list.isEmpty()) {
                goalsRecyclerView.visibility = View.GONE
                goalsEmptyListLayout.visibility = View.VISIBLE
            } else {
                goalsRecyclerView.visibility = View.VISIBLE
                goalsEmptyListLayout.visibility = View.GONE
                adapter.updateGoals(list)
            }
        } catch (ex: java.lang.IllegalStateException) {
            Log.e("Crash", "Trying to update goals screen elements, when no items screen present")
        }
    }

    private fun invokeGeneralErrorActivity(errorText: String) {
        val snackBar = Snackbar.make(
            goalFragmentLayout,
            errorText,
            Snackbar.LENGTH_LONG
        )

        snackBar.view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorError))
        snackBar.setActionTextColor(ContextCompat.getColor(context!!, R.color.colorPrimaryText))
        snackBar.show()
    }

    private fun gettingList() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                launch {
                    val response =
                        RetrofitClient.getGoals(sharedPreferences.getToken()!!)
                    when (response.code()) {
                        200 -> {
                            withContext(Dispatchers.Main) {
                                if (response.body()!!.data != null) {
                                    DataHolder.goalsList =
                                        response.body()!!.data!!.list!!
                                } else {
                                    DataHolder.goalsList.clear()
                                }
                            }
                        }
                        401 -> {
                            openPinActivity()
                        }
                        504, 503, 502, 501, 500 -> {
                            invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                        }
                        else -> {
                            invokeGeneralErrorActivity(
                                response.body()?.firstMessage()
                                    ?: resources.getString(R.string.unknownError)
                            )
                        }
                    }
                }
            }
        }
        updateLayout(DataHolder.goalsList)
    }


    private fun deletingGoal(list: MutableList<GoalElement>, position: Int) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                launch {
                    val response =
                        RetrofitClient.deleteGoal(
                            sharedPreferences.getToken()!!,
                            list[position].Id!!
                        )
                    when (response.code()) {
                        200 -> {
                            withContext(Dispatchers.Main) {
                                gettingList()
                            }
                        }
                        401 -> {
                            openPinActivity()
                        }
                        504, 503, 502, 501, 500 -> {
                            invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                        }
                        401 -> {

                        }
                        else -> {
                            invokeGeneralErrorActivity(
                                response.body()?.firstMessage()
                                    ?: resources.getString(R.string.unknownError)
                            )
                        }
                    }

                }
            }
        }
        updateLayout(DataHolder.goalsList)
    }

    private fun openPinActivity() {
        startActivity(Intent(context, PinActivity::class.java))
        activity!!.finish()
    }
}