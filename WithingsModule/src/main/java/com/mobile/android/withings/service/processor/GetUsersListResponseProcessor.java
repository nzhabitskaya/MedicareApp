package com.mobile.android.withings.service.processor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mobile.android.withings.service.connection.ConnectionResponse;
import com.mobile.android.withings.service.connection.OAuth;

public class GetUsersListResponseProcessor extends ResponseProcessor<ConnectionResponse> {

    public GetUsersListResponseProcessor(Context context, Intent requestIntent) {
        super(context, requestIntent);
    }

    @Override
    void processSuccess(ConnectionResponse connectionResponse) {
        Log.e(OAuth.TAG, connectionResponse.getResponse());
    }

    @Override
    void processError(ConnectionResponse connectionResponse) {
    }
}