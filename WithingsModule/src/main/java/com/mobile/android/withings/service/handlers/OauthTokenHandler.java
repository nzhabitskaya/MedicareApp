package com.mobile.android.withings.service.handlers;

import com.mobile.android.withings.service.RestService;
import com.mobile.android.withings.service.connection.ConnectionResponse;
import com.mobile.android.withings.service.processor.OauthTokenResponseProcessor;
import com.mobile.android.withings.service.processor.ResponseProcessor;
import com.mobile.android.withings.service.rest.api.OauthTokenRequest;
import com.mobile.android.withings.utils.JSONUtils;

import android.content.Context;

public class OauthTokenHandler extends RequestHandler {

	public OauthTokenHandler(Context context) {
		super(context);
	}

	@Override
	ConnectionResponse handleRequest() {
		OauthTokenRequest request = (OauthTokenRequest) JSONUtils.fromJson(mRequestIntent.getStringExtra(RestService.EXTRA_JSON), OauthTokenRequest.class);
		ConnectionResponse response = mConnectionManager.requestOauthToken(request.getKey());
		return response;
	}

	@Override
	ResponseProcessor<ConnectionResponse> getResponseProcessor() {
		return new OauthTokenResponseProcessor(mContext, mRequestIntent);
	}
}
