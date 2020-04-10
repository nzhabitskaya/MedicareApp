package com.mobile.android.withings.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Debug;

import com.mobile.android.withings.application.WithingsModule;
import com.mobile.android.withings.service.connection.ConnectionManager;
import com.mobile.android.withings.service.connection.NetworkManager;
import com.mobile.android.withings.service.handlers.AccessTokenHandler;
import com.mobile.android.withings.service.handlers.EndUserAuthorizationHandler;
import com.mobile.android.withings.service.handlers.GetMeasuresHandler;
import com.mobile.android.withings.service.handlers.GetUsersListHandler;
import com.mobile.android.withings.service.handlers.OauthTokenHandler;
import com.mobile.android.withings.service.handlers.RequestHandler;

public class RestService extends IntentService {

	public static final String TAG = "RestService";

	public static class Action {
		public static final String REQUEST_OAUTH_TOKEN = "com.mobile.android.REQUEST_OAUTH_TOKEN";
        public static final String REQUEST_AUTHORIZE = "com.mobile.android.REQUEST_AUTHORIZE";
        public static final String REQUEST_ACCESS_TOKEN = "com.mobile.android.REQUEST_ACCESS_TOKEN";
		public static final String REQUEST_MEASURES = "com.mobile.android.GET_MEASURES";
		public static final String REQUEST_USERS_LIST = "com.mobile.android.GET_USERS_LIST";
	}

	public static final String ERROR_MESSAGE = "com.mobile.android.extras.ERROR_MESSAGE";
	
	public static final String ERROR_ID = "com.mobile.android.extras.ERROR_ID";

	public static final String EXTRA_INTERNAL_HANDLER = "com.mobile.android.extras.EXTRA_INTERNAL_HANDLER";
	
	public static final String EXTRA_JSON = "com.mobile.android.extras.EXTRA_JSON";

	public static final String EXTRA_NO_CALLBACK = "com.mobile.android.extras.EXTRA_NO_CALLBACK";
	
	public static final String EXTRA_RESPONSE = "com.mobile.android.extras.EXTRA_RESPONSE";
	
	public static final String EXTRA_ACCOUNT_SCOPE = "com.mobile.android.extras.EXTRA_ACCOUNT_SCOPE";
	
	// Response variables
	public static final String OAUTH_TOKEN = "com.mobile.android.extras.OAUTH_TOKEN";
    public static final String OAUTH_TOKEN_SECRET = "com.mobile.android.extras.OAUTH_TOKEN_SECRET";

    public static final String OAUTH_WEB_REDIRECT = "com.mobile.android.extras.OAUTH_WEB_REDIRECT";

	public static final String RESPONSE_STATUS = "com.mobile.android.extras.RESPONSE_STATUS";

	static int EXECUTED_COUNT = 0;

	private static ExecutorService sExecutorService;

	private static Map<String, RequestHandler> sHandlers;

	static {
		sExecutorService = Executors.newCachedThreadPool();
		sHandlers = new HashMap<String, RequestHandler>();
	}

	public static final void cancel(String action) {
		sHandlers.get(action).cancel();
	}

	// Default constructor
	public RestService() {
		super("RestService");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Debug.stopMethodTracing();
	}

	private WithingsModule getApp() {
		WithingsModule app = (WithingsModule) getApplicationContext();
		return app;
	}

	// -----------------------------------------------------------------
	// Initialization section
	// -----------------------------------------------------------------
	private void init() {
		final Context context = getApplicationContext();
		sHandlers.put(Action.REQUEST_OAUTH_TOKEN, new OauthTokenHandler(context));
        sHandlers.put(Action.REQUEST_AUTHORIZE, new EndUserAuthorizationHandler(context));
        sHandlers.put(Action.REQUEST_ACCESS_TOKEN, new AccessTokenHandler(context));
		sHandlers.put(Action.REQUEST_MEASURES, new GetMeasuresHandler(context));
		sHandlers.put(Action.REQUEST_USERS_LIST, new GetUsersListHandler(context));
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		EXECUTED_COUNT++;

		final String action = intent.getAction();

		// Not actual now because we are using multithreading
		// Debug.startMethodTracing(TAG + "#" + action + "_" + EXECUTED_COUNT);

		if (action == null || !sHandlers.containsKey(action))
			throw new IllegalArgumentException(
					"Action " + action + " is not provided");

		// Check INTERNET connection state
		if (!checkNetworkConnection(intent))
			return;

		// Provide request to our thread pool
		sExecutorService.submit(sHandlers.get(intent.getAction()).withIntent(
				intent));

	}

	/**
	 * Check if network is reachable
	 * 
	 * @param intent
	 * @return
	 */
	private boolean checkNetworkConnection(final Intent intent) {
		if (!NetworkManager.isConnected(this)) {
			// Notify activity about error
			WithingsModule.sHandler.post(new Runnable() {

				@Override
				public void run() {
					ConnectionManager.getInstance().notifyErrorCallbacks(intent);
				}
			});
			return false;
		}
		return true;
	}
}
