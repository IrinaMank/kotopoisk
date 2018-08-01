package com.example.zapir.kotopoisk.domain.photo

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import com.example.zapir.kotopoisk.R
import com.example.zapir.kotopoisk.domain.permission.WritePermissionHandler
import com.example.zapir.kotopoisk.ui.main.MainActivity

class PhotoDialog : DialogFragment() {

    private val items: Array<String> by lazy {
        arrayOf(getString(R.string.dialog1), getString(R.string.dialog2))
    }
    private val mainContext by lazy {
        activity as? MainActivity
                ?: throw Exception("PhotoDialog has no mainContext")
    }
    private val photoHandler by lazy { PhotoHandler(mainContext) }
    private val writePermissionHandler by lazy {
        WritePermissionHandler(mainContext,
                photoHandler, getString(R.string.explanation_dialog_message),
                getString(R.string.really_explanation_dialog_message),
                getString(R.string.explanation_dialog_title))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(mainContext)
        builder.setTitle(R.string.dialog_title)
                .setItems(items, { dialog, which ->
                    when (which) {
                        0 -> photoHandler.openGallery()
                        1 -> writePermissionHandler.requestPermission(
                                "android.permission.WRITE_EXTERNAL_STORAGE")
                    }
                })
        return builder.create()
    }

}
