package com.mobile.android.withings.service.rest.api;

import com.google.gson.annotations.Expose;


public class AccessTokenResponse extends BaseResponse{
    @Expose
    private String oauth_token;
    @Expose
    private String oauth_token_secret;

    public String getAccessToken() {
        return oauth_token;
    }

    public String getAccessSecret() {
        return oauth_token_secret;
    }
}