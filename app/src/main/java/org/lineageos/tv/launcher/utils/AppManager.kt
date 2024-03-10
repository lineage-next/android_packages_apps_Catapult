/*
 * SPDX-FileCopyrightText: 2024 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.tv.launcher.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.preference.PreferenceManager
import org.lineageos.tv.launcher.ext.favoriteApps
import org.lineageos.tv.launcher.model.AppInfo

object AppManager {
    fun updateFavoriteApps(context: Context, installedApps: List<AppInfo>) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val favoriteApps = sharedPreferences.favoriteApps.toMutableList()

        // Remove apps from favorite if they got uninstalled
        with(installedApps.map { it.packageName }) {
            favoriteApps.filter {
                !contains(it)
            }
        }.forEach {
            favoriteApps.remove(it)
        }

        sharedPreferences.favoriteApps = favoriteApps
    }

    fun toggleFavoriteApp(context: Context, packageName: String, favorite: Boolean) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val favoriteApps = sharedPreferences.favoriteApps.toMutableList()

        if (favorite) {
            if (!favoriteApps.contains(packageName)) {
                favoriteApps.add(packageName)
            }
        } else {
            favoriteApps.remove(packageName)
        }

        sharedPreferences.favoriteApps = favoriteApps
    }

    fun getFavoriteApps(context: Context): List<String> {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        return sharedPreferences.favoriteApps
    }

    fun uninstallApp(context: Context, packageName: String) {
        val packageUri = Uri.parse("package:$packageName")
        val uninstallIntent = Intent(Intent.ACTION_DELETE, packageUri)
        context.startActivity(uninstallIntent, null)
    }
}
