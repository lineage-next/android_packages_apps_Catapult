package org.lineageos.tv.launcher.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.content.Context
import android.service.notification.NotificationListenerService.RankingMap
import android.service.notification.StatusBarNotification
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.lineageos.tv.launcher.R

class NotificationAdapter(
    private val context: Context,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    data class NotificationEntry(val id: Int, val sbn: StatusBarNotification)

    private var notifications: ArrayList<NotificationEntry> = ArrayList()

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvNotificationViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.notification_item, parent, false
        )
        return TvNotificationViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as TvNotificationViewHolder
        val notification = notifications[position].sbn.notification
        holder.title.text = notification.extras.getString(Notification.EXTRA_TITLE)
        holder.details.text = notification.extras.getString(Notification.EXTRA_TEXT)
        holder.icon.setImageDrawable(notification.smallIcon.loadDrawable(context))
        holder.container.setOnClickListener {
            onItemClickListener.onItemClick(
                position,
                notifications[position].sbn
            )
        }
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    override fun getItemId(position: Int): Long {
        // the item id is the notification id
        return notifications[position].id.toLong()
    }

    @SuppressLint("NotifyDataSetChanged") // Using stable id's
    fun setNotifications(newNotifications: Map<String, StatusBarNotification>, rankingMap: RankingMap) {
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
        val container: LinearLayout = itemView.findViewById(R.id.notification_container)
        val title: TextView = itemView.findViewById(R.id.notification_title)
        val details: TextView = itemView.findViewById(R.id.notification_details)
        val icon: ImageView = itemView.findViewById(R.id.icon)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, sbn: StatusBarNotification)
    }
}
