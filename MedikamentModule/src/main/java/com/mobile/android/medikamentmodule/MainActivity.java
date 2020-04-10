package com.mobile.android.medikamentmodule;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;

public class MainActivity extends Activity {
    private final String NAMESPACE = "http://swissindex.e-mediat.net/SwissindexPharma_out_V101/";
    private final String URL = "https://swissindex.refdata.ch/Swissindex/Pharma/ws_Pharma_V101.asmx";
    private final String METHOD = "GetByGTIN";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.test);

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();
    }

   private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        @Override
        protected String doInBackground(String... params) {
            publishProgress("Loading contents...");

            /*String result = "";
            SoapObject rpc = new SoapObject(NAMESPACE, METHOD_NAME);
            rpc.addProperty("GTIN", "7680336700367");
            rpc.addProperty("lang", "EN");
            HttpTransportSE httpTransport = new HttpTransportSE(URL);

            httpTransport.debug = BuildConfig.DEBUG;

            // Prod
            /*if (Constante.urlWebService.startsWith("https://")) {
                final List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
                headerList.add(new HeaderProperty("Authorization", "Basic "
                        + org.kobjects.base64.Base64.encode((Constante.CERTIFICAT_LOGIN + ":" + Constante.CERTIFICAT_MDP).getBytes())));

                CustomX509TrustManager.allowAllSSL();
                httpTransport.call(NAMESPACE + "/" + soapReq.getName(), soapEnvelope, headerList);
            }
            // Test
            else {
                httpTransport.call(NAMESPACE + "/" + soapReq.getName(), soapEnvelope);
            }*/

            /*CustomX509TrustManager.allowAllSSL();

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = rpc;
            envelope.dotNet = false;
            envelope.setOutputSoapObject(rpc);

            try {
                httpTransport.call(NAMESPACE + METHOD_NAME, envelope);
                envelope.getResponse();
                Log.e("", result);
                }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return result;*/

            try {
                // SoapEnvelop.VER11 is SOAP Version 1.1 constant
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                SoapObject request = new SoapObject(NAMESPACE, METHOD);
                //bodyOut is the body object to be sent out with this envelope
                envelope.bodyOut = request;
                HttpTransportSE transport = new HttpTransportSE(URL);
                try {
                    transport.call(NAMESPACE + METHOD, envelope);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                //bodyIn is the body object received with this envelope
                if (envelope.bodyIn != null) {
                    //getProperty() Returns a specific property at a certain index.
                    SoapPrimitive resultSOAP = (SoapPrimitive) ((SoapObject) envelope.bodyIn).getProperty(0);
                    resp=resultSOAP.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String result) {
            textView.setText(result);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {
            textView.setText(text[0]);
        }
    }
}