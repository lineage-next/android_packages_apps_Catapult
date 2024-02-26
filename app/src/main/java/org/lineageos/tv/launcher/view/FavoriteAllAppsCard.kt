package org.lineageos.tv.launcher.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import org.lineageos.tv.launcher.R

class FavoriteAllAppsCard : Card {
    val mActionIconView: ImageView

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        background = context.getDrawable(R.drawable.favorites_app_card_background)
        mActionIconView = findViewById(R.id.action_image)
    }

    override fun inflate() {
        inflate(context, R.layout.favorites_add_app_card, this)
    }

    fun setActionAdd() {
        mActionIconView.setImageDrawable(context.getDrawable(R.drawable.ic_add))
    }

    fun setActionRemove() {
        mActionIconView.setImageDrawable(context.getDrawable(R.drawable.ic_remove))
    }
}