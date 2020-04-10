package com.mobile.android.withings.service.rest.api;

import com.google.gson.annotations.Expose;

public class BaseResponse {
	@Expose
    private Boolean status;
	@Expose
    private String message;

	public boolean getStatus() {
		return status;
	}
	
	public String getMessage() {
		return message;
	}
}
