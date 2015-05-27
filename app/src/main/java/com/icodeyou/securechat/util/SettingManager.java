package com.icodeyou.securechat.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 设置选项开关
 */
public class SettingManager {

    private Context mContext;

    private SharedPreferences sharedPreferences;

    private static String IS_PLAY = "is_play";
    private static String IS_VIBRATION = "is_vibration";
    private static String IS_SAVE_CHAT_HISTORY = "is_save_chat_history";

    private static final String SETTING = "setting";

    private static SettingManager manager = null;

    private SettingManager(Context context){
        mContext = context;
        sharedPreferences = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);
    }

    public static SettingManager getInstance(Context context){
        if (manager == null)
            manager = new SettingManager(context);
        return manager;
    }

    public boolean isPlay() {
        return sharedPreferences.getBoolean(IS_PLAY, false);
    }

    public void setPlay(boolean isPlay) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_PLAY, isPlay);
        editor.commit();
    }

    public boolean isVibration() {
        return sharedPreferences.getBoolean(IS_VIBRATION, false);
    }

    public void setVibration(boolean isVibration) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_VIBRATION, isVibration);
        editor.commit();
    }

    public boolean isSaveChatHistory() {
        return sharedPreferences.getBoolean(IS_SAVE_CHAT_HISTORY, false);
    }

    public void setSaveChatHistory(boolean isSaveChatHistory) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_SAVE_CHAT_HISTORY, isSaveChatHistory);
        editor.commit();
    }
}
