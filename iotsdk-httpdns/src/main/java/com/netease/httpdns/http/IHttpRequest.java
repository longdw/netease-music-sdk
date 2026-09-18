package com.netease.httpdns.http;

import com.netease.httpdns.module.NAHttpEntity;
import java.util.Map;
import org.json.JSONObject;

public interface IHttpRequest {
    NAHttpEntity doHttpWithGet(String var1, Map<String, String> var2);

    NAHttpEntity doHttpWithPost(String var1, Map<String, String> var2, JSONObject var3);
}
