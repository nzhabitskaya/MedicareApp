package com.mobile.android.withings.service.rest.api;

import com.google.gson.annotations.Expose;

public class EndUserAuthResponse extends BaseResponse{
    @Expose
    private boolean acceptDelegation;
    @Expose
    private String oauth_consumer_key;
    @Expose
    private String oauth_nonce;
    @Expose
    private String oauth_signature;
    @Expose
    private String oauth_signature_method;
    @Expose
    private String oauth_timestamp;
    @Expose
    private String oauth_token;
    @Expose
    private String oauth_version;
    @Expose
    private String userid;

    public boolean isAcceptDelegation() {
        return acceptDelegation;
    }

    public String getKey() {
        return oauth_consumer_key;
    }

    public String getOauth_nonce() {
        return oauth_nonce;
    }

    public String getOauth_signature() {
        return oauth_signature;
    }

    public String getOauth_signature_method() {
        return oauth_signature_method;
    }

    public String getOauth_timestamp() {
        return oauth_timestamp;
    }

    public String getOauthToken() {
        return oauth_token;
    }

    public String getOauth_version() {
        return oauth_version;
    }

    public String getUserId() {
        return userid;
    }
}
