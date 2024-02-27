package org.lineageos.tv.launcher.adapter

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.lineageos.tv.launcher.R
import org.lineageos.tv.launcher.model.Launchable
import org.lineageos.tv.launcher.model.MainRowItem
import org.lineageos.tv.launcher.utils.AppManager
import org.lineageos.tv.launcher.view.AppCard
import org.lineageos.tv.launcher.view.Card
import org.lineageos.tv.launcher.view.FavoriteCard
import org.lineageos.tv.launcher.view.VerticalRowItem

class MainVerticalAdapter(private val mContext: Context, private val mRowList: ArrayList<MainRowItem>) :
    RecyclerView.Adapter<MainVerticalAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        (viewHolder.itemView as VerticalRowItem).setData(mRowList[i])
    }

    override fun getItemCount(): Int {
        return mRowList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(VerticalRowItem(mContext))
    }
}