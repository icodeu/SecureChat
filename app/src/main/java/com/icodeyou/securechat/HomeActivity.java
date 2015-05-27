package com.icodeyou.securechat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.icodeyou.securechat.model.Contact;
import com.icodeyou.securechat.util.ContactManager;
import com.icodeyou.securechat.util.DebugUtil;
import com.icodeyou.securechat.util.HttpUtil;
import com.icodeyou.securechat.util.IPManager;
import com.icodeyou.securechat.util.NewFriendManager;
import com.icodeyou.securechat.util.WantToChatManager;
import com.jauker.widget.BadgeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeActivity extends FragmentActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private ArrayList<Fragment> mFragments;
    private ImageButton ibContact, ibMe;
    private ImageButton ibAddFriend;

    private BadgeView badgeView;

    public static final String RAM = "ram";

    private Handler badgeUpdateHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int totalCount = msg.what;
            if ( totalCount != 0 )
                badgeView.setBadgeCount(totalCount);
            else
                badgeView.setBadgeCount(0);
            // todo 暂时先设置为0
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ibContact = (ImageButton) findViewById(R.id.ibContact);
        ibMe = (ImageButton) findViewById(R.id.ibMe);
        ibAddFriend = (ImageButton) findViewById(R.id.id_ibAddFriend);

        badgeView = new BadgeView(this);
        badgeView.setTargetView(ibContact);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        mFragments = new ArrayList<Fragment>();

        FriendFragment friendFragment = new FriendFragment();
        SettingFragment settingFragment = new SettingFragment();
        mFragments.add(friendFragment);
        mFragments.add(settingFragment);

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();

        viewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int pos) {
                selectIndex(pos);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        viewPager.setCurrentItem(0);

        ibContact.setOnClickListener(this);
        ibMe.setOnClickListener(this);

        ibAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddFriendActivity.class);
                startActivity(intent);
            }
        });

        ibContact.setImageResource(R.drawable.contact_pressed);

        getLocalIP();

        // todo 测试BadgeView
//        BadgeView badgeView = new BadgeView(this);
//        badgeView.setTargetView(ibContact);
//        badgeView.setBadgeCount(3);
        new BadgeUpdateThread().start();

    }

    protected void selectIndex(int pos) {
        if (pos == 0) {
            ibContact.setImageResource(R.drawable.contact_pressed);
            ibMe.setImageResource(R.drawable.me);
        } else if (pos == 1) {
            ibContact.setImageResource(R.drawable.contact);
            ibMe.setImageResource(R.drawable.me_pressed);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibContact:
                viewPager.setCurrentItem(0);
                break;
            case R.id.ibMe:
                viewPager.setCurrentItem(1);
                break;
        }
    }

    private void getLocalIP() {
        IPManager.getInstance().setContext(this);
        String ip = IPManager.getInstance().getIP();
        IPManager.getInstance().setMyIP(ip);
    }

    /*
     * 更新 BadgeView 的线程
     */
    class BadgeUpdateThread extends Thread {

        @Override
        public void run() {
            while (true){
                try {
                    int newFriendCount = NewFriendManager.getInstance().getInfos().size();
                    int wantChatCount = WantToChatManager.getInstance().getInfos().size();
                    int totalCount = newFriendCount + wantChatCount;
                    badgeUpdateHandler.sendEmptyMessage(totalCount);
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}