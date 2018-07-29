package com.example.zapir.kotopoisk.permission

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v7.app.AlertDialog
import com.example.zapir.kotopoisk.activity.BaseActivity
import com.example.zapir.kotopoisk.photo.PhotoHandler
import com.example.zapir.kotopoisk.R

class WritePermissionHandler(private val context: BaseActivity,
                             private val photoHandler: PhotoHandler,
                             private val explanation: String,
                             private val reallyExplanation: String,
                             private val title: String) : OnPermissionCallback {

    private lateinit var builder: AlertDialog
    private val permissionHelper = PermissionHelper(context, this)

    override fun onPermissionGranted(permissionName: String) {
        photoHandler.openCamera()
    }

    override fun onPermissionDeclined(permissionName: String) {}

    override fun onPermissionPreGranted(permissionName: String) {
        photoHandler.openCamera()
    }

    override fun onPermissionReallyDeclined(permissionName: String) {
        getReallyNeedAlertDialog(permissionName).show()
    }

    override fun onNoPermissionNeeded() {
        photoHandler.openCamera()
    }

    override fun onPermissionNeedExplanation(permissionName: String) {
        getNeedExceptionAlertDialog(permissionName).show()
    }

    private fun getNeedExceptionAlertDialog(permissionName: String): AlertDialog {
        builder = AlertDialog.Builder(context)
                .setTitle(title)
                .create()
        builder.setButton(DialogInterface.BUTTON_POSITIVE,
                context.getString(R.string.dialog_positive_button),
                { _, _ ->
                    permissionHelper.requestAfterExplanation(permissionName)
                })
        builder.setMessage(explanation)
        return builder
    }

    private fun getReallyNeedAlertDialog(permissionName: String): AlertDialog {
        builder = AlertDialog.Builder(context)
                .setTitle(title)
                .create()
        builder.setButton(DialogInterface.BUTTON_POSITIVE,
                context.getString(R.string.dialog_positive_button),
                { _, _ ->
                    openSettingsScreen()
                })
        builder.setMessage(reallyExplanation)
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