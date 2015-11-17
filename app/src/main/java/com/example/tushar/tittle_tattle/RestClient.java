package com.example.tushar.tittle_tattle;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.webkit.URLUtil;
import android.widget.EditText;

import com.loopj.android.http.*;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpUriRequest;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;


/**
 * Created by tushar on 2/11/15.
 */
public class RestClient {


    public enum RequestMethod {
        GET,
        POST
    }

    private String mUUID=null;
    private String mOtp=null;
    public int responseCode = 0;
    public String message;
    public String response;

    public void Execute(RequestMethod method, String url, ArrayList<NameValuePair> headers, ArrayList<NameValuePair> params) throws Exception {
        switch (method) {
            case GET: {
                // add parameters
                String combinedParams = "";
                if (params != null) {
                    combinedParams += "?";
                    for (NameValuePair p : params) {
                        String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");
                        if (combinedParams.length() > 1)
                            combinedParams += "&" + paramString;
                        else
                            combinedParams += paramString;
                    }
                }
                HttpGet request = new HttpGet(url + combinedParams);
                // add headers
                if (headers != null) {
                    headers = addCommonHeaderField(headers);
                    for (NameValuePair h : headers)
                        request.addHeader(h.getName(), h.getValue());
                }
                executeRequest(request, url,0);
                break;
            }
            case POST: {
                HttpPost request = new HttpPost(url);
                // add headers
                if (headers != null) {
                    headers = addCommonHeaderField(headers);
                    for (NameValuePair h : headers)
                        request.addHeader(h.getName(), h.getValue());
                }
                if (params != null)
                    request.setEntity(new StringEntity("{\"mobileNo\":\"9956930192\"}", "UTF-8"));
                executeRequest(request, url,0);
                break;
            }
        }
    }

    public void ExecuteLoginRequst(String url, ArrayList<NameValuePair> headers, ArrayList<NameValuePair> params) throws Exception {

                HttpPost request = new HttpPost(url);
                // add headers
               /* if (headers != null) {
                    headers = addCommonHeaderField(headers);
                    for (NameValuePair h : headers)
                        request.addHeader(h.getName(), h.getValue());
                }*/

        boolean a=URLUtil.isValidUrl(url);
        JSONObject j=new JSONObject();
        j.put("mobileNo", "9987387342");

        String gsonString = j.toString();
       // Log.e("Posting", " Posting to web" + gsonString);
        StringEntity input = new StringEntity(gsonString);
        request.addHeader("Content-type", "application/json");
        input.setContentType("application/json");

        // httpPost.addHeader("Authorization", "Basic " + getAuthorizationHeaderValue());
        //request.addHeader("Content-type", "application/json");
        //StringEntity se = new StringEntity( j.toString());
          //          se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

               // if (params != null)
                    //request.setEntity(new StringEntity("{\"mobileNo\":\"9956767777\"}", "UTF-8"));
                //request.s();
                    request.setEntity(new StringEntity("{\"mobileNo\":\"9956767777\"}", "UTF-8"));
                    executeRequest(request, url, 0);


    }

    public void verifyOtpRequest(String url,String otp) {

        String uid=getmUUID();
            HttpPost request = new HttpPost(url);
            // add headers

              ArrayList<NameValuePair>  headers = new ArrayList<NameValuePair>();
                    headers=addCommonHeaderField(headers);
                for (NameValuePair h : headers)
                    request.addHeader(h.getName(), h.getValue());

                request.setEntity(new StringEntity("{\"uuid\":\""+getmUUID()+"\",\"otp\": \""+otp+"\"}", "UTF-8"));
            executeRequest(request, url, 0);



    }

    private ArrayList<NameValuePair> addCommonHeaderField(ArrayList<NameValuePair> _header) {
        _header.add(new BasicNameValuePair("Content-Type", "application/json"));
        return _header;
    }

    private void executeRequest(HttpUriRequest request, String url,int type) {
        HttpClient client = new DefaultHttpClient();
        HttpResponse httpResponse;
        try {
            httpResponse = client.execute(request);
            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();
            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);
                JSONObject OtpString=new JSONObject(response);
                if(type==0){
                    mUUID=OtpString.getString("uuid");
                    //mOtp=OtpString.getInt("otp");
                }
                instream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
        } catch (IOException e) {
        }
        return sb.toString();
    }

    public String getmOtp() {
        return mOtp;
    }

    public String getmUUID() {
        return mUUID;
    }
}
