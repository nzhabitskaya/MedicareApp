package com.mobile.android.withings.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mobile.android.chart.data.DataManager;
import com.mobile.android.withings.ui.controller.ActivityController;
import com.mobile.android.withings.R;
import com.mobile.android.withings.service.RestService;
import com.mobile.android.withings.service.connection.OAuth;
import com.mobile.android.withings.service.rest.api.EndUserAuthRequest;
import com.mobile.android.withings.service.rest.api.GetMeasuresRequest;
import com.mobile.android.withings.service.rest.api.GetUsersListRequest;
import com.mobile.android.withings.service.ServiceHelper;
import com.mobile.android.withings.service.connection.ConnectionManager;
import com.mobile.android.withings.service.rest.api.OauthTokenRequest;
import com.mobile.android.withings.service.rest.callback.RestCallback;
import com.mobile.android.withings.ui.web.WebActivity;

public class RequestWeightDataFragment extends BaseFragment implements ActivityController, RestCallback {
	private ProgressDialog progressDialog;

    private String oauthToken;
    private String oauthSecret;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ConnectionManager.getInstance().registerResponseListener(this);
	}

	@Override
	public void onError(final Intent intent) {
		hideProgress();
		Toast.makeText(getActivity(), R.string.message_connection_error, Toast.LENGTH_LONG);
	}
	
	@Override
	public void onInternalError(final Intent intent) {
		hideProgress();
		Toast.makeText(getActivity(), R.string.message_connection_error, Toast.LENGTH_LONG);
	}

	@Override
	public void onSuccess(Intent intent) {
        hideProgress();
        if(intent.getAction() == RestService.Action.REQUEST_OAUTH_TOKEN){
			Log.d("", "--");
			Log.d("", "Step 1: request oauth token and secret");
            oauthToken = intent.getStringExtra(RestService.OAUTH_TOKEN);
            oauthSecret = intent.getStringExtra(RestService.OAUTH_TOKEN_SECRET);
			DataManager.getInstance().setOauthToken(oauthToken);
			DataManager.getInstance().setOauthSecret(oauthSecret);
            Log.d("", "oauthToken = " + oauthToken);
            Log.d("", "oauthSecret = " + oauthSecret);
            requestEndUserAuthorization(OAuth.KEY, oauthToken);
        }
        else if(intent.getAction() == RestService.Action.REQUEST_AUTHORIZE){
			Log.d("", "--");
			Log.d("", "Step 2: redirect to web page");
            String redirectStr = intent.getStringExtra(RestService.OAUTH_WEB_REDIRECT);
            Intent webActivityIntent = new Intent(getActivity(), WebActivity.class);
            webActivityIntent.putExtra(RestService.OAUTH_WEB_REDIRECT, redirectStr);
            startActivity(webActivityIntent);
        }
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
	}
	
	// get a oAuth "request token"
    protected void requestOauthToken(String key, String secret) {
		progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.title_wait), getResources().getString(R.string.msg_redirect_to_web), true);
        OauthTokenRequest request = new OauthTokenRequest();
        request.setKey(key);
        request.setSecret(secret);
		Log.d(RequestWeightDataFragment.class.getName(), "------------------------------------");
        Log.d(RequestWeightDataFragment.class.getName(), "Step 1 : get a oAuth request token");
        new ServiceHelper(getActivity()).requestOauthToken(request);
    }

    // End-user authorization
    private void requestEndUserAuthorization(String key, String token) {
        EndUserAuthRequest request = new EndUserAuthRequest();
        request.setKey(key);
        request.setToken(token);
		Log.d(RequestWeightDataFragment.class.getName(), "------------------------------------");
        Log.d(RequestWeightDataFragment.class.getName(), "Step 2 : End-user authorization");
        new ServiceHelper(getActivity()).requestEndUserAuthorization(request);
    }

	// Work with api: get measures
	protected void getMeasures(String key, String token) {
		GetMeasuresRequest request = new GetMeasuresRequest();
		request.setKey(key);
		request.setToken(token);
		Log.d(RequestWeightDataFragment.class.getName(), "------------------------------------");
		Log.d(RequestWeightDataFragment.class.getName(), "Step 4 : API get measures");
		new ServiceHelper(getActivity()).requestMeasures(request);
	}

	protected void getUserList(String key, String token) {
		GetUsersListRequest request = new GetUsersListRequest();
		request.setKey(key);
		request.setToken(token);
		Log.d(RequestWeightDataFragment.class.getName(), "------------------------------------");
		Log.d(RequestWeightDataFragment.class.getName(), "Step 5 : API get users list");
		new ServiceHelper(getActivity()).requestUsersList(request);
	}
	
	@Override
	public void showProgress(int titleId, int messageId) {
	}

	@Override
	public void hideProgress() {
		if(progressDialog != null)
			progressDialog.cancel();
	}
}
