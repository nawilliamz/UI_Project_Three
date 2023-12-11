package com.udacity

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment

class DownloadPermissionDialog(permission:String, requestCode:Int) :DialogFragment() {

//    private val download: () -> Unit = downloadFile

    val requestPermission = permission
    val requestPermCode = requestCode

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)



            val listener: DialogInterface.OnClickListener

            builder.setTitle(resources.getString(R.string.storage_approval_dialog_title))
            builder.setMessage(resources.getString(R.string.storage_approval_dialog_message))
            //the dialog and which variables are recognized by Kotlin native SDK. They are not initialized
            //in my files. The listener functionality normally familiar to you is hidden by the Kotlin SDK. You
            //don't need to worry about that here.
            builder.setPositiveButton(R.string.approval_text) { dialog, which ->

                Log.i("DownloadPermissionsDialog", "Marker: Dialog1")
                //Place line of code initiating the download here
//                kotlin.run { download }
//                download()
                //In the positive button, you need to request permission from the User. You do not want to
                //initiate download here because you don't have write-to permission yet
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(requestPermission), requestPermCode )

                Log.i("DownloadPermissionsDialog", "Marker: Dialog2")

            }

            builder.setNegativeButton(R.string.cancel_text) {dialog, which ->
                cancelDialog()
            }
            builder.create()
        }?:throw IllegalStateException("Activity cannot be null")
    }

    private fun cancelDialog () {
        this.cancelDialog()
    }
}