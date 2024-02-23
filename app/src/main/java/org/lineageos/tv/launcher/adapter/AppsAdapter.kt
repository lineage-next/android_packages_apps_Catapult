package org.lineageos.tv.launcher.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.lineageos.tv.launcher.R
import org.lineageos.tv.launcher.model.Launchable
import org.lineageos.tv.launcher.utils.AppManager

open class AppsAdapter(protected val mContext: Context) :
    RecyclerView.Adapter<AppsAdapter.ViewHolder>() {

    protected val mAppsList by lazy { getaAppsList() }
    protected open val mLayoutId = R.layout.app_card

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        var mAppNameView: TextView
        var mIconView: ImageView

        init {
            mAppNameView = itemView.findViewById<View>(R.id.app_name) as TextView
            mIconView = itemView.findViewById<View>(R.id.app_icon) as ImageView
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            handleClick(mAppsList[adapterPosition], v)
        }
    }

    protected open fun handleClick(app: Launchable, v: View) {
        val context = v.context
        context.startActivity(app.mLaunchIntent)
        Toast.makeText(context, app.mLabel, Toast.LENGTH_SHORT).show()
    }

    protected open fun getaAppsList(): ArrayList<Launchable> {
        return AppManager.getInstalledApps(mContext)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val appLabel: String = mAppsList[i].mLabel
        val textView = viewHolder.mAppNameView
        textView.text = appLabel

        val imageView = viewHolder.mIconView
        val appIcon: Drawable = mAppsList[i].mIcon
        imageView.setImageDrawable(appIcon)
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

