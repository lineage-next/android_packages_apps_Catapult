package org.lineageos.tv.launcher.utils

import android.annotation.SuppressLint
import android.content.Context
import android.media.tv.TvContract
import android.util.Log
import androidx.tvprovider.media.tv.PreviewChannel
import androidx.tvprovider.media.tv.PreviewProgram
import androidx.tvprovider.media.tv.TvContractCompat
import androidx.tvprovider.media.tv.WatchNextProgram

@SuppressLint("RestrictedApi")
object Suggestions {
    private const val TAG = "TvLauncher.Suggestions"
    private const val PACKAGE_FRAMEWORK_STUBS = "com.android.tv.frameworkpackagestubs"
    private var mChannelChangeListener: OnChannelChangeListener? = null

    fun setChannelListener(listener: OnChannelChangeListener) {
        mChannelChangeListener = listener
    }

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
                val watchNextProgram = WatchNextProgram.fromCursor(cursor)
                val resolvedActivity = watchNextProgram.intent.resolveActivity(context.packageManager)
                if (resolvedActivity == null ||
                    resolvedActivity.packageName == PACKAGE_FRAMEWORK_STUBS) {
                    // This can't be opened with any app
                    continue
                }
                watchNextList.add(watchNextProgram)
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

    fun getSuggestions(context: Context, id: Long): ArrayList<PreviewProgram> {
        val cursor = context.contentResolver.query(
            TvContractCompat.buildPreviewProgramsUriForChannel(id),
            PreviewProgram.PROJECTION,
            null,
            null,
            null,
        ) ?: return ArrayList()

        val previewProgramList = ArrayList<PreviewProgram>()

        if (!cursor.moveToFirst()) {
            return ArrayList()
        }

        while (cursor.moveToNext()) {
            try {
                val previewProgram = PreviewProgram.fromCursor(cursor)
                val resolvedActivity = previewProgram.intent.resolveActivity(context.packageManager)
                if (resolvedActivity == null ||
                    resolvedActivity.packageName == PACKAGE_FRAMEWORK_STUBS) {
                    // This can't be opened with any app
                    continue
                }
                previewProgramList.add(previewProgram)
            } catch (e: Exception) {
                Log.w(TAG, "Ignoring preview program: $e")
            }
        }

        cursor.close()
        return previewProgramList
    }

    fun setHiddenChannels(context: Context, hiddenChannels: ArrayList<Long>) {
        val sharedPreferences = context.getSharedPreferences("Channels", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val serializedList = hiddenChannels.joinToString(",")
        editor.putString("hiddenChannels", serializedList)
        editor.apply()
    }

    fun getHiddenChannels(context: Context): ArrayList<Long> {
        val sharedPreferences =
            context.getSharedPreferences("Channels", Context.MODE_PRIVATE)
        val serializedList = sharedPreferences.getString("hiddenChannels", "") ?: ""
        if (serializedList == "") {
            return ArrayList()
        }
        return ArrayList(serializedList.split(",").map { it.toLong() })
    }

    fun hideChannel(context: Context, channelId: Long?) {
        channelId ?: return
        val hiddenChannels = getHiddenChannels(context)
        hiddenChannels.add(channelId)
        setHiddenChannels(context, hiddenChannels)

        // Notify
        mChannelChangeListener?.onChannelHidden(channelId)
    }

    fun showChannel(context: Context, channelId: Long?) {
        channelId ?: return
        val hiddenChannels = getHiddenChannels(context)
        hiddenChannels.remove(channelId)
        setHiddenChannels(context, hiddenChannels)

        // Notify
        mChannelChangeListener?.onChannelShown(channelId)
    }

    fun aspectRatioToFloat(aspectRatio: Int): Float {
        return when (aspectRatio) {
            TvContract.PreviewPrograms.ASPECT_RATIO_16_9 -> 16f / 9f
            TvContract.PreviewPrograms.ASPECT_RATIO_4_3 -> 4f / 3f
            TvContract.PreviewPrograms.ASPECT_RATIO_1_1 -> 1f
            TvContract.PreviewPrograms.ASPECT_RATIO_3_2 -> 3f / 2f
            TvContract.PreviewPrograms.ASPECT_RATIO_2_3 -> 2f / 3f
            else -> -1f
        }
    }

    interface OnChannelChangeListener {
        fun onChannelHidden(channelId: Long)
        fun onChannelShown(channelId: Long)
    }
}