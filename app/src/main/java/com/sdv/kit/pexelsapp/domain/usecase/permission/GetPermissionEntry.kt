package com.sdv.kit.pexelsapp.domain.usecase.permission

import com.sdv.kit.pexelsapp.domain.manager.PermissionEntryManager
import javax.inject.Inject

class GetPermissionEntry @Inject constructor(
    private val permissionEntryManager: PermissionEntryManager
) {

    operator fun invoke(permission: String): Boolean {
        return permissionEntryManager.getPermissionEntry(permission = permission)
    }
}