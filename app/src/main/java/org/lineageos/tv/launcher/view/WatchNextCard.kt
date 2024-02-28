package org.lineageos.tv.launcher.view

import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.tvprovider.media.tv.BaseProgram
import androidx.tvprovider.media.tv.PreviewProgram
import androidx.tvprovider.media.tv.WatchNextProgram
import coil.load
import org.lineageos.tv.launcher.R

open class WatchNextCard : Card {
    private val mBannerView: ImageView by lazy { findViewById(R.id.app_banner) }
    private val mIconContainer: LinearLayout by lazy { findViewById(R.id.app_with_icon) }

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

    @SuppressLint("RestrictedApi")
    fun setInfo(info: BaseProgram) {
        mBannerView.load(info.posterArtUri)
        mNameView.text = info.episodeTitle
        mIconContainer.visibility = View.GONE
        mBannerView.visibility = View.VISIBLE
        if (info is WatchNextProgram) {
            mLaunchIntent = info.intent
        } else if (info is PreviewProgram) {
            mLaunchIntent = info.intent
        }
    }
}