package org.lineageos.tv.launcher.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.tvprovider.media.tv.PreviewChannel
import org.lineageos.tv.launcher.R
import org.lineageos.tv.launcher.utils.Suggestions.getAppName

class ToggleChannel : LinearLayout {
    private val mTitleView: TextView by lazy { findViewById(R.id.title) }
    private val mSwitch: Switch by lazy { findViewById(R.id.state_switch) }

    var mMoving = false
    var mChannelId: Long? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        inflate(context, R.layout.toggle_channel, this)
        isFocusable = true
        isClickable = true
        background = AppCompatResources.getDrawable(context, R.drawable.favorites_app_card_background)
    }

    fun setData(previewChannel: PreviewChannel, hidden: Boolean) {
        mTitleView.text = resources.getString(
            R.string.channel_title, previewChannel.getAppName(context), previewChannel.displayName)
        mChannelId = previewChannel.id
        mSwitch.isChecked = !hidden
    }

    fun setMoving() {
        mMoving = true
    }

    fun setMoveDone() {
        mMoving = false
    }
}