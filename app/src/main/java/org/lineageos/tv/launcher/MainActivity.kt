package org.lineageos.tv.launcher

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.leanback.widget.VerticalGridView
import androidx.tvprovider.media.tv.BasePreviewProgram
import androidx.tvprovider.media.tv.PreviewChannel
import androidx.tvprovider.media.tv.TvContractCompat
import org.lineageos.tv.launcher.adapter.AppsAdapter
import org.lineageos.tv.launcher.adapter.FavoritesAdapter
import org.lineageos.tv.launcher.adapter.MainVerticalAdapter
import org.lineageos.tv.launcher.adapter.WatchNextAdapter
import org.lineageos.tv.launcher.model.Channel
import org.lineageos.tv.launcher.model.MainRowItem
import org.lineageos.tv.launcher.utils.AppManager
import org.lineageos.tv.launcher.utils.Suggestions
import org.lineageos.tv.launcher.utils.Suggestions.orderSuggestions


class MainActivity : Activity() {
    private lateinit var mFavoritesAdapter: FavoritesAdapter
    private lateinit var mMainVerticalAdapter: MainVerticalAdapter
    private lateinit var mChannels: List<PreviewChannel>
    private lateinit var mMainVerticalGridView: VerticalGridView

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkCallingOrSelfPermission(TvContractCompat.PERMISSION_READ_TV_LISTINGS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(TvContractCompat.PERMISSION_READ_TV_LISTINGS), 0)
        }

        val mainItems = ArrayList<Pair<Long, MainRowItem>>()
        mFavoritesAdapter = FavoritesAdapter(this)

        val hiddenChannels = Suggestions.getHiddenChannels(this)

        // Add favorites-row. Can't be hidden
        mainItems.add(
            Pair(
                Channel.FAVORITE_APPS_ID,
                MainRowItem(getString(R.string.favorites), mFavoritesAdapter)
            )
        )

        // Add watch next -row
        if (Channel.WATCH_NEXT_ID !in hiddenChannels) {
            mainItems.add(
                Pair(
                    Channel.WATCH_NEXT_ID,
                    MainRowItem(
                        getString(R.string.watch_next),
                        WatchNextAdapter(
                            this,
                            Suggestions.getWatchNextPrograms(this)
                                .filterIsInstance<BasePreviewProgram>() as ArrayList<BasePreviewProgram>
                        )
                    )
                )
            )
        }

        // Add preview channels from apps
        mChannels = Suggestions.getPreviewChannels(this)
        for (channel in mChannels) {
            if (channel.id in hiddenChannels) {
                continue
            }

            val previewPrograms = Suggestions.getSuggestions(this, channel.id)
                .take(5).filterIsInstance<BasePreviewProgram>() as ArrayList<BasePreviewProgram>
            if (previewPrograms.isEmpty()) {
                continue
            }
            mainItems.add(
                Pair(
                    channel.id, MainRowItem(
                        Suggestions.getChannelTitle(this, channel),
                        WatchNextAdapter(this, previewPrograms)
                    )
                )
            )
        }

        // Add All apps -row
        if (Channel.ALL_APPS_ID !in hiddenChannels) {
            mainItems.add(
                Pair(
                    Channel.ALL_APPS_ID,
                    MainRowItem(getString(R.string.other_apps), AppsAdapter(this))
                )
            )
        }

        mMainVerticalGridView = findViewById(R.id.main_vertical_grid)
        mMainVerticalAdapter = MainVerticalAdapter(this,
            mainItems.orderSuggestions(Suggestions.getChannelOrder(this)) { it.first } as ArrayList)

        mMainVerticalGridView.adapter = mMainVerticalAdapter

        AppManager.onFavoriteAddedCallback = ::onFavoriteAdded
        AppManager.onFavoriteRemovedCallback = ::onFavoriteRemoved

        Suggestions.onChannelHiddenCallback = ::onChannelHidden
        Suggestions.onChannelShownCallback = ::onChannelShown
        Suggestions.onChannelOrderChangedCallback = ::onChannelOrderChanged
        Suggestions.onChannelSelectedCallback = ::onChannelSelected
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
        if (channelId == Channel.WATCH_NEXT_ID) {
            mMainVerticalAdapter.addItem(
                Pair(
                    Channel.WATCH_NEXT_ID,
                    MainRowItem(
                        getString(R.string.watch_next), WatchNextAdapter(
                            this,
                            Suggestions.getWatchNextPrograms(this)
                                .filterIsInstance<BasePreviewProgram>() as ArrayList<BasePreviewProgram>
                        )
                    )
                )
            )
            return
        } else if (channelId == Channel.ALL_APPS_ID) {
            mMainVerticalAdapter.addItem(
                Pair(
                    Channel.ALL_APPS_ID,
                    MainRowItem(getString(R.string.other_apps), AppsAdapter(this))
                )
            )
            return
        }

        var channel: PreviewChannel? = null
        for (c in mChannels) {
            if (c.id == channelId) {
                channel = c
            }
        }

        channel ?: return

        val previewPrograms = Suggestions.getSuggestions(this, channel.id).take(5)
            .filterIsInstance<BasePreviewProgram>() as ArrayList<BasePreviewProgram>
        if (previewPrograms.isEmpty()) {
            return
        }

        mMainVerticalAdapter.addItem(
            Pair(
                channel.id, MainRowItem(
                    channel.displayName.toString(),
                    WatchNextAdapter(this, previewPrograms)
                )
            )
        )
    }

    private fun onChannelOrderChanged(
        moveChannelId: Long?,
        otherChannelId: Long?,
    ) {
        val isMovingChannelShowing = mMainVerticalAdapter.isChannelShowing(moveChannelId)
        val isOtherChannelShowing = mMainVerticalAdapter.isChannelShowing(otherChannelId)
        if (!isMovingChannelShowing || !isOtherChannelShowing) {
            return
        }

        val from = mMainVerticalAdapter.findChannelIndex(moveChannelId)
        val to = mMainVerticalAdapter.findChannelIndex(otherChannelId)
        mMainVerticalAdapter.itemMoved(from, to)
    }

    private fun onChannelSelected(channelId: Long, index: Int) {
        if (mMainVerticalAdapter.isChannelShowing(channelId)) {
            val pos = mMainVerticalAdapter.findChannelIndex(channelId)
            mMainVerticalGridView.layoutManager?.scrollToPosition(pos)
        }
    }
}