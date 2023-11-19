package com.udacity

import android.app.Fragment
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.udacity.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fileName = intent.extras?.getString("FILENAME")
        val status = intent.extras?.getString("STATUS")

        Log.i("DetailActivity", "Valaue of fileName in Detail Activity is ${fileName}")
        binding.fileNameText.setText(fileName)
        binding.statusText.setText(status)

    }


    override fun onStart() {
        super.onStart()

        Log.i("DetailActivity", "onStart method in DetailActivity is running.")
        binding.motionLayout.transitionToEnd()
    }


}


