package org.lineageos.tv.launcher.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import org.lineageos.tv.launcher.R
import org.lineageos.tv.launcher.model.Launchable
import org.lineageos.tv.launcher.utils.AppManager

class AllAppsFavoritesAdapter(context: Context) : AppsAdapter(context) {
    override val mLayoutId = R.layout.favorites_app_card
    private var mFavoritePackageNames = HashSet<String>()

    init {
        mFavoritePackageNames = AppManager.getFavoriteApps(mContext) as HashSet<String>
    }

    override fun handleClick(app: Launchable, v: View) {
        val actionImage = v.findViewById<ImageView>(R.id.action_image)
        if (mFavoritePackageNames.contains(app.mPackageName)) {
            AppManager.removeFavoriteApp(mContext, app)
            actionImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_add))
        } else {
            AppManager.addFavoriteApp(mContext, app)
            actionImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_remove))
        }

        mFavoritePackageNames = AppManager.getFavoriteApps(mContext) as HashSet<String>
    }

    override fun onBindViewHolder(viewHolder: AppsAdapter.ViewHolder, i: Int) {
        super.onBindViewHolder(viewHolder, i)

        if (mFavoritePackageNames.contains(mAppsList[i].mPackageName)) {
            val actionImage = viewHolder.itemView.findViewById<ImageView>(R.id.action_image)
            actionImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_remove))
        }
    }
}