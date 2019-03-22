package wse18.ase.qse03.mobile.util

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import wse18.ase.qse03.mobile.ui.myDoctors.ChatFragment

object PermissionUtil {
    /**
     * Checks an array of permissions
     * @param permissions
     * @return
     */
    fun checkPermissions(context: Context, permissions: Array<String>): Boolean {
        Log.d(ChatFragment.TAG, "checkPermissionsArray: checking permissions array.")

        for (i in permissions.indices) {
            val check = permissions[i]
            if (!checkPermission(context, check)) {
                return false
            }
        }
        return true
    }

    /**
     * Checks a single permission whether it has been verified
     * @param permission
     * @return
     */
    fun checkPermission(context: Context, permission: String): Boolean {
        Log.d(ChatFragment.TAG, "checkPermissions: checking permission: $permission")

        val permissionRequest = ActivityCompat.checkSelfPermission(context, permission)

        return permissionRequest == PackageManager.PERMISSION_GRANTED
    }
}