package com.icodeyou.securechat;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Debug;
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

import org.json.JSONObject;


public class AddFriendActivity extends ActionBarActivity {

    private EditText mEtPhone;
    private Button mBtnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        initView();

    }

    private void initView() {
        setTitle("添加好友");
        mEtPhone = (EditText) findViewById(R.id.id_etPhone);
        mBtnOk = (Button) findViewById(R.id.id_btnOK);

        // 获取登录用户手机号
        SharedPreferences sharedPreferences = getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        final String num_src = sharedPreferences.getString("phone", "-1");

        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num_obj = mEtPhone.getText().toString().trim();
                if (!"".equals(num_obj)){
                    HttpUtil.getInstance().addContact(num_src, num_obj, new HttpUtil.HttpCallBackListener() {
                        @Override
                        public void onSuccess(String info) {
                            DebugUtil.print("btnAddContact " + info);
                            try {
                                JSONObject object = new JSONObject(info);
                                int status = object.getInt("status");
                                if (status == 0){
                                    Toast.makeText(AddFriendActivity.this, "已发送添加好友请求", Toast.LENGTH_LONG).show();
                                }else if (status == 1){
                                    Toast.makeText(AddFriendActivity.this, "对方手机号不存在", Toast.LENGTH_LONG).show();
                                }else if (status == 2){
                                    Toast.makeText(AddFriendActivity.this, "已经是好友了", Toast.LENGTH_LONG).show();
                                }else if (status == 3){
                                    Toast.makeText(AddFriendActivity.this, "已经提交添加申请，请等待对方接受", Toast.LENGTH_LONG).show();
                                }
                            } catch(Exception e){
                                e.printStackTrace();
                            }
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
