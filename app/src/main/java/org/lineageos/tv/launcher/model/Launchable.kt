package org.lineageos.tv.launcher.model

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import org.lineageos.tv.launcher.R

open class Launchable(
    val mLabel: String,
    val mPackageName: String,
    val mIcon: Drawable,
    val mContext: Context
) {
    open val mHasMenu = true
    val mLaunchIntent by lazy { setIntent() }

    protected open fun setIntent(): Intent? {
        return null
    }
}