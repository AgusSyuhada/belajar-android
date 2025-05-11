package com.test.belajarloginrolebasedfirebase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.belajarloginrolebasedfirebase.databinding.ActivityStudentSectionBinding

class StudentSectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentSectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentSectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}