package com.example.codescannergenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class WebViewActivity extends AppCompatActivity {
    private static final String TAG = "WebViewActivity";
    WebView webView;
    ProgressBar progressBar;
    ImageView close;
    String link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progress_bar);
        close= findViewById(R.id.close);

        if (getIntent().hasExtra("link")) {
            link = getIntent().getStringExtra("link");

            webView.loadUrl(link);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
        close.setOnClickListener(v -> finish());
    }
    @Override
    public void onBackPressed(){
        if (webView.canGoBack()){
            webView.goBack();
        }else{
            super.onBackPressed();
        }
    }
    }