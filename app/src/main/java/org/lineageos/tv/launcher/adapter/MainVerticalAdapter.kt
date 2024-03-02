package org.lineageos.tv.launcher.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.lineageos.tv.launcher.model.MainRowItem
import org.lineageos.tv.launcher.utils.Suggestions
import org.lineageos.tv.launcher.utils.Suggestions.orderSuggestions
import org.lineageos.tv.launcher.view.VerticalRowItem
import java.util.Collections

class MainVerticalAdapter(
    private val mContext: Context,
    private val mRowList: ArrayList<Pair<Long, MainRowItem>>,
) :
    RecyclerView.Adapter<MainVerticalAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        (viewHolder.itemView as VerticalRowItem).setData(mRowList[i].second)
    }

    override fun getItemCount(): Int {
        return mRowList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(VerticalRowItem(mContext))
    }

    fun removeItem(item: Long) {
        for ((i, row) in mRowList.withIndex()) {
            if (row.first == item) {
                mRowList.remove(row)
                notifyItemRemoved(i)
                return
            }
        }
    }

    fun addItem(item: Pair<Long, MainRowItem>) {
        var temp = mRowList.toMutableList()
        temp.add(item)
        temp =
            temp.orderSuggestions(Suggestions.getChannelOrder(mContext)) { it.first } as ArrayList
        var index = temp.indexOf(item)
        if (index == -1) {
            index = (mRowList.size - 1)
        }
        mRowList.add(index, item)
        notifyItemInserted(index)
    }

    fun isChannelShowing(channelId: Long?): Boolean {
        channelId ?: return false
        val res = mRowList.find { it.first == channelId }
        res ?: return false
        return true
    }

    fun findChannelIndex(channelId: Long?): Int {
        channelId ?: return -1
        return mRowList.indexOfFirst { it.first == channelId }
    }

    fun itemMoved(from: Int, to: Int) {
        Collections.swap(mRowList, from, to)
        notifyItemMoved(from, to)
    }
}