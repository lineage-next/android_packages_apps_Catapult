package org.lineageos.tv.launcher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import org.lineageos.tv.launcher.R
import org.lineageos.tv.launcher.model.AddFavorite
import org.lineageos.tv.launcher.model.AppInfo
import org.lineageos.tv.launcher.model.Launchable
import org.lineageos.tv.launcher.utils.AppManager
import org.lineageos.tv.launcher.view.AppCard
import org.lineageos.tv.launcher.view.Card


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

            val appInfo = AppInfo(
                ai,
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

    override fun handleLongClick(app: Card): Boolean {
        showPopupMenu(app)
        return true
    }

    private fun showPopupMenu(anchorView: View) {
        val popupMenu = PopupMenu(mContext, anchorView)
        popupMenu.menuInflater.inflate(R.menu.favorite_app_long_press, popupMenu.menu)
        popupMenu.setForceShowIcon(true)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_uninstall -> {
                    Toast.makeText(mContext, "Uninstall selected", Toast.LENGTH_SHORT).show()
                    // Replace with your uninstall logic
                    true
                }

                R.id.menu_remove_favorite -> {
                    (anchorView as AppCard).getAppInfo()?.let {
                        AppManager.removeFavoriteApp(mContext, it)
                    }
                    true
                }

                R.id.menu_move -> {
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }
}