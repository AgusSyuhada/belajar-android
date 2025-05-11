package com.test.belajarloginrolebasedfirebase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.belajarloginrolebasedfirebase.databinding.ActivityAdminSectionBinding

class AdminSectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminSectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}