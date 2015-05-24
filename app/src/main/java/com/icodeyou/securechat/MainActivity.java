package com.icodeyou.securechat;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.icodeyou.securechat.contactslist.ContactAdapter;
import com.icodeyou.securechat.contactslist.ContactsManager;
import com.icodeyou.securechat.contactslist.LetterBar;
import com.icodeyou.securechat.contactslist.PinnedSectionListView;
import com.icodeyou.securechat.model.Contact;
import com.icodeyou.securechat.util.DebugUtil;
import com.icodeyou.securechat.util.IPManager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private Button mBtnAccept;

    public static final String CONTACT = "Contact";

    private LetterBar lb;
    private TextView tv_overlay;

    private PinnedSectionListView lv_contacts;

    private ContactAdapter adapter;
    private List<Contact> contacts = new ArrayList<Contact>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        setContactsData();

        getLocalIP();
    }

    private void initView() {
        mBtnAccept = (Button) findViewById(R.id.id_btnAccept);
        lb = (LetterBar) findViewById(R.id.lb);
        tv_overlay = (TextView) findViewById(R.id.tv_overlay);

        lv_contacts = (PinnedSectionListView) findViewById(R.id.lv_contacts);
        lv_contacts.setShadowVisible(false);

        adapter = new ContactAdapter(this, contacts);
        lv_contacts.setAdapter(adapter);

        lv_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Object item = adapter.getItem(position);
                Contact contactBean = (Contact) item;
                Toast.makeText(MainActivity.this, contactBean.getName(), Toast.LENGTH_LONG).show();
                // 点击某一个联系人后进入与该联系人的聊天界面
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                // 设置角色为 发起者
                intent.putExtra(ChatActivity.ROLE, ChatActivity.ROLE_SPONSOR);
                // 传递串行化对象
                intent.putExtra(CONTACT, contactBean);
                startActivity(intent);

            }
        });


        lb.setOnLetterSelectedListener(new LetterBar.OnLetterSelectedListener() {
            @Override
            public void onLetterSelected(String letter) {
                if(TextUtils.isEmpty(letter)) {
                    tv_overlay.setVisibility(View.GONE);
                } else {
                    tv_overlay.setVisibility(View.VISIBLE);
                    tv_overlay.setText(letter);
                    int position = adapter.getLetterPosition(letter);
                    if(position != -1) {
                        lv_contacts.setSelection(position);
                    }
                }
            }
        });

        mBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AcceptDialogActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setContactsData() {
        List<Contact> contactData = ContactsManager.getContacts(this);
        contacts.clear();
        contacts.addAll(contactData);
        adapter.updateList();
    }

    private void getLocalIP() {
//        //获取wifi服务
//        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//        //判断wifi是否开启
//        if (!wifiManager.isWifiEnabled()) {
//            wifiManager.setWifiEnabled(true);
//        }
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        int ipAddress = wifiInfo.getIpAddress();
//        String ip = (ipAddress & 0xFF ) + "." +
//                ((ipAddress >> 8 ) & 0xFF) + "." +
//                ((ipAddress >> 16 ) & 0xFF) + "." +
//                ( ipAddress >> 24 & 0xFF) ;

        IPManager.getInstance().setContext(this);
        String ip = IPManager.getInstance().getIP();
        IPManager.getInstance().setMyIP(ip);
    }

}
