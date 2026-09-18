package com.netease.httpdns.module;

import android.text.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class IPModel {
    private static final String STRING_ZERO = "0";
    private static final String STRING_EMPTY = "";
    private static final float VALUE_ZERO = 0.0F;
    private static final String IP = "ip";
    private static final String PRIORITY = "priority";
    private static final String RTT = "rtt";
    private static final String SUCCESS_NUM = "successNum";
    private static final String ERR_NUM = "errNum";
    private static final String FINALLY_SUCCESS_TIME = "finallySuccessTime";
    private static final String FINALLY_FAIL_TIME = "finallyFailTime";
    public String ip = "";
    public String priority = "0";
    public String rtt = "0";
    public String successNum = "0";
    public String errNum = "0";
    public String finallySuccessTime = "0";
    public String finallyFailTime = "0";
    public float grade = 0.0F;

    public IPModel() {
    }

    public IPModel(String ip) {
        this.ip = ip;
    }

    public String toString() {
        return "IPModel{ip='" + this.ip + '\'' + ", priority='" + this.priority + '\'' + ", rtt='" + this.rtt + '\'' + ", successNum='" + this.successNum + '\'' + ", errNum='" + this.errNum + '\'' + ", finallySuccessTime='" + this.finallySuccessTime + '\'' + ", finallyFailTime='" + this.finallyFailTime + '\'' + ", grade=" + this.grade + '}';
    }

    public String toJson() {
        JSONStringer jsonStringer = new JSONStringer();

        try {
            jsonStringer.object().key("ip").value(this.ip).key("priority").value(this.priority).key("rtt").value(this.rtt).key("successNum").value(this.successNum).key("errNum").value(this.errNum).key("finallySuccessTime").value(this.finallySuccessTime).key("finallyFailTime").value(this.finallyFailTime).endObject();
            return jsonStringer.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "{}";
        }
    }

    public static IPModel create(String json) {
        if (TextUtils.isEmpty(json)) {
            return null;
        } else {
            try {
                JSONObject jsonObject = new JSONObject(json);
                IPModel model = new IPModel();
                model.ip = jsonObject.optString("ip");
                model.priority = jsonObject.optString("priority");
                model.rtt = jsonObject.optString("rtt");
                model.successNum = jsonObject.optString("successNum");
                model.errNum = jsonObject.optString("errNum");
                model.finallySuccessTime = jsonObject.optString("finallySuccessTime");
                model.finallyFailTime = jsonObject.optString("finallyFailTime");
                return model;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}