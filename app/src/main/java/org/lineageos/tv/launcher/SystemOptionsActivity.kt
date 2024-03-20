package org.lineageos.tv.launcher

import android.icu.text.SimpleDateFormat
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import java.util.Calendar
import java.util.Locale


class SystemOptionsActivity : ModalActivity(R.layout.activity_system_options) {
    // Views
    private val dateTextView by lazy { findViewById<TextView>(R.id.date) }
    private val sleepButton by lazy { findViewById<MaterialButton>(R.id.sleep_button) }
    private val settingsButton by lazy { findViewById<MaterialButton>(R.id.settings_button) }
    private val powerButton by lazy { findViewById<MaterialButton>(R.id.power_button) }
    private val networkButton by lazy { findViewById<MaterialButton>(R.id.network_button) }
    private val bluetoothButton by lazy { findViewById<MaterialButton>(R.id.bluetooth_button) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Date
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("EEE, d MMMM", Locale.getDefault())
        dateTextView.text = dateFormat.format(currentDate)

        // Wifi & Bluetooth
        setNetworkButton()
        setBluetoothButton()
    }

    private fun setNetworkButton() {
        var networkString = resources.getString(R.string.unknown)
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capabilities == null
            || !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            || !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            networkString = resources.getString(R.string.not_connected)
        }

        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                networkButton.icon = AppCompatResources.getDrawable(this, R.drawable.ic_ethernet)
            }
        }

        val activeNetwork = connectivityManager.activeNetwork
        if (activeNetwork != null) {
            val linkProperties = connectivityManager.getLinkProperties(activeNetwork)
            networkString = linkProperties?.interfaceName ?: resources.getString(R.string.unknown)
        }

        val networkSpan = SpannableString(resources.getString(R.string.network_status, networkString))
        networkSpan.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.white_disabled)),
            networkSpan.indexOf("\n"),
            networkSpan.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        networkButton.text = networkSpan
    }

    private fun setBluetoothButton() {
        val btString = SpannableString(resources.getString(R.string.bluetooth_status, "-"))

        btString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.white_disabled)),
            btString.indexOf("\n"),
            btString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        bluetoothButton.text = btString

    }
}
