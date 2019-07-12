package com.example.sqlite2.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPRetrieval {
    public String HTTPWeatherGET(String cityCode) {
        String res = "";
        // StringBuilder 的作用类似于 + ，用于将字符串连接在一起
        StringBuilder sb = new StringBuilder();
        String urlString = "http://www.weather.com.cn/adat/cityinfo/" + cityCode + ".html";
        try {
            // URL 是对 url 的处理类
            URL url = new URL(urlString);
            // 得到connection对象
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            // 使用 InputStreamReader 进行数据接收
            InputStreamReader isr = new InputStreamReader(httpURLConnection.getInputStream());
            // 缓存
            BufferedReader br = new BufferedReader(isr);
            String temp = null;
            // 读取接收的数据
            while ( (temp = br.readLine()) != null) {
                sb.append(temp);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        res = sb.toString();
        return res;
    }
}