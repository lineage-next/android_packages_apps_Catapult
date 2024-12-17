/*
 * SPDX-FileCopyrightText: 2024 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.tv.launcher.utils

import android.content.Context

import org.lineageos.internal.tv.TvAdbNetworkManager;

class DeveloperOptions(val context: Context) {

    private val tvAdbNetworkManager: TvAdbNetworkManager

    init {
        tvAdbNetworkManager = TvAdbNetworkManager(context)
    }

    fun adbOverNetworkEnabled(): Boolean {
        return tvAdbNetworkManager.getEnabled()
    }

    fun toggleAdbOverNetwork() {
        tvAdbNetworkManager.setEnabled(!adbOverNetworkEnabled())
    }
}