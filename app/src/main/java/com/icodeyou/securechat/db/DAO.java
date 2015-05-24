package com.icodeyou.securechat.db;

import com.icodeyou.securechat.model.ChatData;

import java.util.List;

/**
 * Created by huan on 15/5/24.
 */
public interface DAO {

    public void insertChat(ChatData chatData);
    public void deleteChat(int _id);
    public void clearChat(String phone);
    public List<ChatData> getChatData(String phone);

}
