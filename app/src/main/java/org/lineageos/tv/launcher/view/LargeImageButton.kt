package org.lineageos.tv.launcher.view

import android.animation.AnimatorInflater
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import org.lineageos.tv.launcher.R


open class LargeImageButton : LinearLayout {
    private val mTextView: TextView by lazy { findViewById(R.id.text) }
    private val mImageView: ImageView by lazy { findViewById(R.id.image) }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setup(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setup(attrs)
    }

    private fun setup(attrs: AttributeSet) {
        val text = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android",
            "text", R.string.empty)
        val drawableResource =
            attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src",
                androidx.leanback.R.drawable.lb_ic_sad_cloud)
        mTextView.text = context.getString(text)
        mImageView.setImageDrawable(AppCompatResources.getDrawable(context, drawableResource))
    }

    init {
        inflate(context, R.layout.large_image_button, this)
        isFocusable = true
        isClickable = true
        stateListAnimator =
            AnimatorInflater.loadStateListAnimator(context, R.anim.app_card_state_animator)
    }
}