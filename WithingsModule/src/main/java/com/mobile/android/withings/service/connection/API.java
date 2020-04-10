package com.mobile.android.withings.service.connection;

// http://oauth.withings.com/api
public class API {

    public static final String BASE_URL = "https://oauth.withings.com/";

	public static final String REQUEST_OAUTH_TOKEN = BASE_URL + "account/request_token";

    public static final String REQUEST_AUTHORIZE = BASE_URL + "account/authorize";

    public static final String REQUEST_ACCESS_TOKEN = BASE_URL + "account/access_token";

	public static final String API_URL = "http://wbsapi.withings.net";

	public static final String REQUEST_ACCOUNT = API_URL + "/account";

	public static final String REQUEST_MEASURES = API_URL + "/measure";


	public class TAG {
		public static final String FORMAT = "format";
		public static final String JSON = "json";
		
		public static final String OAUTH_CALLBACK = "oauth_callback";
		public static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
		public static final String OAUTH_SIGNATURE = "oauth_signature";
		public static final String OAUTH_NONCE = "oauth_nonce";
		public static final String OAUTH_TIMESTAMP = "oauth_timestamp";
		public static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
        public static final String OAUTH_TOKEN = "oauth_token";
		public static final String OAUTH_VERSION = "oauth_version";
        public static final String USER_ID = "userid";
		public static final String EMAIL = "email";
		public static final String ACTION = "action";
		public static final String DEVICE_TYPE = "devtype";
		public static final String MEASURES_TYPE = "meastype";
		
	}

	public class ACTION {
		public static final String GET_MEASURES = "getmeas";
		public static final String GET_USERS_LIST = "getuserslist";
	}

	public class ERRORS {
	}
}
