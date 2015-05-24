package com.icodeyou.securechat.util;

import android.os.Handler;

import com.icodeyou.securechat.model.CryptManager;
import com.icodeyou.securechat.model.PubkeyManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * 被邀请者初始化加密的线程
 */
public class InitCryptAcceptThread extends Thread {

    private String ip;
    private Handler handler;

    public static final int PORT_SERVER_CRYPT = 55555;
    public static final int PORT_CLIENT_CRYPT = 33333;

    public InitCryptAcceptThread(String ip, Handler handler) {
        this.ip = ip;
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();

        try {
            //DH 生成 被邀请者 私钥和公钥
            Map<String, Object> map = DHUtil.initKey();
            byte[] privateKey2 = DHUtil.getPrivateKey(map);
            byte[] publicKey2 = DHUtil.getPublicKey(map);
            CryptManager.getInstance().setPrivateKeyAccept(privateKey2);
            CryptManager.getInstance().setPublicKeyAccept(publicKey2);

            PubkeyManager pubkeyManager = new PubkeyManager(publicKey2);
            // 被邀请者发送自己的公钥
            Socket socket = new Socket(ip, PORT_SERVER_CRYPT);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(pubkeyManager);
            oos.flush();

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            byte[] buffer = ((PubkeyManager)(ois.readObject())).getPublicKey();
            //System.out.println("Client 收到 " + BytesToHex.fromBytesToHex(buffer));
            CryptManager.getInstance().setPublicKeySponsor(buffer);

            //被邀请者构建秘密密钥
            byte[] privateKey = CryptManager.getInstance().getPrivateKeyAccept();
            byte[] publicKey = CryptManager.getInstance().getPublicKeySponsor();
            byte[] secretKey = DHUtil.getSecretKey(publicKey, privateKey);
            CryptManager.getInstance().setSecret(secretKey);
            //System.out.println("Server SecretKey " + BytesToHex.fromBytesToHex(secretKey));

            CryptManager.getInstance().setSecret(secretKey);

            DebugUtil.print("InitCryptAcceptThread " + BytesToHex.fromBytesToHex(CryptManager.getInstance().getSecret()));

            handler.sendEmptyMessage(0);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
