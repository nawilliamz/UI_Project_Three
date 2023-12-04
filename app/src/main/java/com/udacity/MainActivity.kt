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
import android.widget.Toast
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

    private var buttonPressCounter:Boolean


    init {
        loadingStatus = LoadingStatus.LOADING
        resultStatus = ResultStatus.NEUTRAL

        buttonPressCounter = false

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

            val leftPosition = binding.downloadButton.x
            val rightPosition = binding.downloadButton.x + binding.downloadButton.width

            binding.animatedDownloadButton.showAnimatedDownloadButton(leftPosition, rightPosition)

            binding.progressCircle.showAnimatedCircle()

//            Start download with code by referencing DownloadClass
            //This line of code needs to be on separate coroutine using Dispaters.IO

            withContext(Dispatchers.IO) {

                FileDownloader(mainContext).downloadFile(Constants.GLIDE_URL, mainContext)



            }



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


            val leftPosition = binding.downloadButton.x
            val rightPosition = binding.downloadButton.x + binding.downloadButton.width

            binding.animatedDownloadButton.showAnimatedDownloadButton(leftPosition, rightPosition)

            binding.progressCircle.showAnimatedCircle()

            withContext(Dispatchers.IO) {

                FileDownloader(mainContext).downloadFile(Constants.UDACITY_URL, mainContext)


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


                val leftPosition = binding.downloadButton.x
                val rightPosition = binding.downloadButton.x + binding.downloadButton.width

                binding.animatedDownloadButton.showAnimatedDownloadButton(leftPosition, rightPosition)

                binding.progressCircle.showAnimatedCircle()


                withContext(Dispatchers.IO) {

                    FileDownloader(mainContext).downloadFile(Constants.RETROFIT_URL, mainContext)

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

            //Make custom view disappear
            binding.progressCircle.isGone = true
            binding.animatedDownloadButton.isGone = true

            //Re-set the color of DOWNLOAD text to white
            binding.downloadButton.textPaint.color = Color.WHITE
            binding.downloadButton.invalidate()


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


            downloadIntent.also {

                val bundle = Bundle()

                bundle.putString("FILENAME", fileName)
                bundle.putString("STATUS", fileDownloadStatus)


                it.putExtras(bundle)

            }
            //******************************************************************************

            //Reset loadingStatus & fileDownloadStatus progress indicators
            loadingState.value = LoadingStatus.LOADING
            resultStatus = ResultStatus.NEUTRAL


        }

    }

    //**************************************************************************************




    override fun onDestroy() {
        super.onDestroy()

        animationProcessingJob_NoFileSelected.cancel()
        animationProcessingScope_GlideSelected.cancel()
        animationProcessingScope_UdacitySelected.cancel()
        animationProcessingScope_RetrofitSelected.cancel()
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

        //setOnDownloadClickListener runs the lambda passed into it
        binding.downloadButton.setOnDownloadClickListener {
            //put what happens when downloadButton is clicked

            if (buttonPressCounter == false) {

                when (loadingFile) {
                    Loading.GLIDE -> {

                        processAnimation_Glide()
                        buttonPressCounter = true
                    }
                    Loading.UDACITY -> {

                        process_Animation_Udacity()
                        buttonPressCounter = true
                    }
                    Loading.RETROFIT -> {

                        process_Antimation_Retrofit()
                        buttonPressCounter = true

                    }
                    else -> {

                        processAnimation_NoFileSelected()

                    }
                }
            } else {
                Toast.makeText(this, "Screen needs refreshed. Exit app and re-try", Toast.LENGTH_SHORT).show()

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