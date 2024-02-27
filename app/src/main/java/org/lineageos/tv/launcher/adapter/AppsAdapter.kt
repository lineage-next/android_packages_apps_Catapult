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
import org.lineageos.tv.launcher.utils.AppManager
import org.lineageos.tv.launcher.view.AppCard
import org.lineageos.tv.launcher.view.Card
import org.lineageos.tv.launcher.view.FavoriteCard

open class AppsAdapter(protected val mContext: Context) :
    RecyclerView.Adapter<AppsAdapter.ViewHolder>() {

    protected val mAppsList by lazy { getaAppsList() }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener, View.OnKeyListener {

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
            itemView.setOnKeyListener(this)
        }

        override fun onClick(v: View) {
            handleClick(v as Card)
        }

        override fun onLongClick(v: View): Boolean {
            return handleLongClick(v as Card)
        }

        override fun onKey(v: View, keyCode: Int, keyEvent: KeyEvent): Boolean {
            return handleKey(v, keyCode, keyEvent)
        }
    }

    protected open fun handleKey(v: View, keyCode: Int, keyEvent: KeyEvent): Boolean {
        return false
    }

    protected open fun handleClick(app: Card) {
        val context = app.context
        context.startActivity(app.mLaunchIntent)
        Toast.makeText(context, app.mLabel, Toast.LENGTH_SHORT).show()
    }

    protected open fun handleLongClick(app: Card): Boolean {
        showPopupMenu(app, R.menu.app_long_press)
        return true
    }

    protected open fun getaAppsList(): ArrayList<Launchable> {
        return AppManager.getInstalledApps(mContext)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        (viewHolder.itemView as AppCard).setCardInfo(mAppsList[i])
    }

    override fun getItemCount(): Int {
        return mAppsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AppCard(mContext))
    }

    protected fun showPopupMenu(anchorView: View, menuResId: Int) {
        val popupMenu = PopupMenu(mContext, anchorView)
        popupMenu.menuInflater.inflate(menuResId, popupMenu.menu)
        popupMenu.setForceShowIcon(true)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_uninstall -> {
                    AppManager.uninstallApp(mContext, (anchorView as AppCard).mPackageName)
                    true
                }

                R.id.menu_mark_as_favorite -> {
                    AppManager.addFavoriteApp(mContext, (anchorView as AppCard).mPackageName)
                    true
                }

                R.id.menu_remove_favorite -> {
                    AppManager.removeFavoriteApp(mContext, (anchorView as AppCard).mPackageName)
                    true
                }

                R.id.menu_move -> {
                    if (anchorView is FavoriteCard) {
                        anchorView.setMoving()
                    }
                    true
                }

                else -> false
            }
        }

        popupMenu.show()
    }
}