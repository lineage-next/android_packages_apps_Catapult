package org.lineageos.tv.launcher.view

import android.content.Context
import android.util.AttributeSet
import org.lineageos.tv.launcher.R

class FavoriteAllAppsCard : Card {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        background = context.getDrawable(R.drawable.favorites_app_card_background)
    }

    override fun inflate() {
        inflate(context, R.layout.favorites_app_card, this)
    }
}