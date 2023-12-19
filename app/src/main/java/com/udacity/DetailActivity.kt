package com.udacity

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionScene
import androidx.constraintlayout.motion.widget.TransitionAdapter
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

        binding.button.setOnClickListener {


            val intent = Intent(this@DetailActivity, MainActivity::class.java)
            startActivity(intent)
        }

//        binding.motionLayout.transitionToEnd()


    }


    override fun onStart() {
        super.onStart()

//        val transition = binding.motionLayout.getTransition(R.id.resultTransitionX)
        binding.motionLayout.transitionToState(R.id.end, 10000)




    }

    override fun onDestroy() {
        super.onDestroy()

    }
}


