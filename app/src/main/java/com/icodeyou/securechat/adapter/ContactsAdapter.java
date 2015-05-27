package com.icodeyou.securechat.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.icodeyou.securechat.R;
import com.icodeyou.securechat.model.Contact;
import com.icodeyou.securechat.util.ContactManager;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<Contact> mContacts;

    public ContactsAdapter(Context mContext) {
        this.mContext = mContext;
        mContacts = ContactManager.getInstance().getInfos();
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
        Contact contact = (Contact) getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.contact_item, parent, false);
        TextView tvName = (TextView) convertView.findViewById(R.id.id_tvName);
        tvName.setText(contact.getName());
        TextView tvPhoto = (TextView) convertView.findViewById(R.id.id_ivPhoto);
        String firstWord = ((Contact) getItem(position)).getName().substring(0, 1);
        tvPhoto.setText(firstWord);
        tvPhoto.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        TextView tvPhone = (TextView) convertView.findViewById(R.id.id_tvPhone);
        tvPhone.setText(contact.getNumber());
        return convertView;
    }
}
