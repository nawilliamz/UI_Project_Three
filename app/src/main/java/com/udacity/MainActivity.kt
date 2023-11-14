package com.udacity

import android.animation.ValueAnimator
import android.app.Activity
import android.app.Application
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RadioGroup
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.udacity.Util.*
import com.udacity.databinding.ActivityDetailBinding
import com.udacity.databinding.ActivityMainBinding
import kotlinx.coroutines.*



//lateinit var loadingStatus:Events
lateinit var loadingStatus:LoadingStatus
lateinit var resultStatus:ResultStatus

lateinit var loadingState:LoadingState<LoadingStatus>


lateinit var downloadIntent:Intent

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private lateinit var fileName:String
    private lateinit var fileDownloadStatus:String

    private val mainContext: Context = this
    private lateinit var valueAnimator: ValueAnimator


    init {
        loadingStatus = LoadingStatus.LOADING
        resultStatus = ResultStatus.NEUTRAL


        loadingState = States.StateObj
        States.StateObj.value = LoadingStatus.LOADING


//        fileName = ""
//        fileDownloadStatus = ""



//        loadingState.value = LoadingStatus.LOADING
    }



    //***********************Couroutines_NoFileSelected***********************************
    private var animationProcessingJob_NoFileSelected = Job()

    private val animationProcessingScope_NoFileSelected = CoroutineScope(Dispatchers.Main + animationProcessingJob_NoFileSelected)


    private fun processAnimation_NoFileSelected(){

        animationProcessingScope_NoFileSelected.launch {

            //Set the color of DOWNLOAD text to same as background color
            binding.downloadButton.textPaint.color = binding.downloadButton.buttonPrimaryColor
            binding.downloadButton.invalidate()

            val leftPosition = binding.downloadButton.x
            val rightPosition = binding.downloadButton.x + binding.downloadButton.width

            binding.animatedDownloadButton.showAnimatedDownloadButton(leftPosition, rightPosition)

            binding.selectFileButton.showSelectFileButton()

            delay(5000)
            binding.animatedDownloadButton.isGone = true
            binding.selectFileButton.isGone = true

            //Re-set the color of DOWNLOAD text to white
            binding.downloadButton.textPaint.color = Color.WHITE
            binding.downloadButton.invalidate()
        }
    }
    //*************************************************************************************

    //******************************Coroutines_Glide*****************************************

    private var animationProcessingJob_GlideSelected = Job()

    private val animationProcessingScope_GlideSelected = CoroutineScope(Dispatchers.Main + animationProcessingJob_GlideSelected)

    private fun processAnimation_Glide() {
        animationProcessingScope_GlideSelected.launch {




            //Set the color of DOWNLOAD text to same as background color
            binding.downloadButton.textPaint.color = binding.downloadButton.buttonPrimaryColor
            binding.downloadButton.invalidate()
            Log.i("MainActivity", "DOWNLOAD text changed to buttonPrimaryColory")

            val leftPosition = binding.downloadButton.x
            val rightPosition = binding.downloadButton.x + binding.downloadButton.width

            binding.animatedDownloadButton.showAnimatedDownloadButton(leftPosition, rightPosition)

            binding.progressCircle.showAnimatedCircle()

//            Start download with code by referencing DownloadClass
            //This line of code needs to be on separate coroutine using Dispaters.IO

            withContext(Dispatchers.IO) {
                Log.i("MainActivity", "withContext block has started")
//                GlideDownloader.downloadFile(Constants.GLIDE_URL, context)

                FileDownloader(mainContext).downloadFile(Constants.GLIDE_URL, mainContext)
                Log.i("MainActivity", "Glide_URL is ${Constants.GLIDE_URL}")


            }


            //With this code and looking at DownloadReceived.kt, the notification will always be sent BEFORE
            //the extras are put into the intent (which happens in finishDownloadProcessing()). Therefore,
            //the sendNotification() (and thus the creation & sending of the notification) is always being done
            //before the extras are being put into the intent. Therefore, when you click on it, the extras are
            //not being passed to DetailsActivity. You need to move the notification code on DownloadReceived.kt
            //down below the setting of resultStatus and loadingState.value
            loadingState.observe(this@MainActivity, Observer {

                if (loadingState.value == LoadingStatus.DONE) {
                    finishDownloadProcessing()
                }

            })

        }

    }

    //************************************************GLIDE CODE************************************************

    //*******************************************Coroutines_Udacity***************************************************

    private var animationProcessingJob_UdacitySelected = Job()

    private val animationProcessingScope_UdacitySelected = CoroutineScope(Dispatchers.Main + animationProcessingJob_UdacitySelected)

    private fun process_Animation_Udacity() {

        animationProcessingScope_UdacitySelected.launch {

            //Set the color of DOWNLOAD text to same as background color
            binding.downloadButton.textPaint.color = binding.downloadButton.buttonPrimaryColor
            binding.downloadButton.invalidate()
            Log.i("MainActivity", "DOWNLOAD text changed to buttonPrimaryColory")

            val leftPosition = binding.downloadButton.x
            val rightPosition = binding.downloadButton.x + binding.downloadButton.width

            binding.animatedDownloadButton.showAnimatedDownloadButton(leftPosition, rightPosition)

            binding.progressCircle.showAnimatedCircle()

            withContext(Dispatchers.IO) {
                Log.i("MainActivity", "withContext block has started")
//                GlideDownloader.downloadFile(Constants.GLIDE_URL, context)

                FileDownloader(mainContext).downloadFile(Constants.UDACITY_URL, mainContext)
                Log.i("MainActivity", "Udacity_URL is ${Constants.UDACITY_URL}")

            }

            loadingState.observe(this@MainActivity, Observer {

                if (loadingState.value == LoadingStatus.DONE) {
                    finishDownloadProcessing()
                }

            })

        }

    }

    //************************************************UDACITY_CODE**********************************************


    //*******************************************Coroutines_Retrofit***********************************************

    private var animationProcessingJob_RetrofitSelected = Job()

    private val animationProcessingScope_RetrofitSelected = CoroutineScope(Dispatchers.Main + animationProcessingJob_RetrofitSelected)

        private fun process_Antimation_Retrofit() {

            animationProcessingScope_RetrofitSelected.launch {

                //Set the color of DOWNLOAD text to same as background color
                binding.downloadButton.textPaint.color = binding.downloadButton.buttonPrimaryColor
                binding.downloadButton.invalidate()
                Log.i("MainActivity", "DOWNLOAD text changed to buttonPrimaryColory")

                val leftPosition = binding.downloadButton.x
                val rightPosition = binding.downloadButton.x + binding.downloadButton.width

                binding.animatedDownloadButton.showAnimatedDownloadButton(leftPosition, rightPosition)

                binding.progressCircle.showAnimatedCircle()


                withContext(Dispatchers.IO) {
                    Log.i("MainActivity", "withContext block has started")
//                GlideDownloader.downloadFile(Constants.GLIDE_URL, context)

                    FileDownloader(mainContext).downloadFile(Constants.RETROFIT_URL, mainContext)
                    Log.i("MainActivity", "Udacity_URL is ${Constants.RETROFIT_URL}")

                }


                loadingState.observe(this@MainActivity, Observer {

                    if (loadingState.value == LoadingStatus.DONE) {
                        finishDownloadProcessing()
                    }

                })

            }

        }



    //************************************************RETROFIT_CODE***********************************************



    fun finishDownloadProcessing() {
        if (loadingState.value == LoadingStatus.DONE) {

            //Stop animation of customer views
            binding.animatedDownloadButton.cancelAnimators()
            binding.progressCircle.cancelAnimatedCircle()
            Log.i("MainActivity", "DownloadButton & ProgressCircle animates are cancelled")


            //Make custom view disappear
            binding.progressCircle.isGone = true
            binding.animatedDownloadButton.isGone = true

            //Re-set the color of DOWNLOAD text to white
            binding.downloadButton.textPaint.color = Color.WHITE
            binding.downloadButton.invalidate()


            //*****************Navigation to DetailsActivity**************************
            //Trigger navigation to DetailsActivity when download complete using Intent
            //Do you need to place this code inside the CoroutineScope? I do think so insofar as I
            //need to stop this corouting from processing.

//            fileName = Constants.GLIDE_FILE_NAME

            when (loadingFile) {

                Loading.GLIDE -> { fileName = Constants.GLIDE_FILE_NAME}

                Loading.UDACITY -> {fileName = Constants.UDACITY_FILE_NAME}

                Loading.RETROFIT ->  {fileName = Constants.RETROFIT_FILE_NAME}

                else -> {fileName = ""}
            }


            if (resultStatus == ResultStatus.SUCCESS) {
                fileDownloadStatus = "Success"
            } else {
                fileDownloadStatus = "Fail"
            }

            Log.i("MainActivity", "MARKER PLACE EXTRAS MARKER")

            downloadIntent.also {

                val bundle = Bundle()

                bundle.putString("FILENAME", fileName)
                bundle.putString("STATUS", fileDownloadStatus)


                it.putExtras(bundle)


//                startActivity(it)
            }
            //******************************************************************************




            //Reset loadingStatus & fileDownloadStatus progress indicators
            loadingState.value = LoadingStatus.LOADING
            resultStatus = ResultStatus.NEUTRAL

            //Stop the coroutine from processing
            //I don't think any need to do this as it is being cancelled in onDestroy()??

        }

    }

    //**************************************************************************************




    override fun onDestroy() {
        super.onDestroy()

        animationProcessingJob_NoFileSelected.cancel()
        animationProcessingScope_GlideSelected.cancel()
    }


    //**************************************************************************

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        downloadIntent = Intent(this@MainActivity, DetailActivity::class.java)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        setOnCheckedListenerToRadioGroup(binding.radioGroup)


//        binding.animatedDownloadButton.isGone = true
        binding.selectFileButton.isGone = true

        //pass in channel creation
        createChannel(getString(R.string.download_channel_id), getString(R.string.download_channel_name))

        //setOnDownloadClickListener runs the lambda passed into it
        binding.downloadButton.setOnDownloadClickListener {
            //put what happens when downloadButton is clicked


            when (loadingFile) {
                Loading.GLIDE -> {

                    processAnimation_Glide()
                }
                Loading.UDACITY -> {

                    process_Animation_Udacity()
                }
                Loading.RETROFIT -> {

                    process_Antimation_Retrofit()

                }
                else -> {
                    //Make select_downloaod_button visible, animate it, and
                    // make "Select file to download" custom view visible so it
                    //is overlaid on top of select_download_button
                    //This should only last for a say 5 seconds before animation stops
                    // and customer view becomes invisible


                    processAnimation_NoFileSelected()


                }
            }
        }
    }


    private fun setOnCheckedListenerToRadioGroup (group: RadioGroup) {



        val glide = binding.glideButton.id
        val udacity = binding.udacityButton.id
        val retrofit = binding.retrofitButton.id

        val checkedId = -1

        val listener = ClickListenerOuter(glide, udacity, retrofit)

        //Sets the checked change listener for the group
        group.setOnCheckedChangeListener (listener)

        //calls the onCheckedChanged method for the listener
        listener.onCheckedChanged(group, checkedId)

    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }


    private fun createChannel (channelID:String, channelName:String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel = NotificationChannel (channelID, channelName,
                NotificationManager.IMPORTANCE_HIGH ).apply { setShowBadge(false) }

            notificationChannel.description = getString(R.string.notification_channel_description)

            val notificationManager = this.getSystemService(NotificationManager::class.java)

            notificationManager.createNotificationChannel(notificationChannel)

        }

    }




}