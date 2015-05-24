package com.icodeyou.securechat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icodeyou.securechat.R;
import com.icodeyou.securechat.model.ChatData;

import java.util.List;

/**
 * Created by huan on 15/5/5.
 */
public class ChatAdapter extends BaseAdapter {

    private List<ChatData> list;
    private RelativeLayout layout;
    private Context context;

    public ChatAdapter(List<ChatData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (list.get(position).getKind() == ChatData.SENDER){
            layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.right_item, null);
        }else {
            layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.left_item, null);
        }
        TextView tvContent = (TextView) layout.findViewById(R.id.tvContent);
        tvContent.setText(list.get(position).getContent().toString());
        return layout;
    }
}
