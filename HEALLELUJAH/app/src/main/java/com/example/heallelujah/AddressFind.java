package com.example.heallelujah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AddressFind extends AppCompatActivity {

    private WebView webView;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_find);
        init_webView();
        handler = new Handler();
    }

    public void init_webView() {
        webView = (WebView)findViewById(R.id.webviewt);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.addJavascriptInterface(new AddressFind.AndroidBridge(), "TestApp");
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("URL 입력");
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // WebView를 초기화 하지않으면 재사용할 수 없음
                    init_webView();
                    Intent returnmemberinfo = new Intent();
                    returnmemberinfo.putExtra("address1", arg2);
                    returnmemberinfo.putExtra("address2", arg3);
                    setResult(RESULT_OK, returnmemberinfo);
                    finish();
                }
            });
        }
    }
}
