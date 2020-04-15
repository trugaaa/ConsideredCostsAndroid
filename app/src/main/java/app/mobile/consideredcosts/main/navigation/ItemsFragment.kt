package app.mobile.consideredcosts.main.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.adapters.ItemsAdapter
import app.mobile.consideredcosts.data.DataHolder
import app.mobile.consideredcosts.data.SharedPreferencesManager
import app.mobile.consideredcosts.http.RetrofitClient
import app.mobile.consideredcosts.http.models.ItemElement
import kotlinx.android.synthetic.main.fragment_items_.*
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
        return inflater.inflate(R.layout.fragment_items_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemsRecyclerView.layoutManager = LinearLayoutManager(context!!)
        itemsRecyclerView.adapter = itemsAdapter
        updateState(DataHolder.itemListMock)
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
                                    DataHolder.itemListMock =
                                        response.body()!!.data!!.list!!
                                } else {
                                    DataHolder.itemListMock.clear()
                                }
                                updateState(DataHolder.itemListMock)
                            }
                        }
                        400 -> {
                            //todo Сделать обработку
                        }
                        else -> {
                            //todo Сделать обработку
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
                            400 -> {
                                //todo Сделать обработку
                            }
                            else -> {
                                //todo Сделать обработку
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
                        400 -> {
                            //todo Сделать обработку
                        }
                        else -> {
                            //todo Сделать обработку
                        }
                    }

                }
            }
        }
    }
}
