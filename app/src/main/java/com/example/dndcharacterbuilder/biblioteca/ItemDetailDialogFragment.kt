package com.example.dndcharacterbuilder.biblioteca

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import android.widget.TextView
import com.example.dndcharacterbuilder2.R
import com.example.dndcharacterbuilder.api.model.Item

class ItemDetailDialogFragment : DialogFragment() {

    companion object {
        private const val ITEM_KEY = "item_key"

        fun newInstance(item: Item): ItemDetailDialogFragment {
            val fragment = ItemDetailDialogFragment()
            val args = Bundle()
            args.putSerializable(ITEM_KEY, item)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_item_detail, container, false)

        val item = arguments?.getSerializable(ITEM_KEY) as? Item
        val itemName = view.findViewById<TextView>(R.id.item_name)
        itemName.text = item?.name // Actualiza el nombre del item en el modal

        return view
    }
}


