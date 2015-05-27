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

    private static final String BASE_URL = "http://3.huandb.sinaapp.com/";
    private static final String SEND_MY_IP = BASE_URL + "sendMyIP.php";
    private static final String POLL_GET_IP = BASE_URL + "pollGetIP.php";
    private static final String SEND_MY_IP_RAM = BASE_URL + "sendMyIPRAM.php";
    private static final String REGISTER = BASE_URL + "regist.php";
    private static final String LOGIN = BASE_URL + "login.php";
    private static final String GET_CONTACT = BASE_URL + "getContact.php";
    private static final String ADD_CONTACT = BASE_URL + "addContact.php";
    private static final String REC_CONTACT = BASE_URL + "recContact.php";
    private static final String POLL_GET = BASE_URL + "pollget.php";
    private static final String CHANGE_INFO = BASE_URL + "changeInfo.php";
    private static final String GET_MY_INFO = BASE_URL + "getMyInfo.php";
    //todo 还需要再定义一些常量网址

    private HttpUtil() {
    }

    public static HttpUtil getInstance() {
        if (httpUtil == null)
            httpUtil = new HttpUtil();
        return httpUtil;
    }

    public void sendMyIP(final String ip, final String sponsor, final String acceptor, final HttpCallBackListener listener) {
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
                map.put("sponsor", sponsor);
                map.put("acceptor", acceptor);
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

    public void register(final String num, final String name, final String pwd, final HttpCallBackListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                DebugUtil.print("register success " + s);
                // 回调给调用函数
                listener.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                DebugUtil.print("register error " + volleyError.toString());
                listener.onFail(volleyError.toString());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("num", num);
                map.put("name", name);
                map.put("pwd", pwd);
                return map;
            }
        };
        stringRequest.setTag("register");
        MyApplication.getRequestQueue().add(stringRequest);
    }

    /*
     * 用户登录
     */
    public void login(final String num, final String pwd, final HttpCallBackListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                DebugUtil.print("login success " + s);
                // 回调给调用函数
                listener.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                DebugUtil.print("login error " + volleyError.toString());
                listener.onFail(volleyError.toString());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("num", num);
                map.put("pwd", pwd);
                return map;
            }
        };
        stringRequest.setTag("login");
        MyApplication.getRequestQueue().add(stringRequest);
    }

    /*
    * 获取好友列表
    */
    public void getContact(final String num, final HttpCallBackListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_CONTACT, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                DebugUtil.print("getContact success " + s);
                listener.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                DebugUtil.print("getContact error " + volleyError.toString());
                listener.onFail(volleyError.toString());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("num", num);
                return map;
            }
        };
        stringRequest.setTag("getcontact");
        MyApplication.getRequestQueue().add(stringRequest);
    }

    /*
    * 添加好友
    */
    public void addContact(final String num_src, final String num_obj, final HttpCallBackListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_CONTACT, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                DebugUtil.print("addContact success " + s);
                listener.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                DebugUtil.print("addContact error " + volleyError.toString());
                listener.onFail(volleyError.toString());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("num_src", num_src);
                map.put("num_obj", num_obj);
                return map;
            }
        };
        stringRequest.setTag("addcontact");
        MyApplication.getRequestQueue().add(stringRequest);
    }

    /*
     * 接受添加好友请求
     */
    public void recContact(final String num_rec, final String num_apply, final String status, final HttpCallBackListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REC_CONTACT, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                DebugUtil.print("recContact success " + s);
                listener.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                DebugUtil.print("recContact error " + volleyError.toString());
                listener.onFail(volleyError.toString());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("num_rec", num_rec);
                map.put("num_apply", num_apply);
                map.put("status", status);
                return map;
            }
        };
        stringRequest.setTag("reccontact");
        MyApplication.getRequestQueue().add(stringRequest);
    }


    /*
     * 轮询是否有好友请求和会话请求
     */
    public void pollGet(final String num, final HttpCallBackListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, POLL_GET, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                DebugUtil.print("pollGet success " + s);
                listener.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                DebugUtil.print("pollGet error " + volleyError.toString());
                listener.onFail(volleyError.toString());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("num", num);
                return map;
            }
        };
        stringRequest.setTag("pollget");
        MyApplication.getRequestQueue().add(stringRequest);
    }


    /*
     * 修改用户信息
     */
    public void changeInfo(final String num, final String name, final String pwd, final HttpCallBackListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CHANGE_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                DebugUtil.print("changeInfo success " + s);
                listener.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                DebugUtil.print("changeInfo error " + volleyError.toString());
                listener.onFail(volleyError.toString());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("num", num);
                map.put("name", name);
                map.put("pwd", pwd);
                return map;
            }
        };
        stringRequest.setTag("changeinfo");
        MyApplication.getRequestQueue().add(stringRequest);
    }


    /*
     * 获取用户信息
     */
    public void getMyInfo(final String num, final HttpCallBackListener listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_MY_INFO, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                DebugUtil.print("getMyInfo success " + s);
                listener.onSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                DebugUtil.print("getMyInfo error " + volleyError.toString());
                listener.onFail(volleyError.toString());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("num", num);
                return map;
            }
        };
        stringRequest.setTag("getmyinfo");
        MyApplication.getRequestQueue().add(stringRequest);
    }

}
