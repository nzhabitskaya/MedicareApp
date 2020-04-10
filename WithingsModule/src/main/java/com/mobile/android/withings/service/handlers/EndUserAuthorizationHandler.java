package com.mobile.android.withings.service.handlers;

import android.content.Context;
import android.util.Log;

import com.mobile.android.withings.service.RestService;
import com.mobile.android.withings.service.connection.ConnectionResponse;
import com.mobile.android.withings.service.processor.EndUserAuthorizationProcessor;
import com.mobile.android.withings.service.processor.ResponseProcessor;
import com.mobile.android.withings.service.rest.api.EndUserAuthRequest;
import com.mobile.android.withings.utils.JSONUtils;

public class EndUserAuthorizationHandler extends RequestHandler {

    public  EndUserAuthorizationHandler(Context context) {
        super(context);
    }

    @Override
    ConnectionResponse handleRequest() {
        EndUserAuthRequest request = (EndUserAuthRequest) JSONUtils.fromJson(mRequestIntent.getStringExtra(RestService.EXTRA_JSON),  EndUserAuthRequest.class);
        String requestStr = mConnectionManager.requestEndUserAuthorizationUrl(request.getKey(), request.getToken());
        mRequestIntent.putExtra(RestService.OAUTH_WEB_REDIRECT, requestStr);
        return null;
    }

    @Override
    ResponseProcessor<ConnectionResponse> getResponseProcessor() {
        return new EndUserAuthorizationProcessor(mContext, mRequestIntent);
    }
}