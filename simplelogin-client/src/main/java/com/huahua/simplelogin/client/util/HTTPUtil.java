package com.huahua.simplelogin.client.util;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HTTPUtil {
    public static boolean post(String url, Map<String,String> params){
        CloseableHttpClient httpClient= HttpClients.createDefault();
        HttpPost httpPost=new HttpPost(url);
        if(params !=null && !params.isEmpty()){
            List<NameValuePair> list=new ArrayList<NameValuePair>();
            Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<String,String> entry=it.next();
                list.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));

            }
            httpPost.setEntity(new UrlEncodedFormEntity(list, Consts.UTF_8));
        }
        try {
            CloseableHttpResponse response=httpClient.execute(httpPost);
            response.getStatusLine().getStatusCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
