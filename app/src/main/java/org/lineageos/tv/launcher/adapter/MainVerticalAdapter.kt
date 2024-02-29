package org.lineageos.tv.launcher.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.lineageos.tv.launcher.model.MainRowItem
import org.lineageos.tv.launcher.view.VerticalRowItem

class MainVerticalAdapter(private val mContext: Context,
                          private val mRowList: ArrayList<Pair<Long, MainRowItem>>) :
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
        // All apps is always last
        mRowList.add(mRowList.size - 1, item)
        notifyItemInserted(mRowList.size - 2)
    }

    companion object {
        const val STABLE_ITEM_COUNT_TOP = 2
    }
}