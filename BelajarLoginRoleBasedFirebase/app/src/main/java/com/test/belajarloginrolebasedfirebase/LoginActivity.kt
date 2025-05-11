package com.test.belajarloginrolebasedfirebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.belajarloginrolebasedfirebase.databinding.ActivityLoginBinding
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                login(username, password)
            } else {
                Toast.makeText(this, "Lengkapi username dan password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    private fun login(username: String, password: String) {
        val hashedPassword = hashPassword(password)

        val database = Firebase.database
        val dbRef = database.reference

        dbRef.child("admin").get().addOnSuccessListener { adminSnapshot ->
            var found = false

            for (admin in adminSnapshot.children) {
                val adminUsername = admin.child("admin_username").value.toString()
                val adminPassword = admin.child("admin_password").value.toString()

                if (username == adminUsername && hashedPassword == adminPassword) {
                    Toast.makeText(this, "Login sebagai Admin", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AdminSectionActivity::class.java)
                    startActivity(intent)
                    finish()
                    found = true
                    break
                }
            }

            if (!found) {
                dbRef.child("student").get().addOnSuccessListener { studentSnapshot ->
                    for (student in studentSnapshot.children) {
                        val studentUsername = student.child("student_username").value.toString()
                        val studentPassword = student.child("student_password").value.toString()

                        if (username == studentUsername && hashedPassword == studentPassword) {
                            Toast.makeText(this, "Login sebagai Student", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, StudentSectionActivity::class.java)
                            startActivity(intent)
                            finish()
                            return@addOnSuccessListener
                        }
                    }
                    Toast.makeText(this, "Username atau password salah", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal mengakses database", Toast.LENGTH_SHORT).show()
        }
    }

}