package com.cibertec.lastore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Inicio : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        val buttonLogin : Button = findViewById(R.id.buttonLogin)
        val buttonRegistrar : Button = findViewById(R.id.buttonRegistrar)

        buttonLogin.setOnClickListener{
            val LoginWindow = Intent(this, Login::class.java)
            startActivity(LoginWindow)
        }
        buttonRegistrar.setOnClickListener{
            val RegistrarWindow = Intent(this, Registrar::class.java)
            startActivity(RegistrarWindow)
        }
    }
}