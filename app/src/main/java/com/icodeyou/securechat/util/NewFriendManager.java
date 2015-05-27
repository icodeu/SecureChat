package com.icodeyou.securechat.util;

import com.icodeyou.securechat.model.Contact;

import java.util.ArrayList;

public class NewFriendManager {

    private static NewFriendManager manager = null;
    private ArrayList<Contact> mInfos;

    private NewFriendManager(){
        mInfos = new ArrayList<Contact>();
    }

    public static NewFriendManager getInstance(){
        if (manager == null){
            manager = new NewFriendManager();
        }
        return manager;
    }

    public ArrayList<Contact> getInfos(){
        return mInfos;
    }

    public void addContact(Contact contact){
        for (Contact zContact: mInfos)
            if (contact.getNumber().equals(zContact.getNumber()))
                return;
        mInfos.add(contact);
    }

    public void delContact(String phone){
        for (Contact contact : mInfos){
            if (contact.getNumber().equals(phone))
                mInfos.remove(contact);
        }
    }

}
