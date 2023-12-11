package com.udacity

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.constraintlayout.motion.widget.TransitionBuilder
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import com.udacity.databinding.ActivityDetailBinding
import kotlinx.coroutines.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var scene:MotionScene
//    private var downloadAnimationHasRun:Boolean


//    init {
//        downloadAnimationHasRun = false
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //***********************************INTENT CODE****************************************
        val fileName = intent.extras?.getString("FILENAME")
        val status = intent.extras?.getString("STATUS")

        binding.fileNameText.setText(fileName)
        binding.statusText.setText(status)
        //*************************************************************************************

//        if (!downloadAnimationHasRun) {
//            binding.detailOverlayText.visibility = View.VISIBLE
//            downloadAnimationHasRun = true
//        } else {
//            binding.detailOverlayText.visibility = View.INVISIBLE
//        }

//        binding.detailOverlayText.setOnClickListener {
//
//
//            if (downloadAnimationHasRun) {
//
//                it.visibility = View.GONE
//
//                //run the animation
//                binding.motionLayout.transitionToEnd()
//            }
//        }

        binding.button.setOnClickListener {


            val intent = Intent(this@DetailActivity, MainActivity::class.java)
            startActivity(intent)
        }

//        binding.motionLayout.transitionToEnd()


    }


    override fun onStart() {
        super.onStart()

        binding.motionLayout.setTransition(R.id.start, R.id.end)
        binding.motionLayout.transitionToEnd()
    }

    override fun onDestroy() {
        super.onDestroy()


    }
}


