package com.mobile.android.withings.service.helper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.text.TextUtils;
import android.util.Log;

import com.mobile.android.chart.data.DataManager;
import com.mobile.android.withings.service.connection.API;
import com.mobile.android.withings.service.connection.OAuth;
import com.mobile.android.withings.service.connection.SignTool;
import com.mobile.android.withings.service.ssl.CustomSSLSocketFactory;

public class HttpHelper {

    public enum RequestMethod {
        GET_SIGNED_WITH_SECRET, GET, POST, DELETE, PUT, HEAD
    };

    /**
     * Server URL
     */
    private String mServerUrl = "";

    // Cookie store
    public static CustomCookieStore cookieStore;

    // Create local HTTP context
    public static HttpContext localContext;

    private static HttpParams httpParameters;

    // Bind custom cookie store to the local context
    private DefaultHttpClient httpClient;

    // Block to initialize HTTP Client
    {
        refresh();
    }

    public final void refresh() {
        cookieStore = new CustomCookieStore();

        localContext = new BasicHttpContext();
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

        httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        int timeoutConnection = 120000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 120000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        // Resolving problems with handshakes
        httpParameters.setBooleanParameter("http.protocol.expect-continue", false);

        httpClient = getSecuredHttpClient();
        httpClient.setParams(httpParameters);
        httpClient.setCookieStore(cookieStore);
    }

    // -----------------------------------------------------------------
    // Factory methods
    // -----------------------------------------------------------------

    // Request data
    private List<NameValuePair> mParameters;

    private List<NameValuePair> mHeaders;

    // private List<NameValuePair> mUrlParameters;

    private String mPostBody;

    /**
     * Create Rest client for request to <code>fullPathApiUrl</code>
     * 
     * @param fullPathApiUrl - api url
     */
    public HttpHelper(String fullPathApiUrl) {

        mServerUrl = fullPathApiUrl;
        mParameters = new ArrayList<NameValuePair>();
        mHeaders = new ArrayList<NameValuePair>();
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setRequestedUrl(String requestUrl) {
        mServerUrl = requestUrl;
    }

    /**
     * Add parameter to request
     * 
     * @param name
     * @param value
     */
    public void addParam(String name, String value) {
        mParameters.add(new BasicNameValuePair(name, value));
    }
    
    public List<NameValuePair> getParams() {
        return mParameters;
    }

    public void addHeader(String name, String value) {
        mHeaders.add(new BasicNameValuePair(name, value));
    }
    
    public Header[] getHeaders() {
        return null;
    }

    public void setPostBody(String body) {
        this.mPostBody = body;
    }

    public void reset() {
        mHeaders.clear();
        mParameters.clear();
        mPostBody = null;
    }

    public HttpResponse execute() throws ClientProtocolException, IOException, Exception {
    	return execute(RequestMethod.GET);
    }

    public HttpResponse execute(RequestMethod method) throws ClientProtocolException, IOException,
            Exception {
        HttpUriRequest httpRequest = null;
        switch (method) {
            case GET:
                httpRequest = buildGETRequest();
                break;
            case GET_SIGNED_WITH_SECRET:
                httpRequest = buildGETSignedRequest(RequestMethod.GET_SIGNED_WITH_SECRET);
                break;
            case POST:
                httpRequest = buildPOSTRequest();
                break;
            case DELETE:
                httpRequest = buildDELETERequest();
                break;
            case PUT:
                httpRequest = buildPUTRequest();
                break;
            case HEAD:
                httpRequest = buildHEADRequest();
                break;
            default:
                // UNKNOWN METHOD
                throw new IllegalArgumentException("Unknown HTTP Method");
        }

        // IF DEBUG:
        dumpRequest(httpRequest);

        // Now we can reset Rest Client
        reset();

        HttpResponse httpResponse = httpClient.execute(httpRequest, localContext);
        return httpResponse;

    }

    private HttpUriRequest buildGETRequest() {
        return buildUriRequest(RequestMethod.GET);
    }

    private HttpUriRequest buildGETSignedRequest(final RequestMethod method) {
        return buildUriRequest(method);
    }

    private HttpUriRequest buildDELETERequest() {
        return buildUriRequest(RequestMethod.DELETE);
    }

    private String getUrlParameters() {
        StringBuilder urlBuilder = new StringBuilder("");

        // Adding parameters
        for (NameValuePair pair : mParameters) {
            if (urlBuilder.length() > 1)
                urlBuilder.append("&");

            urlBuilder.append(pair.getName() + "=" + pair.getValue());
        }

        return urlBuilder.toString();
    }

    public HttpUriRequest buildUriRequest(RequestMethod mode) {

        HttpUriRequest httpUri = null;
        switch (mode) {
            case GET:
                Log.e("", "GET request: " + mServerUrl + "?" + getUrlParameters());
                httpUri = new HttpGet(mServerUrl + "?" + getUrlParameters());
                break;
            case GET_SIGNED_WITH_SECRET:
                StringBuilder requestStr = new StringBuilder();
                requestStr.append("GET&");
                try {
                    requestStr.append(URLEncoder.encode(mServerUrl, "utf-8"));
                    requestStr.append("&");
                    requestStr.append(URLEncoder.encode(getUrlParameters(), "utf-8"));
                } catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }

                Log.d("", "Request to sign: " + requestStr.toString());
                String secondSecret = DataManager.getInstance().getOauthSecret();
                String doubleSecret = OAuth.SECRET + (secondSecret != null ? secondSecret : "");
                String oauth_signature = SignTool.generateHmacSHA1Signature(requestStr.toString(), doubleSecret);
                Log.d("", "Sign request with secret: " + doubleSecret);

                StringBuilder requestStrSigned = new StringBuilder();
                requestStrSigned.append(mServerUrl);
                requestStrSigned.append("?");
                requestStrSigned.append(getUrlParameters());
                requestStrSigned.append("&");
                requestStrSigned.append(API.TAG.OAUTH_SIGNATURE);
                requestStrSigned.append("=");

                try {
                    // Encode the binary signature into base64 for use within a URL
                    requestStrSigned.append(URLEncoder.encode(oauth_signature, "utf-8"));
                } catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }

                String signedRequest = requestStrSigned.toString();
                String signedRequestStr = signedRequest.substring(0, signedRequest.length() - 3);
                Log.d("", "Final signed request: " + signedRequestStr);
                httpUri = new HttpGet(signedRequestStr);

                break;

            case DELETE:
                httpUri = new HttpDelete(mServerUrl + "?" + getUrlParameters());
                break;

            default:
                break;
        }

        // Adding headers
        for (NameValuePair pair : mHeaders)
            httpUri.addHeader(pair.getName(), pair.getValue());

        return httpUri;
    }

    private String signRequest(String requestStr, String key){
        //try {
            //return SignTool.hmacSha1(requestStr, key);
            return SignTool.generateHmacSHA1Signature(requestStr, key);

        /*} catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        } catch (UnsupportedEncodingException e){
            e.printStackTrace();
        } catch (InvalidKeyException e){
            e.printStackTrace();
        }
        return "";*/
    }

    private HttpUriRequest buildPOSTRequest() throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(mServerUrl + "?" + getUrlParameters());

        // Adding headers
        for (NameValuePair pair : mHeaders)
            httpPost.addHeader(pair.getName(), pair.getValue());

        // Adding Post String Body. Parameters will be ignored
        if (mPostBody != null && !"".equals(mPostBody)) {
            //Log.i(Constants.TAG, mPostBody);
            httpPost.setEntity(new StringEntity(mPostBody, HTTP.UTF_8));
        } else if (!mParameters.isEmpty()) // Adding parameters
            httpPost.setEntity(new UrlEncodedFormEntity(mParameters, HTTP.UTF_8));

        return httpPost;
    }

    private HttpUriRequest buildPUTRequest() throws UnsupportedEncodingException {
        HttpPut httpPut = new HttpPut(mServerUrl);
        // Adding headers
        for (NameValuePair pair : mHeaders)
            httpPut.addHeader(pair.getName(), pair.getValue());

        // Adding Post String Body. Parameters will be ignored
        if (mPostBody != null && !"".equals(mPostBody)) {
        	//Log.i(Constants.TAG, mPostBody);
            httpPut.setEntity(new StringEntity(mPostBody, HTTP.UTF_8));
        } else if (!mParameters.isEmpty()) // Adding parameters
            httpPut.setEntity(new UrlEncodedFormEntity(mParameters, HTTP.UTF_8));

        return httpPut;
    }
    
    private HttpUriRequest buildHEADRequest() throws UnsupportedEncodingException {
    	HttpHead httpHead = new HttpHead(mServerUrl);
        // Adding headers
        for (NameValuePair pair : mHeaders)
        	httpHead.addHeader(pair.getName(), pair.getValue());
        return httpHead;
    }

    public void setCookie(Cookie cookie) {
        cookieStore.addCookie(cookie);
    }

    static class CustomCookieStore extends BasicCookieStore {

        public Cookie getCookie(String name) {
            List<Cookie> cookies = getCookies();
            for (int i = 0; i < cookies.size(); i++) {
                Cookie cookie = cookies.get(i);
                if (cookie.getName().equals(name))
                    return cookie;
            }
            return null;
        }
    }

    // -----------------------------------------------------------------
    // Additional Methods
    // -----------------------------------------------------------------

    private synchronized void dumpRequest(HttpRequest httpRequest) {
    	//Log.i(Constants.TAG, "ID=" + Thread.currentThread().getId());
    	//Log.i(Constants.TAG, "Http Request");
    	Log.i(OAuth.TAG, "------------------------------------------------------");
    	Log.i(OAuth.TAG, httpRequest.getRequestLine().toString());
        // Printing headers:
        if (mHeaders != null && mHeaders.size() > 0) {
        	//Log.i(Constants.TAG, "Headers:");
            for (NameValuePair pair : mHeaders) {
            	//Log.i(Constants.TAG, pair.getName() + ": " + pair.getValue());
            }
        }
        // Printing Parameters
        if (mParameters != null && mParameters.size() > 0) {
        	//Log.i(Constants.TAG, "Params:");
            for (NameValuePair pair : mParameters) {
            	//Log.i(Constants.TAG, pair.getName() + ": " + pair.getValue());
            }
        }
        if (!TextUtils.isEmpty(mPostBody)) {
        	//Log.i(Constants.TAG, "Body:");
        	//Log.i(Constants.TAG, mPostBody);
        }
    }

    private synchronized void dumpResponse() {
        // TODO: Add dump for response here
    }

    // ------------------------------------------------------------------------------------------
    // Secured HttpClient
    // ------------------------------------------------------------------------------------------

    public static DefaultHttpClient getSecuredHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            CustomSSLSocketFactory sf = new CustomSSLSocketFactory(trustStore);
            sf.setHostnameVerifier(CustomSSLSocketFactory.STRICT_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
}
