package com.mobile.android.withings.service.rest.api;


import com.google.gson.annotations.SerializedName;

public class GetMeasuresRequest {
    @SerializedName("key")
    private String key;

    @SerializedName("oauth_token")
    private String token;

    @SerializedName("email")
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
