package com.avidprogrammers.currencynotifier.ui.notification

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.core.app.TaskStackBuilder
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.avidprogrammers.currencynotifier.R
import com.avidprogrammers.currencynotifier.ui.HomeActivity
import com.avidprogrammers.currencynotifier.ui.forex.firebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_notification.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class NotificationActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()

    private val notifViewModelFactory:ForexNotificationViewModelFactory by instance()
    private lateinit var notificationViewModel:ForexNotificationViewModel
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        notificationViewModel= ViewModelProvider(this,notifViewModelFactory).get(ForexNotificationViewModel::class.java)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        init()

        firebaseAnalytics = Firebase.analytics

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "notification history")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val upIntent = Intent(this, HomeActivity::class.java)
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder
                        .from(this)
                        .addNextIntent(Intent(this, HomeActivity::class.java))
                        .addNextIntent(upIntent).startActivities()
                    finish()
                } else {
                    NavUtils.navigateUpTo(this, upIntent)
                }
                return true
            }
        }
        return true
    }

    private fun init(){
        notificaiton_rv.layoutManager=LinearLayoutManager(this)
        notificaiton_rv.setHasFixedSize(true)
        notificationAdapter= NotificationAdapter()
        notificaiton_rv.adapter=notificationAdapter
        notificaiton_rv.addItemDecoration(object:RecyclerView.ItemDecoration(){
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.set(0,0,0,8)
            }
        })

        notificationViewModel.notifications.observe(this){
           it?.let {
               notificationAdapter.submitList(it)
           }
        }

        val swipeCallback=object : SwipeCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                notificationViewModel.deleteForex(viewHolder.adapterPosition)
            }

        }

        val itemTouchHelper=ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(notificaiton_rv)
    }
}