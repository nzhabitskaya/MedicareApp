package com.mobile.android.withings.service;

import android.content.Context;
import android.content.Intent;

import com.mobile.android.withings.service.RestService.Action;
import com.mobile.android.withings.service.rest.api.AccessTokenRequest;
import com.mobile.android.withings.service.rest.api.EndUserAuthRequest;
import com.mobile.android.withings.service.rest.api.GetMeasuresRequest;
import com.mobile.android.withings.service.rest.api.GetUsersListRequest;
import com.mobile.android.withings.service.rest.api.OauthTokenRequest;
import com.mobile.android.withings.utils.JSONUtils;

public class ServiceHelper {
	private Context mContext;
	private Intent mServiceIntent;

	public ServiceHelper(Context context) {
		this.mContext = context.getApplicationContext();
		mServiceIntent = new Intent(mContext, RestService.class);
	}

	// Methods
	public void requestOauthToken(OauthTokenRequest requestOauthToken) {
		runService(Action.REQUEST_OAUTH_TOKEN, requestOauthToken);
	}

    public void requestEndUserAuthorization(EndUserAuthRequest requestEndUserAuthorization) {
        runService(Action.REQUEST_AUTHORIZE, requestEndUserAuthorization);
    }

    public void requestAccessToken(AccessTokenRequest accessTokenRequest) {
        runService(Action.REQUEST_ACCESS_TOKEN, accessTokenRequest);
    }

	public void requestMeasures(GetMeasuresRequest requestGetMeasures) {
		runService(Action.REQUEST_MEASURES, requestGetMeasures);
	}

	public void requestUsersList(GetUsersListRequest requestGetUsersList) {
		runService(Action.REQUEST_USERS_LIST, requestGetUsersList);
	}

	public void runService(String action, Object jsonObject) {
		mServiceIntent.setAction(action);
		if (jsonObject != null) {
			mServiceIntent.putExtra(RestService.EXTRA_JSON,
					JSONUtils.toJson(jsonObject));
		}

		mContext.startService(mServiceIntent);
	}
}
