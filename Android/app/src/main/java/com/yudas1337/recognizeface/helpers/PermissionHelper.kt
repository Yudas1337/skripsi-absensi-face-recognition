package com.yudas1337.recognizeface.helpers

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi

class PermissionHelper {

    companion object{
         fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (permission in permissions) {
                    if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                        return false
                    }
                }
            }
            return true
        }

        @TargetApi(Build.VERSION_CODES.M)
         fun requestPermission(context: Context, permissions: Array<String>, permissionReqCode: Int){
            val activity = context as Activity
            activity.requestPermissions(
                permissions,
                permissionReqCode
            )
        }

        @RequiresApi(Build.VERSION_CODES.R)
        fun requestAccessFiles(context: Context){
            AlertHelper.permissionDialog(context, "Perizinan Dibutuhkan",
                "Izinkan akses untuk mengelola file", "Izinkan",
                confirmClickListener = {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                context.startActivity(intent)
            })
        }
    }

}