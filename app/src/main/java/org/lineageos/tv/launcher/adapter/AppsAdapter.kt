package org.lineageos.tv.launcher.adapter

import android.content.Context
import android.view.LayoutInflater
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

open class AppsAdapter(protected val mContext: Context) :
    RecyclerView.Adapter<AppsAdapter.ViewHolder>() {

    protected val mAppsList by lazy { getaAppsList() }
    protected open val mLayoutId = R.layout.app_card

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {

        val mAppNameView: TextView
        val mIconView: ImageView
        val mBannerView: ImageView?
        val mIconContainer: LinearLayout?

        init {
            mAppNameView = itemView.findViewById(R.id.app_name)
            mIconView = itemView.findViewById(R.id.app_icon)
            mBannerView = itemView.findViewById(R.id.app_banner)
            mIconContainer = itemView.findViewById(R.id.app_with_icon)
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View) {
            handleClick(mAppsList[adapterPosition], v)
        }

        override fun onLongClick(v: View): Boolean {
            return handleLongClick(mAppsList[adapterPosition], v)
        }
    }

    protected open fun handleClick(app: Launchable, v: View) {
        val context = v.context
        context.startActivity(app.mLaunchIntent)
        Toast.makeText(context, app.mLabel, Toast.LENGTH_SHORT).show()
    }

    protected open fun handleLongClick(app: Launchable, v: View): Boolean {
        val context = v.context
        Toast.makeText(context, "long click " + app.mLabel, Toast.LENGTH_SHORT).show()

        return false
    }

    protected open fun getaAppsList(): ArrayList<Launchable> {
        return AppManager.getInstalledApps(mContext)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val appLabel: String = mAppsList[i].mLabel
        val textView = viewHolder.mAppNameView
        textView.text = appLabel

        if (mAppsList[i] is AppInfo && (mAppsList[i] as AppInfo).mBanner != null
            && viewHolder.mBannerView != null && viewHolder.mIconContainer != null) {
            // App with a banner
            viewHolder.mBannerView.setImageDrawable((mAppsList[i] as AppInfo).mBanner)
            viewHolder.mBannerView.visibility = View.VISIBLE
            viewHolder.mIconContainer.visibility = View.GONE
        } else {
            // App with an icon
            viewHolder.mIconView.setImageDrawable(mAppsList[i].mIcon)
        }
    }

    override fun getItemCount(): Int {
        return mAppsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(mLayoutId, parent, false)
        return ViewHolder(view)
    }
}

