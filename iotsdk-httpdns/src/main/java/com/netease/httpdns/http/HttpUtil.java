package com.netease.httpdns.http;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.netease.httpdns.HttpDnsService;
import com.netease.httpdns.configuration.DnsOptions;
import com.netease.httpdns.listener.RequestStatusListener;
import com.netease.httpdns.log.DNSLog;
import com.netease.httpdns.module.NAHttpEntity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONObject;

public class HttpUtil {
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String HTTPS = "https";
    private static final String JSON_HEADER = "application/json";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HOST_ADDRESS = "httpdns.n.netease.com";
    public static final String HOST = "Host";
    public static final String GZIP = "gzip";
    private static final int ERROR_CODE = -1;
    private static final int REQUEST_SUCCESS = 200;

    private HttpUtil() {
    }

    @NonNull
    public static NAHttpEntity httpRequestWithGet(String host, String requestUrl, Map<String, String> header, RequestStatusListener listener) {
        IHttpRequest request = HttpDnsService.getInstance().getOptions().getHttpRequest();
        NAHttpEntity httpEntity = null;
        if (request != null) {
            httpEntity = request.doHttpWithGet(requestUrl, header);
        } else {
            httpEntity = httpRequest(host, requestUrl, "GET", (JSONObject)null, (Map)null, header);
        }

        if (httpEntity == null) {
            httpEntity = new NAHttpEntity(-1, "httpEntity is null.");
        }

        if (!httpEntity.isSuccess() && listener != null) {
            listener.requestFailed(requestUrl, httpEntity.getErrorCode(), httpEntity.getErrorMsg());
        }

        return httpEntity;
    }

    @NonNull
    public static NAHttpEntity httpRequestWithPost(String host, String requestUrl, Map<String, String> header, JSONObject postRequestParams) {
        IHttpRequest request = HttpDnsService.getInstance().getOptions().getHttpRequest();
        NAHttpEntity httpEntity = null;
        if (request != null) {
            httpEntity = request.doHttpWithPost(requestUrl, header, postRequestParams);
        } else {
            httpEntity = httpRequest(host, requestUrl, "POST", postRequestParams, (Map)null, header);
        }

        if (httpEntity == null) {
            httpEntity = new NAHttpEntity(-1, "httpEntity is null.");
        }

        RequestStatusListener listener = HttpDnsService.getInstance().getRequestStatusListener();
        if (!httpEntity.isSuccess() && listener != null) {
            listener.requestFailed(requestUrl, httpEntity.getErrorCode(), httpEntity.getErrorMsg());
        }

        return httpEntity;
    }

    @NonNull
    private static NAHttpEntity httpRequest(String host, String requestUrl, String requestMethod, JSONObject postRequestParams, Map<String, String> requestParams, Map<String, String> header) {
        NAHttpEntity naHttpEntity = new NAHttpEntity(-1, "");
        if (TextUtils.isEmpty(requestUrl)) {
            return naHttpEntity;
        } else {
            HttpURLConnection connection = null;
            InputStream in = null;

            NAHttpEntity var19;
            try {
                String tempUrl = requestUrl;
                if (isNeedUrlAppendParams(requestMethod) && isGetRequestParamLegal(requestParams)) {
                    tempUrl = getUrl(requestUrl, requestParams);
                }

                URL url = new URL(tempUrl);
                DNSLog.i("httpRequest : " + url);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod(requestMethod);
                if (header != null) {
                    for(Map.Entry<String, String> entry : header.entrySet()) {
                        connection.setRequestProperty((String)entry.getKey(), (String)entry.getValue());
                    }
                }

                DnsOptions options = HttpDnsService.getInstance().getOptions();
                int timeout = options == null ? 12000 : options.getHttpTimeOut();
                connection.setConnectTimeout(timeout);
                connection.setReadTimeout(timeout);
                connection.setUseCaches(false);
                if (requestUrl.contains("https")) {
                    ((HttpsURLConnection)connection).setHostnameVerifier(new TrustHostnameVerifier(host));
                    ((HttpsURLConnection)connection).setSSLSocketFactory(new SDKSNISocketFactory(host));
                }

                if (isNeedRequestParams(requestMethod) && isPostRequestParamLegal(postRequestParams)) {
                    byte[] paramsBytes = postRequestParams.toString().getBytes();
                    connection.addRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(paramsBytes);
                    outputStream.close();
                }

                int code = connection.getResponseCode();
                if (code != 200) {
                    return naHttpEntity;
                }

                String encoding = connection.getContentEncoding();
                if (TextUtils.equals("gzip", encoding)) {
                    in = new GZIPInputStream(connection.getInputStream());
                } else {
                    in = connection.getInputStream();
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();

                String line;
                while((line = reader.readLine()) != null) {
                    response.append(line);
                }

                String content = response.toString();
                naHttpEntity = new NAHttpEntity(requestUrl, content, code);
                naHttpEntity.setHeaders(connection.getHeaderFields());
                var19 = naHttpEntity;
            } catch (UnknownHostException e) {
                DNSLog.e("UnknownHostException : " + e.toString());
                naHttpEntity = new NAHttpEntity(-2, e.toString());
                return naHttpEntity;
            } catch (Exception e) {
                e.printStackTrace();
                DNSLog.e("HttpRequestError : " + e.toString());
                naHttpEntity = new NAHttpEntity(-1, e.toString());
                return naHttpEntity;
            } finally {
                if (connection != null) {
                    try {
                        connection.disconnect();
                    } catch (Exception var37) {
                        DNSLog.e("http connection close failed!");
                    }
                }

                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException var36) {
                        DNSLog.e("http input stream close failed!");
                    }
                }

            }

            return var19;
        }
    }

    public static String getIpAddressString() {
        try {
            Enumeration<NetworkInterface> enNetI = NetworkInterface.getNetworkInterfaces();

            while(enNetI.hasMoreElements()) {
                NetworkInterface netI = (NetworkInterface)enNetI.nextElement();
                Enumeration<InetAddress> enumIpAddress = netI.getInetAddresses();

                while(enumIpAddress.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress)enumIpAddress.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return "";
    }

    private static boolean isNeedRequestParams(String requestMethod) {
        return "POST".equals(requestMethod);
    }

    private static boolean isNeedUrlAppendParams(String requestMethod) {
        return "GET".equalsIgnoreCase(requestMethod);
    }

    private static String getUrl(String requestUrl, Map<String, String> requestParams) {
        StringBuilder sb = (new StringBuilder(requestUrl)).append("?");

        for(String key : requestParams.keySet()) {
            String value = (String)requestParams.get(key);
            if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                sb.append(key).append("=").append((String)requestParams.get(key)).append("&");
            }
        }

        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    private static boolean isGetRequestParamLegal(Map<String, String> requestParams) {
        return requestParams != null && requestParams.size() != 0;
    }

    private static boolean isPostRequestParamLegal(JSONObject requestParams) {
        return requestParams != null && requestParams.length() != 0;
    }
}