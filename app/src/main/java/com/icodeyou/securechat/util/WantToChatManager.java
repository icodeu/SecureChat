package com.icodeyou.securechat.util;

import com.icodeyou.securechat.model.Contact;

import java.util.ArrayList;

public class WantToChatManager {

    private static WantToChatManager manager = null;
    private ArrayList<Contact> mInfos;

    private WantToChatManager(){
        mInfos = new ArrayList<Contact>();
    }

    public static WantToChatManager getInstance(){
        if (manager == null){
            manager = new WantToChatManager();
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
