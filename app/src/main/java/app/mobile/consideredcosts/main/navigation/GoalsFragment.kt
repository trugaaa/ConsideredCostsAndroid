package app.mobile.consideredcosts.main.navigation

import android.content.Intent
import android.os.Bundle
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
import app.mobile.consideredcosts.http.models.GoalElement
import app.mobile.consideredcosts.main.navigation.goal.GoalActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_goals.*
import kotlinx.android.synthetic.main.fragment_transactions.*

class GoalsFragment : Fragment() {
    private val sharedPreferences by lazy {
        SharedPreferencesManager(context!!)
    }

    private val adapter by lazy {
        GoalAdapter(mutableListOf()) { position, list ->
            deletingGoal(position,list )
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

        goalsAddButton.setOnClickListener{
            startActivity(Intent(context,GoalActivity::class.java))
        }
    }

    override fun onResume() {
        updateLayout(DataHolder.goalsList)
        super.onResume()
    }

    private fun updateLayout(list: MutableList<GoalElement>) {
        if (list.isEmpty()) {
            goalsRecyclerView.visibility = View.GONE
            goalsEmptyListLayout.visibility = View.VISIBLE
        }
        else{
            goalsRecyclerView.visibility = View.VISIBLE
            goalsEmptyListLayout.visibility = View.GONE
            adapter.updateGoals(list)
        }
    }

    private fun invokeGeneralErrorActivity(errorText: String) {
        val snackBar = Snackbar.make(
            transactionFragmentLayout,
            errorText,
            Snackbar.LENGTH_LONG
        )

        snackBar.view.setBackgroundColor(ContextCompat.getColor(context!!,R.color.colorError))
        snackBar.setActionTextColor(ContextCompat.getColor(context!!,R.color.colorPrimaryText))
        snackBar.show()
    }

    private fun deletingGoal(list: MutableList<GoalElement>, position: Int)
    {
        list.removeAt(position)
        updateLayout(list)
    }
}