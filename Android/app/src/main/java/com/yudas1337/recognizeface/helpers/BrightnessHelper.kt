package com.yudas1337.recognizeface.helpers

import android.content.Context
import android.provider.Settings
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class BrightnessHelper {

    companion object{
        // Check whether this app has android write settings permission.
        @RequiresApi(Build.VERSION_CODES.M)
        fun hasWriteSettingsPermission(context: Context): Boolean {
            var ret = true
            // Get the result from below code.
            ret = Settings.System.canWrite(context)
            return ret
        }

        // Start can modify system settings panel to let user change the write
        // settings permission.
        @RequiresApi(Build.VERSION_CODES.M)
        fun changeWriteSettingsPermission(context: Context) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            context.startActivity(intent)
        }

        // This function only take effect in real physical android device,
        // it can not take effect in android emulator.
        private fun changeScreenBrightness(
            context: Context,
            screenBrightnessValue: Int
        ) {   // Change the screen brightness change mode to manual.
            Settings.System.putInt(
                context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
            )
            // Apply the screen brightness value to the system, this will change
            // the value in Settings ---> Display ---> Brightness level.
            // It will also change the screen brightness for the device.
            Settings.System.putInt(
                context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, screenBrightnessValue
            )
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun checkBrightnessPermission(context: Context, brightnessValue: Int){
            if (!hasWriteSettingsPermission(context)) {
                changeWriteSettingsPermission(context)
            } else {
                BrightnessHelper.changeScreenBrightness(context, brightnessValue)
            }
        }
    }
}