package org.lineageos.tv.launcher.view

import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.tvprovider.media.tv.WatchNextProgram
import com.bumptech.glide.Glide
import org.lineageos.tv.launcher.R

open class WatchNextCard : Card {
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

    @SuppressLint("RestrictedApi")
    fun setWatchNextInfo(info: WatchNextProgram) {
        Glide.with(this).load(info.posterArtUri).into(mBannerView);
        mNameView.text = info.episodeTitle
        mBannerView.visibility = View.VISIBLE
        mIconContainer.visibility = View.GONE
        mLaunchIntent = info.intent
    }
}