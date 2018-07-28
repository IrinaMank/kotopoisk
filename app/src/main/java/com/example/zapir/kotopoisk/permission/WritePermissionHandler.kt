package com.example.zapir.kotopoisk.permission

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v7.app.AlertDialog
import com.example.zapir.kotopoisk.activity.BaseActivity
import com.example.zapir.kotopoisk.photo.PhotoHandler

class WritePermissionHandler(private val context: BaseActivity,
                             private val photoHandler: PhotoHandler) : OnPermissionCallback {

    override fun onPermissionGranted(permissionName: String) {
        photoHandler.openCamera()
    }

    override fun onPermissionDeclined(permissionName: String) {}

    override fun onPermissionPreGranted(permissionName: String) {
        photoHandler.openCamera()
    }

    override fun onPermissionReallyDeclined(permissionName: String) {
        openSettingsScreen()
    }

    override fun onNoPermissionNeeded() {
        photoHandler.openCamera()
    }

    private lateinit var builder: AlertDialog
    private val permissionHelper = PermissionHelper(context, this)

    override fun onPermissionNeedExplanation(permissionName: String) {
        getAlertDialog(permissionName).show()
    }

    private fun getAlertDialog(permission: String): AlertDialog {
        builder = AlertDialog.Builder(context)
                .setTitle("Permission Needs Explanation")
                .create()
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "Request",
                DialogInterface.OnClickListener { dialog, which ->
                    permissionHelper.requestAfterExplanation(permission)
                })
        builder.setMessage("Permission need explanation ($permission)")
        return builder
    }

    private fun openSettingsScreen() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.parse("package:" + context.packageName)
        intent.data = uri
        context.startActivity(intent)
    }

    fun requestPermission(permissionName: String) {
        permissionHelper.request(permissionName)
    }

}