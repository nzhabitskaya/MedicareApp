package com.mobile.android.withings.service.connection.errors;

import com.mobile.android.withings.utils.JSONUtils;

public class OAuthError {

    private String error;

    private String error_description;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return error_description;
    }

    public void setErrorDescription(String error_description) {
        this.error_description = error_description;
    }

    public static final String fromJson(final String json) {
        try {
            OAuthError error = (OAuthError)JSONUtils.fromJson(json, OAuthError.class);
            return error.getErrorDescription();
        } catch (Exception e) {
            return null;
        }
    }
}