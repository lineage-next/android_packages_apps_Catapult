package org.lineageos.tv.launcher

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.leanback.widget.VerticalGridView
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


class MainActivity : Activity(), AppManager.OnFavoritesChangeListener {
    private lateinit var mFavoritesAdapter: FavoritesAdapter

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainItems = ArrayList<MainRowItem>()
        mFavoritesAdapter = FavoritesAdapter(this)
        mainItems.add(MainRowItem(getString(R.string.favorites), mFavoritesAdapter))
        mainItems.add(MainRowItem(getString(R.string.watch_next), WatchNextAdapter(this)))

        val channels = Suggestions.getPreviewChannels(this)
        for (channel in channels) {
            val previewPrograms = Suggestions.getSuggestion(this, channel.id).take(5)
            if (previewPrograms.isEmpty()) {
                continue
            }
            mainItems.add(MainRowItem(channel.displayName.toString(), ChannelAdapter(this, previewPrograms as ArrayList<PreviewProgram>)))
        }

        mainItems.add(MainRowItem(getString(R.string.other_apps), AppsAdapter(this)))

        val mainVerticalGridView: VerticalGridView = findViewById(R.id.main_vertical_grid)
        val mainVerticalAdapter = MainVerticalAdapter(this, mainItems)
        mainVerticalGridView.adapter = mainVerticalAdapter

        AppManager.setFavoritesListener(this)

        if (checkCallingOrSelfPermission(TvContractCompat.PERMISSION_READ_TV_LISTINGS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(TvContractCompat.PERMISSION_READ_TV_LISTINGS), 0)
        }
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

    override fun onFavoriteAdded(packageName: String) {
        mFavoritesAdapter.addItem(packageName)
    }

    override fun onFavoriteRemoved(packageName: String) {
        mFavoritesAdapter.removeItem(packageName)
    }
}