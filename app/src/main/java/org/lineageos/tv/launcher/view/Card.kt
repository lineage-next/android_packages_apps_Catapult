package org.lineageos.tv.launcher.view

import android.animation.AnimatorInflater
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import org.lineageos.tv.launcher.R
import org.lineageos.tv.launcher.model.AppInfo
import org.lineageos.tv.launcher.model.Launchable

open class Card : LinearLayout {
    val mIconView: ImageView
    val mNameView: TextView

    protected var mAppInfo: Launchable? = null

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inflate()
        mIconView = findViewById(R.id.app_icon)
        mNameView = findViewById(R.id.app_name)
    }

    open fun inflate() {
        inflate(context, R.layout.app_card, this)
    }

    open fun setAppInfo(appInfo: Launchable) {
        mAppInfo = appInfo
        mNameView.text = appInfo.mLabel
        mIconView.setImageDrawable(appInfo.mIcon)
    }

    fun getAppInfo() : Launchable? {
        return mAppInfo
    }
}