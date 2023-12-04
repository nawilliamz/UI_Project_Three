package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.udacity.*
import com.udacity.Util.*
import com.udacity.databinding.ActivityMainBinding


class DownloadReceiver() : BroadcastReceiver() {

    private lateinit var binding: ActivityMainBinding


    //Context here is the context in which the Receiver is running
    override fun onReceive(context: Context?, intent: Intent?) {


        //Is there a means in DownloadManager API for determining which URL a given download is
        //associated with? So, we can match that URL with the URL where the most recent download
        //request was sent.
        //Yes????, you can customer the intent action for each download???? So make a separate receiver for
        //each download file type (i.e. android.intent.action.GLIDE_DOWNLOAD_COMPLETE...). You will
        //need to register each of these actions in the Manifest.
        //Looks like you can use the constant COLUMN_URI to catch the URI target of the download. This is
        //accessed as DownloadManager.COLUMN_URI

        val query = DownloadManager.Query().setFilterByStatus(DownloadManager.STATUS_FAILED)



        //Pull the action from the intent returned from the download
        if (intent?.action == "android.intent.action.DOWNLOAD_COMPLETE") {

            //pull the id of the download from the intent returned from the download
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L)


            if (id == FileDownloader.downloadID) {

                //Set download progress indicator so MainActivity knows whether downlaod was SUCCESS or FAIL
                resultStatus = ResultStatus.SUCCESS

                loadingState.value = LoadingStatus.DONE

                //Reset downloadID to zero
                FileDownloader.downloadID = 0

                //Step3: Trigger notification
                //context is the context passed into DownloadReceiver()
                Log.i("DownloadReceiver", "MARKER SEND_NOTIFICATION")
                val notificationManager = context?.let {
                    ContextCompat.getSystemService(
                        it,
                        NotificationManager::class.java
                    )
                } as NotificationManager

                notificationManager.sendNotification(
                    context.getText(R.string.notification_description).toString(),
                    context
                )


            } else {



                //Set download progress indicator so MainActivity knows whether downlaod was SUCCESS or FAIL
                resultStatus = ResultStatus.FAIL


                //Step2: Navigate from MainActivity to Details screen with fileName String and success/failure String
                //May need to use global variable in Events file to do this from MainActivity.By setting loadingStatus
                //to Done, I will trigger the intent to navigate from MainActivity to DetailActivity in MainActivity in
                //processAnimation_Glide()
                loadingState.value = LoadingStatus.DONE

                //Reset downloadID to zero
                FileDownloader.downloadID = 0


                //Step3: Trigger notification
                Log.i("DownloadReceived", "MARKER SEND_NOTIFICATION")
                val notificationManager = context?.let {
                    ContextCompat.getSystemService(
                        it,
                        NotificationManager::class.java
                    )
                } as NotificationManager

                notificationManager.sendNotification(
                    context.getText(R.string.notification_description).toString(),
                    context
                )

            }
        } else  {


            //Set download progress indicator so MainActivity knows whether downlaod was SUCCESS or FAIL
            resultStatus = ResultStatus.FAIL


            //Step3: Navigate from MainActivity to Details screen with fileName String and success/failure String
            //May need to use global variable in Events file to do this from MainActivity.By setting loadingStatus
            //to Done, I will trigger the intent to navigate from MainActivity to DetailActivity in MainActivity in
            //processAnimation_Glide()
            loadingState.value = LoadingStatus.DONE

            //Reset downloadID to zero
            FileDownloader.downloadID = 0
        }
    }
}