package com.example.zapir.kotopoisk.domain.permission

interface OnPermissionCallback {
    fun onPermissionGranted(permissionName: String)
    fun onPermissionDeclined(permissionName: String)
    fun onPermissionPreGranted(permissionName: String)
    fun onPermissionNeedExplanation(permissionName: String)
    fun onPermissionReallyDeclined(permissionName: String)
    fun onNoPermissionNeeded()
}