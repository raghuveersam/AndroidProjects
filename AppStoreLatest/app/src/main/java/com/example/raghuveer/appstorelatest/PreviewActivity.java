//Rgahuveer Sampath Krishnamurthy
// John O' Connor

package com.example.raghuveer.appstorelatest;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class PreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient());
        if(isConnected()){
            Toast.makeText(getApplicationContext(),"Loading.....",Toast.LENGTH_SHORT).show();
            myWebView.loadUrl(getIntent().getExtras().getString("URL"));
        }
        else {
            Toast.makeText(getApplicationContext(),"No Network Connection",Toast.LENGTH_LONG).show();
        }

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
           return false;
        }
    }

    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected()) {
            return true;
        }
        return false;
    }
    @Override
    public void onBackPressed() {
        finish();
    }

}
