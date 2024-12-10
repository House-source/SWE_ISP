package com.example.swe_isp.p4_transit;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

import com.example.swe_isp.R;

public class P4_Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p4_main);  // Set the layout with WebView

        // Initialize the WebView
        WebView webView = findViewById(R.id.webView);

        // Clear the cache before loading the webpage
        webView.clearCache(true);

        // Enable JavaScript (required for many modern websites)
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // Set WebViewClient to make sure links open inside the WebView instead of an external browser
        webView.setWebViewClient(new WebViewClient());

        // Load the desired website
        webView.loadUrl("https://metrobusmobile.com/?mobile");
    }

    // Handle the back button to navigate within WebView history
    @Override
    public void onBackPressed() {
        WebView webView = findViewById(R.id.webView);
        if (webView.canGoBack()) {
            webView.goBack();  // Go back in WebView's history if possible
        } else {
            super.onBackPressed();  // Call the default back press behavior if no history
        }
    }
}

