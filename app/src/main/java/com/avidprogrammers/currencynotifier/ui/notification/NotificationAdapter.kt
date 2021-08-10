package com.avidprogrammers.currencynotifier.ui.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.avidprogrammers.currencynotifier.R
import kotlinx.android.synthetic.main.forex_fragment.*
import org.w3c.dom.Text

class NotificationAdapter() : ListAdapter<Forex, NotificationAdapter.NotificationViewHolder>(DIFFUTIL){

    inner class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currenyCode=itemView.findViewById<TextView>(R.id.cur_code)
        val targetVal=itemView.findViewById<TextView>(R.id.target_value)
        val currentVal=itemView.findViewById<TextView>(R.id.current_value)
        val createdAt=itemView.findViewById<TextView>(R.id.notification_date)

        fun bind(notification:Forex){
            currenyCode.text=notification.currencyCode
            targetVal.text=notification.targetVal
//            currentVal.text=notification.currencyVal

            val number: Float = notification.currencyVal!!.toFloat()
            val number2digits: Float = String.format("%.2f", number).toFloat()
            currentVal.text = number2digits.toString()

            createdAt.text=notification.createdAt
            itemView.findViewById<TextView>(R.id.notification_st).text=notification.notificationStatus

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.notification_item,parent,false);
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification: Forex? = getItem(holder.adapterPosition)
        if (notification != null) {
            holder.bind(notification)

        }
    }

    companion object{
        val DIFFUTIL: DiffUtil.ItemCallback<Forex> = object : DiffUtil.ItemCallback<Forex>() {
            override fun areContentsTheSame(oldItem: Forex, newItem: Forex): Boolean {
                return oldItem.createdAt==newItem.createdAt
            }

            override fun areItemsTheSame(oldItem: Forex, newItem: Forex): Boolean {
                return oldItem==newItem
            }
        }
    }
}