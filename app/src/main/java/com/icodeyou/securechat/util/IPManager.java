package com.icodeyou.securechat.util;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 获取保存本机IP地址、保存对方IP地址
 */
public class IPManager {

    private static IPManager manager = null;

    private String myIP;
    private String friendIP;
    private Context mContext;

    private IPManager(){}

    public static IPManager getInstance(){
        if (manager == null)
            manager = new IPManager();
        return manager;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public String getIP(){
        String ip = "";
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager)(mContext).getSystemService(Context.WIFI_SERVICE);
        // 如果 wifi 没有开启
        if (!wifiManager.isWifiEnabled()) {
            //获取 gprs ip 地址
            try{
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();){
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()){
                            ip = inetAddress.getHostAddress().toString();
                        }
                    }
                }
            }
            catch (SocketException ex){
                Log.e("WifiPreference IpAddress", ex.toString());
            }
            DebugUtil.print("GPRS IP " + ip);
        }else{
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = (ipAddress & 0xFF ) + "." +
                    ((ipAddress >> 8 ) & 0xFF) + "." +
                    ((ipAddress >> 16 ) & 0xFF) + "." +
                    ( ipAddress >> 24 & 0xFF) ;
            DebugUtil.print("WIFI IP " + ip);
        }
        return ip;
    }

    public String getMyIP() {
        return myIP;
    }

    public void setMyIP(String myIP) {
        this.myIP = myIP;
    }

    public String getFriendIP() {
        return friendIP;
    }

    public void setFriendIP(String friendIP) {
        this.friendIP = friendIP;
    }
}
