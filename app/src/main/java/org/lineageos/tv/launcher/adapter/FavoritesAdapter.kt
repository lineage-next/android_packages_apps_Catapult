package org.lineageos.tv.launcher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import org.lineageos.tv.launcher.R
import org.lineageos.tv.launcher.model.AddFavorite
import org.lineageos.tv.launcher.model.AppInfo
import org.lineageos.tv.launcher.model.Launchable

class FavoritesAdapter(context: Context) : AppsAdapter(context) {

    override fun getaAppsList(): ArrayList<Launchable> {
        val list = ArrayList<Launchable>()
        list.add(createAddFavoriteEntry())
        return list
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateFavoriteApps(packageNames: Set<String>) {
        mAppsList.clear()

        for (packageName in packageNames) {
            val pm: PackageManager = mContext.packageManager
            val ai: ApplicationInfo = pm.getApplicationInfo(packageName, 0)

            val appInfo = AppInfo(ai,
                mContext
            )
            mAppsList.add(appInfo)
        }

        mAppsList.add(createAddFavoriteEntry())
        notifyDataSetChanged()
    }

    private fun createAddFavoriteEntry(): Launchable {
        return AddFavorite(
            mContext.getString(R.string.new_favorite),
            mContext.getDrawable(R.drawable.ic_add)!!, mContext
        )
    }
}