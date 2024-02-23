package org.lineageos.tv.launcher.model

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import org.lineageos.tv.launcher.AddFavoriteActivity

class AddFavorite(
    label: String,
    icon: Drawable,
    context: Context
) : Launchable(label, "", icon, context) {

    override fun setIntent(): Intent {
        return Intent(mContext, AddFavoriteActivity::class.java)
    }
}