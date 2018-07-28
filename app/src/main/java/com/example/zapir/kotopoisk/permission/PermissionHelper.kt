package com.example.zapir.kotopoisk.permission

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.example.zapir.kotopoisk.activity.BaseActivity

class PermissionHelper(private val context: BaseActivity,
                       private val permissionCallback: OnPermissionCallback) {

    private val REQUEST_PERMISSIONS = 1

    fun isPermissionDeclined(permissionsName: String): Boolean {
        return ActivityCompat.checkSelfPermission(context, permissionsName) !=
                PackageManager.PERMISSION_GRANTED
    }

    fun isPermissionGranted(permissionsName: String): Boolean {
        return ActivityCompat.checkSelfPermission(context, permissionsName) ==
                PackageManager.PERMISSION_GRANTED
    }

    fun isExplanationNeeded(permissionName: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(context, permissionName)
    }

    fun requestAfterExplanation(permissionName: String) {
        if (isPermissionDeclined(permissionName)) {
            ActivityCompat.requestPermissions(context, arrayOf(permissionName), REQUEST_PERMISSIONS)
        } else {
            permissionCallback.onPermissionPreGranted(permissionName)
        }
    }

    /**
     * @return true if permission exists in the manifest, false otherwise.
     */
    fun permissionExists(permissionName: String): Boolean {
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
        if (permissionExists(permissionName)) {// android M throws exception when requesting
            // run time permission that does not exists in AndroidManifest.
            if (isPermissionDeclined(permissionName)) {
                showToast(context, "PermissionDeclined")
                if (isExplanationNeeded(permissionName)) {
                    permissionCallback.onPermissionNeedExplanation(permissionName)
                    showToast(context, "PermissionNeedExplanation")
                } else {
                    ActivityCompat.requestPermissions(context, arrayOf(permissionName), REQUEST_PERMISSIONS)
                    showToast(context, "PermissionPreGranted")
                }
            } else {
                permissionCallback.onPermissionPreGranted(permissionName)
                showToast(context, "PermissionPreGranted")
            }
        }
    }

    fun request(permissionName: String): PermissionHelper {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            handle(permissionName)
        } else {
            permissionCallback.onNoPermissionNeeded()
            showToast(context, "NoPermissionNeeded")
        }
        return this
    }

    private fun showToast(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

}