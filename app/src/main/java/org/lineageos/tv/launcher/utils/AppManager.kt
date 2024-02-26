package org.lineageos.tv.launcher.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import org.lineageos.tv.launcher.MainActivity
import org.lineageos.tv.launcher.model.AppInfo
import org.lineageos.tv.launcher.model.Launchable


object AppManager {
    private var mFavoritesChangeListener: OnFavoritesChangeListener? = null

    fun setFavoritesListener(listener: OnFavoritesChangeListener) {
        mFavoritesChangeListener = listener
    }

    fun getInstalledApps(context: Context): ArrayList<Launchable> {
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val apps = pm.queryIntentActivities(intent, 0)
        val appsList: ArrayList<Launchable> = ArrayList()
        for (app in apps) {
            val appInfo = AppInfo(app, context)
            appsList.add(appInfo)
        }

        return appsList
    }

    fun removeFavoriteApp(context: Context, app: Launchable) {
        removeFavoriteApp(context, app.mPackageName)
    }

    fun removeFavoriteApp(context: Context, packageName: String) {
        val sharedPreferences = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        val favoritesSet =
            sharedPreferences.getStringSet("favoriteApps", HashSet()) ?: HashSet()
        val newFavoritesSet = HashSet(favoritesSet) // Make a copy
        newFavoritesSet.remove(packageName)
        val editor = sharedPreferences.edit()
        editor.putStringSet("favoriteApps", newFavoritesSet)
        editor.apply()

        // Notify
        mFavoritesChangeListener?.onFavoriteRemoved(packageName)
    }

    fun addFavoriteApp(context: Context, app: Launchable) {
        addFavoriteApp(context, app.mPackageName)
    }

    fun addFavoriteApp(context: Context, packageName: String) {
        val sharedPreferences = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        val favoritesSet =
            sharedPreferences.getStringSet("favoriteApps", HashSet()) ?: HashSet()
        val newFavoritesSet = HashSet(favoritesSet) // Make a copy
        newFavoritesSet.add(packageName)
        val editor = sharedPreferences.edit()
        editor.putStringSet("favoriteApps", newFavoritesSet)
        editor.apply()

        // Notify
        mFavoritesChangeListener?.onFavoriteAdded(packageName)
    }

    fun getFavoriteApps(context: Context): Set<String> {
        val sharedPreferences =
            context.getSharedPreferences("Favorites", Context.MODE_PRIVATE)
        return sharedPreferences.getStringSet("favoriteApps", HashSet()) ?: HashSet()
    }

    fun uninstallApp(context: Context, packageName: String) {
        val packageUri = Uri.parse("package:$packageName")
        val uninstallIntent = Intent(Intent.ACTION_DELETE, packageUri)
        (context as Activity).startActivityForResult(uninstallIntent, MainActivity.REQUEST_CODE_UNINSTALL,null)
    }

    interface OnFavoritesChangeListener {
        fun onFavoriteAdded(packageName: String)
        fun onFavoriteRemoved(packageName: String)
    }
}