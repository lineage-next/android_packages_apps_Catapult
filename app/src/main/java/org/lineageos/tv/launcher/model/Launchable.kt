package org.lineageos.tv.launcher.model

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable

open class Launchable(
    val mLabel: String,
    val mPackageName: String,
    val mIcon: Drawable,
    val mContext: Context
) {

    val mLaunchIntent by lazy { setIntent() }

    protected open fun setIntent(): Intent? {
        return null
    }
}