package org.lineageos.tv.launcher.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.tvprovider.media.tv.PreviewChannel
import androidx.tvprovider.media.tv.PreviewProgram
import org.lineageos.tv.launcher.view.WatchNextCard


class ChannelAdapter(
    private val mContext: Context,
    private val mWatchableList: ArrayList<PreviewProgram>,
) :
    RecyclerView.Adapter<ChannelAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            handleClick(v as WatchNextCard)
        }
    }

    private fun handleClick(v: WatchNextCard) {
        val context = v.context
        context.startActivity(v.mLaunchIntent)
        Toast.makeText(context, v.mLabel, Toast.LENGTH_SHORT).show()
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        (viewHolder.itemView as WatchNextCard).setInfo(mWatchableList[i])
    }

    override fun getItemCount(): Int {
        return mWatchableList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(WatchNextCard(mContext))
    }
}