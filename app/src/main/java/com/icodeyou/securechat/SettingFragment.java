package com.icodeyou.securechat;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


public class SettingFragment extends Fragment {

    private LinearLayout mLLUserInfoSetting, mLLMessageNotifySetting;

    private Button mBtnQuit;

    public SettingFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        mLLUserInfoSetting = (LinearLayout) view.findViewById(R.id.id_llUserInfoSetting);
        mLLMessageNotifySetting = (LinearLayout) view.findViewById(R.id.id_llMessageNotifySetting);
        mBtnQuit = (Button) view.findViewById(R.id.id_btnQuit);

        mLLUserInfoSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到用户信息设置的界面
                Intent intent = new Intent(getActivity().getApplicationContext(), SettingUserinfoActivity.class);
                startActivity(intent);
            }
        });

        mLLMessageNotifySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到新消息设置的界面
                Intent intent = new Intent(getActivity().getApplicationContext(), SettingMessageActivity.class);
                startActivity(intent);
            }
        });

        mBtnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("phone", "-1");
                editor.commit();
                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });


        return view;
    }

}
