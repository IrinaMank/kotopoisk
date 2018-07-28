package com.example.zapir.kotopoisk.fragment.photo

import android.os.Parcel
import android.os.Parcelable

class PermissionModel() : Parcelable {

    lateinit var permissionName: String
    lateinit var title: String
    lateinit var explanationMessage: String
    lateinit var scope: String

    constructor(parcel: Parcel) : this() {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PermissionModel> {
        override fun createFromParcel(parcel: Parcel): PermissionModel {
            return PermissionModel(parcel)
        }

        override fun newArray(size: Int): Array<PermissionModel?> {
            return arrayOfNulls(size)
        }
    }

    fun clearScope(){
        scope = ""
    }

    fun scopeSetGranted(){
        scope = "granted"
    }

    fun scopeSetDenied(){
        scope = "denied"
    }

}