package com.mobile.android.withings.service.processor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mobile.android.withings.service.RestService;
import com.mobile.android.withings.service.connection.ConnectionResponse;
import com.mobile.android.withings.service.connection.OAuth;
import com.mobile.android.withings.service.rest.api.AccessTokenResponse;
import com.mobile.android.withings.service.rest.api.OauthTokenResponse;
import com.mobile.android.withings.utils.JSONUtils;


public class AccessTokenResponseProcessor extends ResponseProcessor<ConnectionResponse> {

    public AccessTokenResponseProcessor(Context context, Intent requestIntent) {
        super(context, requestIntent);
    }

    @Override
    void processSuccess(ConnectionResponse connectionResponse) {
        Log.d(OAuth.TAG, "Access token response: " + connectionResponse.getResponse());
        String responseStr = JSONUtils.getJSONFromResponseStr(connectionResponse.getResponse());
        AccessTokenResponse response = (AccessTokenResponse)JSONUtils.fromJson(responseStr, AccessTokenResponse.class);

        mRequestIntent.putExtra(RestService.OAUTH_TOKEN, response.getAccessToken());
        mRequestIntent.putExtra(RestService.OAUTH_TOKEN_SECRET, response.getAccessSecret());
    }

    @Override
    void processError(ConnectionResponse connectionResponse) {
    }
}
