package com.icodeyou.securechat;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


public class AcceptDialogActivity extends ActionBarActivity {

    private Button mBtnOK;
    private EditText mEtRam;

    public static final String RAM = "ram";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_dialog);

        setTitle("请输入会话邀请码");

        initView();
    }

    private void initView() {
        mBtnOK = (Button) findViewById(R.id.id_btnOK);
        mEtRam = (EditText) findViewById(R.id.id_etRam);

        mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ram = mEtRam.getText().toString().trim();
                if (!"".equals(ram)){
                    Intent intent = new Intent(AcceptDialogActivity.this, ChatActivity.class);
                    // 设置角色为 受邀请者
                    intent.putExtra(ChatActivity.ROLE, ChatActivity.ROLE_ACCEPTOR);
                    intent.putExtra(RAM, ram);
                    startActivity(intent);
                }

            }
        });
    }

}
