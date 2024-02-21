package com.yudas1337.recognizeface.database

import android.content.SharedPreferences
import com.yudas1337.recognizeface.constants.ConstShared
import com.yudas1337.recognizeface.recognize.LoadFace

class SharedPref {

    companion object{
        fun putString(sharedPreferences: SharedPreferences, key: String, value: String) {
            sharedPreferences.edit().putString(key, value).apply()
        }

        fun putInt(sharedPreferences: SharedPreferences, key: String, value: Int) {
            sharedPreferences.edit().putInt(key, value).apply()
        }

        fun putBoolean(sharedPreferences: SharedPreferences, key: String, value: Boolean) {
            sharedPreferences.edit().putBoolean(key,value).apply()
        }

        fun getString(sharedPreferences: SharedPreferences?, key: String, defaultValue: String = ""): String {
            if (sharedPreferences != null) {
                return sharedPreferences.getString(key, defaultValue) ?: defaultValue
            }

            return defaultValue
        }

        fun getInt(sharedPreferences: SharedPreferences?, key: String, defaultValue: Int = 0): Int {
            if (sharedPreferences != null) {
                return sharedPreferences.getInt(key, defaultValue)
            }

            return defaultValue
        }

        fun isSerializedDataStored(sharedPreferences: SharedPreferences, key: String, defaultValue: Boolean = false): Boolean {
            return sharedPreferences.getBoolean(key , defaultValue)
        }

        fun clearData(sharedPreferences: SharedPreferences, key: String){
            sharedPreferences.edit().remove(key).apply()
        }
    }
}