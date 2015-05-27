package com.icodeyou.securechat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Debug;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.icodeyou.securechat.db.DAOImp;
import com.icodeyou.securechat.util.DebugUtil;
import com.icodeyou.securechat.util.SettingManager;


public class SettingMessageActivity extends ActionBarActivity {

    private Switch mSwMusic, mSwVibration, mSwChatHistory;
    private Button mBtnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_message);
        setTitle("消息设置");

        initView();
    }

    private void initView() {
        mSwMusic = (Switch) findViewById(R.id.id_swMusic);
        mSwVibration = (Switch) findViewById(R.id.id_swVibration);
        mSwChatHistory = (Switch) findViewById(R.id.id_swChatHistory);
        mBtnClear = (Button) findViewById(R.id.id_btnClearChatHistory);

        mSwMusic.setChecked(SettingManager.getInstance(getApplicationContext()).isPlay());
        mSwVibration.setChecked(SettingManager.getInstance(getApplicationContext()).isVibration());
        mSwChatHistory.setChecked(SettingManager.getInstance(getApplicationContext()).isSaveChatHistory());

        mSwMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DebugUtil.print("switch music " + " ON");
                    SettingManager.getInstance(getApplicationContext()).setPlay(true);
                } else {
                    DebugUtil.print("switch music " + " OFF");
                    SettingManager.getInstance(getApplicationContext()).setPlay(false);
                }
            }
        });

        mSwVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DebugUtil.print("switch vibration " + " ON");
                    SettingManager.getInstance(getApplicationContext()).setVibration(true);
                } else {
                    DebugUtil.print("switch vibration " + " OFF");
                    SettingManager.getInstance(getApplicationContext()).setVibration(false);
                }
            }
        });

        mSwChatHistory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DebugUtil.print("switch chathistory " + " ON");
                    SettingManager.getInstance(getApplicationContext()).setSaveChatHistory(true);
                } else {
                    DebugUtil.print("switch chathistory " + " OFF");
                    SettingManager.getInstance(getApplicationContext()).setSaveChatHistory(false);
                }
            }
        });

        mBtnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清空聊天记录
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingMessageActivity.this);

                builder.setMessage("确定清空所有聊天记录吗?").setTitle("提示").setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 确定清空聊天记录
                        new DAOImp(getApplicationContext()).clearAll();
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 取消清空
                        return;
                    }
                });

                builder.create().show();
            }
        });
    }


}
