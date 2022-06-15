package com.example.lib_base.helper.`fun`.data

import android.graphics.drawable.Drawable

data class AppData(
    val packName: String,
    val appName: String,
    val appIcon: Drawable,
    val firstRunName: String,
    val isSystemApp: Boolean
)

//联系人
data class ContactData(
    val phoneName: String,
    val phoneNumber: String
)