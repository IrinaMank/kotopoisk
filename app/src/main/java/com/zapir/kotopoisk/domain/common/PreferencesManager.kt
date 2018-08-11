package com.zapir.kotopoisk.domain.common

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences


class PreferencesManager {

    private var mSharedPreferences: SharedPreferences? = null
    private val HINT_MAP = "Hint map"

    private var mName: String = ""

    fun setName(name: String): PreferencesManager {
        mName = name
        return this
    }

    fun init(mContext: Context) {
        if (mName.isEmpty()) {
            mName = "SharedPrefs"
        }

        mSharedPreferences = mContext.getSharedPreferences(mName, MODE_PRIVATE)
    }

    fun isMapHint(): Boolean {
        return if (getBoolean(HINT_MAP, true)) {
            putBoolean(HINT_MAP, false)
            true
        } else {
            false
        }
    }
    fun putString(key: String, value: String) {
        mSharedPreferences?.let {
            val editor = it.edit()
            editor.putString(key, value)
            editor.apply()
        }
    }

    fun getString(key: String, defValue: String = ""): String {
        return if (mSharedPreferences == null) {
            defValue
        } else mSharedPreferences!!.getString(key, defValue)
    }

    fun putStringSet(key: String, values: Set<String>) {
        mSharedPreferences?.let {
            val editor = it.edit()
            editor.putStringSet(key, values)
            editor.apply()
        }
    }

    fun getStringSet(key: String, defValues: Set<String> = emptySet()): Set<String> {
        return if (mSharedPreferences == null) {
            defValues
        } else mSharedPreferences!!.getStringSet(key, defValues)
    }

    fun putInt(key: String, value: Int) {
        mSharedPreferences?.let {
            val editor = it.edit()
            editor.putInt(key, value)
            editor.apply()
        }
    }

    fun getInt(key: String, defValue: Int = 0): Int {
        return if (mSharedPreferences == null) {
            defValue
        } else mSharedPreferences!!.getInt(key, defValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        mSharedPreferences?.let {
            val editor = it.edit()
            editor.putBoolean(key, value)
            editor.apply()
        }
    }

    fun getBoolean(key: String, defValue: Boolean = true): Boolean {
        return if (mSharedPreferences == null) {
            defValue
        } else mSharedPreferences!!.getBoolean(key, defValue)
    }


}