package com.modefin.magicrecipe.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.modefin.magicrecipe.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_selected_food_details.*


class SelectedFoodDetailsActivity : AppCompatActivity() {
    lateinit var ref_path: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_food_details)
        getIntentData()
    }

    private fun getIntentData() {
        let {
            ref_path = intent.getStringExtra("href")!!

            initWebView()
            setWebClient()
            webView.loadUrl(ref_path)

        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override
            fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }
        }
    }
    private fun setWebClient() {
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(
                view: WebView,
                newProgress: Int
            ) {
                super.onProgressChanged(view, newProgress)
                progressbar.progress = newProgress
                if (newProgress < 100 && progressbar.visibility == ProgressBar.GONE) {
                    progressbar.visibility = ProgressBar.VISIBLE
                }
                if (newProgress == 100) {
                    progressbar.visibility = ProgressBar.GONE
                }
            }
        }
    }
}