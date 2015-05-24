package com.icodeyou.securechat.model;

/**
 * Created by huan on 15/5/5.
 */
public class ChatData {

    private String content;
    public static final int SENDER = 1;
    public static final int RECEIVER = 2;
    private int kind;

    private String phone;
    private String time;

    public ChatData(String content, int kind) {
        this.content = content;
        this.kind = kind;
    }

    public ChatData(String content, int kind, String phone, String time) {
        this.content = content;
        this.kind = kind;
        this.phone = phone;
        this.time = time;
    }

    public ChatData() {
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
