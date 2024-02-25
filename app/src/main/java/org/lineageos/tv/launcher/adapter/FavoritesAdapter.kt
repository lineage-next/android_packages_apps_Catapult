package org.lineageos.tv.launcher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
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

    override fun handleLongClick(app: Launchable, v: View): Boolean {
        showPopupMenu(v)
        return true
    }

    private fun showPopupMenu(anchorView: View) {
        val popupMenu = PopupMenu(mContext, anchorView)
        popupMenu.menuInflater.inflate(R.menu.app_long_press, popupMenu.menu)
        popupMenu.setForceShowIcon(true)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_uninstall -> {
                    Toast.makeText(mContext, "Uninstall selected", Toast.LENGTH_SHORT).show()
                    // Replace with your uninstall logic
                    true
                }
                R.id.menu_mark_as_favorite -> {
                    Toast.makeText(mContext, "Mark as Favorite selected", Toast.LENGTH_SHORT).show()
                    // Replace with your mark as favorite logic
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }
}