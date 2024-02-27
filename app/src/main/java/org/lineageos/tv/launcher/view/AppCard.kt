package org.lineageos.tv.launcher.view

import android.animation.AnimatorInflater
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import org.lineageos.tv.launcher.R
import org.lineageos.tv.launcher.model.AppInfo
import org.lineageos.tv.launcher.model.Launchable

open class AppCard : Card {
    val mBannerView: ImageView by lazy { findViewById(R.id.app_banner) }
    val mIconContainer: LinearLayout by lazy { findViewById(R.id.app_with_icon) }

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        stateListAnimator =
            AnimatorInflater.loadStateListAnimator(context, R.anim.app_card_state_animator)
    }

    override fun inflate() {
        inflate(context, R.layout.app_card, this)
    }

    override fun setCardInfo(appInfo: Launchable) {
        super.setCardInfo(appInfo)

        if (appInfo is AppInfo && appInfo.mBanner != null) {
            // App with a banner
            mBannerView.setImageDrawable(appInfo.mBanner)
            mBannerView.visibility = View.VISIBLE
            mIconContainer.visibility = View.GONE
        } else {
            // App with an icon
            mIconView.setImageDrawable(appInfo.mIcon)
        }
    }
}