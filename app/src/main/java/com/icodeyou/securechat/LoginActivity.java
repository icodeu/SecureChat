package com.icodeyou.securechat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.icodeyou.securechat.util.DebugUtil;
import com.icodeyou.securechat.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends ActionBarActivity {

    private EditText mEtPhone;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private Button mBtnSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        isLogined();
    }

    private void isLogined() {
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "-1");
        if (!"-1".equals(phone)){
            MyApplication.setMyPhone(phone);
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initView() {
        mEtPhone = (EditText) findViewById(R.id.id_etPhone);
        mEtPassword = (EditText) findViewById(R.id.id_etPassword);
        mBtnLogin = (Button) findViewById(R.id.id_btnLogin);
        mBtnSign = (Button) findViewById(R.id.id_btnSign);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phone = mEtPhone.getText().toString();
                String password = mEtPassword.getText().toString();
                if (!"".equals(phone) && !"".equals(password)){
                    // todo 访问网络登录
                    HttpUtil.getInstance().login(phone, password, new HttpUtil.HttpCallBackListener() {
                        @Override
                        public void onSuccess(String info) {
                            try {
                                JSONObject object = new JSONObject(info);
                                int status = object.getInt("status");
                                if (status == 0){
                                    // 加入到 sharedpreference
                                    SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("phone", phone);
                                    editor.commit();
                                    MyApplication.setMyPhone(phone);
                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    // 跳转到主界面
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if(status == 1){
                                    Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                } else if (status == 2){
                                    Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFail(String info) {

                        }
                    });
                    if ("188".equals(phone)){

                    }
                }
            }
        });

        mBtnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

}
