package com.example.test.testintent;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    private static final String LOG_TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.launch_intent_button);
        WebView webView = (WebView) findViewById(R.id.default_webview);
        WebView webViewCustomClient = (WebView) findViewById(R.id.custom_client_webview);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean installed = appInstalledOrNot("com.example.test.target");
                if (installed) {
                    //This intent will help you to launch if the package is already installed
                    Intent LaunchIntent = getPackageManager()
                            .getLaunchIntentForPackage("com.example.test.target");
                    startActivity(LaunchIntent);

                    Log.i(LOG_TAG, "App is already installed on your phone");
                } else {
                    Log.i(LOG_TAG, "App is not installed on your phone");
                }
            }
        });

        configureWebView(webView);
        configureWebView(webViewCustomClient);


        webViewCustomClient.setWebViewClient(new WebViewClient() {
                                                 @Override
                                                 public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                                     return super.shouldOverrideUrlLoading(view, url);
                                                 }
                                             }
        );

        webView.loadUrl("file:///android_asset/testLaunch.html");
        webViewCustomClient.loadUrl("file:///android_asset/testLaunch.html");
    }

    private void configureWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        webView.setWebChromeClient(new WebChromeClient() {


            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.e("WebConsole", consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
}
