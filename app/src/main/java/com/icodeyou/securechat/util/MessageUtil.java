package com.icodeyou.securechat.util;

import android.telephony.SmsManager;

/**
 * Created by huan on 15/5/20.
 */
public class MessageUtil {

    private static MessageUtil messageUtil = null;

    private MessageUtil(){}

    public static MessageUtil getInstance(){
        if (messageUtil == null)
            messageUtil = new MessageUtil();
        return messageUtil;
    }

    public void sendMessage(String to, String content){
        // todo
        /*
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(to, null, content, null, null);
        */
    }

}
