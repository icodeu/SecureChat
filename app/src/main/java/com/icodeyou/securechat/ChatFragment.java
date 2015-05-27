package com.icodeyou.securechat;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.icodeyou.securechat.adapter.ContactsAdapter;
import com.icodeyou.securechat.model.Contact;
import com.icodeyou.securechat.util.ContactManager;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {

    private ContactsAdapter contactsAdapter;
    private ListView mLvChat;

    public ChatFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        contactsAdapter = new ContactsAdapter(getActivity().getApplicationContext());
        mLvChat = (ListView) view.findViewById(R.id.id_lvChat);
        mLvChat.setAdapter(contactsAdapter);

        getContacts();

        return view;
    }

    private void getContacts() {
    }

}
