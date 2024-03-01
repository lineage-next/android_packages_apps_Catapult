package org.lineageos.tv.launcher

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.leanback.widget.VerticalGridView
import androidx.tvprovider.media.tv.PreviewChannel
import androidx.tvprovider.media.tv.PreviewProgram
import androidx.tvprovider.media.tv.TvContractCompat
import org.lineageos.tv.launcher.adapter.AppsAdapter
import org.lineageos.tv.launcher.adapter.ChannelAdapter
import org.lineageos.tv.launcher.adapter.FavoritesAdapter
import org.lineageos.tv.launcher.adapter.MainVerticalAdapter
import org.lineageos.tv.launcher.adapter.WatchNextAdapter
import org.lineageos.tv.launcher.model.MainRowItem
import org.lineageos.tv.launcher.utils.AppManager
import org.lineageos.tv.launcher.utils.Suggestions
import org.lineageos.tv.launcher.utils.Suggestions.getAppName
import org.lineageos.tv.launcher.utils.Suggestions.orderSuggestions


class MainActivity : Activity() {
    private lateinit var mFavoritesAdapter: FavoritesAdapter
    private lateinit var mMainVerticalAdapter: MainVerticalAdapter
    private lateinit var mChannels: List<PreviewChannel>

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkCallingOrSelfPermission(TvContractCompat.PERMISSION_READ_TV_LISTINGS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(TvContractCompat.PERMISSION_READ_TV_LISTINGS), 0)
        }

        val mainItems = ArrayList<Pair<Long, MainRowItem>>()
        mFavoritesAdapter = FavoritesAdapter(this)
        mainItems.add(Pair(-1, MainRowItem(getString(R.string.favorites), mFavoritesAdapter)))
        mainItems.add(Pair(-1, MainRowItem(getString(R.string.watch_next), WatchNextAdapter(this))))

        val channelOrder = Suggestions.getChannelOrder(this)
        val hiddenChannels = Suggestions.getHiddenChannels(this)
        mChannels = Suggestions.getPreviewChannels(this).orderSuggestions(channelOrder) { it.id }
        for (channel in mChannels) {
            if (channel.id in hiddenChannels) {
                continue
            }

            val previewPrograms = Suggestions.getSuggestions(this, channel.id).take(5)
            if (previewPrograms.isEmpty()) {
                continue
            }
            mainItems.add(Pair(channel.id,
                MainRowItem(resources.getString(
                    R.string.channel_title, channel.getAppName(this), channel.displayName),
                    ChannelAdapter(this, previewPrograms as ArrayList<PreviewProgram>))))
        }

        mainItems.add(Pair(-1, MainRowItem(getString(R.string.other_apps), AppsAdapter(this))))

        val mainVerticalGridView: VerticalGridView = findViewById(R.id.main_vertical_grid)
        mMainVerticalAdapter = MainVerticalAdapter(this, mainItems)
        mainVerticalGridView.adapter = mMainVerticalAdapter

        AppManager.onFavoriteAddedCallback = ::onFavoriteAdded
        AppManager.onFavoriteRemovedCallback = ::onFavoriteRemoved

        Suggestions.onChannelHiddenCallback = ::onChannelHidden
        Suggestions.onChannelShownCallback = ::onChannelShown
        Suggestions.onChannelOrderChangedCallback = ::onChannelOrderChanged
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_UNINSTALL) {
            if (resultCode == RESULT_OK) {
                mFavoritesAdapter.updateFavoriteApps(AppManager.getFavoriteApps(this))
            }
        }
    }

    companion object {
        const val REQUEST_CODE_UNINSTALL = 1
    }

    private fun onFavoriteAdded(packageName: String) {
        mFavoritesAdapter.addItem(packageName)
    }

    private fun onFavoriteRemoved(packageName: String) {
        mFavoritesAdapter.removeItem(packageName)
    }

    private fun onChannelHidden(channelId: Long) {
        mMainVerticalAdapter.removeItem(channelId)
    }

    private fun onChannelShown(channelId: Long) {
        var channel: PreviewChannel? = null
        for (c in mChannels) {
            if (c.id == channelId) {
                channel = c
            }
        }

        channel ?: return

        val previewPrograms = Suggestions.getSuggestions(this, channel.id).take(5)
        if (previewPrograms.isEmpty()) {
            return
        }

        mMainVerticalAdapter.addItem(Pair(channel.id, MainRowItem(channel.displayName.toString(),
            ChannelAdapter(this, previewPrograms as ArrayList<PreviewProgram>))))
    }

    private fun onChannelOrderChanged(from: Int, to: Int) {
        mMainVerticalAdapter.notifyItemMoved(from, to)
    }
}