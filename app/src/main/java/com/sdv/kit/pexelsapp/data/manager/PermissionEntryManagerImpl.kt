package com.sdv.kit.pexelsapp.data.manager

import android.content.SharedPreferences
import com.sdv.kit.pexelsapp.domain.manager.PermissionEntryManager
import javax.inject.Inject

class PermissionEntryManagerImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : PermissionEntryManager {

    override fun savePermissionEntry(permission: String) {
        with(sharedPreferences.edit()) {
            putBoolean(permission, true)
            apply()
        }
    }

    override fun getPermissionEntry(permission: String): Boolean {
        return sharedPreferences.getBoolean(permission, DEFAULT_PERMISSION_ENTRY)
    }

    companion object {
        const val DEFAULT_PERMISSION_ENTRY = false
    }
}