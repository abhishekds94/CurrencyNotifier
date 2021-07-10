package com.avidprogrammers.currencynotifier.ui.forex

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.avidprogrammers.currencynotifier.R

class SourceForexSpinnerAdapter(val context: Context, var listItemsTxt: Array<String>) :
    BaseAdapter() {

    val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.spinner_items, parent, false)
            vh = ItemRowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }

        vh.sourceForexName.text = listItemsTxt[position]
        return view
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return listItemsTxt.size
    }

    private class ItemRowHolder(row: View?) {
        val sourceForexName: TextView = row?.findViewById(R.id.sourceForexName) as TextView
        val sourceForexImage: ImageView = row?.findViewById(R.id.sourceForexImage) as ImageView
    }

}