/*
 * SPDX-FileCopyrightText: 2024 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.tv.launcher.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.service.notification.NotificationListenerService.RankingMap
import android.service.notification.StatusBarNotification
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.lineageos.tv.launcher.R
import org.lineageos.tv.launcher.view.NotificationItem

class NotificationAdapter(
    private val context: Context,
    private val onItemActionListener: OnItemActionListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    data class NotificationEntry(val id: Int, val sbn: StatusBarNotification)

    private var notifications: ArrayList<NotificationEntry> = ArrayList()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvNotificationViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.notification_list_item, parent, false
        )
        return TvNotificationViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as TvNotificationViewHolder
        val sbn = notifications[position].sbn
        val notification = sbn.notification
        holder.notificationView.statusBarNotification = sbn
        holder.notificationView.title.text = notification.extras.getString(Notification.EXTRA_TITLE)
        holder.notificationView.details.text =
            notification.extras.getString(Notification.EXTRA_TEXT)
        holder.notificationView.icon.setImageDrawable(notification.smallIcon.loadDrawable(context))
        holder.notificationView.setOnClickListener { view ->
            onItemActionListener.onItemClick(
                view,
                position
            )
        }
        holder.notificationView.setOnKeyListener { view, keyCode, event ->
            return@setOnKeyListener onItemActionListener.onKey(
                view,
                keyCode,
                event
            )
        }
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    override fun getItemId(position: Int): Long {
        // The item id is the notification id
        return notifications[position].id.toLong()
    }

    @SuppressLint("NotifyDataSetChanged") // Using stable id's
    fun setNotifications(
        newNotifications: Map<String, StatusBarNotification>,
        rankingMap: RankingMap
    ) {
        notifications.clear()
        for (key in rankingMap.orderedKeys) {
            val sbn: StatusBarNotification? = newNotifications[key]
            if (sbn != null) {
                notifications.add(NotificationEntry(sbn.id, sbn))
            }
        }

        notifyDataSetChanged()
    }

    class TvNotificationViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val notificationView: NotificationItem = itemView.findViewById(R.id.notification_view)
    }

    interface OnItemActionListener {
        fun onItemClick(view: View, position: Int)
        fun onKey(view: View, keyCode: Int, event: KeyEvent): Boolean
    }
}
