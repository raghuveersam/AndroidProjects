package com.example.raghuveer.triviaapp;

//Rgahuveer Sampath Krishnamurthy
// John O' Connor

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Raghuveer on 9/26/2015.
 */
public class RequestParams {

    String method, base_url;
    HashMap<String, String> params = new HashMap<String, String>();

    public void addParams(String key, String value) {
        params.put(key, value);
    }

    public RequestParams(String method, String base_url) {
        this.method = method;
        this.base_url = base_url;
    }

    public String createQuestion(String question,String url,ArrayList<String> options,int answer){
        StringBuilder sb = new StringBuilder();
        sb.append(question);
        sb.append(";");
        for(String option:options){
            sb.append(option);
            sb.append(";");
        }
        sb.append(url);
        sb.append(";");
        sb.append(answer);
        sb.append(";");
        return sb.toString();
    }
    public String getEncodedParams() {

        StringBuilder sb = new StringBuilder();

        for (String key : params.keySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(key + "=" + params.get(key));
        }
        return sb.toString();
    }

    public String getEncodedUrl(){
        return this.base_url + "?" + getEncodedParams();
    }


    public HttpURLConnection setConnection() throws IOException {
        if(method.equals("POST")){
        try {
            URL url = new URL(this.base_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(this.method);
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(getEncodedParams());
            writer.flush();
            return con;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
        }
        else {
            if (method.equals("GET")) {
                URL url = new URL(getEncodedUrl());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                return con;
                //InputStreamReader reader = new InputStreamReader(con.getInputStream());
            }
        }
        return null;
    }
}
