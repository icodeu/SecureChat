package com.icodeyou.securechat;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.icodeyou.securechat.model.Contact;
import com.icodeyou.securechat.util.ContactManager;
import com.icodeyou.securechat.util.DebugUtil;
import com.icodeyou.securechat.util.HttpUtil;
import com.icodeyou.securechat.util.NewFriendManager;

import org.json.JSONObject;

import java.util.ArrayList;

public class NewFriendAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<Contact> mContacts;

    public NewFriendAdapter(Context mContext) {
        this.mContext = mContext;
        mContacts = NewFriendManager.getInstance().getInfos();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.add_contact_item, parent, false);
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
                HttpUtil.getInstance().recContact(MyApplication.getMyPhone(), contact.getNumber(), "0", new HttpUtil.HttpCallBackListener() {
                    @Override
                    public void onSuccess(String info) {
                        try {
                            JSONObject object = new JSONObject(info);
                            int status = object.getInt("status");
                            if (status == 0){
                                DebugUtil.print("NewFriendAdapter BtnAccept " + contact.getNumber());
                                Toast.makeText(mContext, "添加好友成功", Toast.LENGTH_SHORT).show();
                                NewFriendManager.getInstance().delContact(contact.getNumber());
                                // 更新好友请求列表
                                notifyDataSetChanged();
                                // todo 更新好友列表

                            } else if (status == 1){
                                Toast.makeText(mContext, "添加好友异常", Toast.LENGTH_SHORT).show();
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

        // 拒绝好友请求 Button
        Button btnRefuse = (Button) convertView.findViewById(R.id.id_btnRefuse);
        btnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.getInstance().recContact(MyApplication.getMyPhone(), contact.getNumber(), "1", new HttpUtil.HttpCallBackListener() {
                    @Override
                    public void onSuccess(String info) {
                        try {
                            JSONObject object = new JSONObject(info);
                            int status = object.getInt("status");
                            if (status == 0){
                                DebugUtil.print("NewFriendAdapter BtnAccept " + contact.getNumber());
                                Toast.makeText(mContext, "已拒绝", Toast.LENGTH_SHORT).show();
                                NewFriendManager.getInstance().delContact(contact.getNumber());
                                // 更新好友请求列表
                                notifyDataSetChanged();
                                // todo 更新好友列表

                            } else if (status == 1){
                                Toast.makeText(mContext, "拒绝添加异常", Toast.LENGTH_SHORT).show();
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
        return convertView;
    }
}
