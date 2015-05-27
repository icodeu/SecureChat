package com.icodeyou.securechat;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.icodeyou.securechat.util.HttpUtil;


public class SettingUserinfoActivity extends ActionBarActivity {

    private EditText mEtName, mEtPassword, mEtPhone;
    private Button mBtnOK;

    private String preName = "init", prePassword = "init";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_userinfo);
        setTitle("用户信息设置");

        initView();

        getUserInfo();

    }

    /*
     * 从网络获取用户信息
     */
    private void getUserInfo() {
        // todo 在此模拟
        mEtName.setText("wanghuan");
        mEtPassword.setText("123456");
        preName = "wanghuan";
        prePassword = "123456";




        mBtnOK.setEnabled(false);
    }

    private void initView() {
        mEtName = (EditText) findViewById(R.id.id_etName);
        mEtPassword = (EditText) findViewById(R.id.id_etPassword);
        mBtnOK = (Button) findViewById(R.id.id_btnOK);
        mEtPhone = (EditText) findViewById(R.id.id_etPhone);
        mEtPhone.setText(MyApplication.getMyPhone());

        mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo 提交修改到网络
//                HttpUtil.getInstance()
            }
        });

        mEtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                monitorStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                monitorStatus();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void monitorStatus() {
        String name = mEtName.getText().toString();
        String password = mEtPassword.getText().toString();
        if (preName.equals(name) && prePassword.equals(password))
            mBtnOK.setEnabled(false);
        else
            mBtnOK.setEnabled(true);
    }

}
