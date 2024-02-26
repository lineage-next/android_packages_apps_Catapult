package org.lineageos.tv.launcher.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import org.lineageos.tv.launcher.R
import org.lineageos.tv.launcher.utils.AppManager
import org.lineageos.tv.launcher.view.Card
import org.lineageos.tv.launcher.view.FavoriteAllAppsCard

class AllAppsFavoritesAdapter(context: Context) : AppsAdapter(context) {
    private var mFavoritePackageNames = HashSet<String>()

    init {
        mFavoritePackageNames = AppManager.getFavoriteApps(mContext) as HashSet<String>
    }

    override fun handleClick(app: Card) {
        if (mFavoritePackageNames.contains(app.mPackageName)) {
            AppManager.removeFavoriteApp(mContext, app.mPackageName)
            app.mIconView.setImageDrawable(mContext.getDrawable(R.drawable.ic_add))
        } else {
            AppManager.addFavoriteApp(mContext, app.mPackageName)
            app.mIconView.setImageDrawable(mContext.getDrawable(R.drawable.ic_remove))
        }

        mFavoritePackageNames = AppManager.getFavoriteApps(mContext) as HashSet<String>
    }


    override fun onBindViewHolder(viewHolder: AppsAdapter.ViewHolder, i: Int) {
        (viewHolder.itemView as FavoriteAllAppsCard).setAppInfo(mAppsList[i])

        if (mFavoritePackageNames.contains(mAppsList[i].mPackageName)) {
            val actionImage = viewHolder.itemView.findViewById<ImageView>(R.id.action_image)
            actionImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_remove))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FavoriteAllAppsCard(mContext))
    }
}