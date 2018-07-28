package com.example.zapir.kotopoisk.permission

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import java.lang.ref.WeakReference


class PermissionsContext {

    var activityReference: WeakReference<Activity>? = null
    var fragmentReference: WeakReference<Fragment>? = null

    val context: Context?
        @RequiresApi(Build.VERSION_CODES.M)
        get() {
            if (fragmentReference != null) {
                return fragmentReference?.get()?.context
            } else if (activityReference != null) {
                return activityReference?.get()
            }
            return null
        }

    private constructor(activity: Activity) {
        activityReference = WeakReference(activity)
    }

    private constructor(fragment: Fragment) {
        fragmentReference = WeakReference(fragment)
    }
}