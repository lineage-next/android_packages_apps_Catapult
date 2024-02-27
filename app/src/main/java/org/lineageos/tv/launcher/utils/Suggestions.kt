package org.lineageos.tv.launcher.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.tvprovider.media.tv.TvContractCompat
import androidx.tvprovider.media.tv.WatchNextProgram

object Suggestions {
    @SuppressLint("RestrictedApi")
    fun getWatchNextPrograms(context: Context): List<WatchNextProgram> {
        val cursor = context.contentResolver.query(
            TvContractCompat.WatchNextPrograms.CONTENT_URI,
            WatchNextProgram.PROJECTION,
            null,
            null,
            null
        ) ?: return ArrayList()

        val watchNextList = ArrayList<WatchNextProgram>()

        if (!cursor.moveToFirst()) {
            return ArrayList()
        }

        while (cursor.moveToNext()) {
            watchNextList.add(WatchNextProgram.fromCursor(cursor))
        }

        cursor.close()
        return watchNextList
    }
}