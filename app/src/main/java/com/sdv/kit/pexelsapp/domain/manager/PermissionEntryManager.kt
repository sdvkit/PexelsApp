package com.sdv.kit.pexelsapp.domain.manager

interface PermissionEntryManager {
    fun savePermissionEntry(permission: String)
    fun getPermissionEntry(permission: String): Boolean
}