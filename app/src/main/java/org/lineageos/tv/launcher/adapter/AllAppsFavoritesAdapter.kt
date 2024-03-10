/*
 * SPDX-FileCopyrightText: 2024 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.tv.launcher.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.lineageos.tv.launcher.model.AppInfo
import org.lineageos.tv.launcher.view.AddFavoriteCard

class AllAppsFavoritesAdapter :
    ListAdapter<Pair<AppInfo, Boolean>, AllAppsFavoritesAdapter.ViewHolder>(diffCallback) {
    var onFavoriteChanged: (packageName: String, favorite: Boolean) -> Unit = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        AddFavoriteCard(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val card: AddFavoriteCard) : RecyclerView.ViewHolder(card) {
        init {
            card.apply {
                setOnClickListener {
                    val packageName = card.packageName

                    currentList.find {
                        it.first.packageName == card.packageName
                    }?.let {
                        onFavoriteChanged(packageName, !it.second)
                    }
                }
            }
        }

        fun bind(item: Pair<AppInfo, Boolean>) {
            card.setCardInfo(item.first)
            card.setActionToggle(item.second)
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Pair<AppInfo, Boolean>>() {
            override fun areItemsTheSame(
                oldItem: Pair<AppInfo, Boolean>,
                newItem: Pair<AppInfo, Boolean>
            ) = oldItem.first.packageName == newItem.first.packageName

            override fun areContentsTheSame(
                oldItem: Pair<AppInfo, Boolean>,
                newItem: Pair<AppInfo, Boolean>
            ) = compareValuesBy(
                oldItem, newItem,
                { it.first.label },
                { it.first.icon.hashCode() },
                { it.second },
            ) == 0
        }
    }
}
