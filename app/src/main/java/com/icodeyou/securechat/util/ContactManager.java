package com.icodeyou.securechat.util;

import com.icodeyou.securechat.model.Contact;

import java.util.ArrayList;

public class ContactManager {

    private static ContactManager contactManager = null;
    private ArrayList<Contact> mInfos;

    private ContactManager(){
        mInfos = new ArrayList<Contact>();
    }

    public static ContactManager getInstance(){
        if (contactManager == null){
            contactManager = new ContactManager();
        }
        return contactManager;
    }

    public ArrayList<Contact> getInfos(){
        return mInfos;
    }

}
