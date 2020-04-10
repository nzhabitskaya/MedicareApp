package com.mobile.android.withings.service.processor;

import android.content.Context;
import android.content.Intent;

import com.mobile.android.withings.service.RestService;
import com.mobile.android.withings.service.connection.ConnectionResponse;

public abstract class ResponseProcessor<T extends ConnectionResponse> {

    protected Context mContext;

    protected Intent mRequestIntent;

    abstract void processSuccess(T connectionResponse);

    abstract void processError(T connectionResponse);

    public ResponseProcessor(Context context, Intent requestIntent) {
        this.mContext = context;
        this.mRequestIntent = requestIntent;
    }

    public void setIntent(Intent requestIntent) {
        this.mRequestIntent = requestIntent;
    }

    public void processResponse(T connectionResponse) {

        if (connectionResponse == null)
            return;

        // Check error
        if (connectionResponse.hasError()) {
            mRequestIntent.putExtra(RestService.ERROR_MESSAGE, connectionResponse.getError());
            processError(connectionResponse);
        } else {
            mRequestIntent.putExtra(RestService.EXTRA_RESPONSE, connectionResponse);
            processSuccess(connectionResponse);
        }
    }
}
