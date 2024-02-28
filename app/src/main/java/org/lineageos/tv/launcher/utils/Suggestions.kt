package org.lineageos.tv.launcher.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.tvprovider.media.tv.PreviewChannel
import androidx.tvprovider.media.tv.PreviewProgram
import androidx.tvprovider.media.tv.TvContractCompat
import androidx.tvprovider.media.tv.WatchNextProgram

@SuppressLint("RestrictedApi")
object Suggestions {
    private const val TAG = "TvLauncher.Suggestions"

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
            try {
                watchNextList.add(WatchNextProgram.fromCursor(cursor))
            } catch (e: Exception) {
                Log.w(TAG, "Ignoring watch next: $e")
            }
        }

        cursor.close()
        return watchNextList
    }

    fun getPreviewChannels(context: Context): List<PreviewChannel> {
        val cursor = context.contentResolver.query(
            TvContractCompat.Channels.CONTENT_URI,
            PreviewChannel.Columns.PROJECTION,
            null,
            null,
            null
        ) ?: return ArrayList()

        val previewChannelList = ArrayList<PreviewChannel>()

        if (!cursor.moveToFirst()) {
            return ArrayList()
        }

        while (cursor.moveToNext()) {
            try {
                if (!cursor.getString(PreviewChannel.Columns.COL_APP_LINK_INTENT_URI)
                        .isNullOrEmpty()
                    && !cursor.getString(PreviewChannel.Columns.COL_DISPLAY_NAME).isNullOrEmpty()
                ) {
                    previewChannelList.add(PreviewChannel.fromCursor(cursor))
                }
            } catch (e: Exception) {
                Log.w(TAG, "Ignoring preview channel: $e")
            }
        }

        cursor.close()
        return previewChannelList
    }

    fun getSuggestion(context: Context, id: Long): ArrayList<PreviewProgram> {
        val cursor = context.contentResolver.query(
            TvContractCompat.buildPreviewProgramsUriForChannel(id),
            PreviewProgram.PROJECTION,
            null,
            arrayOf("LIMIT 5"),
            null,
        ) ?: return ArrayList()

        val previewProgramList = ArrayList<PreviewProgram>()

        if (!cursor.moveToFirst()) {
            return ArrayList()
        }

        while (cursor.moveToNext()) {
            try {
                previewProgramList.add(PreviewProgram.fromCursor(cursor))
            } catch (e: Exception) {
                Log.w(TAG, "Ignoring preview program: $e")
            }
        }

        cursor.close()
        return previewProgramList
    }
}