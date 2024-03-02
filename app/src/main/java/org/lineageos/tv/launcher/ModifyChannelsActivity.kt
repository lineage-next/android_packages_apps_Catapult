package org.lineageos.tv.launcher

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.leanback.widget.VerticalGridView
import org.lineageos.tv.launcher.adapter.ModifyChannelsAdapter
import org.lineageos.tv.launcher.model.Channel
import org.lineageos.tv.launcher.utils.Suggestions
import org.lineageos.tv.launcher.utils.Suggestions.orderSuggestions

class ModifyChannelsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_channels)

        val channelsGrid: VerticalGridView = findViewById(R.id.modify_channels_grid)
        val channelOrder = Suggestions.getChannelOrder(this)
        val previewChannels = Suggestions.getPreviewChannels(this)
        val channels: ArrayList<Channel> = previewChannels.map {
            Channel(
                it.id,
                Suggestions.getChannelTitle(this, it)
            )
        } as ArrayList<Channel>
        channels.add(Channel(Channel.FAVORITE_APPS_ID, getString(R.string.favorites)))
        channels.add(Channel(Channel.ALL_APPS_ID, getString(R.string.other_apps)))
        channels.add(Channel(Channel.WATCH_NEXT_ID, getString(R.string.watch_next)))

        channelsGrid.adapter =
            ModifyChannelsAdapter(this, channels.orderSuggestions(channelOrder) { it.id })

        val layoutParams = window.attributes
        layoutParams.gravity = Gravity.END
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = layoutParams
    }
}