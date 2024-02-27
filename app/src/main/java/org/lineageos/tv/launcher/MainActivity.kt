package org.lineageos.tv.launcher

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.leanback.widget.HorizontalGridView
import androidx.tvprovider.media.tv.TvContractCompat
import org.lineageos.tv.launcher.adapter.AppsAdapter
import org.lineageos.tv.launcher.adapter.FavoritesAdapter
import org.lineageos.tv.launcher.adapter.WatchNextAdapter
import org.lineageos.tv.launcher.utils.AppManager
import org.lineageos.tv.launcher.utils.Suggestions


class MainActivity : Activity(), AppManager.OnFavoritesChangeListener {
    private lateinit var mFavoritesAdapter: FavoritesAdapter
    private lateinit var mAllAppsGridView: AppsAdapter

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppManager.setFavoritesListener(this)

        val favoritesGridView: HorizontalGridView = findViewById(R.id.favorites_grid)
        mFavoritesAdapter = FavoritesAdapter(this)
        favoritesGridView.adapter = mFavoritesAdapter

        val watchNextGridView: HorizontalGridView = findViewById(R.id.watch_next_grid)
        val watchNextAdapter = WatchNextAdapter(this)
        watchNextGridView.adapter = watchNextAdapter

        val allAppsGridView: HorizontalGridView = findViewById(R.id.all_apps_grid)
        mAllAppsGridView = AppsAdapter(this)
        allAppsGridView.adapter = mAllAppsGridView

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