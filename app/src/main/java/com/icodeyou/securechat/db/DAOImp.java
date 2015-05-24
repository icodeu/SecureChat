package com.icodeyou.securechat.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.icodeyou.securechat.model.ChatData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huan on 15/5/24.
 */
public class DAOImp implements DAO {

    private DBHelper mHelper = null;

    public DAOImp(Context context) {
        mHelper = new DBHelper(context);
    }

    @Override
    public void insertChat(ChatData chatData) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("insert into chat(phone, content, kind, time) values (?, ?, ?, ?)",
                new Object[]{chatData.getPhone(), chatData.getContent(), chatData.getKind(), chatData.getTime()});
        db.close();
    }

    @Override
    public void deleteChat(int _id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("delete from chat where _id = ?", new Object[]{_id});
        db.close();
    }

    @Override
    public void clearChat(String phone) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("delete from chat where phone = ?", new Object[]{phone});
        db.close();
    }

    @Override
    public List<ChatData> getChatData(String phone) {
        List<ChatData> chatDatas = new ArrayList<ChatData>();
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from chat where phone = ?", new String[]{phone});
        while (cursor.moveToNext()){
            ChatData chatData = new ChatData();
            chatData.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            chatData.setContent(cursor.getString(cursor.getColumnIndex("content")));
            chatData.setKind(cursor.getInt(cursor.getColumnIndex("kind")));
            chatData.setTime(cursor.getString(cursor.getColumnIndex("time")));
            chatDatas.add(chatData);
        }
        cursor.close();
        db.close();
        return chatDatas;
    }
}
