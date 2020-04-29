package app.mobile.consideredcosts.main.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.adapters.ItemsAdapter
import app.mobile.consideredcosts.data.DataHolder
import app.mobile.consideredcosts.data.SharedPreferencesManager
import app.mobile.consideredcosts.http.RetrofitClient
import app.mobile.consideredcosts.http.models.ItemElement
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_items.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemsFragment : Fragment() {
    private val sharedPreferences by lazy {
        SharedPreferencesManager(context!!)
    }

    private val itemsAdapter by lazy {
        ItemsAdapter(mutableListOf()) { position, list ->
            deletingItem(list, position)
            gettingList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_items, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemsRecyclerView.layoutManager = LinearLayoutManager(context!!)
        itemsRecyclerView.adapter = itemsAdapter
        updateState(DataHolder.itemsList)
    }

    private fun updateState(list: MutableList<ItemElement>) {
        if (list.isEmpty()) {
            itemsRecyclerView.visibility = View.GONE
            itemsEmptyListLayout.visibility = View.VISIBLE
        } else {
            itemsRecyclerView.visibility = View.VISIBLE
            itemsEmptyListLayout.visibility = View.GONE
            itemsAdapter.listUpdate(list)
        }
    }

    override fun onResume() {
        super.onResume()
        itemsAddButton.setOnClickListener {
            savingItem()
        }
        gettingList()
    }

    private fun gettingList() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                launch {
                    val response =
                        RetrofitClient.getItems(sharedPreferences.getToken()!!)
                    when (response.code()) {
                        200 -> {
                            withContext(Dispatchers.Main) {
                                if (response.body()!!.data != null) {
                                    DataHolder.itemsList =
                                        response.body()!!.data!!.list!!
                                } else {
                                    DataHolder.itemsList.clear()
                                }
                                updateState(DataHolder.itemsList)
                            }
                        }
                        504,503,502,501,500->
                        {
                            invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                        }
                        else -> {
                            invokeGeneralErrorActivity(response.body()!!.firstMessage!!)
                        }
                    }

                }
            }
        }
    }

    private fun savingItem() {
        if (!itemEditText.text.isNullOrBlank()) {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    launch {
                        val response = RetrofitClient.postItems(
                            sharedPreferences.getToken()!!,
                            ItemElement(null, itemEditText.text.toString(), 0.0, 0, 0.0, null)
                        )
                        when (response.code()) {
                            200 -> {
                                withContext(Dispatchers.Main) {
                                    itemEditText.text.clear()
                                    gettingList()
                                }
                            }
                            504,503,502,501,500->
                            {
                                invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                            }
                            else -> {
                                invokeGeneralErrorActivity(response.body()!!.firstMessage!!)
                            }
                        }
                    }
                }
            }
        } else {
            itemEditText.error = resources.getString(R.string.errorFieldIsRequired)
        }
    }

    private fun deletingItem(list: MutableList<ItemElement>, position: Int) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                launch {
                    val response =
                        RetrofitClient.deleteItem(
                            sharedPreferences.getToken()!!,
                            list[position].Id!!
                        )
                    when (response.code()) {
                        200 -> {
                            withContext(Dispatchers.Main) {
                                gettingList()
                            }
                        }
                        504,503,502,501,500->
                        {
                            invokeGeneralErrorActivity(resources.getString(R.string.serverNotAvailable))
                        }
                        else -> {
                            invokeGeneralErrorActivity(response.body()!!.firstMessage!!)
                        }
                    }

                }
            }
        }
    }

    private fun invokeGeneralErrorActivity(errorText: String) {
        val snackBar = Snackbar.make(
            itemsFragmentLayout,
            errorText,
            Snackbar.LENGTH_LONG
        )

        snackBar.view.setBackgroundColor(ContextCompat.getColor(context!!,R.color.colorError))
        snackBar.setActionTextColor(ContextCompat.getColor(context!!,R.color.colorPrimaryText))
        snackBar.show()
    }
}
