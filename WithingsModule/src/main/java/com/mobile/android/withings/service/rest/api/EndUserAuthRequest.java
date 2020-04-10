package com.mobile.android.withings.service.rest.api;

import com.google.gson.annotations.SerializedName;

public class EndUserAuthRequest {

    @SerializedName("key")
    private String key;

    @SerializedName("oauth_token")
    private String token;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
