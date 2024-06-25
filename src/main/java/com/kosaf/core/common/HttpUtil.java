package com.kosaf.core.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class HttpUtil {

  private final static String GET = "GET";
  private final static String POST = "POST";
  private final static String PATCH = "PATCH";
  private final static String PUT = "PUT";

  /**
   * HTTP(S) 요청 실행
   * 
   * @param method HTTP(S) 메서드 (GET, POST, PUT, PATCH, DELETE)
   * @param url 요청 URL
   * @param params 파라미터
   * @return String
   * @throws IOException
   * @throws NoSuchAlgorithmException
   */
  public static String executeRequest(String method, String url, Map<String, Object> headers,
      Map<String, Object> params) throws IOException, NoSuchAlgorithmException {

    // url 확인
    if (url == null || url.isEmpty())
      throw new IllegalArgumentException("[HttpUtil] URL cannot be null or empty");

    // url 설정
    StringBuilder requestUrl = new StringBuilder(url);
    // 파라미터 설정
    String parameter = buildString(params);

    // GET url에 파라미터 추가
    if (GET.equals(method) && !parameter.isEmpty())
      requestUrl.append((url.contains("?")) ? "&" : "?").append(parameter);

    // connection 설정
    HttpURLConnection con = null;

    if (url.startsWith("https")) {
      con = (HttpsURLConnection) new URL(requestUrl.toString()).openConnection();
      ((HttpsURLConnection) con).setSSLSocketFactory(SSLContext.getDefault().getSocketFactory());
    } else {
      con = (HttpURLConnection) new URL(requestUrl.toString()).openConnection();
    }

    // header 설정
    if (headers != null && !headers.isEmpty()) {
      for (Entry<String, Object> entry : headers.entrySet())
        con.setRequestProperty(entry.getKey(), String.valueOf(entry.getValue()));
    }

    // method 설정
    con.setRequestMethod(method);
    if (POST.equals(method) || PATCH.equals(method) || PUT.equals(method)) {
      con.setDoOutput(true);
      con.getOutputStream().write(parameter.getBytes("UTF-8"));
    }

    // TimeOut 설정 (5초)
    con.setConnectTimeout(5000);
    con.setReadTimeout(5000);

    // 응답 설정
    StringBuilder response = new StringBuilder();
    BufferedReader br = null;

    try {
      br = new BufferedReader(new InputStreamReader(con.getInputStream()));
      String inputLine;

      while ((inputLine = br.readLine()) != null)
        response.append(inputLine);

    } catch (IOException e) {
      BufferedReader errorReader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
      StringBuilder errorResponse = new StringBuilder();
      String errorInputLine;

      while ((errorInputLine = errorReader.readLine()) != null)
        errorResponse.append(errorInputLine);

      throw new IOException("HTTP Method: " + method + " request failed: " + con.getResponseCode()
          + " " + con.getResponseMessage() + "\nError response: " + errorResponse.toString(), e);

    } finally {
      if (br != null)
        br.close();

      if (con != null)
        con.disconnect();
    }

    return response.toString();
  }

  /**
   * Map 파라미터 문자열 변환
   * 
   * @param params 파라미터
   * @return String
   * @throws UnsupportedEncodingException
   */
  private static String buildString(Map<String, Object> params)
      throws UnsupportedEncodingException {

    StringBuilder parameter = new StringBuilder();

    if (params != null && !params.isEmpty()) {
      for (Map.Entry<String, Object> entry : params.entrySet()) {
        if (entry.getValue() != null) {
          parameter.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
          parameter.append("=");
          parameter.append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
          parameter.append("&");
        }
      }
      parameter.deleteCharAt(parameter.length() - 1);
    }

    return parameter.toString();
  }
}
