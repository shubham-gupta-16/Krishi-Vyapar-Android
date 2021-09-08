package com.acoder.krishivyapar

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.acoder.krishivyapar.api.Api
import com.acoder.krishivyapar.api.ApiData
import com.acoder.krishivyapar.databinding.ActivityNameRegisterBinding

class AuthNameRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNameRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nameEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val name = binding.nameEditText.text.toString()
                if (name.length < 3) {
                    binding.nameTextInput.error = "Please Enter a valid name"
                    return@setOnEditorActionListener false
                }
                Api(this).requestUpdateName(name).atSuccess {
                    val i = Intent(this, MainActivity::class.java)
                    startActivity(i)
                    finish()
                }.execute()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }
}