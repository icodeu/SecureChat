package com.icodeyou.securechat;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by huan on 15/5/20.
 */
public class MyApplication extends Application{

    private static RequestQueue requestQueue;
    private static String myPhone;

    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public static RequestQueue getRequestQueue(){
        return requestQueue;
    }

    public static String getMyPhone() {
        return myPhone;
    }

    public static void setMyPhone(String myPhone) {
        MyApplication.myPhone = myPhone;
    }
}
