package com.mobile.android.withings.ui.web;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mobile.android.chart.data.DataManager;
import com.mobile.android.withings.R;
import com.mobile.android.withings.service.RestService;
import com.mobile.android.withings.service.ServiceHelper;
import com.mobile.android.withings.service.connection.ConnectionManager;
import com.mobile.android.withings.service.connection.OAuth;
import com.mobile.android.withings.service.rest.api.AccessTokenRequest;
import com.mobile.android.withings.service.rest.api.EndUserAuthResponse;
import com.mobile.android.withings.service.rest.callback.RestCallback;
import com.mobile.android.withings.ui.CustomUserDialog;
import com.mobile.android.withings.ui.controller.ActivityController;
import com.mobile.android.withings.utils.JSONUtils;


public class WebActivity extends Activity implements ActivityController, RestCallback {
    private CustomUserDialog userDialog;
    private WebView webView;

    private boolean isAllowPermissions = false;
    private boolean isUserIdReceived = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_dialog);
        webView = (WebView) findViewById(R.id.web_view);

        ConnectionManager.getInstance().registerResponseListener(this);
        userDialog = new CustomUserDialog(this);

        webView.getSettings().setUserAgentString( System.getProperty("http.agent"));
        webView.getSettings().setJavaScriptEnabled(true);
        final String redirectStr = getIntent().getStringExtra(RestService.OAUTH_WEB_REDIRECT);
        webView.loadUrl(redirectStr);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(url.contains("account_login")){
                    Log.d("", "First login");
                } else if(url.contains("authorize")){
                    Log.d("", "User allows permissions");
                    isAllowPermissions = true;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("", "onFinish page " + url);
                if (url.contains("http://oauth.withings.com/account/authorize?acceptDelegation")){
                    isUserIdReceived = true;
                    saveUserId(url);
                    requestAccessToken(OAuth.KEY, DataManager.getInstance().getOauthToken());
                }
            }
        });
    }

    @Override
    public void onError(final Intent intent) {
        hideProgress();
        userDialog.showWarningMessage("Error in request");
    }

    @Override
    public void onInternalError(final Intent intent) {
        hideProgress();
        String errorMsg = getString(R.string.message_connection_error);
        userDialog.showInternalErrorMessage(errorMsg);
    }

    @Override
    public void onSuccess(Intent intent) {
        hideProgress();
        if(intent.getAction() == RestService.Action.REQUEST_ACCESS_TOKEN){
            String accessToken = intent.getStringExtra(RestService.OAUTH_TOKEN);
            String accessSecret = intent.getStringExtra(RestService.OAUTH_TOKEN_SECRET);
            DataManager.getInstance().setAccessToken(WebActivity.this, accessToken);
            DataManager.getInstance().setOauthSecret(accessSecret);
            Log.d("", "accessToken = " + accessToken);
            Log.d("", "accessSecret = " + accessSecret);
            finish();
        }
    }

    private void saveUserId(String url){
        Log.d(WebActivity.class.getName(), "Redirect to page url:");
        Log.d(WebActivity.class.getName(), url);
        url = url.replace("http://oauth.withings.com/account/authorize?acceptDelegation=true&", "");
        String responseStr = JSONUtils.getJSONFromResponseStr(url);
        Log.d(WebActivity.class.getName(), "Response: " + responseStr);
        EndUserAuthResponse response = (EndUserAuthResponse) JSONUtils.fromJson(responseStr, EndUserAuthResponse.class);
        String userId = response.getUserId();
        Log.d(WebActivity.class.getName(), "User id from url is: " + userId);
        DataManager.getInstance().setUserId(WebActivity.this, userId);
    }

    private void requestAccessToken(String key, String token){
        AccessTokenRequest request = new AccessTokenRequest();
        request.setKey(key);
        request.setToken(token);
        Log.d(WebActivity.class.getName(), "--");
        Log.d(WebActivity.class.getName(), "Step 3 : Generate Access Token");
        new ServiceHelper(this).requestAccessToken(request);
    }

    @Override
    public void onPause() {
        super.onPause();
        ConnectionManager.getInstance().unregisterListener(this);
        hideProgress();
    }

    @Override
    public void onResume() {
        super.onResume();
        ConnectionManager.getInstance().registerResponseListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ConnectionManager.getInstance().unregisterListener(this);
        userDialog = null;
    }

    @Override
    public void showProgress(int titleId, int messageId) {
        userDialog.showProgress(titleId, messageId);
    }

    @Override
    public void hideProgress() {
        userDialog.hideProgress();
    }
}
