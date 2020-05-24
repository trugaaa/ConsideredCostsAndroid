package app.mobile.consideredcosts.main.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import app.mobile.consideredcosts.R
import app.mobile.consideredcosts.adapters.ItemsAdapter
import app.mobile.consideredcosts.data.DataHolder
import app.mobile.consideredcosts.data.SharedPreferencesManager
import app.mobile.consideredcosts.http.RetrofitClient
import app.mobile.consideredcosts.http.models.ItemElement
import app.mobile.consideredcosts.sign.PinActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_items.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemsFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferencesManager

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

        try {
            if (list.isEmpty()) {
                itemsRecyclerView.visibility = View.GONE
                itemsEmptyListLayout.visibility = View.VISIBLE
            } else {
                itemsRecyclerView.visibility = View.VISIBLE
                itemsEmptyListLayout.visibility = View.GONE
                itemsAdapter.listUpdate(list)
            }
        }catch (ex:java.lang.IllegalStateException)
        {
            Log.e("Crash","Trying to update items screen elements, when no items screen present")
        }
    }

    override fun onResume() {
        super.onResume()
        sharedPreferences = SharedPreferencesManager(context!!)

        itemsAddButton.setOnClickListener {
            closeKeyboard(context!!, itemsAddButton)
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
                                if (response.body()!!.data!!.list!!.isNotEmpty()) {
                                    DataHolder.itemsList =
                                        response.body()!!.data!!.list!!.toMutableList()
                                } else {
                                    DataHolder.itemsList.clear()
                                }
                                updateState(DataHolder.itemsList)
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
    }

    private fun savingItem() {
        if (!itemEditText.text.isNullOrBlank()) {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    launch {
                        val response = RetrofitClient.postItems(
                            sharedPreferences.getToken()!!,
                            ItemElement(
                                null,
                                itemEditText.text.toString(),
                                null,
                                null,
                                null,
                                null
                            )
                        )
                        when (response.code()) {
                            200 -> {
                                withContext(Dispatchers.Main) {
                                    try {
                                        itemEditText.text.clear()
                                    } catch (ex: IllegalStateException) {
                                        ex.message.let {
                                            Log.e("Crash caught:", ex.message!!)
                                        }
                                    }
                                    gettingList()
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
    }

    private fun invokeGeneralErrorActivity(errorText: String) {
        val snackBar = Snackbar.make(
            itemsFragmentLayout,
            errorText,
            Snackbar.LENGTH_LONG
        )

        snackBar.view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorError))
        snackBar.setActionTextColor(ContextCompat.getColor(context!!, R.color.colorPrimaryText))
        snackBar.show()
    }

    private fun closeKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun openPinActivity() {
        startActivity(Intent(context, PinActivity::class.java))
        activity!!.finish()
    }
}
