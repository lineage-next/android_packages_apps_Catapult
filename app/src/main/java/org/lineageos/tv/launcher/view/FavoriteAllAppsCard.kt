package org.lineageos.tv.launcher.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import org.lineageos.tv.launcher.R

class FavoriteAllAppsCard : Card {
    private val mActionIconView: ImageView

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inflate(context, R.layout.favorites_add_app_card, this)
        background = AppCompatResources.getDrawable(context, R.drawable.favorites_app_card_background)
        mActionIconView = findViewById(R.id.action_image)
    }

    override fun inflate() {
        inflate(context, R.layout.favorites_add_app_card, this)
    }

    fun setActionAdd() {
        mActionIconView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_add))
    }

    fun setActionRemove() {
        mActionIconView.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_remove))
    }
}