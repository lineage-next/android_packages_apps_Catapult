package org.lineageos.tv.launcher.view

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import org.lineageos.tv.launcher.R
import org.lineageos.tv.launcher.model.Launchable

open class Card : LinearLayout {
    val mIconView: ImageView by lazy { findViewById(R.id.app_icon) }
    val mNameView: TextView by lazy { findViewById(R.id.app_name) }
    var mPackageName: String = ""
    var mLabel: String = ""
    var mLaunchIntent: Intent? = null

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inflate()
    }

    open fun inflate() {
        inflate(context, R.layout.app_card, this)
    }

    open fun setCardInfo(appInfo: Launchable) {
        mNameView.text = appInfo.mLabel
        mIconView.setImageDrawable(appInfo.mIcon)
        mLabel = appInfo.mLabel
        mPackageName = appInfo.mPackageName
        mLaunchIntent = appInfo.mLaunchIntent
    }
}