package com.mobile.android.withings.service.connection.errors;

import com.mobile.android.withings.utils.JSONUtils;

public class RestError {

    private String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public static final String fromJson(final String json) {
        try {
        	RestError error = (RestError)JSONUtils.fromJson(json, RestError.class);
            return error.getErrorCode();
        } catch (Exception e) {
            return null;
        }
    }
}
