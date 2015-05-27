package com.icodeyou.securechat;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.icodeyou.securechat.adapter.ChatAdapter;
import com.icodeyou.securechat.db.DAOImp;
import com.icodeyou.securechat.model.ByteManager;
import com.icodeyou.securechat.model.ChatData;
import com.icodeyou.securechat.model.Contact;
import com.icodeyou.securechat.model.CryptManager;
import com.icodeyou.securechat.util.AESUtil;
import com.icodeyou.securechat.util.BytesToHex;
import com.icodeyou.securechat.util.DHUtil;
import com.icodeyou.securechat.util.DebugUtil;
import com.icodeyou.securechat.util.HttpUtil;
import com.icodeyou.securechat.util.IPManager;
import com.icodeyou.securechat.util.InitCryptAcceptThread;
import com.icodeyou.securechat.util.InitCryptSponsorThread;
import com.icodeyou.securechat.util.MessageUtil;
import com.icodeyou.securechat.util.RingtoneVibratorManager;
import com.icodeyou.securechat.util.SettingManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * 聊天界面
 */
public class ChatActivity extends ActionBarActivity {

    private ListView mLvChat;
    private Button mBtnSend;
    private Contact mContact;
    private String mRam;
    private EditText mEtMessage;

    private List<ChatData> mChatDatas;
    private ChatAdapter mChatAdapter;

    private String mRole;

    public static final String ROLE_SPONSOR = "role_sponsor";
    public static final String ROLE_ACCEPTOR = "role_acceptor";
    public static final String ROLE = "role";

    public static final int PORT_CLIENT = 12345;
    public static final int PORT_SERVER = 54321;

    // 负责收消息的ServerSocket
    ServerSocket serverSocket = null;
    // 负责发消息的Socket
    Socket socket = null;

    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;

    // 当DH密钥初始化好更新UI mBtnSend
    Handler initCryptDoneHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            changeCryptDoneStatus();
        }
    };


    // 用于更新UI--Receiver
    Handler receiveHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String message = (String) msg.obj;
            DebugUtil.print("receiveHandler " + message);
            mChatDatas.add(new ChatData(message, ChatData.RECEIVER));
            mChatAdapter.notifyDataSetChanged();

            if (SettingManager.getInstance(getApplicationContext()).isSaveChatHistory()){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                String time = formatter.format(curDate);
                new DAOImp(ChatActivity.this).insertChat(new ChatData(message, ChatData.RECEIVER, mContact.getNumber(), time));
            }

            //播放声音与震动(开关在manager中)
            RingtoneVibratorManager.playMusic(ChatActivity.this);
            RingtoneVibratorManager.vibration(ChatActivity.this);
        }
    };

    // 用于更新UI--Sender
    Handler sendHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String message = (String) msg.obj;
            DebugUtil.print("sendHandler " + message);
            mChatDatas.add(new ChatData(message, ChatData.SENDER));
            mChatAdapter.notifyDataSetChanged();

            if (SettingManager.getInstance(getApplicationContext()).isSaveChatHistory()) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                String time = formatter.format(curDate);
                new DAOImp(ChatActivity.this).insertChat(new ChatData(message, ChatData.SENDER, mContact.getNumber(), time));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initView();

        showChatHistory();

        // 根据角色不同初始化不同连接
        String role = getIntent().getStringExtra(ChatActivity.ROLE);
        mRole = role;
        if (ROLE_SPONSOR.equals(role)){
            // 发起者初始化连接
            initConnection();
            // 523 发起者相当于Server 要先跑起来
            new InitCryptSponsorThread(initCryptDoneHandler).start();
        }
        else{
            // todo 被邀请者初始化连接
            DebugUtil.print("ChatActivity " + "我是受邀请者");
            mRam = getIntent().getStringExtra(HomeActivity.RAM);
            initConnectionAccept();
        }

        // 接收消息时只需要在端口等待连接即可
        receiveMessage();

    }

    private void showChatHistory() {
        if (mContact != null){
            List<ChatData> chatDatas = new DAOImp(ChatActivity.this).getChatData(mContact.getNumber());
            for (ChatData data : chatDatas){
                mChatDatas.add(data);
            }
            mChatAdapter.notifyDataSetChanged();
        }
    }

    private void receiveMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mRole.equals(ChatActivity.ROLE_SPONSOR))
                        serverSocket = new ServerSocket(PORT_SERVER);
                    else
                        serverSocket = new ServerSocket(PORT_CLIENT);
                    System.out.println("正待等待客户端连接");
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("有客户端连接 IP为 " + clientSocket.getInetAddress().getHostAddress().toString());

                    ois = new ObjectInputStream(clientSocket.getInputStream());
                    ByteManager byteManager = new ByteManager();

                    while ( (byteManager = (ByteManager) ois.readObject()) != null  ){
                        DebugUtil.print("服务器端收到密文 >>>>>> " + BytesToHex.fromBytesToHex(byteManager.getBytes()));
                        byte[] plainBytes = AESUtil.getInstance().decrypt(byteManager.getBytes(), CryptManager.getInstance().getSecret());
                        String plainString = new String(plainBytes);
                        Message msg = Message.obtain();
                        msg.obj = plainString;
                        receiveHandler.sendMessage(msg);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void send(final String message, final String ip) {
        // 第一次连接则建立Socket及BufferedWritter
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ByteManager byteManager = new ByteManager(AESUtil.getInstance().encrypt(message.getBytes(), CryptManager.getInstance().getSecret()));
                    if (socket == null) {
                        System.out.println("建立新Socket");
                        if (mRole.equals(ChatActivity.ROLE_SPONSOR))
                            socket = new Socket(ip, PORT_CLIENT);
                        else
                            socket = new Socket(ip, PORT_SERVER);
                        System.out.println("Socket建立完毕");
                        oos = new ObjectOutputStream(socket.getOutputStream());
                        System.out.println("客户端连接远程服务器并得到输出流");
                    }
                    oos.writeObject(byteManager);
                    oos.flush();
                    DebugUtil.print("客户端发出密文 >>>>>> " + BytesToHex.fromBytesToHex(byteManager.getBytes()));
                    Message msg = Message.obtain();
                    msg.obj = message;
                    sendHandler.sendMessage(msg);
                } catch (Exception e) {
                    System.out.println("Exception Error");
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void initView() {
        // 获得当前聊天的联系人对象
        mContact = (Contact) getIntent().getSerializableExtra(MainActivity.CONTACT);
        if (mContact !=null) {
            // 初始化标题
            setTitle("与" + mContact.getName() + "聊天中");
        }else{
            setTitle("正在与对方建立连接");
        }

        mLvChat = (ListView) findViewById(R.id.id_lvChat);
        mBtnSend = (Button) findViewById(R.id.id_btnSend);
        mEtMessage = (EditText) findViewById(R.id.id_etMessage);

        mLvChat = (ListView) findViewById(R.id.id_lvChat);
        mChatDatas = new ArrayList<ChatData>();
        mChatAdapter = new ChatAdapter(mChatDatas, this);
        mLvChat.setAdapter(mChatAdapter);

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 发送消息
                String message = mEtMessage.getText().toString().trim();
                if (!"".equals(message)){
                    send(message, mContact.getIp());
                    mEtMessage.setText("");
                }
            }
        });

    }

    // 以下为 被邀请者 方法
    private void initConnectionAccept() {
        HttpUtil.getInstance().sendMyIPRAM(IPManager.getInstance().getMyIP(), mRam, new HttpUtil.HttpCallBackListener() {
            @Override
            public void onSuccess(String info) {
                changeInitStatus();
//                setTitle("建立连接成功");
                DebugUtil.print("ChatActivity " + info);
                getIPFromJson(info);
                // 523 加密通信初始化密钥开始！！！
                initDH();
            }

            @Override
            public void onFail(String info) {

            }
        });
    }

    /*
     *  523 初始化对称密码密钥
     */
    private void initDH(){
        DebugUtil.print("initDH start");
        new InitCryptAcceptThread(mContact.getIp(), initCryptDoneHandler).start();
    }

    private void getIPFromJson(String json) {
        // todo 此处先模拟json数据
//        json = "{\"status\":0,\"ip\":\"10.0.3.15\"}";
        int status = -1;
        String ip = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            status = jsonObject.getInt("status");
            ip = jsonObject.getString("ip");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DebugUtil.print("status " + status);
        DebugUtil.print("ip " + ip);
        mContact.setIp(ip);
//        mContact = new Contact(ip);
        // todo 暂时先设置一个号码 方便数据库存储 不然报空指针异常
        // mContact.setNumber("00000000000");
    }
    // 被邀请者 方法结束


    // 以下为 发起者 方法
    private void initConnection() {
        HttpUtil.getInstance().sendMyIP(IPManager.getInstance().getMyIP(), MyApplication.getMyPhone(), mContact.getNumber(), new HttpUtil.HttpCallBackListener() {
            @Override
            public void onSuccess(String info) {
                mBtnSend.setText("建立连接成功！");
                DebugUtil.print("ChatActivity " + info);
                DebugUtil.print("sponsor:" + MyApplication.getMyPhone() + " acceptor:" + mContact.getNumber());
                getRamFromJson(info);
            }

            @Override
            public void onFail(String info) {

            }
        });
    }

    /*
     * 解析出json数据
     */
    private void getRamFromJson(String json) {
        // todo 此处先模拟json数据
//        json = "{\"status\":0,\"ram\":9573}";
        int status = -1, ram = -1;
        try {
            JSONObject jsonObject = new JSONObject(json);
            status = jsonObject.getInt("status");
            ram = jsonObject.getInt("ram");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DebugUtil.print("status " + status);
        DebugUtil.print("ram " + ram);

        // 给另一方用户发送短信，内容为随机数
        MessageUtil.getInstance().sendMessage(mContact.getNumber(), ram+"");

        mBtnSend.setText("等待对方上线" + ram);

        // 发完短信之后就可以开一条线程去轮询： 该ram在数据库中是否有对应的IP
        new WaitThread(ram+"").start();
    }


    /*
     * 轮询等待 该ram在数据库中是否有对应的IP
     */
    class WaitThread extends Thread {

        private String ram = "";
        private boolean pool = true;

        public WaitThread(String ram){
            this.ram = ram;
        }

        @Override
        public void run() {
            while (pool){
                try {
                    //todo pollGetIP.php
                    HttpUtil.getInstance().pollGetIP(ram, new HttpUtil.HttpCallBackListener() {
                        @Override
                        public void onSuccess(String info) {
                            // todo 此处先模拟json数据
//                            info = "{\"status\":0,\"ip\":127.0.0.1}";
                            int status = -1;
                            String ip = "";
                            try {
                                JSONObject jsonObject = new JSONObject(info);
                                status = jsonObject.getInt("status");
                                ip = jsonObject.getString("ip");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            DebugUtil.print("status " + status);
                            DebugUtil.print("ip " + ip);
                            // 轮询到对方ip地址 可以终止轮询并准备通信
                            if (status == 0){
                                changeInitStatus();
                                mContact.setIp(ip);
                                pool = false;
                                initCommunication();
                            }
                        }

                        @Override
                        public void onFail(String info) {
                        }
                    });
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * 已获得对方IP 通信准备
     */
    private void initCommunication() {
        // todo 这个似乎并没有卵用
        DebugUtil.print("initCommunication friIP " + mContact.getIp());
    }

    private void changeInitStatus(){
        mBtnSend.setText("正在初始化加密信息");
    }

    private void changeCryptDoneStatus(){
        mBtnSend.setText("发送");
        mBtnSend.setEnabled(true);
        mEtMessage.setVisibility(View.VISIBLE);
    }

    /*
     * 释放之前连接的资源
     */
    @Override
    protected void onPause() {
        super.onPause();
        DebugUtil.print("ChatActivity 释放资源开始");
        try{
            if (oos != null)
                oos.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            if (ois != null)
                ois.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            if (socket != null){
                socket.close();
                socket = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            if (serverSocket != null){
                serverSocket.close();
                serverSocket = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        DebugUtil.print("ChatActivity 释放资源完成");
    }
}
