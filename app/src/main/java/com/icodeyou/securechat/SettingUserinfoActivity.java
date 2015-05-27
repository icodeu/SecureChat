package com.icodeyou.securechat;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.icodeyou.securechat.util.HttpUtil;

import org.json.JSONObject;



public class SettingUserinfoActivity extends ActionBarActivity {

    private EditText mEtName, mEtPassword, mEtPhone;
    private Button mBtnOK;

    private String preName = "init", prePassword = "init";

    private Handler getUserInfoHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String name = (String) msg.obj;
            mEtName.setText(name);
            mEtPassword.setText("123456");
            preName = name;
            prePassword = "123456";
        }
    };

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
        HttpUtil.getInstance().getMyInfo(MyApplication.getMyPhone(), new HttpUtil.HttpCallBackListener() {
            @Override
            public void onSuccess(String info) {
                try{
                    JSONObject object = new JSONObject(info);
                    String name = object.getString("name");
                    Message msg = Message.obtain();
                    msg.obj = name;
                    getUserInfoHandler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFail(String info) {
            }
        });

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
                String name = mEtName.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();
                String phone = mEtPhone.getText().toString().trim();
                if (!"".equals(name) && !"".equals(password) && !"".equals(phone))
                    HttpUtil.getInstance().changeInfo(phone, name, password, new HttpUtil.HttpCallBackListener() {
                        @Override
                        public void onSuccess(String info) {
                            try {
                                JSONObject object = new JSONObject(info);
                                int status = object.getInt("status");
                                if (status == 0){
                                    Toast.makeText(SettingUserinfoActivity.this, "未修改", Toast.LENGTH_SHORT).show();
                                }
                                if (status == 1){
                                    Toast.makeText(SettingUserinfoActivity.this, "已修改用户名", Toast.LENGTH_SHORT).show();
                                }
                                if (status == 2){
                                    Toast.makeText(SettingUserinfoActivity.this, "已修改密码", Toast.LENGTH_SHORT).show();
                                }
                                if (status == 3){
                                    Toast.makeText(SettingUserinfoActivity.this, "已修改用户名和密码", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFail(String info) {
                        }
                    });
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
