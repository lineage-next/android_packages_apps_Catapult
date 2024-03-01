package org.lineageos.tv.launcher.model

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable

class ActivityLauncher(
    label: String,
    icon: Drawable,
    context: Context,
    private val intent: Intent
) : Launchable(label, "", icon, context) {
    override var mHasMenu: Boolean = false

    init {
        icon.setTint(mContext.getColor(android.R.color.black))
    }

    override fun setIntent(): Intent {
        return intent
    }
}