package com.alland.myapplication.ui.main

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alland.myapplication.R
import com.alland.myapplication.databinding.ActivityWebBinding


class WebActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.webViewNews.settings.javaScriptEnabled  = true
        binding.webViewNews.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        })

        val extraUrl = intent.getStringExtra(EXTRA_URL)

        if(extraUrl != null){
            binding.webViewNews.loadUrl(extraUrl)
        }

    }

    companion object {
        val EXTRA_URL: String = "EXTRA_URL"
    }
}