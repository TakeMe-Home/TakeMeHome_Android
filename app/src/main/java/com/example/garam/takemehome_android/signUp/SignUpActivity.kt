package com.example.garam.takemehome_android.signUp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.garam.takemehome_android.R
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        riderSignUp.setOnClickListener {
            finish()
        }
    }
}