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
 * 发起者初始化加密的线程
 */
public class InitCryptSponsorThread extends Thread {

    private Handler handler;

    public static final int PORT_SERVER_CRYPT = 55555;
    public static final int PORT_CLIENT_CRYPT = 33333;

    public InitCryptSponsorThread(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();

        try {
            ServerSocket serverSocket = new ServerSocket(PORT_SERVER_CRYPT);
            Socket socket = serverSocket.accept();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            byte[] buffer = ((PubkeyManager)(ois.readObject())).getPublicKey();
            //System.out.println("Server 收到数据 " + BytesToHex.fromBytesToHex(buffer));
            CryptManager.getInstance().setPublicKeyAccept(buffer);

            //DH 生成 发起者 私钥和公钥
            Map<String, Object> map = DHUtil.initKey(buffer);
            byte[] privateKey1 = DHUtil.getPrivateKey(map);
            byte[] publicKey1 = DHUtil.getPublicKey(map);
            CryptManager.getInstance().setPrivateKeySponsor(privateKey1);
            CryptManager.getInstance().setPublicKeySponsor(publicKey1);

            //发起者构建秘密密钥
            byte[] privateKey = CryptManager.getInstance().getPrivateKeySponsor();
            byte[] publicKey = CryptManager.getInstance().getPublicKeyAccept();
            byte[] secretKey = DHUtil.getSecretKey(publicKey, privateKey);
            CryptManager.getInstance().setSecret(secretKey);
            //System.out.println("Server SecretKey " + BytesToHex.fromBytesToHex(secretKey));

            // 发起者发送自己的公钥
            PubkeyManager pubkeyManager = new PubkeyManager(publicKey1);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(pubkeyManager);
            oos.flush();

            CryptManager.getInstance().setSecret(secretKey);

            handler.sendEmptyMessage(0);

            // 关闭连接，释放资源
            ois.close();
            oos.close();
            socket.close();
            socket = null;
            serverSocket.close();
            serverSocket = null;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
