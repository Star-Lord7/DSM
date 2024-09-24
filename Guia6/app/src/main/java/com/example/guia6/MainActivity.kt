package com.example.guia6

import android.os.Bundle
import android.view.View
import android.content.Intent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun onClickFrame(v: View?) {
        val llamar = Intent(this, FrameLayout::class.java)
        startActivity(llamar)
    }
    fun onClickLinear(v: View?) {
        val llamar = Intent(this, LinearLayout::class.java)
        startActivity(llamar)
    }
    fun onClickRelative(v: View?) {
        val llamar = Intent(this, RelativeLayout::class.java)
        startActivity(llamar)
    }
    fun onClickTable(v: View?) {
        val llamar = Intent(this, TableLayout::class.java)
        startActivity(llamar)
    }
    fun onClickGrid(v: View?) {
        val llamar = Intent(this, GridLayout::class.java)
        startActivity(llamar)
    }
}