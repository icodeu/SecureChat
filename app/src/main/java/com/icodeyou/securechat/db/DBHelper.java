package com.icodeyou.securechat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by huan on 15/5/24.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "securechat.db";
    public static final int VERSION = 1;

    public static final String SQL_CREATE = "create table chat(" +
                                                        "_id integer primary key autoincrement, " +
                                                        "phone text, " +
                                                        "content text, " +
                                                        "kind integer, " +
                                                        "time text)";

    public static final String SQL_DROP = "drop table if exists chat";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        db.execSQL(SQL_CREATE);
    }
}
