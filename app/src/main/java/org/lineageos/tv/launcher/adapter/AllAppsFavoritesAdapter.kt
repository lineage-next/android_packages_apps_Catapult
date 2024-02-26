package org.lineageos.tv.launcher.adapter

import android.content.Context
import android.view.ViewGroup
import org.lineageos.tv.launcher.utils.AppManager
import org.lineageos.tv.launcher.view.Card
import org.lineageos.tv.launcher.view.FavoriteAllAppsCard

class AllAppsFavoritesAdapter(context: Context) : AppsAdapter(context) {
    private var mFavoritePackageNames = ArrayList<String>()

    init {
        mFavoritePackageNames = AppManager.getFavoriteApps(mContext)
    }

    override fun handleClick(app: Card) {
        app as FavoriteAllAppsCard
        if (mFavoritePackageNames.contains(app.mPackageName)) {
            AppManager.removeFavoriteApp(mContext, app.mPackageName)
            app.setActionAdd()
        } else {
            AppManager.addFavoriteApp(mContext, app.mPackageName)
            app.setActionRemove()
        }

        mFavoritePackageNames = AppManager.getFavoriteApps(mContext)
    }


    override fun onBindViewHolder(viewHolder: AppsAdapter.ViewHolder, i: Int) {
        val card = viewHolder.itemView as FavoriteAllAppsCard
        card.setAppInfo(mAppsList[i])

        if (mFavoritePackageNames.contains(mAppsList[i].mPackageName)) {
            card.setActionRemove()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FavoriteAllAppsCard(mContext))
    }
}