package com.icodeyou.securechat.model;

import java.io.Serializable;

/**
 * Created by huan on 15/5/17.
 */
public class Contact implements Serializable{

    private String name;
    private String ip;
    private String number;
    private long rawContactId;
    private String ram;

    public Contact(String name, String ip, String number) {
        this.name = name;
        this.ip = ip;
        this.number = number;
    }

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }


    public Contact() {
    }

    public Contact(String ip){
        this.ip = ip;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getRawContactId() {
        return rawContactId;
    }

    public void setRawContactId(long rawContactId) {
        this.rawContactId = rawContactId;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }
}
