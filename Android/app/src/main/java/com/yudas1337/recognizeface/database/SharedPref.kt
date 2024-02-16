package com.yudas1337.recognizeface.database

import android.content.SharedPreferences

class SharedPref {

    companion object{
        fun putString(sharedPreferences: SharedPreferences, key: String, value: String) {
            sharedPreferences.edit().putString(key, value).apply()
        }

        fun putInt(sharedPreferences: SharedPreferences, key: String, value: Int) {
            sharedPreferences.edit().putInt(key, value).apply()
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
    }
}