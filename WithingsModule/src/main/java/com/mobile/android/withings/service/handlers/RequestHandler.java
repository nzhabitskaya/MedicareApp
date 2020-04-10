package com.mobile.android.withings.service.handlers;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.mobile.android.withings.application.WithingsModule;
import com.mobile.android.withings.service.RestService;
import com.mobile.android.withings.service.connection.ConnectionManager;
import com.mobile.android.withings.service.connection.ConnectionResponse;
import com.mobile.android.withings.service.processor.ResponseProcessor;

public abstract class RequestHandler implements Runnable {

	private Handler mUiHandler = new Handler();

	protected Context mContext;

	protected Intent mRequestIntent;

	protected ConnectionManager mConnectionManager = ConnectionManager
			.newDefaultConnectionManager();

	private ResponseProcessor<ConnectionResponse> mProcessor;

	abstract ConnectionResponse handleRequest();

	abstract ResponseProcessor<ConnectionResponse> getResponseProcessor();

	public RequestHandler(Context context) {
		this.mContext = context;
	}

	public RequestHandler withIntent(Intent requestIntent) {
		this.mRequestIntent = new Intent(requestIntent);
		mProcessor = getResponseProcessor();
		return this;
	}

	// -----------------------------------------------------------------
	// Initialization section
	// -----------------------------------------------------------------

	public void cancel() {
		this.mProcessor = null;
	}

	// -----------------------------------------------------------------
	// Access methods
	// -----------------------------------------------------------------

	public WithingsModule getApp() {
		WithingsModule app = (WithingsModule) mContext
				.getApplicationContext();
		return app;
	}

	// -----------------------------------------------------------------
	// Execution
	// -----------------------------------------------------------------

	@Override
	public void run() {

		final ConnectionResponse response = handleRequest();
		if (mProcessor != null)
			mProcessor.processResponse(response);

		notifyCallback(mRequestIntent);
	}

	// -----------------------------------------------------------------
	// Notifications
	// -----------------------------------------------------------------

	private void notifyCallback(Intent intent) {
		if (intent.hasExtra(RestService.EXTRA_NO_CALLBACK)) {
			intent.putExtra(RestService.EXTRA_NO_CALLBACK, false);
			return;
		}

		if (intent.hasExtra(RestService.ERROR_MESSAGE)) {
			notifyError(intent);
		} else
			notifySuccess(intent);
	}

	private void notifyError(final Intent intent) {
		mUiHandler.post(new Runnable() {

			@Override
			public void run() {
				ConnectionManager.getInstance().notifyErrorCallbacks(intent);
				if (intent.getStringExtra(RestService.ERROR_MESSAGE) == null)
					notifyInternalError(intent);
			}
		});
	}
	
	private void notifyInternalError(Intent intent) {
		ConnectionManager.getInstance().notifyInternalErrorCallbacks(intent);
	}

	private void notifySuccess(final Intent intent) {
		mUiHandler.post(new Runnable() {

			@Override
			public void run() {
				ConnectionManager.getInstance().notifySuccessCallbacks(intent);
			}
		});
	}
}
