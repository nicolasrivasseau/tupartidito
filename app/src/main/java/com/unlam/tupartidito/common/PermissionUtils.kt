package com.unlam.tupartidito.common

import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

@RequiresApi(Build.VERSION_CODES.M)
fun AppCompatActivity.checkAndLaunch(permission: String, notGranted: Int, exec: () -> Unit): ActivityResultLauncher<String> {
    return registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        when {
            isGranted -> exec()
            shouldShowRequestPermissionRationale(permission) -> toast(notGranted)
            else -> toast(notGranted)
        }
    }
}
