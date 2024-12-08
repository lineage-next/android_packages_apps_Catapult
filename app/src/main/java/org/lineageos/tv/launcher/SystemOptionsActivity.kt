/*
 * SPDX-FileCopyrightText: 2024 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.tv.launcher

import android.app.ActivityOptions
import android.app.PendingIntent
import android.bluetooth.BluetoothManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.icu.text.SimpleDateFormat
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.service.notification.StatusBarNotification
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.leanback.widget.VerticalGridView
import com.google.android.material.button.MaterialButton
import org.lineageos.tv.launcher.notification.NotificationAdapter
import org.lineageos.tv.launcher.notification.NotificationUtils
import org.lineageos.tv.launcher.notification.TvNotificationListener
import java.util.Calendar
import java.util.Locale


class SystemOptionsActivity : ModalActivity(R.layout.activity_system_options),
    NotificationAdapter.OnItemClickListener {
    // Views
    private val dateTextView by lazy { findViewById<TextView>(R.id.date) }
    private val sleepButton by lazy { findViewById<MaterialButton>(R.id.sleep_button) }
    private val settingsButton by lazy { findViewById<MaterialButton>(R.id.settings_button) }
    private val powerButton by lazy { findViewById<MaterialButton>(R.id.power_button) }
    private val networkButton by lazy { findViewById<MaterialButton>(R.id.network_button) }
    private val bluetoothButton by lazy { findViewById<MaterialButton>(R.id.bluetooth_button) }
    private val notificationList by lazy { findViewById<VerticalGridView>(R.id.notification_list) }
    private val noNotifications by lazy { findViewById<TextView>(R.id.no_notifications) }
    private val noNotificationAccess by lazy { findViewById<LinearLayout>(R.id.no_notification_access) }
    private val allowNotificationAccess by lazy { findViewById<MaterialButton>(R.id.allow_notification_access) }

    private val colorStateList by lazy {
        ContextCompat.getColorStateList(
            this,
            R.color.system_options_button_content_secondary_tint
        )
    }

    private var notificationListener: TvNotificationListener? = null
    private val notificationAdapter: NotificationAdapter by lazy { NotificationAdapter(this, this) }
    private var notificationUpdateListener: TvNotificationListener.NotificationUpdateListener? =
        null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Animate
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, R.anim.slide_in_right, R.anim.slide_out_right)
            overrideActivityTransition(OVERRIDE_TRANSITION_CLOSE, R.anim.slide_in_right, R.anim.slide_out_right)
        }

        // Date
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("EEE, d MMMM", Locale.getDefault())
        dateTextView.text = dateFormat.format(currentDate)

        // Wifi & Bluetooth
        setNetworkButton()
        setBluetoothButton()

        settingsButton.setOnClickListener {
            startActivity(SETTINGS)
        }

        allowNotificationAccess.setOnClickListener {
            startActivity(NOTIFICATION_SETTINGS)
        }

        notificationList.adapter = notificationAdapter
    }

    override fun onStart() {
        super.onStart()
        if (!NotificationUtils.notificationPermissionGranted(this)) {
            noNotificationAccess.visibility = View.VISIBLE
            noNotifications.visibility = View.GONE
            notificationList.visibility = View.GONE
            return
        }

        val intent = Intent(this, TvNotificationListener::class.java)
        intent.setAction(TvNotificationListener.ACTION_LOCAL_BINDING)
        bindService(intent, notificationListenerConnectionListener, Context.BIND_AUTO_CREATE)
    }

    override fun onResume() {
        super.onResume()
        if (NotificationUtils.notificationPermissionGranted(this)) {
            noNotificationAccess.visibility = View.GONE
        } else {
            noNotificationAccess.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationListener?.removeNotificationUpdateListener(notificationUpdateListener)
    }

    private fun setNetworkButton() {
        var networkString = resources.getString(R.string.unknown)
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capabilities == null
            || !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            || !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        ) {
            networkString = resources.getString(R.string.not_connected)
        } else {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                networkButton.icon = AppCompatResources.getDrawable(this, R.drawable.ic_ethernet)
            }

            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                networkString = resources.getString(R.string.enabled)
            }
        }

        val networkSpan =
            SpannableString(resources.getString(R.string.network_status, networkString))
        networkButton.text = updateButton(networkSpan, false)

        networkButton.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            networkButton.text = updateButton(networkSpan, hasFocus)
        }

        networkButton.setOnLongClickListener {
            startActivity(WIFI_SETTINGS)
            true
        }
    }

    private fun setBluetoothButton() {
        var btString = resources.getString(R.string.disabled)
        val bluetoothAdapter =
            (this.getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled) {
            btString = resources.getString(R.string.enabled)
        }

        val btSpan = SpannableString(resources.getString(R.string.bluetooth_status, btString))
        bluetoothButton.text = updateButton(btSpan, false)

        bluetoothButton.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            bluetoothButton.text = updateButton(btSpan, hasFocus)
        }

        bluetoothButton.setOnLongClickListener {
            startActivity(BLUETOOTH_SETTINGS)
            true
        }
    }

    private fun updateButton(content: SpannableString, hasFocus: Boolean): SpannableString {
        val color = colorStateList?.getColorForState(
            if (hasFocus) intArrayOf(android.R.attr.state_focused) else intArrayOf(),
            colorStateList!!.defaultColor
        ) ?: colorStateList?.defaultColor ?: ContextCompat.getColor(this, R.color.white_disabled)

        content.setSpan(
            ForegroundColorSpan(color),
            content.indexOf("\n"),
            content.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return content
    }

    private fun loadNotifications() {
        val notifications = notificationListener?.getNotifications()
        val ranking = notificationListener?.currentRanking
        if (!notifications.isNullOrEmpty() && ranking != null) {
            notificationAdapter.setNotifications(notifications, ranking)
            noNotifications.visibility = View.GONE
            notificationList.visibility = View.VISIBLE
        } else {
            noNotifications.visibility = View.VISIBLE
            notificationList.visibility = View.GONE
        }
    }

    private val notificationListenerConnectionListener: ServiceConnection =
        object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName, binder: IBinder) {
                notificationListener = (binder as TvNotificationListener.LocalBinder).getService()
                loadNotifications()

                notificationUpdateListener =
                    object : TvNotificationListener.NotificationUpdateListener {
                        override fun onNotificationsChanged() {
                            loadNotifications()
                        }
                    }
                notificationListener?.addNotificationUpdateListener(notificationUpdateListener)
            }

            override fun onServiceDisconnected(className: ComponentName) {
                notificationListener = null
            }
        }

    override fun onItemClick(position: Int, sbn: StatusBarNotification) {
        val notification = sbn.notification
        try {
            if (notification.contentIntent != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    val activityOptions = ActivityOptions.makeBasic()
                    activityOptions.setPendingIntentBackgroundActivityStartMode(
                        ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOWED
                    )
                    notification.contentIntent?.send(activityOptions.toBundle())
                } else {
                    notification.contentIntent?.send()
                }
            }

            if (NotificationUtils.shouldAutoCancel(notification)) {
                notificationListener?.cancelNotification(sbn.key)
            }
        } catch (e: PendingIntent.CanceledException) {
            Log.d(
                "SystemOptionsActivity",
                "Pending intent canceled for : ${notification.contentIntent}"
            )
        }
    }

    companion object {
        val SETTINGS: Intent = Intent(Settings.ACTION_SETTINGS)
        val WIFI_SETTINGS: Intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        val BLUETOOTH_SETTINGS: Intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
        val NOTIFICATION_SETTINGS: Intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
    }
}
