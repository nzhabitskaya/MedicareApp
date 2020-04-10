package com.mobile.android.withings.service.processor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mobile.android.withings.service.RestService;
import com.mobile.android.withings.service.connection.ConnectionResponse;
import com.mobile.android.withings.service.connection.OAuth;
import com.mobile.android.withings.service.rest.api.OauthTokenResponse;
import com.mobile.android.withings.utils.JSONUtils;

public class OauthTokenResponseProcessor extends ResponseProcessor<ConnectionResponse> {

    public OauthTokenResponseProcessor(Context context, Intent requestIntent) {
        super(context, requestIntent);
    }

    @Override
    void processSuccess(ConnectionResponse connectionResponse) {
        //Log.d(OAuth.TAG, connectionResponse.getResponse());
        String responseStr = JSONUtils.getJSONFromResponseStr(connectionResponse.getResponse());
        OauthTokenResponse response = (OauthTokenResponse)JSONUtils.fromJson(responseStr, OauthTokenResponse.class);
        
        mRequestIntent.putExtra(RestService.OAUTH_TOKEN, response.getOauthToken());
        mRequestIntent.putExtra(RestService.OAUTH_TOKEN_SECRET, response.getOauthSecret());
    }

    @Override
    void processError(ConnectionResponse connectionResponse) {
    }
}
