/*
 * Fiberhome SDN Platform
 * Copyright (c)  Fiberhome Technologies.
 * 88,YouKeYuan Road,Hongshan District.,Wuhan,P.R.China,
 * Wuhan Research Institute of Post &amp; Telecommunication. 
 *
 * All rights reserved.
 */
package org.onosproject.dhcp.utils;

import org.onosproject.dhcp.bean.NotifyNetconf;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 创建人: hwpeng5896
 * 创建时间 2017-05-06 上午11:20
 * 功能描述:这个类没有被调用，主要用来在分配完ip后通过netconf下发控制器ip给设备，
 * 让设备和控制器建立openfolw连接
 **/

public final class GetJsonStringAndNotify {

    public static StringBuffer getJson (NotifyNetconf notifyNetconf){
        StringBuffer netconfHeader = new StringBuffer("{\"apps\":{\"org.onosproject.netconf\":");
        StringBuffer needSend = new StringBuffer();

        String  bean = JSONParser.returnJsonString(notifyNetconf);
        System.out.println("输出bean: " + bean);
        needSend.append(netconfHeader);
        needSend.append(bean);
        needSend.append("}}");
        System.out.println("输出需要发送的bean: " + needSend);
        return needSend;
    }

    public static void notifyNetconf(String json) {

    }


    public static Map sendPost(String data) {
        String basicAuthor = "onos:rocks";
        try {
            basicAuthor = Base64.getEncoder().encodeToString(basicAuthor.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        basicAuthor = "Basic " + basicAuthor;
        Map<String, String> res = new HashMap<>();
        StringBuffer responseMessage = null;
        java.net.URLConnection connection = null;
        HttpURLConnection httpURLConnection = null;
        java.net.URL reqUrl = null;
        OutputStreamWriter reqOut = null;
        InputStream in = null;
        BufferedReader br = null;
        String paramContent = data;
        String param = paramContent;
        try {
            responseMessage = new StringBuffer();
            reqUrl = new java.net.URL("http://localhost:8181/onos/v1/network/configuration");
            connection = reqUrl.openConnection();
            httpURLConnection = (HttpURLConnection) connection;
            httpURLConnection.setRequestProperty("Authorization", basicAuthor);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setDoOutput(true);
            reqOut = new OutputStreamWriter(connection.getOutputStream(),
                    "utf-8");
            reqOut.write(param);
            // reqOut.write(new
            // String(param.getBytes("iso8859-1")));//这个可以提交中文参数，比如站点名称
            reqOut.flush();
            int charCount = -1;
            in = httpURLConnection.getInputStream();

            br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            while ((charCount = br.read()) != -1) {
                responseMessage.append((char) charCount);
            }
            res.put("State", String.valueOf(httpURLConnection.getResponseCode()));
            res.put("Content", responseMessage.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                in.close();
                reqOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return res;
    }
}