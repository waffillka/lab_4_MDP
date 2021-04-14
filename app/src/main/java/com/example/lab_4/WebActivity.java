package com.example.lab_4;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WebActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Uri url = getIntent().getData();
        webView = findViewById(R.id.view_web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new Callback());
        webView.loadUrl(url.toString());
    }

    public void onCrossClicked(View view){
        this.finish();
    }

    public void onUpdateClicked(View view){
        webView.reload();
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return(false);
        }
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
