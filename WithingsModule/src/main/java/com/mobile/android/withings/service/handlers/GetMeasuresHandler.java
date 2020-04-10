package com.mobile.android.withings.service.handlers;

import android.content.Context;

import com.mobile.android.chart.data.DataManager;
import com.mobile.android.withings.service.RestService;
import com.mobile.android.withings.service.connection.ConnectionResponse;
import com.mobile.android.withings.service.processor.GetMeasuresResponseProcessor;
import com.mobile.android.withings.service.processor.ResponseProcessor;
import com.mobile.android.withings.service.rest.api.GetMeasuresRequest;
import com.mobile.android.withings.utils.JSONUtils;


public class GetMeasuresHandler extends RequestHandler {
    private Context mContext;

    public GetMeasuresHandler(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    ConnectionResponse handleRequest() {
        GetMeasuresRequest request = (GetMeasuresRequest) JSONUtils.fromJson(mRequestIntent.getStringExtra(RestService.EXTRA_JSON), GetMeasuresRequest.class);
        String token = DataManager.getInstance().getAccessToken(mContext);
        ConnectionResponse response = mConnectionManager.getMeasures(request.getKey(), token);
        return response;
    }

    @Override
    ResponseProcessor<ConnectionResponse> getResponseProcessor() {
        return new GetMeasuresResponseProcessor(mContext, mRequestIntent);
    }
}