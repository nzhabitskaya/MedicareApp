package com.mobile.android.withings.service.handlers;

import android.content.Context;

import com.mobile.android.withings.service.RestService;
import com.mobile.android.withings.service.connection.ConnectionResponse;
import com.mobile.android.withings.service.processor.AccessTokenResponseProcessor;
import com.mobile.android.withings.service.processor.ResponseProcessor;
import com.mobile.android.withings.service.rest.api.AccessTokenRequest;
import com.mobile.android.withings.utils.JSONUtils;

public class AccessTokenHandler extends RequestHandler {

    public AccessTokenHandler(Context context) {
        super(context);
    }

    @Override
    ConnectionResponse handleRequest() {
        AccessTokenRequest request = (AccessTokenRequest) JSONUtils.fromJson(mRequestIntent.getStringExtra(RestService.EXTRA_JSON), AccessTokenRequest.class);
        ConnectionResponse response = mConnectionManager.requestEndUserAuthorization(request.getKey(), request.getToken());
        return response;
    }

    @Override
    ResponseProcessor<ConnectionResponse> getResponseProcessor() {
        return new AccessTokenResponseProcessor(mContext, mRequestIntent);
    }
}