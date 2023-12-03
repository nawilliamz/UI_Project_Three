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
    private var downloadAnimationHasRun:Boolean


    init {
        downloadAnimationHasRun = false
    }

//    private val motionTransitionJob = Job()
//
//    private val motionTransitionScope = CoroutineScope(Dispatchers.Default +  motionTransitionJob)



//    private fun ConstraintLayout.initStartSet() = ConstraintSet().apply {
//        clone(this@initStartSet)
//        applyTo(this@initStartSet)
//    }
//
//    private fun ConstraintLayout.endStartSet() = ConstraintSet().apply {
//        clone(this@endStartSet)
//        applyTo(this@endStartSet)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //***********************************INTENT CODE****************************************
        val fileName = intent.extras?.getString("FILENAME")
        val status = intent.extras?.getString("STATUS")

        Log.i("DetailActivity", "Valaue of fileName in Detail Activity is ${fileName}")
        binding.fileNameText.setText(fileName)
        binding.statusText.setText(status)
        //*************************************************************************************

        if (!downloadAnimationHasRun) {
            binding.detailOverlayText.visibility = View.VISIBLE
            downloadAnimationHasRun = true
        } else {
            binding.detailOverlayText.visibility = View.INVISIBLE
        }

        binding.detailOverlayText.setOnClickListener {


            if (downloadAnimationHasRun) {

                it.visibility = View.GONE
//                    it.requestLayout()
//                    it.invalidate()


                //run the anbimation
                binding.motionLayout.transitionToEnd()
            }
        }

        binding.button.setOnClickListener {

//                downloadAnimationHasRun = false

            val intent = Intent(this@DetailActivity, MainActivity::class.java)
            startActivity(intent)
        }


    }

//    }

//    private fun runMotionLayoutTransition() {
//
////        val initSet = binding.root.getConstraintSet(binding.root.endState)
//
//        scene = MotionScene(binding.root).apply {
//
//            val startSet = this.getConstraintSet(this@DetailActivity, "start")
//
//            val transition = TransitionBuilder.buildTransition(
//                this,
//                R.id.transition_id,
//                R.id.start_set_id,
//                binding.root.initStartSet(),
//                R.id.end_set_id,
//                binding.root.endStartSet()
//            )
//
//            setTransition(transition)
//            binding.root.scene = this
//        }
//
//        binding.root.apply {
//            setTransitionDuration(5000)
//            startLayoutAnimation()
////            transitionToEnd()
//        }
//
//    }


    override fun onStart() {
        super.onStart()

//
//        motionTransitionScope.launch {
//            Log.i("DetailActivity", "onStart method in DetailActivity is running.")
//
////            val transition = binding.motionLayout.setTransition(1)
//            binding.motionLayout.rootView
//            binding.motionLayout.scene
////            binding.motionLayout.setProgress(0f)
//            binding.motionLayout.startViewTransition(binding.statusText)
//        }

    }

    override fun onDestroy() {
        super.onDestroy()

//        motionTransitionScope.cancel()

    }
}


