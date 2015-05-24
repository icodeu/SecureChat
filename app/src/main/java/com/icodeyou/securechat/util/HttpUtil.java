package com.icodeyou.securechat.util;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.icodeyou.securechat.MyApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * 网络访问工具类
 */
public class HttpUtil {

    public interface HttpCallBackListener {
        public void onSuccess(String info);
        public void onFail(String info);
    }

    private static HttpUtil httpUtil = null;

    private static final String BASE_URL = "http://2.huandb.sinaapp.com/";
    private static final String SEND_MY_IP = BASE_URL + "sendMyIP.php";
    private static final String POLL_GET_IP = BASE_URL + "pollGetIP.php";
    private static final String SEND_MY_IP_RAM = BASE_URL + "sendMyIPRAM.php";
    //todo 还需要再定义一些常量网址

    private HttpUtil() {
    }

    public static HttpUtil getInstance() {
        if (httpUtil == null)
            httpUtil = new HttpUtil();
        return httpUtil;
    }

    public void sendMyIP(final String ip, final HttpCallBackListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEND_MY_IP, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                DebugUtil.print("sendMyIP success " + s);
                // 回调给调用函数
                listener.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                DebugUtil.print("sendMyIP error " + volleyError.toString());
                listener.onFail(volleyError.toString());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("ip", ip);
                return map;
            }
        };
        stringRequest.setTag("sendmyip");
        MyApplication.getRequestQueue().add(stringRequest);
    }

    public void pollGetIP(final String ram, final HttpCallBackListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, POLL_GET_IP, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                DebugUtil.print("pollGetIP success " + s);
                // 回调给调用函数
                listener.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                DebugUtil.print("pollGetIP error " + volleyError.toString());
                listener.onFail(volleyError.toString());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("ram", ram);
                return map;
            }
        };
        stringRequest.setTag("pollgetip");
        MyApplication.getRequestQueue().add(stringRequest);
    }

    public void sendMyIPRAM(final String ip, final String ram, final HttpCallBackListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEND_MY_IP_RAM, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                DebugUtil.print("sendMyIPRAM success " + s);
                // 回调给调用函数
                listener.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                DebugUtil.print("sendMyIPRAM error " + volleyError.toString());
                listener.onFail(volleyError.toString());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("ip", ip);
                map.put("ram", ram);
                return map;
            }
        };
        stringRequest.setTag("sendmyipram");
        MyApplication.getRequestQueue().add(stringRequest);
    }

}
