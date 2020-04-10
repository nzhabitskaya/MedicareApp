package com.mobile.android.withings.service.connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;

import android.content.Intent;
import android.text.TextUtils;

import com.mobile.android.database.utils.TimestampUtil;
import com.mobile.android.withings.service.connection.errors.ConnectionError;
import com.mobile.android.withings.service.connection.errors.OAuthError;
import com.mobile.android.withings.service.connection.errors.RestError;
import com.mobile.android.withings.service.helper.HttpHelper;
import com.mobile.android.withings.service.helper.HttpHelper.RequestMethod;
import com.mobile.android.withings.service.rest.callback.RestCallback;
import com.mobile.android.withings.utils.IOUtils;

public class ConnectionManager {

	public static final String EXTRA_CONNECTION_ERROR = "err_connection_error";

	private static ConnectionManager INSTANCE;

	// private Context mContext;

	private HttpHelper mHttpHelper;

	private HttpResponse httpResponse;

	private List<RestCallback> mListeners = new ArrayList<RestCallback>();

	public static ConnectionManager getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ConnectionManager();

		return INSTANCE;
	}

	public static final ConnectionManager newDefaultConnectionManager() {
		return new ConnectionManager();
	}

	private ConnectionManager() {
		mHttpHelper = new HttpHelper(API.BASE_URL);
	}

	public synchronized void registerResponseListener(RestCallback callback) {
		try {
			if (!mListeners.contains(callback))
				mListeners.add(callback);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void unregisterResponseListener(RestCallback callback) {
		try {
			if (mListeners.contains(callback))
				mListeners.remove(callback);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void unregisterListener(RestCallback callback) {
		try {
			if (mListeners.contains(callback))
				mListeners.remove(callback);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void unregisterAll() {
		mListeners.clear();
	}

	public synchronized void notifySuccessCallbacks(Intent intent) {
		for (int i = 0; i < mListeners.size(); i++) {
			final RestCallback callback = mListeners.get(i);
			if (callback != null){
				//Log.i(Constants.TAG, "notify success callbacks");
				callback.onSuccess(intent);
			}
		}
	}

	public synchronized void notifyErrorCallbacks(Intent intent) {
		for (int i = 0; i < mListeners.size(); i++) {
			final RestCallback callback = mListeners.get(i);
			if (callback != null)
				callback.onError(intent);
		}
	}
	
	public synchronized void notifyInternalErrorCallbacks(Intent intent) {
		for (int i = 0; i < mListeners.size(); i++) {
			final RestCallback callback = mListeners.get(i);
			if (callback != null)
				callback.onInternalError(intent);
		}
	}

	// -----------------------------------------------------------------
	// Requests
	// -----------------------------------------------------------------


    // http://oauth.withings.com/api -> Step 1 : get a oAuth "request token"
	public ConnectionResponse requestOauthToken(String key) {
		mHttpHelper.setRequestedUrl(API.REQUEST_OAUTH_TOKEN);
		mHttpHelper.addHeader("Content-Type",
				"application/x-www-form-urlencoded");
        mHttpHelper.addParam(API.TAG.OAUTH_CALLBACK, "");
		mHttpHelper.addParam(API.TAG.OAUTH_CONSUMER_KEY, key);
        mHttpHelper.addParam(API.TAG.OAUTH_NONCE, TimestampUtil.generateNonce(32));
        mHttpHelper.addParam(API.TAG.OAUTH_SIGNATURE_METHOD, OAuth.HMAC_SHA1);
        mHttpHelper.addParam(API.TAG.OAUTH_TIMESTAMP, TimestampUtil.getCurrentTimestamp());
        mHttpHelper.addParam(API.TAG.OAUTH_VERSION, "1.0");

        return synchronizedExecute(RequestMethod.GET_SIGNED_WITH_SECRET);
	}

    // http://oauth.withings.com/api -> Step 2 : End-user authorization
    public String requestEndUserAuthorizationUrl(String key, String token) {
        mHttpHelper.setRequestedUrl(API.REQUEST_AUTHORIZE);
        mHttpHelper.addHeader("Content-Type",
                "application/x-www-form-urlencoded");
        mHttpHelper.addParam(API.TAG.OAUTH_CONSUMER_KEY, key);
        mHttpHelper.addParam(API.TAG.OAUTH_NONCE, TimestampUtil.generateNonce(32));
        mHttpHelper.addParam(API.TAG.OAUTH_SIGNATURE_METHOD, OAuth.HMAC_SHA1);
        mHttpHelper.addParam(API.TAG.OAUTH_TIMESTAMP, TimestampUtil.getCurrentTimestamp());
        mHttpHelper.addParam(API.TAG.OAUTH_TOKEN, token);
        mHttpHelper.addParam(API.TAG.OAUTH_VERSION, "1.0");

        return getRequest(RequestMethod.GET_SIGNED_WITH_SECRET);
    }

    // http://oauth.withings.com/api -> Step 3 : End-user authorization
    public ConnectionResponse requestEndUserAuthorization(String key, String token) {
        mHttpHelper.setRequestedUrl(API.REQUEST_ACCESS_TOKEN);
        mHttpHelper.addHeader("Content-Type",
                "application/x-www-form-urlencoded");
        mHttpHelper.addParam(API.TAG.OAUTH_CONSUMER_KEY, key);
        mHttpHelper.addParam(API.TAG.OAUTH_NONCE, TimestampUtil.generateNonce(32));
        mHttpHelper.addParam(API.TAG.OAUTH_SIGNATURE_METHOD, OAuth.HMAC_SHA1);
        mHttpHelper.addParam(API.TAG.OAUTH_TIMESTAMP, TimestampUtil.getCurrentTimestamp());
        mHttpHelper.addParam(API.TAG.OAUTH_TOKEN, token);
        mHttpHelper.addParam(API.TAG.OAUTH_VERSION, "1.0");

		return synchronizedExecute(RequestMethod.GET_SIGNED_WITH_SECRET);
    }

	// Work with withing api: get measures
	public ConnectionResponse getMeasures(String key, String token) {
		mHttpHelper.setRequestedUrl(API.REQUEST_MEASURES);
		mHttpHelper.addHeader("Content-Type",
				"application/x-www-form-urlencoded");
		mHttpHelper.addParam(API.TAG.ACTION, API.ACTION.GET_MEASURES);

		mHttpHelper.addParam("appli", "1");

		//mHttpHelper.addParam(API.TAG.DEVICE_TYPE, "1");
		//mHttpHelper.addParam(API.TAG.MEASURES_TYPE, "1");

		mHttpHelper.addParam(API.TAG.OAUTH_CONSUMER_KEY, key);
		mHttpHelper.addParam(API.TAG.OAUTH_NONCE, TimestampUtil.generateNonce(32));
		mHttpHelper.addParam(API.TAG.OAUTH_SIGNATURE_METHOD, OAuth.HMAC_SHA1);
		mHttpHelper.addParam(API.TAG.OAUTH_TIMESTAMP, TimestampUtil.getCurrentTimestamp());
		mHttpHelper.addParam(API.TAG.OAUTH_TOKEN, token);
		mHttpHelper.addParam(API.TAG.OAUTH_VERSION, "1.0");

		return synchronizedExecute(RequestMethod.GET_SIGNED_WITH_SECRET);
	}

	public ConnectionResponse getUsersList(String key, String token) {
		mHttpHelper.setRequestedUrl(API.REQUEST_ACCOUNT);
		mHttpHelper.addHeader("Content-Type",
				"application/x-www-form-urlencoded");
		mHttpHelper.addParam(API.TAG.ACTION, API.ACTION.GET_USERS_LIST);

		mHttpHelper.addParam("appli", "1");
		mHttpHelper.addParam(API.TAG.EMAIL, OAuth.USER_EMAIL);

		//mHttpHelper.addParam(API.TAG.DEVICE_TYPE, "1");
		//mHttpHelper.addParam(API.TAG.MEASURES_TYPE, "1");

		mHttpHelper.addParam(API.TAG.OAUTH_CONSUMER_KEY, key);
		mHttpHelper.addParam(API.TAG.OAUTH_NONCE, TimestampUtil.generateNonce(32));
		mHttpHelper.addParam(API.TAG.OAUTH_SIGNATURE_METHOD, OAuth.HMAC_SHA1);
		mHttpHelper.addParam(API.TAG.OAUTH_TIMESTAMP, TimestampUtil.getCurrentTimestamp());
		mHttpHelper.addParam(API.TAG.OAUTH_TOKEN, token);
		mHttpHelper.addParam(API.TAG.OAUTH_VERSION, "1.0");

		return synchronizedExecute(RequestMethod.GET_SIGNED_WITH_SECRET);
	}

	// -----------------------------------------------------------------
	// Headers
	// -----------------------------------------------------------------

	private void setDefaultUrlEncodedHeaders() {
		mHttpHelper.addHeader("Content-Type",
				"application/x-www-form-urlencoded");
		mHttpHelper.addHeader("Accept", "application/json");
	}

	private void setDefaultJsonHeaders() {
		mHttpHelper.addHeader("Content-Type", "application/json");
		mHttpHelper.addHeader("Accept", "application/json");
	}

	private void setDefaultOctetStreamHeaders() {
		mHttpHelper.addHeader("Content-Type",
				"application/x-www-form-urlencoded");
		mHttpHelper.addHeader("Accept", "application/octet-stream");
	}

	public Header[] getHeaders() {
		return httpResponse.getAllHeaders();
	}

	public String getHeaders(String name) {
		Header header = httpResponse.getHeaders(name)[0];
		//Log.i(Constants.TAG, header.toString());
		return header.getValue();
	}

	// -----------------------------------------------------------------
	// High level API methods to get Response from the server
	// -----------------------------------------------------------------

	public ConnectionResponse putJson(String api, String json,
			String accessToken, boolean cpJson) {
		return getResponse(RequestMethod.PUT, api, json, accessToken, cpJson);
	}

	public ConnectionResponse postJson(String json, String accessToken,
			boolean cpJson) {
		return getResponse(RequestMethod.POST, null, json, accessToken, cpJson);
	}

	public ConnectionResponse putJson(String api, String json,
			String accessToken) {
		return getResponse(RequestMethod.PUT, api, json, accessToken, false);
	}

	public ConnectionResponse postJson(String json, String accessToken) {
		return getResponse(RequestMethod.POST, null, json, accessToken, false);
	}

	public ConnectionResponse delete(String api, String accessToken,
			boolean cpJson) {
		return getResponse(RequestMethod.DELETE, api, null, accessToken, cpJson);
	}

	public ConnectionResponse delete(String api, String accessToken) {
		return getResponse(RequestMethod.DELETE, null, api, accessToken, false);
	}

	public ConnectionResponse getResponse(String api, String accessToken,
			boolean cpJson) {
		return getResponse(RequestMethod.GET, api, null, accessToken, cpJson);
	}

	public ConnectionResponse getResponse(String api, String accessToken,
			boolean cpJson, int from, int count) {
		mHttpHelper.addParam("start", "" + from);
		mHttpHelper.addParam("limit", "" + count);
		return getResponse(RequestMethod.GET, api, null, accessToken, cpJson);
	}

	public ConnectionResponse getResponse(String api, String accessToken,
			boolean cpJson, String tripId) {
		mHttpHelper.addParam("tripId", tripId);
		return getResponse(RequestMethod.PUT, api, null, accessToken, cpJson);
	}

	public ConnectionResponse getResponse(String api, String accessToken) {
		return getResponse(RequestMethod.GET, api, null, accessToken, false);
	}

	public ConnectionResponse getResponse(RequestMethod method, String api,
			String json, String accessToken, boolean cpJson) {
		if (api != null)
			mHttpHelper.setRequestedUrl(api);

		if (json != null)
			mHttpHelper.setPostBody(json);

		if (cpJson)
			setDefaultJsonHeaders();
		else
			setDefaultUrlEncodedHeaders();

		if (accessToken != null)
			mHttpHelper.addHeader("Authorization", "Bearer " + accessToken);
		return synchronizedExecute(method);
	}

	// -----------------------------------------------------------------
	// Execution
	// -----------------------------------------------------------------

    private String getRequest(final RequestMethod method) {
        return mHttpHelper.buildUriRequest(method).getURI().toString();
    }

	private ConnectionResponse synchronizedExecute(final RequestMethod method) {
		// synchronized (this) {
		final ConnectionResponse response = execute(method);
		return response;
		// }
	}

	private ConnectionResponse execute(RequestMethod method) {
		ConnectionResponse response = new ConnectionResponse();

		try {
			httpResponse = mHttpHelper.execute(method);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			//Log.i(Constants.TAG, "ID=" + Thread.currentThread().getId());
			//Log.i(Constants.TAG, statusCode + " "
					//+ httpResponse.getStatusLine().getReasonPhrase());
			// if (shutdown)
			// return null; // We don't want to handle this response
			if (statusCode != HttpStatus.SC_OK
					&& statusCode != HttpStatus.SC_NO_CONTENT) {
				ConnectionError error = new ConnectionError(statusCode,
						fetchError(httpResponse));
				response.setError(error);
				return response;
			}

			if (statusCode != HttpStatus.SC_NO_CONTENT) {
				String responseString = fetchContent(httpResponse);
				response.setResponse(responseString);
			} else {
				// D.i(this, "204 NO Content");
			}

			// Notifying about response

			return response;

		} catch (ClientProtocolException e) {
			ConnectionError error = new ConnectionError(-1,
					EXTRA_CONNECTION_ERROR);
			response.setError(error);
			e.printStackTrace();
		} catch (IOException e) {
			ConnectionError error = new ConnectionError(-1,
					EXTRA_CONNECTION_ERROR);
			response.setError(error);
			e.printStackTrace();
		} catch (Exception e) {
			ConnectionError error = new ConnectionError(-1,
					EXTRA_CONNECTION_ERROR);
			response.setError(error);
			e.printStackTrace();
		} finally {
			mHttpHelper.getHttpClient().getConnectionManager().shutdown();
			mHttpHelper.refresh();
		}

		return response;
	}

	// -----------------------------------------------------------------
	// Utils
	// -----------------------------------------------------------------

	private String fetchContent(HttpResponse httpResponse)
			throws IllegalStateException, IOException {
		if (httpResponse != null) {
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				final String content = IOUtils.convertStreamToString(httpEntity
						.getContent());
				//Log.i(Constants.TAG, "Http Response");
				//Log.i(Constants.TAG, content);
				return content;
			}
		}

		return "";
	}

	private String fetchError(HttpResponse httpResponse)
			throws IllegalStateException, IOException {
		final int httpCode = httpResponse.getStatusLine().getStatusCode();
		final String content = fetchContent(httpResponse);
		// Trying to parse error from the Way2 server:
		String sError = RestError.fromJson(content);
		if (sError == null) {
			if (httpCode == 401 && TextUtils.isEmpty(content)) {
				sError = "USER_NOT_AUTHORIZED";
				return sError;
			} else {
				// Trying to parse error from the OAuth service:
				sError = OAuthError.fromJson(content);
				if (sError == null && httpCode == 500) {
					sError = "SERVER_ERROR";
				} else {
					// We have Oauth Error
					if (sError != null) {
						sError = sError.replaceAll(" ", "_").toUpperCase();
						return sError;
					}
				}

			}

			if (sError == null)
				sError = "HTTP Error has occured: "
						+ httpResponse.getStatusLine().getStatusCode() + " ("
						+ httpResponse.getStatusLine().getReasonPhrase() + ")";
		}

		return sError;
	}
}