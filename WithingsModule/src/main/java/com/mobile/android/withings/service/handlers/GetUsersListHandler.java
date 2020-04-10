package com.mobile.android.withings.service.handlers;

import android.content.Context;

import com.mobile.android.chart.data.DataManager;
import com.mobile.android.withings.service.RestService;
import com.mobile.android.withings.service.connection.ConnectionResponse;
import com.mobile.android.withings.service.processor.GetUsersListResponseProcessor;
import com.mobile.android.withings.service.processor.ResponseProcessor;
import com.mobile.android.withings.service.rest.api.GetUsersListRequest;
import com.mobile.android.withings.utils.JSONUtils;

public class GetUsersListHandler extends RequestHandler {
    private Context mContext;

    public GetUsersListHandler(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    ConnectionResponse handleRequest() {
        GetUsersListRequest request = (GetUsersListRequest) JSONUtils.fromJson(mRequestIntent.getStringExtra(RestService.EXTRA_JSON), GetUsersListRequest.class);
        String token = DataManager.getInstance().getAccessToken(mContext);
        ConnectionResponse response = mConnectionManager.getUsersList(request.getKey(), token);
        return response;
    }

    @Override
    ResponseProcessor<ConnectionResponse> getResponseProcessor() {
        return new GetUsersListResponseProcessor(mContext, mRequestIntent);
    }
}
