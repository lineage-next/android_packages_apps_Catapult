/*
 * SPDX-FileCopyrightText: 2023 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.tv.launcher.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import org.lineageos.tv.launcher.R

/**
 * A class that checks main app permissions before starting the callback.
 */
class PermissionsGatedCallback private constructor(
    caller: ActivityResultCaller,
    private val getContext: () -> Context,
    private val getActivity: () -> Activity,
    private val callback: () -> Unit,
) {
    constructor(
        fragment: Fragment,
        callback: () -> Unit,
    ) : this(
        fragment,
        { fragment.requireContext() },
        { fragment.requireActivity() },
        callback,
    )

    constructor(
        activity: ComponentActivity,
        callback: () -> Unit,
    ) : this(
        activity,
        { activity },
        { activity },
        callback,
    )

    private val mainPermissionsRequestLauncher = caller.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it.isNotEmpty()) {
            if (!PermissionsUtils.mainPermissionsGranted(getContext())) {
                Toast.makeText(
                    getContext(), R.string.app_permissions_toast, Toast.LENGTH_SHORT
                ).show()
                getActivity().finish()
            } else {
                callback()
            }
        }
    }

    fun runAfterPermissionsCheck() {
        if (!PermissionsUtils.mainPermissionsGranted(getContext())) {
            mainPermissionsRequestLauncher.launch(PermissionsUtils.mainPermissions)
        } else {
            callback()
        }
    }
}
