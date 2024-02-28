package org.lineageos.tv.launcher.adapter

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import org.lineageos.tv.launcher.R
import org.lineageos.tv.launcher.model.AddFavorite
import org.lineageos.tv.launcher.model.AppInfo
import org.lineageos.tv.launcher.model.Launchable
import org.lineageos.tv.launcher.utils.AppManager
import org.lineageos.tv.launcher.view.Card
import org.lineageos.tv.launcher.view.FavoriteCard
import java.util.Collections


class FavoritesAdapter(context: Context) : AppsAdapter(context) {
    init {
        updateFavoriteApps(AppManager.getFavoriteApps(mContext))
    }

    override fun getaAppsList(): ArrayList<Launchable> {
        val list = ArrayList<Launchable>()
        list.add(createAddFavoriteEntry())
        return list
    }

    fun updateFavoriteApps(packageNames: List<String>) {
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

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        (viewHolder.itemView as FavoriteCard).setCardInfo(mAppsList[i])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FavoriteCard(mContext))
    }

    private fun createAddFavoriteEntry(): Launchable {
        return AddFavorite(
            mContext.getString(R.string.new_favorite),
            mContext.getDrawable(R.drawable.ic_add)!!, mContext
        )
    }

    override fun handleKey(v: View, keyCode: Int, keyEvent: KeyEvent): Boolean {
        v as FavoriteCard

        // Leave center key for onClick handler
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            return false
        }

        // Only handle keyDown events here
        if (keyEvent.action != KeyEvent.ACTION_DOWN) {
            return v.mMoving
        }

        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (v.mMoving) {
                    val i = findAppIndex(v.mPackageName)
                    if (i == 0) {
                        return true
                    }
                    Collections.swap(mAppsList, i, i - 1)
                    notifyItemMoved(i, i - 1)
                    return true
                }
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (v.mMoving) {
                    val i = findAppIndex(v.mPackageName)
                    if (i == mAppsList.size - 2) {
                        return true
                    }
                    Collections.swap(mAppsList, i, i + 1)
                    notifyItemMoved(i, i + 1)
                    return true
                }
            }
            // Don't allow moving up or down while moving a favorite app
            KeyEvent.KEYCODE_DPAD_DOWN,
            KeyEvent.KEYCODE_DPAD_UP ->
                return v.mMoving
        }

        return false
    }

    override fun handleLongClick(app: Card): Boolean {
        app as FavoriteCard
        if (app.mMoving) {
            return true
        }

        showPopupMenu(app, R.menu.favorite_app_long_press)
        return true
    }

    override fun handleClick(app: Card) {
        app as FavoriteCard
        if (!app.mMoving) {
            super.handleClick(app)
            return
        }

        app.setMoveDone()

        // Save new favorites order
        val newFavoritesSet = ArrayList<String>()
        for (a in mAppsList) {
            if (a.mPackageName != "") {
                newFavoritesSet.add(a.mPackageName)
            }
        }

        AppManager.setFavorites(mContext, newFavoritesSet)
    }

    fun addItem(packageName: String) {
        val ai: ApplicationInfo = mContext.packageManager.getApplicationInfo(packageName, 0)
        val appInfo = AppInfo(ai, mContext)
        mAppsList.add(mAppsList.size - 1, appInfo) // Take 'Add new' into account
        notifyItemInserted(mAppsList.size - 2)
    }

    fun removeItem(packageName: String) {
        val i = findAppIndex(packageName)
        mAppsList.removeAt(i)
        notifyItemRemoved(i)
    }

    private fun findAppIndex(packageName: String): Int {
        for (i in 0 until mAppsList.size) {
            if (mAppsList[i].mPackageName == packageName) {
                return i
            }
        }

        return -1
    }
}