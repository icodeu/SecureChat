package com.icodeyou.securechat.util;

/**
 * Created by huan on 15/5/20.
 */
public class DebugUtil {

    private boolean sw = true;

    public void setSw(boolean sw) {
        this.sw = sw;
    }

    public static void print(String info){
        System.out.println("debug>>> " + info);
    }

}
