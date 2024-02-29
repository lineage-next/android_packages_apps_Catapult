package org.lineageos.tv.launcher.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.recyclerview.widget.RecyclerView
import androidx.tvprovider.media.tv.PreviewChannel
import org.lineageos.tv.launcher.R
import org.lineageos.tv.launcher.utils.Suggestions
import org.lineageos.tv.launcher.view.ToggleChannel

class ModifyChannelsAdapter(private val mContext: Context, private val mChannels: List<PreviewChannel>) :
    RecyclerView.Adapter<ModifyChannelsAdapter.ViewHolder>() {

    val hiddenChannels: List<Long> by lazy { Suggestions.getHiddenChannels(mContext) }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        @SuppressLint("UseSwitchCompatOrMaterialCode") // Not available for leanback
        val mSwitch: Switch = itemView.findViewById<Switch>(R.id.state_switch)

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        override fun onClick(v: View) {
            v as ToggleChannel
            if (mSwitch.isChecked) {
                mSwitch.isChecked = false
                Suggestions.hideChannel(mContext, v.mChannelId)
            } else {
                mSwitch.isChecked = true
                Suggestions.showChannel(mContext, v.mChannelId)
            }
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        var hidden = false
        if (hiddenChannels.contains(mChannels[i].id)) {
            hidden = true
        }
        (viewHolder.itemView as ToggleChannel).setData(mChannels[i], hidden)
    }

    override fun getItemCount(): Int {
        return mChannels.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = ToggleChannel(parent.context)

        itemView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        return ViewHolder(itemView)
    }
}