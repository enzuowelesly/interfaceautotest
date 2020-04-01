package com.tanzi.config;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.CloseableHttpClient;

public class TestConfig {
    public  static String loginurl;
    public  static String updateUserInfoUrl;
    public  static String getUserInfoUrl;
    public  static String getUserListUrl;
    public  static String addUserUrl;
    public static CloseableHttpClient httpClient;
    public static  CookieStore cookieStore;

}
