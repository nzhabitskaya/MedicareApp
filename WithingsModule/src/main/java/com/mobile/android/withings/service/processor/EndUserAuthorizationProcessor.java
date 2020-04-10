package com.mobile.android.withings.service.processor;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mobile.android.withings.service.RestService;
import com.mobile.android.withings.service.connection.ConnectionResponse;

public class EndUserAuthorizationProcessor extends ResponseProcessor<ConnectionResponse> {

    public EndUserAuthorizationProcessor(Context context, Intent requestIntent) {
        super(context, requestIntent);
    }

    @Override
    void processSuccess(ConnectionResponse connectionResponse) {
    }

    @Override
    void processError(ConnectionResponse connectionResponse) {
    }
}
