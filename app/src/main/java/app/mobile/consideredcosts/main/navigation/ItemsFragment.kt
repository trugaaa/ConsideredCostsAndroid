package app.mobile.consideredcosts.main.navigation


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.mobile.consideredcosts.R

/**
 * A simple [Fragment] subclass.
 */
class ItemsFragment : Fragment() {
    private val itemsAdapter by lazy { ItemsAdapter(mutableListOf()) { position, list ->
        list.removeAt(position)
        updateState(list)
    } }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_items_, container, false)
    }


}
