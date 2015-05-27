package com.icodeyou.securechat.util;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

/**
 * Created by huan on 15/5/26.
 */
public class RingtoneVibratorManager {

    public static void playMusic(Context context) {
        if (SettingManager.getInstance(context).isPlay()){
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        }
    }

    public static void vibration(Context context) {
        if (SettingManager.getInstance(context).isVibration()){
            Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(100);
        }
    }

}
