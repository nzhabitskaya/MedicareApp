package com.mobile.android.withings.utils;

import com.google.gson.Gson;

public class JSONUtils {
	private static Gson sGson;

	public static Gson getGson() {
		if (sGson == null)
			sGson = new Gson();
		return sGson;
	}

	/*static Gson sGson = new GsonBuilder().setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.VOLATILE)
            .setDateFormat(Formatter.ISO8601FORMAT).create();*/

    public static final String toJson(Object obj) {
        return getGson().toJson(obj);
    }

    public static final Object fromJson(String json, Class<?> clazz) {
        return getGson().fromJson(json, clazz);
    }

    public static String getJSONFromResponseStr(String responseStr){
        return "{\"" + responseStr.replaceAll("=", "\"=\"").replaceAll("&", "\", \"") + "\"}";

    }

    public static String parseJSONFromResponseStr(String responseStr){
        return "{" + responseStr.replaceAll("&", "\", ")
                .replace("oauth_consumer_key=", "\"" + "oauth_consumer_key" + "\"=\"")
                .replace("oauth_nonce=", "\"" + "oauth_nonce" + "\"=\"")
                .replace("oauth_signature_method=", "\"" + "oauth_signature_method" + "\"=\"")
                .replace("oauth_timestamp=", "\"" + "oauth_timestamp" + "\"=\"")
                .replace("oauth_token=", "\"" + "oauth_token" + "\"=\"")
                .replace("oauth_version=", "\"" + "oauth_version" + "\"=\"")
                .replace("oauth_signature=", "\"" + "oauth_signature" + "\"=\"") + "\"}";
    }
}