package com.mobile.android.withings.service.rest.callback;

import android.content.Intent;

public interface RestCallback {

	public void onError(final Intent intent);
	
	public void onInternalError(final Intent intent);

    public void onSuccess(final Intent success);

}
