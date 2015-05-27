package com.icodeyou.securechat;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.icodeyou.securechat.adapter.ContactsAdapter;
import com.icodeyou.securechat.adapter.WantToChatAdapter;
import com.icodeyou.securechat.model.Contact;
import com.icodeyou.securechat.util.ContactManager;
import com.icodeyou.securechat.util.DebugUtil;
import com.icodeyou.securechat.util.HttpUtil;
import com.icodeyou.securechat.util.NewFriendManager;
import com.icodeyou.securechat.util.PollThread;
import com.icodeyou.securechat.util.WantToChatManager;

import org.json.JSONArray;
import org.json.JSONObject;


public class FriendFragment extends Fragment {

    // todo 发短信时需要这个
//    private Button mBtnAccept;

    private TextView mTvWantToChat, mTvNewFriend;

    private ListView mLvNewFriend;
    private ListView mLvWantToChat;
    private ListView mLvContact;

    private ContactsAdapter contactsAdapter;
    private NewFriendAdapter newFriendAdapter;
    private WantToChatAdapter wantToChatAdapter;


    public static final String CONTACT = "Contact";

    public FriendFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
//        mBtnAccept = (Button) view.findViewById(R.id.id_btnAccept);
        mTvNewFriend = (TextView) view.findViewById(R.id.id_tvNewFriend);
        mTvWantToChat = (TextView) view.findViewById(R.id.id_tvWantToChat);
        mLvNewFriend = (ListView) view.findViewById(R.id.id_lvNewFriend);
        mLvContact = (ListView) view.findViewById(R.id.id_lvContact);
        mLvWantToChat = (ListView) view.findViewById(R.id.id_lvWantToChat);
        contactsAdapter = new ContactsAdapter(getActivity().getApplicationContext());
        mLvContact.setAdapter(contactsAdapter);
        newFriendAdapter = new NewFriendAdapter(getActivity().getApplicationContext());
        mLvNewFriend.setAdapter(newFriendAdapter);
        wantToChatAdapter = new WantToChatAdapter(getActivity().getApplicationContext());
        mLvWantToChat.setAdapter(wantToChatAdapter);

        // 点击某位联系人跳转到聊天界面
        mLvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = contactsAdapter.getItem(position);
                Contact contact = (Contact) item;
                Toast.makeText(getActivity().getApplicationContext(), contact.getName(), Toast.LENGTH_SHORT).show();
                // 点击某一个联系人后进入与该联系人的聊天界面
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                // 设置角色为 发起者
                intent.putExtra(ChatActivity.ROLE, ChatActivity.ROLE_SPONSOR);
                // 传递串行化对象
                intent.putExtra(CONTACT, contact);
                startActivity(intent);
            }
        });

//        // todo 发短信时需要这个
//        mBtnAccept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity().getApplicationContext(), AcceptDialogActivity.class);
//                startActivity(intent);
//            }
//        });

        getContact();

        refreshLvNewFriend();

        new PollThread(new PollThread.PollCallBackListener() {
            @Override
            public void onSuccess(boolean isRefresh) {
                getContact();
                refreshLvWantChat();
                refreshLvNewFriend();
//                if (!isRefresh)
//                    return;
//                if (WantToChatManager.getInstance().getInfos().size() == 0){
//                    // 无聊天会话邀请信息
//                } else {
//                    // 更新聊天会话adapter
//                    refreshLvWantChat();
//                }
//                if (NewFriendManager.getInstance().getInfos().size() == 0){
//                    // 无新添加好友请求
//                } else {
//                    // 更新添加好友adapter
//                    refreshLvNewFriend();
//                }
            }
        }).start();

        return view;
    }

    private void getContact() {
        // 访问网络 获取好友列表
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
        String phone = sharedPreferences.getString("phone", "-1");
        if (!"-1".equals(phone)){
            HttpUtil.getInstance().getContact(phone, new HttpUtil.HttpCallBackListener() {
                @Override
                public void onSuccess(String info) {
                    DebugUtil.print("getContact " + info);
                    try{
                        JSONArray jsonArray = new JSONArray(info);
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            String name = object.getString("name");
                            String num = object.getString("num");
                            ContactManager.getInstance().addContact(new Contact(name, num));
                        }
                        DebugUtil.print("getContact done " + ContactManager.getInstance().getInfos().toString());
                        refreshLvContact();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFail(String info) {
                }
            });
        }

    }



    private void refreshLvContact(){
        contactsAdapter.notifyDataSetChanged();
    }

    private void refreshLvNewFriend(){
        if (NewFriendManager.getInstance().getInfos().size() != 0)
            mTvNewFriend.setVisibility(View.VISIBLE);
        else
            mTvNewFriend.setVisibility(View.GONE);
        newFriendAdapter.notifyDataSetChanged();
    }

    private void refreshLvWantChat() {
        if (WantToChatManager.getInstance().getInfos().size() != 0)
            mTvWantToChat.setVisibility(View.VISIBLE);
        else
            mTvWantToChat.setVisibility(View.GONE);
        wantToChatAdapter.notifyDataSetChanged();
    }

}
