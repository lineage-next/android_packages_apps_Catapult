package org.lineageos.tv.launcher.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.lineageos.tv.launcher.R
import org.lineageos.tv.launcher.model.AppInfo
import org.lineageos.tv.launcher.model.Launchable
import org.lineageos.tv.launcher.utils.AppManager
import org.lineageos.tv.launcher.view.AppCard
import org.lineageos.tv.launcher.view.Card

open class AppsAdapter(protected val mContext: Context) :
    RecyclerView.Adapter<AppsAdapter.ViewHolder>() {

    protected val mAppsList by lazy { getaAppsList() }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            handleClick(v as Card)
        }

        override fun onLongClick(v: View): Boolean {
            return handleLongClick(v as Card)
        }
    }

    protected open fun handleClick(app: Card) {
        val context = app.context
        context.startActivity(app.getAppInfo()!!.mLaunchIntent)
        Toast.makeText(context, app.getAppInfo()!!.mLabel, Toast.LENGTH_SHORT).show()
    }

    protected open fun handleLongClick(app: Card): Boolean {
        val context = app.context
        Toast.makeText(context, "long click " + app.getAppInfo()!!.mLabel, Toast.LENGTH_SHORT).show()

        return false
    }

    protected open fun getaAppsList(): ArrayList<Launchable> {
        return AppManager.getInstalledApps(mContext)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        (viewHolder.itemView as AppCard).setAppInfo(mAppsList[i])
    }

    override fun getItemCount(): Int {
        return mAppsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AppCard(mContext))
    }
}

