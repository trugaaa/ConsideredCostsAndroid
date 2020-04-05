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
import app.mobile.consideredcosts.http.models.ItemElement
import kotlinx.android.synthetic.main.fragment_items_.*

class ItemsFragment : Fragment() {
    private val itemsAdapter by lazy { ItemsAdapter(mutableListOf()) { position, list ->
        list.removeAt(position)
        updateState(list)
    } }
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

    private fun updateState(list: MutableList<ItemElement>)
    {
        if(list.isEmpty())
        {
            itemsRecyclerView.visibility = View.GONE
            itemsEmptyListLayout.visibility = View.VISIBLE
        }
        else {
            itemsRecyclerView.visibility = View.VISIBLE
            itemsEmptyListLayout.visibility = View.GONE
            itemsAdapter.listUpdate(list)
        }
    }

    override fun onResume() {
        super.onResume()
        updateState(DataHolder.itemListMock)
    }
}
