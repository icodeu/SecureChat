package com.icodeyou.securechat;

import android.support.v4.app.Fragment;

/**
 * Created by huan on 15/5/25.
 */
public class FragmentManager {

    public static Fragment newInstance(int position){
        if (position == 1)
            return new ChatFragment();
        if (position == 2)
            return new FriendFragment();
        if (position == 3)
            return new SettingFragment();
        return null;
    }

}
