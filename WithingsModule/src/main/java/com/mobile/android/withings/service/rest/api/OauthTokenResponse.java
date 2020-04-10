package com.mobile.android.withings.service.rest.api;

import com.google.gson.annotations.Expose;

public class OauthTokenResponse extends BaseResponse{
	@Expose
    private String oauth_token;
    @Expose
    private String oauth_token_secret;

    public String getOauthToken() {
        return oauth_token;
    }

    public String getOauthSecret() {
        return oauth_token_secret;
    }
}
