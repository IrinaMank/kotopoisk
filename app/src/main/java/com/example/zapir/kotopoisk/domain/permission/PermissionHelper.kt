package com.example.zapir.kotopoisk.domain.permission

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.example.zapir.kotopoisk.KotopoiskApplication
import com.example.zapir.kotopoisk.ui.base.BaseActivity

class PermissionHelper(private val context: BaseActivity,
                       private val permissionCallback: OnPermissionCallback) {

    private val REQUEST_PERMISSIONS = 1

    private fun isPermissionDeclined(permissionsName: String): Boolean {
        return ActivityCompat.checkSelfPermission(context, permissionsName) !=
                PackageManager.PERMISSION_GRANTED
    }

    private fun isPermissionGranted(permissionsName: String): Boolean {
        return ActivityCompat.checkSelfPermission(context, permissionsName) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun isPermissionBanned(permissionsName: String): Boolean {
        return ((ActivityCompat.checkSelfPermission(context, permissionsName) ==
                PackageManager.PERMISSION_DENIED) && (!isExplanationNeeded(permissionsName)))
    }

    private fun isExplanationNeeded(permissionName: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(context, permissionName)
    }

    private fun verifyPermissions(result: Int): Boolean {
        if (result != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true
    }

    fun requestAfterExplanation(permissionName: String) {
        if (isPermissionDeclined(permissionName)) {
            ActivityCompat.requestPermissions(context, arrayOf(permissionName), REQUEST_PERMISSIONS)
        } else {
            permissionCallback.onPermissionPreGranted(permissionName)
        }
    }

    private fun permissionExists(permissionName: String): Boolean {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName,
                    PackageManager.GET_PERMISSIONS)
            if (packageInfo.requestedPermissions != null) {
                for (p in packageInfo.requestedPermissions) {
                    if (p == permissionName) {
                        return true
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    private fun handle(permissionName: String) {
        val prefKeyFirstAskingPermission = "pref_first_perm_ask"
        if (permissionExists(permissionName)) {
            if (KotopoiskApplication.preferencesManager().getBoolean(prefKeyFirstAskingPermission)) {
                KotopoiskApplication.preferencesManager().putBoolean(prefKeyFirstAskingPermission,
                        false)
                ActivityCompat.requestPermissions(context, arrayOf(permissionName), REQUEST_PERMISSIONS)
            } else if (isPermissionBanned(permissionName)) {
                permissionCallback.onPermissionReallyDeclined(permissionName)
            } else if (isPermissionDeclined(permissionName)) {
                if (isExplanationNeeded(permissionName)) {
                    permissionCallback.onPermissionNeedExplanation(permissionName)
                } else {
                    ActivityCompat.requestPermissions(context, arrayOf(permissionName), REQUEST_PERMISSIONS)
                }
            } else {
                permissionCallback.onPermissionPreGranted(permissionName)
            }
        }
    }

    fun request(permissionName: String): PermissionHelper {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            handle(permissionName)
        } else {
            permissionCallback.onNoPermissionNeeded()
        }
        return this
    }

    fun onRequestPermissionsResult(requestCode: Int, permissionName: String, result: Int) {
        if (requestCode == REQUEST_PERMISSIONS) {
            if (verifyPermissions(result)) {
                permissionCallback.onPermissionGranted(permissionName)
            } else {
                if (!isExplanationNeeded(permissionName)) {
                    permissionCallback.onPermissionReallyDeclined(permissionName)
                }
            }
        }
    }

    private fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}