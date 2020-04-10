package com.mobile.android.withings.service.rest.api;

import com.google.gson.annotations.SerializedName;

public class AccessTokenRequest {

    @SerializedName("key")
    private String key;

    @SerializedName("oauth_token")
    private String token;

    @SerializedName("oauth_token_secret")
    private String secret;

    public String getKey() {
        return key;
    }

    public String getToken() {
        return token;
    }

    public String getSecret() {
        return secret;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
