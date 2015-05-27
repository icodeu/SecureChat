package com.icodeyou.securechat.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.icodeyou.securechat.ChatActivity;
import com.icodeyou.securechat.FriendFragment;
import com.icodeyou.securechat.HomeActivity;
import com.icodeyou.securechat.MyApplication;
import com.icodeyou.securechat.R;
import com.icodeyou.securechat.model.Contact;
import com.icodeyou.securechat.util.DebugUtil;
import com.icodeyou.securechat.util.HttpUtil;
import com.icodeyou.securechat.util.NewFriendManager;
import com.icodeyou.securechat.util.WantToChatManager;

import org.json.JSONObject;

import java.util.ArrayList;

public class WantToChatAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<Contact> mContacts;


    public WantToChatAdapter(Context mContext) {
        this.mContext = mContext;
        mContacts = WantToChatManager.getInstance().getInfos();
    }

    @Override
    public int getCount() {
        return mContacts.size();
    }

    @Override
    public Object getItem(int position) {
        return mContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Contact contact = (Contact) getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.want_chat_item, parent, false);
        TextView tvName = (TextView) convertView.findViewById(R.id.id_tvName);
        tvName.setText(contact.getName());
        TextView tvPhoto = (TextView) convertView.findViewById(R.id.id_ivPhoto);
        String firstWord = ((Contact) getItem(position)).getName().substring(0, 1);
        tvPhoto.setText(firstWord);
        tvPhoto.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        // 接受好友请求 Button
        Button btnAccept = (Button) convertView.findViewById(R.id.id_btnAccept);
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 接受会话请求
                String ram = contact.getRam();
                if (!"".equals(ram)){
                    Intent intent = new Intent(mContext, ChatActivity.class);
                    // 设置角色为 受邀请者
                    intent.putExtra(ChatActivity.ROLE, ChatActivity.ROLE_ACCEPTOR);
                    intent.putExtra(HomeActivity.RAM, ram);
                    // 传递串行化对象
                    intent.putExtra(FriendFragment.CONTACT, contact);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    // 从 WantChatManager 中删除对应记录
                    WantToChatManager.getInstance().delContact(contact.getNumber());
                }

            }
        });

        // 拒绝好友请求 Button
        Button btnRefuse = (Button) convertView.findViewById(R.id.id_btnRefuse);
        btnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 拒绝会话请求
            }
        });
        return convertView;
    }
}
