package com.icodeyou.securechat;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.icodeyou.securechat.util.HttpUtil;


public class RegisterActivity extends ActionBarActivity {

    private EditText mEtName, mEtPassword, mEtPasswordAgain, mEtPhone;
    private Button mBtnSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    private void initView() {
        mEtName = (EditText) findViewById(R.id.id_etName);
        mEtPhone = (EditText) findViewById(R.id.id_etPhone);
        mEtPassword = (EditText) findViewById(R.id.id_etPassword);
        mEtPasswordAgain = (EditText) findViewById(R.id.id_etPasswordAgain);
        mBtnSign = (Button) findViewById(R.id.id_btnSign);

        mBtnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEtName.getText().toString().trim();
                String phone = mEtPhone.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();
                String passwordAgain = mEtPasswordAgain.getText().toString().trim();

                if (!password.equals(passwordAgain)){
                    Toast.makeText(RegisterActivity.this, "两次密码不匹配", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!"".equals(name) && !"".equals(phone) && !"".equals(password) && !"".equals(passwordAgain)){
                    HttpUtil.getInstance().register(phone, name, password, new HttpUtil.HttpCallBackListener() {
                        @Override
                        public void onSuccess(String info) {
                            // 注册成功，跳转到LoginActivity
                            Toast.makeText(RegisterActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        @Override
                        public void onFail(String info) {

                        }
                    });
                }
            }
        });
    }

}
