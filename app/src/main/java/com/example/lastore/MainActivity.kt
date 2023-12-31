package com.example.lastore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.lastore.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainUpload.setOnClickListener(View.OnClickListener{
            val intent = Intent(this@MainActivity, UploadActivity::class.java)
            startActivity(intent)
            finish()
        })
        binding.mainUpdate.setOnClickListener(View.OnClickListener{
            val intent = Intent(this@MainActivity, UpdateActivity::class.java)
            startActivity(intent)
            finish()
        })
        binding.mainDelete.setOnClickListener(View.OnClickListener{
            val intent = Intent(this@MainActivity, DeleteActivity::class.java)
            startActivity(intent)
            finish()
        })
        binding.recyclerviewbtn.setOnClickListener(View.OnClickListener{
            val intent = Intent(this@MainActivity, UserlistActivity::class.java)
            startActivity(intent)
            finish()
        })


    }
}