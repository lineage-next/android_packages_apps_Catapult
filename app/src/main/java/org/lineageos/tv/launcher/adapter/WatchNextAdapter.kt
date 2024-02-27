package org.lineageos.tv.launcher.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.tvprovider.media.tv.WatchNextProgram
import org.lineageos.tv.launcher.utils.Suggestions
import org.lineageos.tv.launcher.view.WatchNextCard


class WatchNextAdapter(private val mContext: Context) :
    RecyclerView.Adapter<WatchNextAdapter.ViewHolder>() {

    private val mWatchableList by lazy { getWatchNextList() }

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

    private fun getWatchNextList(): ArrayList<WatchNextProgram> {
        return Suggestions.getWatchNextPrograms(mContext) as ArrayList<WatchNextProgram>
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        (viewHolder.itemView as WatchNextCard).setWatchNextInfo(mWatchableList[i])
    }

    override fun getItemCount(): Int {
        return mWatchableList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(WatchNextCard(mContext))
    }
}