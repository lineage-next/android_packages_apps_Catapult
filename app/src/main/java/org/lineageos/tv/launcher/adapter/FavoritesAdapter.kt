package org.lineageos.tv.launcher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import android.view.ViewGroup
import org.lineageos.tv.launcher.R
import org.lineageos.tv.launcher.model.AddFavorite
import org.lineageos.tv.launcher.model.AppInfo
import org.lineageos.tv.launcher.model.Launchable
import org.lineageos.tv.launcher.utils.AppManager
import org.lineageos.tv.launcher.view.AppCard
import org.lineageos.tv.launcher.view.Card


class FavoritesAdapter(context: Context) : AppsAdapter(context) {
    init {
        updateFavoriteApps(AppManager.getFavoriteApps(mContext))
    }

    override fun getaAppsList(): ArrayList<Launchable> {
        val list = ArrayList<Launchable>()
        list.add(createAddFavoriteEntry())
        return list
    }

    fun updateFavoriteApps(packageNames: Set<String>) {
        for (packageName in packageNames) {
            val pm: PackageManager = mContext.packageManager
            try {
                val ai: ApplicationInfo = pm.getApplicationInfo(packageName, 0)
                val appInfo = AppInfo(ai, mContext)
                mAppsList.add(mAppsList.size - 1, appInfo)
            } catch (e: PackageManager.NameNotFoundException) {
                AppManager.removeFavoriteApp(mContext, packageName)
            }
        }

        notifyItemRangeChanged(0, mAppsList.size)
    }

    private fun createAddFavoriteEntry(): Launchable {
        return AddFavorite(
            mContext.getString(R.string.new_favorite),
            mContext.getDrawable(R.drawable.ic_add)!!, mContext
        )
    }

    override fun handleLongClick(app: Card): Boolean {
        showPopupMenu(app, R.menu.favorite_app_long_press)
        return true
    }

    fun addItem(packageName: String) {
        val ai: ApplicationInfo = mContext.packageManager.getApplicationInfo(packageName, 0)
        val appInfo = AppInfo(ai, mContext)
        mAppsList.add(mAppsList.size - 1, appInfo) // Take 'Add new' into account
        notifyItemInserted(mAppsList.size - 2)
    }

    fun removeItem(packageName: String) {
        for (i in 0 until mAppsList.size) {
            if (mAppsList[i].mPackageName == packageName) {
                mAppsList.removeAt(i)
                notifyItemRemoved(i)
                break
            }
        }
    }
}