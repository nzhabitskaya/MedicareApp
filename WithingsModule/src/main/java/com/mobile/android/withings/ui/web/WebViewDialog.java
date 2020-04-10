package com.mobile.android.withings.ui.web;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mobile.android.withings.R;

public class WebViewDialog extends Dialog {
    private WebView webView;

    public WebViewDialog(Context context){
        super(context);
        initViews();
    }

    public void setWebContent(String html){
        final String mimeType = "text/html";
        final String encoding = "UTF-8";
        webView.loadDataWithBaseURL("", html, mimeType, encoding, "");
    }

    public void addJSCallback(Activity activity){
        webView.getSettings().setJavaScriptEnabled(true);
    }

    private void initViews(){
        setContentView(R.layout.webview_dialog);
        setTitle(R.string.web_dialog_title_login);
        webView = (WebView) findViewById(R.id.web_view);
        webView.setWebViewClient(client);
    }

    /**
     * WebView client listens when web view url will be redirected
     */
    WebViewClient client = new WebViewClient() {
        private int       webViewPreviousState;
        private final int PAGE_STARTED    = 0x1;
        private final int PAGE_REDIRECTED = 0x2;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
            super.shouldOverrideUrlLoading(view, urlNewString);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.e("", "OnPage started");
            webViewPreviousState = PAGE_STARTED;
            /*if (userDialog == null || !userDialog.isShowing())
                userDialog = ProgressDialog.show(WebViewActivity.this, "", getString(R.string.loadingMessege), true, true,
                        new OnCancelListener() {

                            @Override
                            public void onCancel(DialogInterface userDialog) {
                                // do something
                            }
                        });*/
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e("", "OnPage finished " + view.getUrl());



            if (webViewPreviousState == PAGE_STARTED) {
                //dismiss();
            }
        }
    };
}
