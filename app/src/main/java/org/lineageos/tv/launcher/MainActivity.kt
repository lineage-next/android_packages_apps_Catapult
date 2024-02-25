package org.lineageos.tv.launcher

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.leanback.widget.HorizontalGridView
import org.lineageos.tv.launcher.adapter.AppsAdapter
import org.lineageos.tv.launcher.adapter.FavoritesAdapter
import org.lineageos.tv.launcher.utils.AppManager


class MainActivity : Activity() {
    private lateinit var mFavoritesAdapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val favoritesGridView: HorizontalGridView = findViewById(R.id.favorites_grid)
        mFavoritesAdapter = FavoritesAdapter(this)
        favoritesGridView.adapter = mFavoritesAdapter

        val allAppsGridView: HorizontalGridView = findViewById(R.id.all_apps_grid)
        val allAppsAdapter = AppsAdapter(this)
        allAppsGridView.adapter = allAppsAdapter
    }

    override fun onResume() {
        super.onResume()
        mFavoritesAdapter.updateFavoriteApps(AppManager.getFavoriteApps(applicationContext))
    }
}