package com.example.zapir.kotopoisk.fragment.photo

import android.app.Notification
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import com.example.zapir.kotopoisk.activity.BaseActivity
import android.os.Build
import android.provider.Settings
import android.support.annotation.RequiresApi
import com.example.zapir.kotopoisk.permission.PermissionsContext


class PermissionHandler(private val context: BaseActivity,
                        private val permissionsContext: PermissionsContext?) {

    private val REQUEST_PERMISSIONS = 1
    private val PERMISSION_SETTINGS = 2

    fun check(model: PermissionModel): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsContext?.context == null) {
                throw IllegalArgumentException("Provide permissions context")
            } else {
                checkPermissions(model)
                when (model.scope) {
                    "granted" -> return "granted"
                    "denied" -> {
                        showExplanation(model)
                        return "denied"
                    }
                    "banned" -> {
                        context.startActivityForResult(Intent(getSettingsIntent(context.packageName)),
                                PERMISSION_SETTINGS)
                        return "banned"
                    }
                }
            }
        }
        return ""
    }


    fun showExplanation(model: PermissionModel) {
        if (isPermissionDenied(model.permissionName)) {
            AlertDialog.Builder(context)
                    .setTitle(model.title)
                    .setMessage(model.explanationMessage)
                    .setPositiveButton("Request", { dialog, which ->
                        requestAfterExplanation(model.permissionName)
                    }).show()
        }
    }

    fun isPermissionGranted(permissionsName: String): Boolean {
        return ActivityCompat.checkSelfPermission(context, permissionsName) == PackageManager.PERMISSION_GRANTED
    }

    fun isPermissionDenied(permissionsName: String): Boolean {
        return ActivityCompat.checkSelfPermission(context, permissionsName) == PackageManager.PERMISSION_DENIED
    }

    fun requestAfterExplanation(permissionName: String) {
        ActivityCompat.requestPermissions(context, arrayOf(permissionName), REQUEST_PERMISSIONS)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermissions(model: PermissionModel) {
        model.clearScope()
        val permission = model.permissionName
        if (isPermissionGranted(permission)) {
            model.scope = "granted"
        } else if (isPermissionDenied(permission)) {
            if (shouldShowRationale(permission)) {
                model.scope = "denied"
            } else {
                model.scope = "banned"
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun shouldShowRationale(permission: String): Boolean {
        if (permissionsContext == null) {
            throw IllegalArgumentException("Provide permissions context")
        } else {
            if (permissionsContext.fragmentReference != null) {
                return permissionsContext.fragmentReference?.get()?.shouldShowRequestPermissionRationale(permission)
                        ?: throw Exception("null fragment received in permissionsContext of PermissionHandler")
            } else if (permissionsContext.activityReference != null) {
                return ActivityCompat.shouldShowRequestPermissionRationale(
                        permissionsContext.activityReference?.get()
                                ?: throw Exception("null activity received in permissionsContext of PermissionHandler"),
                        permission)
            }
        }
        return false
    }

    private fun getSettingsIntent(appPackage: String): Intent {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:$appPackage")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        return intent
    }

}