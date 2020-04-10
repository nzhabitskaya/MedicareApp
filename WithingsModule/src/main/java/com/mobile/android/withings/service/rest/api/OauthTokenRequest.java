package com.mobile.android.withings.service.rest.api;

import com.google.gson.annotations.SerializedName;

public class OauthTokenRequest {
	
	@SerializedName("key")
    private String key;
	
	@SerializedName("secret")
    private String secret;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
}
