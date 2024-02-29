package org.lineageos.tv.launcher

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.leanback.widget.VerticalGridView
import org.lineageos.tv.launcher.adapter.ModifyChannelsAdapter
import org.lineageos.tv.launcher.utils.Suggestions
import org.lineageos.tv.launcher.utils.Suggestions.orderSuggestions

class ModifyChannelsActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_channels)

        val channelsGrid: VerticalGridView = findViewById(R.id.modify_channels_grid)
        val channelOrder = Suggestions.getChannelOrder(this)
        val channels = Suggestions.getPreviewChannels(this).orderSuggestions(channelOrder) { it.id }
        channelsGrid.adapter = ModifyChannelsAdapter(this, channels)

        val layoutParams = window.attributes
        layoutParams.gravity = Gravity.END
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = layoutParams
    }
}