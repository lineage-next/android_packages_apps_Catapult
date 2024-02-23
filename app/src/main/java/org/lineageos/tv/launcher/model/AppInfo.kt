package org.lineageos.tv.launcher.model

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable

class AppInfo(
    label: String,
    packageName: String,
    icon: Drawable,
    context: Context
) : Launchable(label, packageName, icon, context) {

    override fun setIntent(): Intent? {
        return mContext.packageManager.getLaunchIntentForPackage(mPackageName)
    }
}