package com.haseeb.unacademyassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editclick.setOnClickListener {
            val amount = edit.text.toString().toInt()
            if (amount <= 100) {
                progress.setProgress(amount)
            } else {
                Toast.makeText(this@MainActivity, "Enter a valid number", Toast.LENGTH_LONG).show()
            }
        }
    }
}
